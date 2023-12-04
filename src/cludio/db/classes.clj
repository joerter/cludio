(ns cludio.db.classes
  (:require [next.jdbc :as jdbc]
            [next.jdbc.result-set :as rs]
            [next.jdbc.date-time :as ndt]
            [honey.sql :as sql]
            [java-time.api :as jt]
            [malli.core :as m]
            [malli.experimental.time :as met]
            [malli.registry :as mr]))

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
   [:datetime inst?]
   [:class-id int?]
   [:name string?]
   [:teacher-id int?]
   [:first-name string?]
   [:last-name string?]])

(def ScheduledClasses
  [:vector ScheduledClass])

(defn classes-by-date-range
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
        classes (jdbc/execute! (db) select-classes {:builder-fn rs/as-unqualified-kebab-maps})
        valid? (m/validate ScheduledClasses classes)]
    (if valid?
      classes
      (throw (Exception. "Invalid schema")))))

(comment (def classes (classes-by-date-range db (jt/local-date 2023 11 1) (jt/local-date 2023 11 30)))
         (count classes)
         (m/validate [:vector ScheduledClass] classes)
         (mr/set-default-registry!
          (mr/composite-registry
           (m/default-schemas)
           (met/schemas)))
         (ndt/read-as-local)
         (reduce (fn [acc {:keys [datetime] :as current-class}]
                   (let [k (->> datetime (jt/format "YYYY-MM-dd") keyword)]
                     (update acc k (fn [existing-value]
                                     (if existing-value
                                       (conj existing-value current-class)
                                       [current-class]))))) {} classes))
