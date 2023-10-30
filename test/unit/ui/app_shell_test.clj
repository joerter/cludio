(ns unit.ui.app-shell-test
  [:require [clojure.test :refer :all]
   [cludio.ui.app-shell :as app-shell]
   [cludio.ui.icons :as icons]
   [cludio.routes.app.dashboard :as dashboard]])

(defn contains-element? [hiccup elem]
  (some #(= % elem) (tree-seq coll? seq hiccup)))

(deftest interceptor-test
  (testing ":enter"
    (testing "adds sections to :sections"
          (let [enter (:enter app-shell/interceptor)
                context {}
                result (-> (enter context) ::app-shell/sections)]
            (is (= false (nil? result)))))
    (testing "adds sections to :sections with :isActive based on :page"
      (let [enter (:enter app-shell/interceptor)
            context {:page dashboard/page}
            expected {:name "Dashboard" :link "/" :icon icons/home :page dashboard/page :isActive true}
            result (-> (enter context) ::app-shell/sections)]
        (is (= expected (first (filter (fn [s] (= "Dashboard" (:name s)))  result)))))))
  (testing ":leave"
    (testing "adds rendered html to the :html keyword"
      (let [leave (:leave app-shell/interceptor)
            result (-> (leave {:page "" :content []}) :html)]
        (is (= false (nil? result)))))
    (testing ":html should include whatever was passed on :content"
      (let [leave (:leave app-shell/interceptor)
            fake-content [:div "Fake Content"]
            context {:page "" :content fake-content}
            result (-> (leave context) :html)]
        (is (= true (contains-element? result fake-content)))))))

(run-tests)

(comment
  (let [sut (:leave app-shell/interceptor)]
    (sut {}))
  (first (filter (fn [s] (= "Dashboard" (:name s))) [{:name "Dashboard"}
   {:name "Calendar" }
   {:name "Classes" }])))
