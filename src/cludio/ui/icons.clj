(ns cludio.ui.icons)

(def home
  [:svg {:class "h-6 w-6 shrink-0 text-white"
         :fill "none"
         :viewBox "0 0 24 24"
         :stroke-width "1.5"
         :stroke "currentColor"
         :aria-hidden "true"}
   [:path {:stroke-linecap "round"
           :stroke-linejoin "round"
           :d "M2.25 12l8.954-8.955c.44-.439 1.152-.439 1.591 0L21.75 12M4.5 9.75v10.125c0 .621.504 1.125 1.125 1.125H9.75v-4.875c0-.621.504-1.125 1.125-1.125h2.25c.621 0 1.125.504 1.125 1.125V21h4.125c.621 0 1.125-.504 1.125-1.125V9.75M8.25 21h8.25"}]])

(def calendar
  [:svg {:xmlns "http://www.w3.org/2000/svg"
         :fill "none"
         :viewBox "0 0 24 24"
         :stroke-width "1.5"
         :stroke "currentColor"
         :class "w-6 h-6"}
   [:path {:stroke-linecap "round"
           :stroke-linejoin "round"
           :d "M6.75 3v2.25M17.25 3v2.25M3 18.75V7.5a2.25 2.25 0 012.25-2.25h13.5A2.25 2.25 0 0121 7.5v11.25m-18 0A2.25 2.25 0 005.25 21h13.5A2.25 2.25 0 0021 18.75m-18 0v-7.5A2.25 2.25 0 015.25 9h13.5A2.25 2.25 0 0121 11.25v7.5"}]])

(def academic-cap
  [:svg {:xmlns "http://www.w3.org/2000/svg"
         :fill "none"
         :viewBox "0 0 24 24"
         :stroke-width "1.5"
         :stroke "currentColor"
         :class "w-6 h-6"}
   [:path {:stroke-linecap "round"
           :stroke-linejoin "round"
           :d "M4.26 10.147a60.436 60.436 0 00-.491 6.347A48.627 48.627 0 0112 20.904a48.627 48.627 0 018.232-4.41 60.46 60.46 0 00-.491-6.347m-15.482 0a50.57 50.57 0 00-2.658-.813A59.905 59.905 0 0112 3.493a59.902 59.902 0 0110.399 5.84c-.896.248-1.783.52-2.658.814m-15.482 0A50.697 50.697 0 0112 13.489a50.702 50.702 0 017.74-3.342M6.75 15a.75.75 0 100-1.5.75.75 0 000 1.5zm0 0v-3.675A55.378 55.378 0 0112 8.443m-7.007 11.55A5.981 5.981 0 006.75 15.75v-1.5"}]])

(def search
  [:svg {:class "pointer-events-none absolute inset-y-0 left-0 h-full w-5 text-gray-400"
         :viewBox "0 0 20 20"
         :fill "currentColor"
         :aria-hidden "true"}
   [:path {:fill-rule "evenodd"
           :d "M9 3.5a5.5 5.5 0 100 11 5.5 5.5 0 000-11zM2 9a7 7 0 1112.452 4.391l3.328 3.329a.75.75 0 11-1.06 1.06l-3.329-3.328A7 7 0 012 9z"
           :clip-rule "evenodd"}]])

(def notification-bell
  [:svg {:class "h-6 w-6"
         :fill "none"
         :viewBox "0 0 24 24"
         :stroke-width "1.5"
         :stroke "currentColor"
         :aria-hidden "true"}
   [:path {:stroke-linecap "round"
           :stroke-linejoin "round"
           :d "M14.857 17.082a23.848 23.848 0 005.454-1.31A8.967 8.967 0 0118 9.75v-.7V9A6 6 0 006 9v.75a8.967 8.967 0 01-2.312 6.022c1.733.64 3.56 1.085 5.455 1.31m5.714 0a24.255 24.255 0 01-5.714 0m5.714 0a3 3 0 11-5.714 0"}]])

(def chevron-down
  [:svg {:class "ml-2 h-5 w-5 text-gray-400"
         :viewBox "0 0 20 20"
         :fill "currentColor"
         :aria-hidden "true"}
   [:path {:fill-rule "evenodd"
           :d "M5.23 7.21a.75.75 0 011.06.02L10 11.168l3.71-3.938a.75.75 0 111.08 1.04l-4.25 4.5a.75.75 0 01-1.08 0l-4.25-4.5a.75.75 0 01.02-1.06z"
           :clip-rule "evenodd"}]])
