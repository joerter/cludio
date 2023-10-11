(ns cludio.core
  (:require [cludio.config :as config]
            [com.stuartsierra.component :as component]
            [cludio.components.example-component :as example-component]
            [cludio.components.pedestal-component :as pedestal-component]
            [cludio.components.in-memory-state-component :as in-memory-state-component]
            [next.jdbc.connection :as connection])
  (:import (com.zaxxer.hikari HikariDataSource)))

(defn api-system
  [config]
  (component/system-map
   :example-component (example-component/new-example-component config)
   :in-memory-state-component (in-memory-state-component/new-in-memory-state-component config)
   :data-source (connection/component HikariDataSource (:db-spec config))
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
