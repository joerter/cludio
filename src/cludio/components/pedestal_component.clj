(ns cludio.components.pedestal-component
  (:require
   [cheshire.core :as json]
   [com.stuartsierra.component :as component]
   [io.pedestal.http :as http]
   [io.pedestal.http.body-params :as body-params]
   [io.pedestal.http.content-negotiation :as content-negotiation]
   [io.pedestal.http.route :as route]
   [io.pedestal.interceptor :as interceptor]
   [next.jdbc :as jdbc]
   [schema.core :as s]))

(s/defschema
  TodoItem
  {:id s/Str
   :name s/Str
   :status s/Str})

(s/defschema Todo
  {:id s/Str
   :name s/Str
   :items [TodoItem]})

(comment
  (s/validate Todo {:id "id" :name "some name" :items []}))

(def supported-types ["application/json"])

(def content-negotiation-interceptor (content-negotiation/negotiate-content supported-types))

(defn response
  ([status]
   (response status nil))
  ([status body]
   (merge
    {:status status
     :headers {"Content-Type" "application/json"}}
    (when body {:body (json/encode body)}))))

(def ok (partial response 200))
(def not-found (partial response 404))
(def created (partial response 201))

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
                                          (ok todo)
                                          (not-found))]
                           (assoc context :response response)))})

(defn save-todo!
  [{:keys [in-memory-state-component]} todo]
  (swap! (:state-atom in-memory-state-component) conj todo))

(def post-todo-handler {:name :post-todo-handler :enter
                        (fn [{:keys [dependencies] :as context}]
                          (println "post-todo-handler" (keys context))
                          (let [request (:request context)
                                todo (s/validate Todo (:json-params request))]
                            (save-todo! dependencies todo)
                            (assoc context :response (created todo))))})

(def info-handler {:name :info-handler :enter
                   (fn [{:keys [dependencies] :as context}]
                     (let [{:keys [data-source]} dependencies
                           db-response (first (jdbc/execute! (data-source) ["SHOW SERVER_VERSION"]))]
                       (clojure.pprint/pprint db-response)
                       (assoc context :response {:status 200 :body (str "Database server version" db-response)})))})

(def routes
  (route/expand-routes
   #{["/greet" :get [echo] :route-name :greet]
     ["/info" :get info-handler :route-name :info]
     ["/todo/:todo-id" :get get-todo-handler :route-name :get-todo]
     ["/todo" :post [(body-params/body-params) post-todo-handler] :route-name :post-todo]}))

(def url-for (route/url-for-routes routes))

(defn inject-dependencies [dependencies]
  (interceptor/interceptor
   {:name ::inject-dependencies
    :enter (fn [context]
             (assoc context :dependencies dependencies))}))

(defrecord PedestalComponent
           [config
            example-component
            in-memory-state-component
            data-source]
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





