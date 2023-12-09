(ns dev
  (:require
   [cludio.config :as config]
   [cludio.core :as core]
   [com.stuartsierra.component.repl :as component-repl]
   [malli.core :as m]
   [malli.dev :as mdev]
   [malli.dev.pretty :as mpretty]
   [malli.registry :as mr]
   [malli.experimental.time :as met]))

(component-repl/set-init
 (fn [old-system]
   (let [port (-> (config/read-config) :server :port)]
     (core/api-system {:server {:port port} :db-spec {:jdbcUrl "jdbc:postgresql://localhost:5432/cludio"
                                                      :username "cludio"
                                                      :password "cludio"}}))))

(defn reload []
  (mdev/start! {:report (mpretty/thrower)})
  (mr/set-default-registry!
   (mr/composite-registry
    (m/default-schemas)
    (met/schemas)))
  (component-repl/reset))

(comment
  (reload)
  (mdev/start! {:report (mpretty/thrower)})
  (m/function-schemas)
  (component-repl/reset)
  (-> (config/read-config) :server :port))

