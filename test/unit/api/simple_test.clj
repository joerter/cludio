(ns unit.api.simple-test
  (:require [clojure.test :refer :all]))

(deftest simple-test
  (is (= 1 1)))

(comment (run-tests))
