(ns cludio.routes.app.root
  (:require
   [cludio.util :as util]
   [hiccup.page :as p]
   [cludio.ui.app-shell :as app-shell]))

(defn handler
  [request]
  (let [body [:html {:class "h-full bg-white"} [:head
                                                [:title "Cludio"]
                                                [:meta {:charset "UTF-8"}]
                                                [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
                                                [:link {:href "/dist/output.css" :rel "stylesheet"}]]
              [:body {:class "h-full"} (app-shell/render)
               (p/include-js "/dist/htmx.min.js" "/dist/_hyperscript.min.js" "/dist/cdn.js")]]]
    (util/html-ok body)))
