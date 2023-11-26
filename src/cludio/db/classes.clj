(ns cludio.db.classes
  (:require [next.jdbc :as jdbc]
            [next.jdbc.result-set :as rs]
            [honey.sql :as sql]
            [java-time.api :as jt]))

(def db-spec
  {:dbtype   "postgresql"
   :dbname   "cludio"
   :host     "localhost"
   :user     "cludio"
   :password "cludio"})

(defn db [] (jdbc/get-datasource db-spec))

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
        classes (jdbc/execute! (db) select-classes {:builder-fn rs/as-unqualified-kebab-maps})]
    classes))

(comment (def classes (classes-by-date-range db (jt/local-date 2023 11 1) (jt/local-date 2023 11 30)))
         (count classes)
         ;; Need to convert sql timestamp to java date here
         ;; also need to make sure I'm not messing up the dates
         ;; when they are grabbed from the db
         (reduce (fn [acc {:keys [datetime] :as c}]
                   (let [k (->> datetime (jt/format "YYYY-MM-dd") keyword)]
                     (assoc acc k c))) {} classes))
