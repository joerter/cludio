(ns database.create-class-schedule
  (:require [next.jdbc :as jdbc]
            [honey.sql :as sql]
            [next.jdbc.result-set :as rs]
            [java-time.api :as jt]))

(def db-spec
  {:dbtype   "postgresql"
   :dbname   "cludio"
   :host     "localhost"
   :user     "cludio"
   :password "cludio"})

(def ds (jdbc/get-datasource db-spec))

(defn execute! [query]
  (jdbc/execute! ds query {:builder-fn rs/as-unqualified-kebab-maps}))

(defn create-season []
  (let [insert-season ["INSERT INTO season (date_range)
VALUES
('[2023-09-01, 2024-06-01]');"]]
    (jdbc/execute! ds insert-season)))

(defn create-class-repetitions [season-id]
  (let [select-classes (-> {:select :*
                            :from [:class]
                            :where [:= :class.season-id season-id]}
                           (sql/format))
        classes-result (execute! select-classes)
        select-teachers (-> {:select :*
                             :from [:teacher]}
                            (sql/format))
        teachers-result (execute! select-teachers)
        select-days ["SELECT * FROM day_of_the_week d where d.day in ('Monday', 'Tuesday', 'Wednesday', 'Thursday')"]
        days-result (execute! select-days)
        [monday tuesday wednesday thursday] days-result
        day-groups [[monday wednesday] [tuesday thursday]]
        time-groups [(jt/local-time 16) (jt/local-time 17) (jt/local-time 18) (jt/local-time 19) (jt/local-time 20) (jt/local-time 16 30) (jt/local-time 17 30) (jt/local-time 18 30)]
        class->repetitions (fn [{:keys [id]}] (let [teacher-id (inc (rand-int (count teachers-result)))
                                                    class-group (nth day-groups (mod id 2))
                                                    time (nth time-groups (mod id 4))] [[(-> (first class-group) (:id))
                                                                                         teacher-id
                                                                                         id
                                                                                         time]
                                                                                        [(-> (last class-group) (:id))
                                                                                         teacher-id
                                                                                         id
                                                                                         time]]))
        values (into [] (mapcat class->repetitions classes-result))
        insert-query (-> {:insert-into [:class-repetition]
                          :columns [:day-of-the-week-id :teacher-id :class-id :time]
                          :values values} (sql/format))]

    (execute! insert-query)))

(comment (create-class-repetitions 1))

