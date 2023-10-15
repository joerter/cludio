(ns persistence.api.simple-test
  (:require [clojure.test :refer :all]
            [next.jdbc :as jdbc]
            [cludio.components.pedestal-component :refer [url-for]]
            [cludio.core :as core]
            [com.stuartsierra.component :as component]
            [clojure.string :as str])
  (:import (org.testcontainers.containers PostgreSQLContainer) (java.net ServerSocket)))

(defmacro with-system
  [[bound-var binding-expr] & body]
  `(let [~bound-var (component/start ~binding-expr)]
     (try
       ~@body
       (finally
         (component/stop ~bound-var)))))

(defn sut->url
  [sut path]
  (str/join ["http://localhost:"
             (-> sut :pedestal-component :config :server :port)
             path]))

(defn get-free-port []
  (with-open [socket (ServerSocket. 0)]
    (.getLocalPort socket)))

(deftest a-simple-persistence-test
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
        (is (= "" (-> (sut->url sut (url-for :info)))))
        (is (= "" (.getJdbcUrl database-container)))
        (let [ds (jdbc/get-datasource {:jdbcUrl (.getJdbcUrl database-container)
                                       :user (.getUsername database-container)
                                       :password (.getPassword database-container)})]
          (is (= {:r 1} (first (jdbc/execute! ds ["select 1 as r;"]))))))
      (finally
        (.stop database-container)))))

(comment (run-tests))
