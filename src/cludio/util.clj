(ns cludio.util
  (:require [hiccup2.core :as h]
            [hiccup.page :as p]))

(defn page [body-content title]
  [:html {:class "h-full bg-white"} [:head
                                     [:title (str "Cludio - " title)]
                                     [:meta {:charset "UTF-8"}]
                                     [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
                                     [:link {:href "/dist/output.css" :rel "stylesheet"}]]
   [:body {:class "h-full"} body-content
    (p/include-js "/dist/htmx.min.js" "/dist/cdn.js")]])

(defn html-ok
  [body]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (-> body
             (h/html)
             (str))})
