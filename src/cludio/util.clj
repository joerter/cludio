(ns cludio.util
  (:require [hiccup2.core :as h]))

(defn html-ok
  [body]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (-> body
             (h/html)
             (str))})
