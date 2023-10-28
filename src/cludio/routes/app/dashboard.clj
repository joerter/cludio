(ns cludio.routes.app.dashboard
  (:require
   [cludio.util :as util]
   [cludio.ui.app-shell :as app-shell]))

(defn root []
  [:h1 "Dashboard"])

(def loader
  {:name ::app-dashboard-loader
   :enter (fn [context]
            (assoc context :sections {}))})

(def handler
  {:name ::app-dashboard-handler
   :enter (fn [{:keys [sections] :as context}]
            (let [html (-> (root) (app-shell/render sections) (util/page "Dashboard"))]
              (assoc context :response (util/html-ok html))))})

(comment (util/page [:div "john"] "Dashboard"))
