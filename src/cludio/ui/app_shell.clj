(ns cludio.ui.app-shell)

(defn- app-section
  [name]
  [:li
  [:a {:href "#"
       :class "bg-indigo-700 text-white group flex gap-x-3 rounded-md p-2 text-sm leading-6 font-semibold"}
   [:svg {:class "h-6 w-6 shrink-0 text-white"
          :fill "none"
          :viewBox "0 0 24 24"
          :stroke-width "1.5"
          :stroke "currentColor"
          :aria-hidden "true"}
    [:path {:stroke-linecap "round"
            :stroke-linejoin "round"
            :d "M2.25 12l8.954-8.955c.44-.439 1.152-.439 1.591 0L21.75 12M4.5 9.75v10.125c0 .621.504 1.125 1.125 1.125H9.75v-4.875c0-.621.504-1.125 1.125-1.125h2.25c.621 0 1.125.504 1.125 1.125V21h4.125c.621 0 1.125-.504 1.125-1.125V9.75M8.25 21h8.25"}]]
   name]])

(defn- app-sections
  [links]
  [:li
   (into [:ul {:role "list" :class "-mx-2 space-y-1"}] (map app-section links))])

(defn- sidebar-nav
  [links]
  [:nav {:class "flex flex-1 flex-col"}
   [:ul {:role "list" :class "flex flex-1 flex-col gap-y-7"}
    (app-sections links)]])

(defn- sidebar-brand
  []
  [:div {:class "flex h-16 shrink-0 items-center"}
   [:img {:class "h-8 w-auto" 
        :src "https://tailwindui.com/img/logos/mark.svg?color=white" 
        :alt "Your Company"}]])

(defn sidebar
  [links]
  [:div {:class "flex grow flex-col gap-y-5 overflow-y-auto bg-indigo-600 px-6 pb-4"}
   (sidebar-brand)
   (sidebar-nav links)])

(comment
  (sidebar '("Dashboard" "Team" "Projects")))

(def content
  [:main {:class "py-10"}
   [:div {:class "px-4 sm:px-6 lg:px-8"}]])
