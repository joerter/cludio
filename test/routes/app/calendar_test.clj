(ns routes.app.calendar-test
  (:require [clojure.test :refer :all]
            [cludio.routes.app.calendar :as cal]
            [java-time.api :as jt]
            [cludio.test-util :as util]
            [honey.sql :as sql]
            [next.jdbc :as jdbc]))

(deftest month-view-start
  (testing "returns the start date for month view based on 42 days and Sunday as first day of the week"
    (is (= (jt/local-date 2023 10 29) (cal/month-view-start (jt/local-date 2023 11 1))))
    (is (= (jt/local-date 2023 11 26) (cal/month-view-start (jt/local-date 2023 12 1))))
    (is (= (jt/local-date 2023 12 31) (cal/month-view-start (jt/local-date 2024 1 1))))))

(deftest month-view
  (testing "always returns 42 days"
    (is (= 42 (count (cal/month-view (jt/local-date 2023 10 1)))))
    (is (= 42 (count (cal/month-view (jt/local-date 2023 11 1)))))
    (is (= 42 (count (cal/month-view (jt/local-date 2023 12 1)))))))

(deftest get-classes-test
  (let [database-container (util/create-database-container)]
    (try
      (.start database-container)
      (util/with-system
        [sut (util/datasource-only-system
              {:db-spec {:jdbcUrl (.getJdbcUrl database-container)
                         :username (.getUsername database-container)
                         :password (.getPassword database-container)}})]
        (let [{:keys [data-source]} sut
              insert-class (-> {:insert-into [:class]
                                :columns [:name :season-id]
                                :values [["Ballet I" 1]]}
                               (sql/format))
              insert-class-repetition (-> {:insert-into [:class-repetition]
                                           :columns [:day-of-the-week-id :teacher-id :class-id :time]
                                           :values [[2 1 1 "16:00:00"]
                                                    [4 1 1 "16:00:00"]]}
                                          (sql/format))
              insert-teacher (-> {:insert-into [:teacher]
                                  :columns [:first-name :last-name :title]
                                  :values [["Test" "Teacher" "Ms."]]}
                                 (sql/format))
              insert-class-schedule (-> {:insert-into [:class-schedule]
                                         :columns [:class-id :class-repetition-id :datetime]
                                         :values [[1 1 (jt/local-date-time 2023 9 4 16 0 0)]
                                                  [1 2 (jt/local-date-time 2023 9 6 16 0 0)]]}
                                        (sql/format))]
          (jdbc/execute! (data-source) insert-class)
          (jdbc/execute! (data-source) insert-class-repetition)
          (jdbc/execute! (data-source) insert-teacher)
          (jdbc/execute! (data-source) insert-class-schedule)
          (testing "returns a map of local-date -> class coll"
            (let [start (jt/local-date 2023 9 1)
                  end (jt/local-date 2023 9 30)
                  result (cal/get-classes data-source start end)]
              (is (= 2 (count result)))))))
      (finally
        (.stop database-container)))))

(comment (run-tests))
