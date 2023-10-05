(ns cludio.core                                                ;; <1>
  (:require [io.pedestal.http :as http]                  ;; <2>
            [io.pedestal.http.route :as route]
            [io.pedestal.http.content-negotiation :as conneg]
            [clojure.string :as str]
            [clojure.data.json :as json]
            [cludio.config :as config]
            [com.stuartsierra.component :as component]
            [cludio.components.example-component :as example-component]))         ;; <3>

(def unmentionables #{"YHWH" "Voldemort" "Mxyzptlk" "Rumplestiltskin" "曹操"})

(def supported-types ["text/html" "application/edn" "application/json" "text/plain"])

(def content-neg-intc (conneg/negotiate-content supported-types))

(defn accepted-type
  [context]
  (get-in context [:request :accept :field] "text/plain"))

(defn transform-content
  [body content-type]
  (case content-type
    "text/html"        body
    "text/plain"       body
    "application/edn"  (pr-str body)
    "application/json" (json/write-str body)))

(defn coerce-to
  [response content-type]
  (-> response
      (update :body transform-content content-type)
      (assoc-in [:headers "Content-Type"] content-type)))

(defn ok [body]
  {:status 200 :body body})

(defn not-found []
  {:status 404 :body "Not found\n"})

(defn greeting-for [nm]
  (cond
    (unmentionables (str/lower-case nm)) nil
    (empty? nm)         "Hello, world!\n"
    :else               (str "Hello, " nm "\n")))

(defn respond-hello [request]
  (let [nm   (get-in request [:query-params :name])
        resp (greeting-for nm)]
    (if resp
      (ok resp)
      (not-found))))

(def echo
  {:name ::echo
   :enter #(assoc % :response (ok (:request %)))})

(def coerce-body
  {:name ::coerce-body
   :leave
   (fn [context]
     (cond-> context
       (nil? (get-in context [:response :headers "Content-Type"]))                    
       (update-in [:response] coerce-to (accepted-type context))))})

(def routes
  (route/expand-routes                                   ;; <1>
   #{["/greet" :get [coerce-body content-neg-intc respond-hello] :route-name :greet]
     [ "/echo" :get echo ]})) ;; <2>

(def service-map
  {::http/routes routes
   ::http/type   :jetty})

(defn start []
  (http/start (http/create-server service-map)))

                                                                                        ;; For interactive development
(defonce server (atom nil))                                                             ;; <1>

(defn start-dev [config]
  (reset! server                                                                        ;; <2>
          (http/start (http/create-server
                       (assoc service-map
                              ::http/join? false
                              ::http/port (-> config :server :port))))))                                   ;; <3>

(defn stop-dev []
  (http/stop @server))

(defn restart []                                                                        ;; <4>
  (stop-dev)
  (let [config (config/read-config)] 
    (start-dev config)))

(defn api-system
  [config]
  (component/system-map
    :example-component (example-component/new-example-component config)))

(defn -main
  []
  (let [system (-> (config/read-config) (api-system) (component/start-system))]
   (println "Starting Cludio with system" system) 
   (.addShutdownHook
     (Runtime/getRuntime)
     (new Thread #(component/stop-system system)))
    ))

(comment (-main) (stop-dev) (restart))
