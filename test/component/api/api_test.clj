(ns component.api.api-test
  (:require
   [clj-http.client :as client]
   [cludio.core :as core]
   [com.stuartsierra.component :as component]
   [clojure.test :refer :all]))

(defmacro with-system
  [[bound-var binding-expr] & body]
  `(let [~bound-var (component/start ~binding-expr)]
     (try
       ~@body
       (finally
         (component/stop ~bound-var)))))

(deftest greeting-test
  (with-system 
    [sut (core/api-system {:server {:port 9000}})]
      (is (= {:body "Hello world" :status 200} 
             (-> (str "http://localhost:" 9000 "/greet")
                 (client/get)
                 (select-keys [:body :status]))))))

(deftest get-todo-test
  (let [todo-id-1 (random-uuid)
        todo-1 {:id todo-id-1
                :name "My todo for test"
                :items [{:id (random-uuid) :name "finish the test"}]}] 
    (with-system 
      [sut (core/api-system {:server {:port 9000}})]
        (reset! (-> sut :in-memory-state-component :state-atom)
                [todo-1])
        (let [expected {:body (pr-str todo-1) :status 200}
              actual (-> (str "http://localhost:" 9000 "/todo/" todo-id-1)
                       (client/get)
                       (select-keys [:body :status]))] 
          (is (= expected actual)))
        (testing "Empty body is returned for not found todo id"
          (let [expected {:body "" :status 200}
                actual (-> (str "http://localhost:" 9000 "/todo/" (random-uuid))
                       (client/get)
                       (select-keys [:body :status]))]
            (is (= expected actual)))))))

(deftest simple-test
  (is (= 1 1)))

(run-tests)

(comment 
  (-> (str "http://localhost:8080/todo/9")
                   (client/get)
                   (select-keys [:body :status]))
  (-> (str "http://localhost:8080/greet") (client/get) (select-keys [:body :status])))
