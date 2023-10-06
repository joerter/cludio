(ns cludio.components.pedestal-component
  (:require [com.stuartsierra.component :as component]
            [io.pedestal.http.route :as route]
            [io.pedestal.http :as http]))

(defn response [status body]
  {:status status
   :body body
   :headers nil})

(def ok (partial response 200))

(defn echo [request]
  {:status 200
   :body "Hello world"})

(def get-todo-handler {:name :get-todo-handler :enter
                       (fn [context]
                         (let [request (:request context)
                               response (ok context)]
                           (assoc context :response response)))})

(def routes
  (route/expand-routes                                   
   #{["/greet" :get [echo] :route-name :greet]
     ["/todo/:list-id" :get get-todo-handler :route-name :get-todo]}))

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

            
            
            
            
