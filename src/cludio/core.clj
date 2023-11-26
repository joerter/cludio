(ns cludio.core
  (:require
   [clojure.tools.logging :as log]
   [cludio.components.example-component :as example-component]
   [cludio.components.in-memory-state-component :as in-memory-state-component]
   [cludio.components.pedestal-component :as pedestal-component]
   [cludio.config :as config]
   [com.stuartsierra.component :as component]
   [next.jdbc.date-time :as ndt]
   [next.jdbc.connection :as connection])
  (:import
   (com.zaxxer.hikari HikariDataSource)
   [org.flywaydb.core Flyway]))

(defn datasource-component [config]
  (connection/component HikariDataSource (assoc (:db-spec config)
                                                :init-fn (fn [datasource]
                                                           (log/info "Running Database init")
                                                           (ndt/read-as-local)
                                                           (.migrate
                                                            (.. (Flyway/configure)
                                                                (dataSource datasource)
                                                                (locations (into-array String ["classpath:database/migrations"]))
                                                                (table "schema_version")
                                                                (load)))))))

(defn api-system
  [config]
  (component/system-map
   :example-component (example-component/new-example-component config)
   :in-memory-state-component (in-memory-state-component/new-in-memory-state-component config)
   :data-source (datasource-component config)
   :pedestal-component (component/using (pedestal-component/new-pedestal-component config)
                                        [:example-component :data-source :in-memory-state-component])))

(defn -main
  []
  (let [system (-> (config/read-config) (api-system) (component/start-system))]
    (println "Starting Cludio with system" system)
    (.addShutdownHook
     (Runtime/getRuntime)
     (new Thread #(component/stop-system system)))))

(comment (-main))
