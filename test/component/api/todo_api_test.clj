(ns component.api.todo-api-test
  (:require
   [clj-http.client :as client]
   [clojure.string :as str]
   [clojure.test :refer :all]
   [cludio.components.pedestal-component :refer [url-for]]
   [cludio.core :as core]
   [com.stuartsierra.component :as component]
   [honey.sql :as sql]
   [next.jdbc :as jdbc]
   [next.jdbc.result-set :as rs])
  (:import
   (java.net ServerSocket)
   (org.testcontainers.containers PostgreSQLContainer)))

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

(deftest get-todo-test
  (let [database-container (PostgreSQLContainer. "postgres:15.4")]
    (try
      (.start database-container)
      (with-system
        [sut (core/api-system {:server {:port (get-free-port)}
                               :db-spec {:jdbcUrl (.getJdbcUrl database-container)
                                         :username (.getUsername database-container)
                                         :password (.getPassword database-container)}})]

        (let [{:keys [data-source]} sut
              {:keys [todo-id
                      title]} (jdbc/execute-one! (data-source) (-> {:insert-into [:todo]
                                                                    :columns [:title]
                                                                    :values [["My todo for test"]]
                                                                    :returning :*}
                                                                   (sql/format))
                                                 {:builder-fn rs/as-unqualified-kebab-maps})
              {:keys [status body]} (-> (sut->url sut (url-for :db-get-todo {:path-params {:todo-id todo-id}}))
                                        (client/get {:accept :json
                                                     :as :json
                                                     :throw-exceptions false})
                                        (select-keys [:body :status]))]
          (is (= 200 status))
          (is (some? (:created-at body)))
          (is (= {:todo-id (str todo-id) :title title} (select-keys body [:todo-id :title]))))
        (comment (testing "Empty body is returned for not found todo id"
                   (let [expected {:body "" :status 404}
                         actual (-> (sut->url sut (url-for :get-todo
                                                           {:path-params {:todo-id (random-uuid)}}))
                                    (client/get {:throw-exceptions false})
                                    (select-keys [:body :status]))]
                     (is (= expected actual))))))
      (finally
        (.stop database-container)))))

(comment
  (-> (str "http://localhost:8080/todo/9")
      (client/get)
      (select-keys [:body :status]))
  (-> (str "http://localhost:8080/greet") (client/get) (select-keys [:body :status]))
  (println (get-free-port)))
