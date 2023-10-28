(ns cludio.routes.app.dashboard
  (:require
   [cludio.util :as util]
   [cludio.ui.app-shell :as app-shell]))

(defn root []
  [:h1 "Dashboard"])

(def interceptor
  {:name ::interceptor
   :enter (fn [context]
            (assoc context :title "Dashboard" :content (root)))})

(comment (util/page [:div "john"] "Dashboard")
         (def handler
           {:name ::app-dashboard-handler
            :enter (fn [{:keys [::app-shell/sections] :as context}]
                     (let [html (-> (root) (app-shell/render sections) (util/page "Dashboard"))]
                       (assoc context :response (util/html-ok html))))}))
