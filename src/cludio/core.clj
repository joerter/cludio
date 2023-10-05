(ns cludio.core                                               
  (:require [cludio.config :as config]
            [com.stuartsierra.component :as component]
            [cludio.components.example-component :as example-component]
            [cludio.components.pedestal-component :as pedestal-component]))

(defn api-system
  [config]
  (component/system-map
    :example-component (example-component/new-example-component config)
    :pedestal-component (component/using (pedestal-component/new-pedestal-component config) [:example-component])))


(defn -main
  []
  (let [system (-> (config/read-config) (api-system) (component/start-system))]
   (println "Starting Cludio with system" system) 
   (.addShutdownHook
     (Runtime/getRuntime)
     (new Thread #(component/stop-system system)))
    ))

(comment (-main))
