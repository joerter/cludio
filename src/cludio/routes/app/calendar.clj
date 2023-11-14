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

(defn first-day-of-month [year month]
  (let [local-date (jt/local-date year month 1)]
    (cond
      (jt/sunday? local-date) :sunday
      (jt/monday? local-date) :monday
      (jt/tuesday? local-date) :tuesday
      (jt/wednesday? local-date) :wednesday
      (jt/thursday? local-date) :thursday
      (jt/friday? local-date) :friday
      (jt/saturday? local-date) :saturday)))

(first-day-of-month 2023 12)

(defn generate-month [year month]
  (println "generate-month" year month))

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

(comment (let [days (range 1 32)] (->> days (map (fn [n] {:day n})))))
