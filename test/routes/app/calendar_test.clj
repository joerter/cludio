(ns routes.app.calendar-test
  (:require [clojure.test :refer :all]
            [cludio.routes.app.calendar :as cal]
            [java-time.api :as jt]))

(deftest month-view-start-and-end
  (testing "returns correct first day of month start and end day for 35 day calendar view"
    (is (= [(jt/local-date 2023 10 1) (jt/local-date 2023 10 1) (jt/local-date 2023 11 4)] (cal/month-view-start-and-end 2023 10)))
    (is (= [(jt/local-date 2023 11 1) (jt/local-date 2023 10 29) (jt/local-date 2023 12 2)] (cal/month-view-start-and-end 2023 11)))
    (is (= [(jt/local-date 2023 12 1) (jt/local-date 2023 11 26) (jt/local-date 2024 1 6)] (cal/month-view-start-and-end 2023 12)))))

(deftest generate-month
  (testing "always returns 42 days"
    (is (= 35 (count (cal/generate-month 2023 10))))))

(run-tests)
