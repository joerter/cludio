(ns cludio.ui.calendar.month-view)

(defn view-picker
  []
  [:div {:x-data "{viewPickerOpen: false}" :class "hidden md:ml-4 md:flex md:items-center"}
   [:div {:class "relative"}
    [:button {:type "button"
              "@click" "viewPickerOpen = !viewPickerOpen"
              :class "flex items-center gap-x-1.5 rounded-md bg-white px-3 py-2 text-sm font-semibold text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 hover:bg-gray-50"
              :id "menu-button"
              :aria-expanded "false"
              :aria-haspopup "true"} "Month view"
     [:svg
      {:class "-mr-1 h-5 w-5 text-gray-400"
       :viewBox "0 0 20 20"
       :fill "currentColor"
       :aria-hidden "true"}
      [:path
       {:fill-rule "evenodd"
        :d "M5.23 7.21a.75.75 0 011.06.02L10 11.168l3.71-3.938a.75.75 0 111.08 1.04l-4.25 4.5a.75.75 0 01-1.08 0l-4.25-4.5a.75.75 0 01.02-1.06z"
        :clip-rule "evenodd"}]]]
    [:div
     {:x-show "viewPickerOpen"
      :x-transition ""
      :class "absolute right-0 z-10 mt-3 w-36 origin-top-right overflow-hidden rounded-md bg-white shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none"
      :role "menu"
      :aria-orientation "vertical"
      :aria-labelledby "menu-button"
      :tabindex "-1"}
     [:div
      {:class "py-1",
       :role "none"}
      [:a
       {:href "#",
        :class "text-gray-700 block px-4 py-2 text-sm",
        :role "menuitem",
        :tabindex "-1"
        :id "menu-item-0"}
       "Day view"]
      [:a
       {:href "#",
        :class "text-gray-700 block px-4 py-2 text-sm",
        :role "menuitem",
        :tabindex "-1"
        :id "menu-item-1"}
       "Week view"]
      [:a
       {:href "#",
        :class "text-gray-700 block px-4 py-2 text-sm",
        :role "menuitem",
        :tabindex "-1"
        :id "menu-item-2"}
       "Month view"]
      [:a
       {:href "#",
        :class "text-gray-700 block px-4 py-2 text-sm",
        :role "menuitem",
        :tabindex "-1"
        :id "menu-item-3"}
       "Year view"]]]]])

(defn month-selector
  []
  [:div
   {:class "relative flex items-center rounded-md bg-white shadow-sm md:items-stretch"}
   [:button
    {:type "button"
     :class "flex h-9 w-12 items-center justify-center rounded-l-md border-y border-l border-gray-300 pr-1 text-gray-400 hover:text-gray-500 focus:relative md:w-9 md:pr-0 md:hover:bg-gray-50"}
    [:span {:class "sr-only"} "Previous month"]
    [:svg
     {:class "h-5 w-5",
      :viewBox "0 0 20 20",
      :fill "currentColor",
      :aria-hidden "true"}
     [:path
      {:fill-rule "evenodd",
       :d "M12.79 5.23a.75.75 0 01-.02 1.06L8.832 10l3.938 3.71a.75.75 0 11-1.04 1.08l-4.5-4.25a.75.75 0 010-1.08l4.5-4.25a.75.75 0 011.06.02z",
       :clip-rule "evenodd"}]]
    [:button
     {:type "button"
      :class "hidden border-y border-gray-300 px-3.5 text-sm font-semibold text-gray-900 hover:bg-gray-50 focus:relative md:block"}
     "January"]
    [:span {:class "relative -mx-px h-5 w-px bg-gray-300 md:hidden"}]
    [:button
     {:type "button"
      :class "flex h-9 w-12 items-center justify-center rounded-r-md border-y border-r border-gray-300 pl-1 text-gray-400 hover:text-gray-500 focus:relative md:w-9 md:pl-0 md:hover:bg-gray-50"}
     [:span {:class "sr-only"} "Next month"]
     [:svg
      {:class "h-5 w-5",
       :viewBox "0 0 20 20",
       :fill "currentColor",
       :aria-hidden "true"}
      [:path
       {:fill-rule "evenodd",
        :d "M7.21 14.77a.75.75 0 01.02-1.06L11.168 10 7.23 6.29a.75.75 0 111.04-1.08l4.5 4.25a.75.75 0 010 1.08l-4.5 4.25a.75.75 0 01-1.06-.02z",
        :clip-rule "evenodd"}]]]]])

(defn header
  []
  [:header {:class "flex items-center justify-between border-b border-gray-200 px-6 py-4 lg:flex-none"}
   [:h1 {:class "text-base font-semibold leading-6 text-gray-900"}
    [:time {:datetime "2022-01"} "January 2022"]]
   [:div {:class "flex items-center"}
    (month-selector)
    (view-picker)]])

(defn days-of-the-week
  []
  (list
   [:div {:class "flex justify-center bg-white py-2"}
    [:span "M"]
    [:span {:class "sr-only sm:not-sr-only"} "on"]]
   [:div {:class "flex justify-center bg-white py-2"}
    [:span "T"]
    [:span {:class "sr-only sm:not-sr-only"} "ue"]]
   [:div {:class "flex justify-center bg-white py-2"}
    [:span "W"]
    [:span {:class "sr-only sm:not-sr-only"} "ed"]]
   [:div {:class "flex justify-center bg-white py-2"}
    [:span "T"]
    [:span {:class "sr-only sm:not-sr-only"} "hu"]]
   [:div {:class "flex justify-center bg-white py-2"}
    [:span "F"]
    [:span {:class "sr-only sm:not-sr-only"} "ri"]]
   [:div {:class "flex justify-center bg-white py-2"}
    [:span "S"]
    [:span {:class "sr-only sm:not-sr-only"} "at"]]
   [:div {:class "flex justify-center bg-white py-2"}
    [:span "S"]
    [:span {:class "sr-only sm:not-sr-only"} "un"]]))

(defn month-day [{:keys [is-current-month is-today date day]}]
  (let [current-month "bg-white"
        other-month "bg-gray-50 text-gray-500"
        today "flex h-6 w-6 items-center justify-center rounded-full bg-indigo-600 font-semibold text-white"]
    [:div {:class (str (if is-current-month current-month other-month) "relative bg-gray-50 px-3 py-2 text-gray-500")}
     [:time {:datetime date :class (if is-today today "")} day]]))

(defn month-days
  [days]
  (into [:div {:class "hidden w-full lg:grid lg:grid-cols-7 lg:grid-rows-6 lg:gap-px"}] (map month-day days)))

(defn month-calendar
  [days]
  [:div {:class "shadow ring-1 ring-black ring-opacity-5 lg:flex lg:flex-auto lg:flex-col"}
   [:div {:class "grid grid-cols-7 gap-px border-b border-gray-300 bg-gray-200 text-center text-xs font-semibold leading-6 text-gray-700 lg:flex-none"}
    (days-of-the-week)]
   [:div {:class "flex bg-gray-200 text-xs leading-6 text-gray-700 lg:flex-auto"}
    (month-days days)]])
