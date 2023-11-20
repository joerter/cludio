(ns cludio.routes.app.calendar
  (:require [cludio.ui.calendar.month-view :as month-view]
            [java-time.api :as jt]
            [java-time.repl :as jtr]))

(def page
  ::app-calendar)

(defn month-view-start
  "Find the start date to show in the month view based on 42 days and week starting on Sunday"
  [first-of-month]
  (let [first-of-month-dow (-> first-of-month (jt/adjust :first-day-of-month) (jt/as :day-of-week))
        previous-month-days (if (= 7 first-of-month-dow) 0 first-of-month-dow)]
    (jt/minus first-of-month (jt/days previous-month-days))))

(defn generate-month [first-of-month]
  (let [start-date (month-view-start first-of-month)
        month-of-year (jt/as first-of-month :month-of-year)
        today (jt/local-date)]
    (loop [days (jt/iterate jt/plus start-date (jt/days 1))
           result []]
      (let [next-day (first days)]
        (if (>= (count result) 42)
          result
          (recur (rest days)
                 (conj result {:local-date next-day
                               :current-month? (= (jt/as next-day :month-of-year) month-of-year)
                               :today? (= next-day today)})))))))

(defn root [days]
  [:div {:class "lg:flex lg:h-full lg:flex-col"}
   (month-view/header)
   (month-view/month-calendar days)])

(def interceptor
  {:name ::interceptor
   :enter (fn [{:keys [:request] :as context}]
            (let [path-params (:path-params request)
                  year (Integer/parseInt (:year path-params))
                  month (Integer/parseInt (:month path-params))]
              (assoc context :title "Calendar" ::days (generate-month (jt/local-date year month 1)))))
   :leave (fn [{:keys [::days] :as context}]
            (assoc context :content (root days)))})
