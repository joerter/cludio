(ns cludio.ui.app-shell
  (:require
   [cludio.ui.icons :as icons]
   [cludio.routes.app.dashboard :as dashboard]
   [cludio.routes.app.calendar :as calendar]))

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

(def close-sidebar-button
  [:div {:class "absolute left-full top-0 flex w-16 justify-center pt-5"}
 [:button {:type "button" :class "-m-2.5 p-2.5"}
  [:span {:class "sr-only"} "Close sidebar"]
  [:svg {:class "h-6 w-6 text-white" :fill "none" :viewBox "0 0 24 24" :stroke-width "1.5" :stroke "currentColor" :aria-hidden "true"}
   [:path {:stroke-linecap "round" :stroke-linejoin "round" :d "M6 18L18 6M6 6l12 12"}]]]])

(def off-canvas-menu-backdrop
  [:div {:class "fixed inset-0 bg-gray-900/80"}])

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
          :src "/images/mark.svg"
          :alt "Your Company"}]])

(defn- sidebar
  [sections studios]
  [:div {:class "flex grow flex-col gap-y-5 overflow-y-auto bg-indigo-600 px-6 pb-4" :data-element "sidebar"}
   (sidebar-brand)
   (sidebar-nav sections studios)])

(defn- mobile-sidebar
  [sections studios]
  [:div {:class "relative z-50 lg:hidden" :role "dialog" :aria-modal "true" :data-element "mobile-sidebar"}
   off-canvas-menu-backdrop
   [:div {:class "fixed inset-0 flex"}
    close-sidebar-button
    (sidebar sections studios)]])

(defn- desktop-sidebar
  [sections studios]
  [:div {:class "hidden lg:fixed lg:inset-y-0 lg:z-50 lg:flex lg:w-72 lg:flex-col" :data-element "desktop-sidebar"}
   (sidebar sections studios)])

(defn content [app-content]
  [:main {:class "py-10" :data-element "app-content"}
   [:div {:class "px-4 sm:px-6 lg:px-8"}
    app-content]])

(def open-sidebar
  [:button {:type "button"
            :class "-m-2.5 p-2.5 text-gray-700 lg:hidden"}
   [:span {:class "sr-only"} "Open sidebar"]
   [:svg {:class "h-6 w-6"
          :fill "none"
          :viewBox "0 0 24 24"
          :stroke-width "1.5"
          :stroke "currentColor"
          :aria-hidden "true"}
    [:path {:stroke-linecap "round"
            :stroke-linejoin "round"
            :d "M3.75 6.75h16.5M3.75 12h16.5m-16.5 5.25h16.5"}]]])

(def separator
  [:div {:class "h-6 w-px bg-gray-900/10 lg:hidden" :aria-hidden "true"}])

(defn- profile-dropdown-menu []
  [:div {:class "absolute right-0 z-10 mt-2.5 w-32 origin-top-right rounded-md bg-white py-2 shadow-lg ring-1 ring-gray-900/5 focus:outline-none"
         :role "menu"
         :aria-orientation "vertical"
         :aria-labelledby "user-menu-button"
         :tabindex "-1"
         :x-show "profileDropdownMenuOpen"
         :x-transition ""}
   [:a {:href "#"
        :class "block px-3 py-1 text-sm leading-6 text-gray-900"
        :role "menuitem"
        :tabindex "-1"
        :id "user-menu-item-0"} "Your profile"]
   [:a {:href "#"
        :class "block px-3 py-1 text-sm leading-6 text-gray-900"
        :role "menuitem"
        :tabindex "-1"
        :id "user-menu-item-1"} "Sign out"]])

(def ^:private profile-dropdown
  [:div {:class "relative"
         :x-data "{profileDropdownMenuOpen: false}"}
   [:button {:type "button"
             :class "-m-1.5 flex items-center p-1.5"
             :id "user-menu-button"
             :aria-expanded "false"
             :aria-haspopup "true"
             "@click" "profileDropdownMenuOpen = !profileDropdownMenuOpen"}
    [:span {:class "sr-only"} "Open user menu"]
    [:img {:class "h-8 w-8 rounded-full bg-gray-50"
           :src "/images/profile.avif"
           :alt ""}]
    [:span {:class "hidden lg:flex lg:items-center"}
     [:span {:class "ml-4 text-sm font-semibold leading-6 text-gray-900"
             :aria-hidden "true"} "Tom Cook"]
     icons/chevron-down]]
   (profile-dropdown-menu)])

(def header
  [:div {:class "flex flex-1 gap-x-4 self-stretch lg:gap-x-6"}
   [:form {:class "relative flex flex-1 m-0"
           :action "#"
           :method "GET"}
    [:label {:for "search-field"
             :class "sr-only"} "Search"]
    icons/search
    [:input {:id "search-field"
             :class "block h-full w-full border-0 py-0 pl-8 pr-0 text-gray-900 placeholder:text-gray-400 focus:ring-0 sm:text-sm"
             :placeholder "Search..."
             :type "search"
             :name "search"}]]
   [:div {:class "flex items-center gap-x-4 lg:gap-x-6"}
    [:button {:type "button"
              :class "-m-2.5 p-2.5 text-gray-400 hover:text-gray-500"}
     [:span {:class "sr-only"} "View notifications"]
     icons/notification-bell]
    [:div {:class "hidden lg:block lg:h-6 lg:w-px lg:bg-gray-900/10"}]
    profile-dropdown]])

(defn right-side [app-content]
  [:div {:class "lg:pl-72"}
   [:div {:class "sticky top-0 z-40 flex h-16 shrink-0 items-center gap-x-4 border-b border-gray-200 bg-white px-4 shadow-sm sm:gap-x-6 sm:px-6 lg:px-8"}
    open-sidebar
    separator
    header]
   (content app-content)])

(def studios
  [{:name "NEBT School" :link "#" :isActive false}])

(defn render
  [sections app-content]
  (println sections)
  [:div
   (mobile-sidebar sections studios)
   (desktop-sidebar sections studios)
   (right-side app-content)])

(def static-sections
  [{:name "Dashboard" :link "/" :icon icons/home :page dashboard/page}
   {:name "Calendar" :link "/calendar" :icon icons/calendar :page calendar/page}
   {:name "Classes" :link "/classes" :icon icons/academic-cap :page nil}])

(defn get-sections
  [page]
  (map (fn [section] (assoc section :isActive (= page (:page section)))) static-sections))

(def interceptor
  {:name ::interceptor
   :leave (fn [{:keys [page content] :as context}]
            (assoc context :html (render (get-sections page) content)))})

