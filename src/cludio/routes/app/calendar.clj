(ns cludio.routes.app.calendar
  (:require [cludio.ui.calendar.month-view :as month-view]
            [java-time.api :as jt]
            [java-time.repl :as jtr]))

(def page
  ::app-calendar)

(defn derived-days
  []
  (let [december {:date "2022-12-31"
                  :day "31"
                  :is-current-month false
                  :is-today false}
        days (range 1 32)
        january (->> days (map (fn
                                 [n]
                                 {:date (str "2023-01-" n)
                                  :day n
                                  :is-current-month true
                                  :is-today (= 1 n)})))
        february (->> (range 1 11) (map (fn [n] {:date (str "2023-02-" n)
                                                 :day n
                                                 :is-current-month false
                                                 :is-today false})))]
    (conj (concat january february) december)))

(defn previous-month-days
  "The number of days from the previous month to show based on the first day of the month and a 42 day month view.
  The Java Date-Time API uses integer values of 1-7 for days of the week starting with Monday.
  However, we want to show Sunday as the first day of the week"
  [first-day-of-month]
  (let [day-of-week (jt/as first-day-of-month :day-of-week)]
    (if (= 7 day-of-week) 0 day-of-week)))

(defn next-month-days
  "The number of days from the next month to show based on the last day of the month and a 42 day month view.
  The Java Date-Time API uses integer values of 1-7 for days of the week starting with Monday.
  However, we want to show Sunday as the first day of the week"
  [last-day-of-month]
  (let [day-of-week (jt/as last-day-of-month :day-of-week)]
    (cond
      (= 7 day-of-week) 6
      (= 1 day-of-week) 5
      (= 2 day-of-week) 4
      (= 3 day-of-week) 3
      (= 4 day-of-week) 2
      (= 5 day-of-week) 1
      (= 6 day-of-week) 0)))

(defn month-view-start-and-end 
  "The start and end dates based for a 42 day calendar view for a given year and month"
  [year month]
  (let [first-day-of-month (jt/local-date year month 1)
        last-day-of-month (jt/adjust first-day-of-month :last-day-of-month)
        start-date (jt/minus first-day-of-month (jt/days (previous-month-days first-day-of-month)))
        end-date (jt/plus last-day-of-month (jt/days (next-month-days last-day-of-month)))]
    [start-date end-date]))
(month-view-start-and-end 2023 12)

(defn generate-month [year month]
  (let [[start end] (month-view-start-and-end year month)]))

(defn root [days]
  [:div {:class "lg:flex lg:h-full lg:flex-col"}
   (month-view/header)
   (month-view/month-calendar days)])

(def interceptor
  {:name ::interceptor
   :enter (fn [{:keys [:request] :as context}]
            (let [path-params (:path-params request)
                  year (:year path-params)
                  month (:month path-params)]
              (generate-month year month)
              (assoc context :title "Calendar" ::days (derived-days))))
   :leave (fn [{:keys [::days] :as context}]
            (assoc context :content (root days)))})
