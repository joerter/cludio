(ns util
  (:require [com.stuartsierra.component :as component]
            [cludio.core :as core])
  (:import
   (org.testcontainers.containers PostgreSQLContainer)))

(defmacro with-system
  [[bound-var binding-expr] & body]
  `(let [~bound-var (component/start ~binding-expr)]
     (try
       ~@body
       (finally
         (component/stop ~bound-var)))))

(defn datasource-only-system
  [config]
  (component/system-map
   :data-source (core/datasource-component config)))

(defn create-database-container
  []
  (PostgreSQLContainer. "postgres:15.4"))
