(ns cludio.config
  (:require [aero.core :as aero]
            [clojure.java.io :as io]))

(defn read-config
  []
  (-> "config.edn" (io/resource) (aero/read-config)))

(def routes
  {:calendar {:month {:path "/calendar/month/:year/:month"
                      :build
                      (fn [year month]
                        (str "/calendar/month/" year "/" month))}}})

(comment (read-config))
