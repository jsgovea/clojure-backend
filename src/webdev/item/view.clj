(ns webdev.item.view
  [:require [hiccup.page :refer [html5]]
   [hiccup2.core :as h]])

(defn new-item []
  (h/html [:form.form-horizontal
           {:method "POST" :action "/items"}
           [:div.form-group
            [:label.control-label.col-sm-2 {:for :name-input}
             "Name"]
            [:div.col-sm-10
             [:input#name-input.form-control
              {:name :name
               :placeholder "Name"}]]]
           [:div.form-group
            [:label.control-label.col-sm-2 {:for :desc-input}
             "Description"]
            [:div.col-sm-10
             [:input#desc-input.form-control
              {:name :description
               :placeholder "Description"}]]]
           [:div.form-group
            [:div.col-sm-offset-2.col-sm-10
             [:input.btn.btn-primary.mt-4
              {:type :submit
               :value "New Item"}]]]]))

(defn delete-item [id]
  (h/html
   [:form
    {:method "POST" :action (str "/items/" id)}
    [:input {:type :hidden
             :name "_method"
             :value "DELETE"}]
    [:div.btn-group
     [:input.btn.btn-danger.btn-sm
      {:type :submit
       :value "Delete"}]]]))

(defn update-item [id checked]
  (h/html
   [:form
    {:method "POST"
     :action (str "/items/" id)}
    [:input {:type :hidden
             :name "_method"
             :value "PUT"}]
    [:input {:type :hidden
             :name "checked"
             :value (if checked "false" "true")}]
    [:div.btn-group
     [:button.btn.btn-primary.btn-sm
      (if checked "DONE" "TODO")]]]))

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
                 [:th.col-sm-2]
                 [:th.col-sm-2]
                 [:th "Name"]
                 [:th "Description"]]]
               [:tbody
                (for [i items]
                  [:tr
                   [:td (delete-item (:id i))]
                   [:td (update-item (:id i) (:checked i))]
                   [:td (h/html (:name i))]
                   [:td (h/html (:description i))]])]]
              [:div.col-sm-offset-1 "There are no items."])]
           [:div.col-sm-6
            [:h2 "Create new item"]
            (new-item)]]
          [:script {:src "https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js"}]
          [:script {:script "bootstrap/js/bootstrap.min.js"}]]))
