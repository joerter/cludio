(ns cludio.routes.app.calendar
  (:require
   [cludio.util :as util]
   [cludio.ui.app-shell :as app-shell]))

(defn root []
  [:h1 "Calendar"])

(def handler
  {:name ::app-calendar-handler
   :enter (fn [{:keys [sections] :as context}]
            (let [html (-> (root) (app-shell/render sections) (util/page "Calendar"))]
              (assoc context :response (util/html-ok html))))})
