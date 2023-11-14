(ns routes.app.calendar-test
  (:require [clojure.test :refer :all]
            [cludio.routes.app.calendar :as cal]))

(deftest first-day-of-month
  (testing "returns correct day of the week based on year and month"
    (is (= :wednesday (cal/first-day-of-month 2023 11)))
    (is (= :friday (cal/first-day-of-month 2023 12)))
    (is (= :monday (cal/first-day-of-month 2024 1)))))

(run-tests)
