(ns component.api.info-handler-test
  (:require
   [clojure.string :as str]
   [clojure.test :refer :all]
   [cludio.components.pedestal-component :refer [url-for]]
   [cludio.core :as core]
   [com.stuartsierra.component :as component]
   [clj-http.client :as client])
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

(deftest info-handler-test
  (let [database-container (doto (PostgreSQLContainer. "postgres:15.4")
                             (.withDatabaseName "cludio-db")
                             (.withUsername "test")
                             (.withPassword "test"))]
    (try
      (.start database-container)
      (with-system
        [sut (core/api-system {:server {:port (get-free-port)}
                               :db-spec {:jdbcUrl (.getJdbcUrl database-container)
                                         :username (.getUsername database-container)
                                         :password (.getPassword database-container)}})]
        (let [expected {:body "Database server version{:server_version \"15.4 (Debian 15.4-2.pgdg120+1)\"}" :status 200}
              actual (-> (sut->url sut (url-for :info))
                         (client/get {:throw-exceptions false})
                         (select-keys [:body :status]))]
          (is (= expected actual))))
      (finally
        (.stop database-container)))))

(comment (run-tests))

