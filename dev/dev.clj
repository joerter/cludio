(ns dev
  (:require
    [cludio.core :as core]
    [com.stuartsierra.component.repl :as component-repl]))

(component-repl/set-init
  (fn [old-system]
    (core/api-system {:server {:port 3001}})))

(comment
  (component-repl/reset))
