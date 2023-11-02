(ns cludio.routes.app.calendar
  (:require [cludio.ui.calendar.month-view :as month-view]))

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
                                  :is-today (= 1 n)})))]
    (conj january december)))

(defn root [days]
  [:div {:class "lg:flex lg:h-full lg:flex-col"}
   (month-view/header)
   (month-view/month-calendar days)])

(def interceptor
  {:name ::interceptor
   :enter (fn [context]
            (assoc context :title "Calendar" ::days (derived-days)))
   :leave (fn [{:keys [::days] :as context}]
            (assoc context :content (root days)))})

(comment (let [days (range 1 32)] (->> days (map (fn [n] {:day n})))))
