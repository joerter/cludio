(ns cludio.routes.app.calendar
  (:require [cludio.ui.calendar.month-view :as month-view]))

(def page
  ::app-calendar)

(defn root []
  [:div {:class "lg:flex lg:h-full lg:flex-col"} 
   (month-view/header)
   (month-view/month-calendar)])

(def interceptor
  {:name ::interceptor
   :enter (fn [context]
            (assoc context :title "Calendar" :content (root)))})

(comment
  (assoc {} :page page :title "Calendar" :content (root)))
