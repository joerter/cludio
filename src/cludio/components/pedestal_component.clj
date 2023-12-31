(ns cludio.components.pedestal-component
  (:require
   [cheshire.core :as json]
   [cludio.html :as html]
   [cludio.routes.app.calendar :as app-calendar]
   [cludio.routes.app.dashboard :as app-dashboard]
   [cludio.ui.app-shell :as app-shell]
   [com.stuartsierra.component :as component]
   [honey.sql :as sql]
   [io.pedestal.http :as http]
   [io.pedestal.http.content-negotiation :as content-negotiation]
   [io.pedestal.http.route :as route]
   [io.pedestal.interceptor :as interceptor]
   [next.jdbc :as jdbc]
   [next.jdbc.result-set :as rs]
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

(def db-get-todo-handler {:name :db-get-todo-handler :enter
                          (fn [{:keys [dependencies] :as context}]
                            (println "db-get-todo-handler")
                            (let [{:keys [data-source]} dependencies
                                  todo-id (-> context :request :path-params :todo-id (parse-uuid))
                                  todo-by-id-query (sql/format {:select :*
                                                                :from :todo
                                                                :where [:= :todo-id todo-id]})
                                  todo (jdbc/execute-one!
                                        (data-source)
                                        todo-by-id-query
                                        {:builder-fn rs/as-unqualified-kebab-maps})
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
   #{["/" :get [(html/full-page-interceptor app-dashboard/page) app-shell/interceptor app-dashboard/interceptor] :route-name :index-page]
     ["/calendar/month/:year/:month" :get [app-calendar/error-handler-interceptor (html/full-page-interceptor app-calendar/page) app-shell/interceptor app-calendar/interceptor] :route-name :app-calendar]}))

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
                      ::http/resource-path "/public"
                      ::http/port (-> config :server :port)
                      ::http/secure-headers {:content-security-policy-settings "default-src 'none'; script-src 'self' 'unsafe-eval'; style-src 'self' 'unsafe-inline'; img-src 'self'; font-src 'self'; connect-src 'self'; object-src 'none'; frame-src 'none'; base-uri 'self'; form-action 'self'"}}
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





