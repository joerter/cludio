(ns cludio.html
  (:require [hiccup2.core :as h]
            [hiccup.page :as p]))

(defn- full-page [body-content title]
  [:html {:class "h-full bg-white"} [:head
                                     [:title (str "Cludio - " title)]
                                     [:meta {:charset "UTF-8"}]
                                     [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
                                     [:link {:href "/dist/output.css" :rel "stylesheet"}]]
   [:body {:class "h-full"} body-content
    (p/include-js "/dist/htmx.min.js" "/dist/cdn.js")]])

(defn- ok
  [body]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (-> body
             (h/html)
             (str))})

(def full-page-interceptor
  {:name ::interceptor
   :leave (fn [{:keys [html title] :as context}]
            (assoc context :response (ok (full-page html title))))})
