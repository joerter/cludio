(ns cludio.routes.app.calendar)

(def page
  ::app-calendar)

(defn root []
  [:h1 "Calendar"])

(def interceptor
  {:name ::interceptor
   :enter (fn [context]
            (assoc context :title "Calendar" :content (root)))})

(comment
  (assoc {} :page page :title "Calendar" :content (root)))
