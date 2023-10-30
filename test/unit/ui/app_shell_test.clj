(ns unit.ui.app-shell-test
  [:require [clojure.test :refer :all]
   [cludio.ui.app-shell :as app-shell]])

(defn contains-element? [hiccup elem]
  (some #(= % elem) (tree-seq coll? seq hiccup)))

(deftest interceptor-test
  (testing ":leave"
    (testing ":leave adds rendered html to the :html keyword on context"
      (let [leave (:leave app-shell/interceptor)
            result (-> (leave {:page "" :content []}) :html)]
        (is (= false (nil? result)))))
    (testing ":html should include whatever was passed on the :content keyword"
      (let [leave (:leave app-shell/interceptor)
            fake-content [:div "Fake Content"]
            context {:page "" :content fake-content}
            result (-> (leave context) :html)]
        (println result)
        (is (= true (contains-element? result fake-content)))))))

(comment (run-tests))

(comment
  (let [sut (:leave app-shell/interceptor)]
    (sut {})))
