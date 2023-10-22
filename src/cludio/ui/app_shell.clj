(ns cludio.ui.app-shell)

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
  [links]
  [:li
   (into [:ul {:role "list" :class "-mx-2 space-y-1"}] (map app-section links))])

(defn- sidebar-nav
  [links studios]
  [:nav {:class "flex flex-1 flex-col"}
   [:ul {:role "list" :class "flex flex-1 flex-col gap-y-7"}
    (app-sections links)
    (app-studios studios)]])

(defn- sidebar-brand
  []
  [:div {:class "flex h-16 shrink-0 items-center"}
   [:img {:class "h-8 w-auto"
          :src "https://tailwindui.com/img/logos/mark.svg?color=white"
          :alt "Your Company"}]])

(defn sidebar
  [links studios]
  [:div {:class "flex grow flex-col gap-y-5 overflow-y-auto bg-indigo-600 px-6 pb-4"}
   (sidebar-brand)
   (sidebar-nav links studios)])

(comment
  (sidebar '("Dashboard" "Team" "Projects") '("NEBT School")))

(def content
  [:main {:class "py-10"}
   [:div {:class "px-4 sm:px-6 lg:px-8"}]])
