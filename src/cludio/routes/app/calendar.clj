(ns cludio.routes.app.calendar
  (:require
   [cludio.config :as config]
   [cludio.db.classes :as classes-db]
   [cludio.ui.calendar.month-view :as month-view]
   [io.pedestal.interceptor.error :as p-error]
   [malli.util :as mu]
   [java-time.api :as jt]))

(def route-builder (-> config/routes :calendar :month :build))

(defn local-date->keyword [local-date]
  (->> local-date (jt/format "YYYY-MM-dd") keyword))

(def page
  ::app-calendar)

(def MonthDay
  [:map
   [:local-date :time/local-date]
   [:current-month? boolean?]
   [:today? boolean?]])

(def MonthDayWithClasses
  (mu/merge MonthDay [:map [:classes any?]]))

(def MonthDays
  [:vector MonthDay])

(def ScheduledClassesOnDay
  [:map-of :keyword any?])

(defn month-view-start
  "Find the start date to show in the month view based on 42 days and week starting on Sunday"
  {:malli/schema [:=> [:cat :time/local-date] :time/local-date]}
  [first-of-month]
  (let [first-of-month-dow (-> first-of-month (jt/adjust :first-day-of-month) (jt/as :day-of-week))
        previous-month-days (if (= 7 first-of-month-dow) 0 first-of-month-dow)]
    (jt/minus first-of-month (jt/days previous-month-days))))

(defn month-view
  "Finds the 42 days to display based on the first day of the month"
  {:malli/schema [:=> [:cat :time/local-date] MonthDays]}
  [first-of-month]
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

(defn get-classes
  "Gets classes from db based on start and end date. Transforms the coll into a map with local-date key"
  {:malli/schema [:=> [:cat :any :time/local-date :time/local-date] ScheduledClassesOnDay]}
  [db start end]
  (reduce (fn [acc {:keys [datetime] :as current-class}]
            (update acc (local-date->keyword datetime)
                    (fn [existing-value]
                      (if existing-value
                        (conj existing-value current-class)
                        [current-class])))) {}
          (classes-db/classes-by-date-range db start end)))

(defn enrich-days-with-classes
  "Adds a :classes key to month-days with the coll of classes on that day"
  {:malli/schema [:=> [:cat MonthDays ScheduledClassesOnDay] MonthDayWithClasses]}
  [month-days classes]
  (map (fn [{:keys [:local-date] :as day}]
         (assoc day 
                :classes (get classes (local-date->keyword local-date) []))) 
       month-days))

(defn generate-month
  "Generates all the days to display for the month, along with classes"
  {:malli/schema [:=> [:cat :any :time/local-date] :any]}
  [data-source first-of-month]
  (let [month-days (month-view first-of-month)
        start (-> (first month-days) :local-date)
        end (-> (last month-days) :local-date)]
    (enrich-days-with-classes month-days (get-classes data-source start end))))

(defn root
  "Build the month calendar UI"
  [year month days next-month previous-month]
  (let [next-month-path (apply route-builder (jt/as next-month :year :month-of-year))
        prev-month-path (apply route-builder (jt/as previous-month :year :month-of-year))]
    [:div {:class "lg:flex lg:h-full lg:flex-col"}
     (month-view/header year month next-month-path prev-month-path)
     (month-view/month-calendar days)]))

(def interceptor
  {:name ::interceptor
   :enter (fn [{:keys [:request dependencies] :as context}]
            (let [year (-> request :path-params :year Integer/parseInt)
                  month (-> request :path-params :month Integer/parseInt)
                  first-of-month (jt/local-date year month 1)
                  {:keys [data-source]} dependencies]
              (assoc context :title "Calendar"
                     ::next-month (jt/plus first-of-month (jt/months 1))
                     ::previous-month (jt/minus first-of-month (jt/months 1))
                     ::year year ::month (jt/format "MMMM" first-of-month)
                     ::days (generate-month data-source first-of-month))))
   :leave (fn [{:keys [::year ::month ::days ::next-month ::previous-month] :as context}]
            (assoc context :content (root year month days next-month previous-month)))})

(def error-handler-interceptor
  (p-error/error-dispatch [ctx ex]
                          [{:exception-type :java.lang.NumberFormatException :interceptor ::interceptor}]
                          (assoc ctx :response {:status 400 :body "Bad request"})
                          :else
                          (assoc ctx :io.pedestal.interceptor.chain/error ex)))

(comment
  (month-view (jt/local-date 2023 11 1)))
