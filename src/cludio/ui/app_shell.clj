(ns cludio.ui.app-shell)

(def header
  [:div {:class "sticky top-0 z-40 flex items-center gap-x-6 bg-indigo-600 px-4 py-4 shadow-sm sm:px-6 lg:hidden"}
 [:button {:type "button", :class "-m-2.5 p-2.5 text-indigo-200 lg:hidden"}
  [:span.sr-only "Open sidebar"]
  [:svg {:class "h-6 w-6", :fill "none", :viewBox "0 0 24 24", :stroke-width "1.5", :stroke "currentColor", :aria-hidden "true"}
   [:path {:stroke-linecap "round", :stroke-linejoin "round"} "M3.75 6.75h16.5M3.75 12h16.5m-16.5 5.25h16.5"]]]
 [:div.flex-1.text-sm.font-semibold.leading-6.text-white "Dashboard"]
 [:a {:href "#"}
  [:span.sr-only "Your profile"]
  [:img {:class "h-8 w-8 rounded-full bg-indigo-700", 
         :src "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd"}]]])

(def content
  [:main {:class "py-10 lg:pl-72"}
   [:div {:class "px-4 sm:px-6 lg:px-8"}]])
