(ns cludio.ui.app-shell
  (:require
   [cludio.ui.icons :as icons]))

(defn- app-section
  [{:keys [name link icon isActive]}]
  (let [active "bg-indigo-700 text-white"
        non-active "text-indigo-200 hover:text-white hover:bg-indigo-700"
        all " group flex gap-x-3 rounded-md p-2 text-sm leading-6 font-semibold"]
    [:li
     [:a {:href link
          :class (if isActive (str active all) (str non-active all))}
      icon
      name]]))

(defn- app-studio
  [{:keys [name link isActive]}]
  (let [active "bg-indigo-700 text-white"
        non-active "text-indigo-200 hover:text-white hover:bg-indigo-700"
        all " group flex gap-x-3 rounded-md p-2 text-sm leading-6 font-semibold"
        name-letter (subs name 0 1)]
    [:li
     [:a {:href link
          :class (if isActive (str active all) (str non-active all))}
      [:span {:class "flex h-6 w-6 shrink-0 items-center justify-center rounded-lg border border-indigo-400 bg-indigo-500 text-[0.625rem] font-medium text-white"} name-letter]
      [:span {:class "truncate"} name]]]))

(defn- app-studios
  [studios]
  [:li
   [:div {:class "text-xs font-semibold leading-6 text-indigo-200"} "Your Studios"
    (into [:ul {:role "list" :class "-mx-2 mt-2 space-y-1"}] (map app-studio studios))]])

(defn- app-sections
  [sections]
  [:li
   (into [:ul {:role "list" :class "-mx-2 space-y-1"}] (map app-section sections))])

(defn- app-settings
  []
  [:li {:class "mt-auto"}
   [:a {:href "#"
        :class "group -mx-2 flex gap-x-3 rounded-md p-2 text-sm font-semibold leading-6 text-indigo-200 hover:bg-indigo-700 hover:text-white"}
    [:svg {:class "h-6 w-6 shrink-0 text-indigo-200 group-hover:text-white"
           :fill "none"
           :viewBox "0 0 24 24"
           :stroke-width "1.5"
           :stroke "currentColor"
           :aria-hidden "true"}
     [:path {:stroke-linecap "round"
             :stroke-linejoin "round"
             :d "M9.594 3.94c.09-.542.56-.94 1.11-.94h2.593c.55 0 1.02.398 1.11.94l.213 1.281c.063.374.313.686.645.87.074.04.147.083.22.127.324.196.72.257 1.075.124l1.217-.456a1.125 1.125 0 011.37.49l1.296 2.247a1.125 1.125 0 01-.26 1.431l-1.003.827c-.293.24-.438.613-.431.992a6.759 6.759 0 010 .255c-.007.378.138.75.43.99l1.005.828c.424.35.534.954.26 1.43l-1.298 2.247a1.125 1.125 0 01-1.369.491l-1.217-.456c-.355-.133-.75-.072-1.076.124a6.57 6.57 0 01-.22.128c-.331.183-.581.495-.644.869l-.213 1.28c-.09.543-.56.941-1.11.941h-2.594c-.55 0-1.02-.398-1.11-.94l-.213-1.281c-.062-.374-.312-.686-.644-.87a6.52 6.52 0 01-.22-.127c-.325-.196-.72-.257-1.076-.124l-1.217.456a1.125 1.125 0 01-1.369-.49l-1.297-2.247a1.125 1.125 0 01.26-1.431l1.004-.827c.292-.24.437-.613.43-.992a6.932 6.932 0 010-.255c.007-.378-.138-.75-.43-.99l-1.004-.828a1.125 1.125 0 01-.26-1.43l1.297-2.247a1.125 1.125 0 011.37-.491l1.216.456c.356.133.751.072 1.076-.124.072-.044.146-.087.22-.128.332-.183.582-.495.644-.869l.214-1.281z"}]
     [:path {:stroke-linecap "round"
             :stroke-linejoin "round"
             :d "M15 12a3 3 0 11-6 0 3 3 0 016 0z"}]]
    "Settings"]])

(defn- sidebar-nav
  [sections studios]
  [:nav {:class "flex flex-1 flex-col"}
   [:ul {:role "list" :class "flex flex-1 flex-col gap-y-7"}
    (app-sections sections)
    (app-studios studios)
    (app-settings)]])

(defn- sidebar-brand
  []
  [:div {:class "flex h-16 shrink-0 items-center"}
   [:img {:class "h-8 w-auto"
          :src "https://tailwindui.com/img/logos/mark.svg?color=white"
          :alt "Your Company"}]])

(defn- sidebar
  [sections studios]
  [:div {:class "flex grow flex-col gap-y-5 overflow-y-auto bg-indigo-600 px-6 pb-4"}
   (sidebar-brand)
   (sidebar-nav sections studios)])

(defn- desktop-sidebar
  [sections studios]
  [:div {:class "hidden lg:fixed lg:inset-y-0 lg:z-50 lg:flex lg:w-72 lg:flex-col"}
   (sidebar sections studios)])

(def content
  [:main {:class "py-10"}
   [:div {:class "px-4 sm:px-6 lg:px-8"}]])

(def sections
  [{:name "Dashboard" :link "#" :icon icons/home :isActive true}
   {:name "Calendar" :link "#" :icon icons/calendar :isActive false}
   {:name "Classes" :link "#" :icon icons/academic-cap :isActive false}])

(def studios
  [{:name "NEBT School" :link "#" :isActive false}])

(defn render
  []
  [:div
   (desktop-sidebar sections studios)
   content])

(comment (render))
