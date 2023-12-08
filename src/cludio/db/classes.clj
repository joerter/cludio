(ns cludio.db.classes
  (:require
   [honey.sql :as sql]
   [java-time.api :as jt]
   [malli.core :as m]
   [malli.dev :as mdev]
   [malli.dev.pretty :as mpretty]
   [malli.experimental.time :as met]
   [malli.registry :as mr]
   [next.jdbc :as jdbc]
   [next.jdbc.date-time :as ndt]
   [next.jdbc.result-set :as rs]))

(def db-spec
  {:dbtype   "postgresql"
   :dbname   "cludio"
   :host     "localhost"
   :user     "cludio"
   :password "cludio"})

(defn db [] (jdbc/get-datasource db-spec))

(def ScheduledClass
  [:map {:closed true}
   [:class-schedule-id int?]
   [:datetime :time/local-date-time] ;; needs to be a local time
   [:class-id int?]
   [:name string?]
   [:teacher-id int?]
   [:first-name string?]
   [:last-name string?]])

(def ScheduledClasses
  [:* ScheduledClass])

(defn classes-by-date-range
  "Gets rows from the class_schedule table for the given date range"
  {:malli/schema [:=> [:cat :any :time/local-date :time/local-date] ScheduledClasses]}
  [db start-date end-date]
  (let [select-classes (-> {:select [[:cs.id :class-schedule-id]
                                     [:cs.datetime]
                                     [:c.id :class-id]
                                     [:c.name]
                                     [:t.id :teacher-id]
                                     [:t.first-name]
                                     [:t.last-name]]
                            :from [[:class-schedule :cs]]
                            :join [[:class :c] [:= :c.id :cs.class-id]
                                   [:class-repetition :cr] [:= :cr.id :cs.class-repetition-id]
                                   [:teacher :t] [:= :t.id :cr.teacher-id]]
                            :where [:and
                                    [:>= :cs.datetime start-date]
                                    [:<= :cs.datetime end-date]]
                            :order-by [:cs.datetime]}
                           (sql/format))
        classes (jdbc/execute! (db) select-classes {:builder-fn rs/as-unqualified-kebab-maps})]
    classes))

(comment (def classes (classes-by-date-range db (jt/local-date 2023 9 1) (jt/local-date 2023 9 30)))
         (first classes)
         (m/explain ScheduledClass (first classes))
         (count classes)
         (m/validate [:map-of :keyword int?] {:john 1 :oerter 2})
         (m/validate [:map [:classes [:schema ScheduledClasses]]] {:classes 1})
         (mdev/start! {:report (mpretty/reporter)})
         (m/function-schemas)
         (ndt/read-as-local)
         (mr/set-default-registry!
          (mr/composite-registry
           (m/default-schemas)
           (met/schemas)))
         (reduce (fn [acc {:keys [datetime] :as current-class}]
                   (let [k (->> datetime (jt/format "YYYY-MM-dd") keyword)]
                     (update acc k (fn [existing-value]
                                     (if existing-value
                                       (conj existing-value current-class)
                                       [current-class]))))) {} classes))
