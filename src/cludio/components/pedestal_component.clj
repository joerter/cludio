(ns cludio.components.pedestal-component
  (:require [com.stuartsierra.component :as component]
            [io.pedestal.http.route :as route]
            [io.pedestal.http :as http]))

(defn echo [request]
  {:status 200
   :body "Clojure is incredible."})

(def routes
  (route/expand-routes                                   
   #{["/greet" :get [echo] :route-name :greet]}))

(defrecord PedestalComponent [config example-component]
  component/Lifecycle

  (start [component]
    (println "Starting Pedestal...")
    (let [server (-> {::http/routes routes
                      ::http/type :jetty
                      ::http/join? false
                      ::http/port (-> config :server :port)}
                    (http/create-server)
                    (http/start))]
      (assoc component :server server)))

  (stop [component]
    (println "Stopping Pedestal...")
    (when-let [server (:server component)]
      (http/stop server))
    (assoc component :server nil)))

(defn new-pedestal-component [config]
  (map->PedestalComponent {:config config}))

            
            
            
            
