(ns cludio.routes.app.dashboard)

(def page
  ::app-dashboard)

(defn root []
  [:h1 "Dashboard"])

(def interceptor
  {:name ::interceptor
   :enter (fn [context]
            (assoc context :page page :title "Dashboard" :content (root)))})
