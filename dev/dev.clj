(ns dev
  (:require
    [cludio.core :as core]
    [cludio.config :as config]
    [com.stuartsierra.component.repl :as component-repl]))

(component-repl/set-init
  (fn [old-system]
    (let [port (-> (config/read-config) :server :port)] 
      (core/api-system {:server {:port port} :db-spec {:jdbcUrl "jdbc:postgresql://localhost:5432/cludio"
                                                       :username "cludio"
                                                       :password "cludio"}}))))

(defn reload []
  (component-repl/reset))

(comment
  (reload)
  (component-repl/reset)
  (-> (config/read-config) :server :port))

