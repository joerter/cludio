(ns cludio.routes.app.calendar
  (:require [cludio.ui.calendar.month-view :as month-view]
            [cludio.db.classes :as classes-db]
            [cludio.config :as config]
            [java-time.api :as jt]
            [java-time.repl :as jtr]))

(def route-builder (-> config/routes :calendar :month :build))

(def page
  ::app-calendar)

(defn month-view-start
  "Find the start date to show in the month view based on 42 days and week starting on Sunday"
  [first-of-month]
  (let [first-of-month-dow (-> first-of-month (jt/adjust :first-day-of-month) (jt/as :day-of-week))
        previous-month-days (if (= 7 first-of-month-dow) 0 first-of-month-dow)]
    (jt/minus first-of-month (jt/days previous-month-days))))

(defn month-view [first-of-month]
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

(defn get-classes [db start end]
  (let [class-coll (classes-db/classes-by-date-range db start end)] 
    {}))

(defn generate-month [data-source first-of-month]
  (let [month-days (month-view first-of-month)
        start (-> (first month-days) :local-date)
        end (-> (last month-days) :local-date)
        classes (get-classes data-source start end)]
    (println classes)
    month-days))

(defn root [year month days next-month previous-month]
  (let [next-month-path (apply route-builder (jt/as next-month :year :month-of-year))
        prev-month-path (apply route-builder (jt/as previous-month :year :month-of-year))]
    [:div {:class "lg:flex lg:h-full lg:flex-col"}
     (month-view/header year month next-month-path prev-month-path)
     (month-view/month-calendar days)]))

(def interceptor
  {:name ::interceptor
   :enter (fn [{:keys [:request dependencies] :as context}]
            (let [path-params (:path-params request)
                  year (Integer/parseInt (:year path-params))
                  month (Integer/parseInt (:month path-params))
                  first-of-month (jt/local-date year month 1)
                  next-month (jt/plus first-of-month (jt/months 1))
                  previous-month (jt/minus first-of-month (jt/months 1))
                  {:keys [data-source]} dependencies]
              (assoc context :title "Calendar" ::next-month next-month ::previous-month previous-month ::year year ::month (jt/format "MMMM" first-of-month) ::days (generate-month data-source first-of-month))))
   :leave (fn [{:keys [::year ::month ::days ::next-month ::previous-month] :as context}]
            (assoc context :content (root year month days next-month previous-month)))})
