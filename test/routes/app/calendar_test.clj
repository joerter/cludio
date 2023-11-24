(ns routes.app.calendar-test
  (:require [clojure.test :refer :all]
            [cludio.routes.app.calendar :as cal]
            [java-time.api :as jt]
            [cludio.test-util :as util]))

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
              classes (cal/get-classes data-source (jt/local-date 2023 11 1) (jt/local-date 2023 11 30))]
          (is (= 0 (count classes)))))
      (finally
        (.stop database-container)))))

(run-tests)
