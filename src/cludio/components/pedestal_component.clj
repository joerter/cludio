(ns cludio.components.pedestal-component
  (:require
   [com.stuartsierra.component :as component]
   [io.pedestal.http :as http]
   [io.pedestal.http.route :as route]
   [io.pedestal.interceptor :as interceptor]
   [io.pedestal.http.content-negotiation :as content-negotiation]
   [cheshire.core :as json]))

(def supported-types ["application/json"])

(def content-negotiation-interceptor (content-negotiation/negotiate-content supported-types))

(defn response 
  ([status]
   (response status nil))
  ([status body]
  (merge 
    {:status status
     :headers {"Content-Type" "application/json"}}
    (when body {:body body}))))

(def ok (partial response 200))
(def not-found (partial response 404))

(defn echo [request]
  {:status 200
   :body "Hello world"})

(defn get-todo-by-id
  [{:keys [in-memory-state-component]} todo-id]
  (->> @(:state-atom in-memory-state-component)
       (filter (fn [todo]
                 (= todo-id (:id todo))))
       (first)))

(def get-todo-handler {:name :get-todo-handler :enter
                       (fn [{:keys [dependencies] :as context}]
                         (println "get-todo-handler" (keys context))
                         (let [request (:request context)
                               todo (get-todo-by-id dependencies
                                (-> request :path-params :todo-id))
                               response (if todo 
                                          (ok (json/encode todo))
                                          (not-found))]
                           (assoc context :response response)))})

(def routes
  (route/expand-routes                                   
   #{["/greet" :get [echo] :route-name :greet]
     ["/todo/:todo-id" :get get-todo-handler :route-name :get-todo]}))

(def url-for (route/url-for-routes routes))

(defn inject-dependencies [dependencies]
  (interceptor/interceptor
    {:name ::inject-dependencies
     :enter (fn [context]
              (assoc context :dependencies dependencies))}))

(defrecord PedestalComponent 
  [config 
   example-component 
   in-memory-state-component]
  component/Lifecycle

  (start [component]
    (println "Starting Pedestal...")
    (let [server (-> {::http/routes routes
                      ::http/type :jetty
                      ::http/join? false
                      ::http/port (-> config :server :port)}
                    (http/default-interceptors)
                    (update 
                      ::http/interceptors concat 
                      [(inject-dependencies component) content-negotiation-interceptor])
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

            
            
            
            
