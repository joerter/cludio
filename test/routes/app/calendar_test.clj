(ns routes.app.calendar-test
  (:require [clojure.test :refer :all]
            [cludio.routes.app.calendar :as cal]
            [java-time.api :as jt]))

(deftest month-view-start
  (testing "returns the start date for month view based on 42 days and Sunday as first day of the week"
    (is (= (jt/local-date 2023 10 29) (cal/month-view-start (jt/local-date 2023 11 1))))
    (is (= (jt/local-date 2023 11 26) (cal/month-view-start (jt/local-date 2023 12 1))))
    (is (= (jt/local-date 2023 12 31) (cal/month-view-start (jt/local-date 2024 1 1))))))

(deftest generate-month
  (testing "always returns 42 days"
    (is (= 42 (count (cal/generate-month (jt/local-date 2023 10 1)))))
    (is (= 42 (count (cal/generate-month (jt/local-date 2023 11 1)))))
    (is (= 42 (count (cal/generate-month (jt/local-date 2023 12 1)))))))

(run-tests)
