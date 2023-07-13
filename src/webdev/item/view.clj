(ns webdev.item.view
  [:require [hiccup.page :refer [html5]]
   [hiccup.core :refer [html h]]])

(defn items-page [items]
  (html5 {:lang :en}
         [:head
          [:title "Listronica"]
          [:meta {:name :viewport
                  :content "width=device-width, initial-scale=1.0"}]
          [:link {:href "/bootstrap/css/bootstrap.min.css"
                  :rel :stylesheet}]]
         [:body
          [:div.container
           [:h1 "My items"]
           [:div.row
            (if (seq items)
              [:table.table.table-striped
               [:thead
                [:tr
                 [:th "Name"]
                 [:th "Description"]]]
               [:tbody
                (for [i items]
                  [:tr
                   [:td (h (:name i))]
                   [:td (h (:description i))]])]]
              [:div.col-sm-offset-1 "There are no items."])]]
          [:script {:src "https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js"}]
          [:script {:script "bootstrap/js/bootstrap.min.js"}]]))
