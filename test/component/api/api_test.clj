(ns component.api.api-test
  (:require
   [cheshire.core :as json]
   [clj-http.client :as client]
   [clojure.string :as str]
   [clojure.test :refer :all]
   [cludio.components.pedestal-component :refer [url-for]]
   [cludio.core :as core]
   [com.stuartsierra.component :as component])
  (:import
   (java.net ServerSocket)))

(defmacro with-system
  [[bound-var binding-expr] & body]
  `(let [~bound-var (component/start ~binding-expr)]
     (try
       ~@body
       (finally
         (component/stop ~bound-var)))))

(defn get-free-port []
  (with-open [socket (ServerSocket. 0)]
    (.getLocalPort socket)))

(defn sut->url
  [sut path]
  (str/join ["http://localhost:"
             (-> sut :pedestal-component :config :server :port)
             path]))

(deftest greeting-test
  (with-system
    [sut (core/api-system {:server {:port (get-free-port)}})]
    (is (= {:body "Hello world" :status 200}
           (-> (sut->url sut (url-for :greet))
               (client/get)
               (select-keys [:body :status]))))))

(deftest get-todo-test
  (let [todo-id-1 (str (random-uuid))
        todo-1 {:id todo-id-1
                :name "My todo for test"
                :items [{:id (str (random-uuid)) :name "finish the test"}]}]
    (with-system
      [sut (core/api-system {:server {:port (get-free-port)}})]
      (reset! (-> sut :in-memory-state-component :state-atom)
              [todo-1])
      (let [expected {:body todo-1 :status 200}
            actual (-> (sut->url sut (url-for :get-todo {:path-params {:todo-id todo-id-1}}))
                       (client/get {:accept :json
                                    :as :json
                                    :throw-exceptions false})
                       (select-keys [:body :status]))]
        (is (= expected actual)))
      (testing "Empty body is returned for not found todo id"
        (let [expected {:body "" :status 404}
              actual (-> (sut->url sut (url-for :get-todo
                                                {:path-params {:todo-id (random-uuid)}}))
                         (client/get {:throw-exceptions false})
                         (select-keys [:body :status]))]
          (is (= expected actual)))))))

(deftest post-todo-test
  (let [todo-id-1 (str (random-uuid))
        todo-1 {:id todo-id-1
                :name "My todo for test"
                :items [{:id (str (random-uuid)) :name "finish the test"}]}]
    (with-system
      [sut (core/api-system {:server {:port (get-free-port)}})]
      (let [expected {:body todo-1 :status 201}
            actual (-> (sut->url sut (url-for :post-todo))
                       (client/post {:accept :json
                                     :content-type :json
                                     :as :json
                                     :throw-exceptions false
                                     :body (json/encode todo-1)})
                       (select-keys [:body :status]))]
        (is (= expected actual)))
      )))

(run-tests)

(comment
  (-> (str "http://localhost:8080/todo/9")
      (client/get)
      (select-keys [:body :status]))
  (-> (str "http://localhost:8080/greet") (client/get) (select-keys [:body :status]))
  (println (get-free-port)))
