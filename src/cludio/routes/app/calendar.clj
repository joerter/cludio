(ns cludio.routes.app.calendar)

(defn root []
  [:h1 "Calendar"])

(def interceptor
  {:name ::interceptor
   :enter (fn [context]
            (assoc context :title "Calendar" :content (root)))})
