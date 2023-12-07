(ns dev
  (:require
   [cludio.core :as core]
   [cludio.config :as config]
   [com.stuartsierra.component.repl :as component-repl]
   [malli.dev :as mdev]
   [malli.dev.pretty :as mpretty]
   [malli.core :as m]))

(component-repl/set-init
 (fn [old-system]
   (let [port (-> (config/read-config) :server :port)]
     (mdev/start! {:report (mpretty/thrower)})
     (core/api-system {:server {:port port} :db-spec {:jdbcUrl "jdbc:postgresql://localhost:5432/cludio"
                                                      :username "cludio"
                                                      :password "cludio"}}))))

(defn reload []
  (component-repl/reset))

(comment
  (reload)
  (mdev/start! {:report (mpretty/thrower)})
  (m/function-schemas)
  (component-repl/reset)
  (-> (config/read-config) :server :port))

