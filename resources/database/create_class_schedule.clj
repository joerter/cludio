(ns database.create-class-schedule
  (:require [next.jdbc :as jdbc]
            [next.jdbc.date-time :as dt]
            [honey.sql :as sql]
            [next.jdbc.result-set :as rs]
            [java-time.api :as jt]
            [java-time.repl :as jtrepl]
            [clojure.string :as string]))

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

(defn create-class-schedule
  "For each class-repetition create schedule record for each day from season start to season end"
  [season-id]
  (dt/read-as-default)
  (let [select-season (-> {:select :date-range
                           :from [:season]
                           :where [:= :id season-id]}
                          (sql/format))
        parse-date (partial map (partial jt/local-date "yyyy-MM-dd"))
        season (->
                (execute! select-season)
                (first)
                (:date-range)
                .getValue
                (string/replace #"\[|\]|\)" "")
                (string/split #",")
                (parse-date))

        select-class-repetitions (-> {:select :*
                                      :from [[:class-repetition :cr]]
                                      :inner-join [[:day-of-the-week :dotw] [:= :cr.day-of-the-week-id :dotw.id]]} (sql/format))
        enrich-with-dotw (partial map (fn [r]
                                        (assoc r :dotw (keyword (string/lower-case (:day r))))))
        class-repetitions (->
                           (execute! select-class-repetitions)
                           enrich-with-dotw)]
    class-repetitions))

(def test-repetition
  {:id 2,
   :created-at #inst "2023-11-12T01:51:25.395743000-00:00",
   :day-of-the-week-id 4,
   :teacher-id 3,
   :class-id 1,
   :time "16:00:00"})

(def season-range
  (list (jt/local-date "yyyy-MM-dd" "2023-09-01") (jt/local-date "yyyy-MM-dd" "2024-06-02")))

(defn repetition->schedule
  [time day-of-the-week [season-start season-end]]
  (let [day-before-season-start (jt/minus season-start (jt/days 1))]
    (loop [all-days (rest (jt/iterate jt/adjust day-before-season-start :next-day-of-week day-of-the-week))
           season-days []]
      (let [next-day (first all-days)]
        (if (jt/after? next-day season-end)
          season-days
          (recur (rest all-days) (conj season-days (jt/local-date-time next-day time))))))))

(create-class-schedule 1)
(comment
  (repetition->schedule "16:00" :monday season-range)
  (conj [1 2] 3)
  (jt/local-date-time (jt/local-date 2023 9 1) "16:00")
  (jtrepl/show-adjusters))

