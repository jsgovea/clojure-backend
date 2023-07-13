(ns webdev.core
  (:require [webdev.item.model :as items]
            [webdev.item.handler :refer [handle-index-items handle-created-item]])

  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [ring.middleware.file :refer [wrap-file]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [compojure.core :refer [defroutes ANY GET POST PUT DELETE]]
            [compojure.route :refer [not-found]]
            [ring.handler.dump :refer [handle-dump]]))

(def db "jdbc:postgresql://localhost/webdev")

(defn greet [req]
  {:status 200
   :body "Hello, world!"
   :headers {}})

(defn goodbye [req]
  {:status 200
   :body "Goodbye cruel world!"
   :headers {}})

(defn about [req]
  {:status 200
   :body "Hello, I am Jorge and I'm learning clojure"
   :headers {}})

(defn yo [req]
  (let [name (get-in req [:route-params :name])]
    {:status 200
     :body (str "Yo! " name "!")
     :headers {}}))

(def ops
  {"+" +
   "-" -
   "*" *
   ":" /})


(defn calc [req]
  (let [first-value (Integer. (get-in req [:route-params :first-value]))
        second-value (Integer. (get-in req [:route-params :second-value]))
        symbol (get-in req [:route-params :symbol])
        operation (get ops symbol)]
    (if operation
      {:status 200
       :body (str (operation first-value second-value))
       :headers {}}
      {:status 400
       :body (str "Unkown operator: " symbol)
       :headers {}})))

(defroutes routes
  (GET "/" [] greet)
  (GET "/goodbye" [] goodbye)
  (GET "/about" [] about)
  (GET "/yo/:name" [] yo)
  (GET "/calc/:first-value/:symbol/:second-value" [] calc)
  (ANY "/request" [] handle-dump)
  (GET "/items" [] handle-index-items)
  (POST "/items" [] handle-created-item)
  (not-found "Page not found."))

(defn wrap-db [hdlr]
  (fn [req]
    (hdlr (assoc req :webdev/db db))))

(defn wrap-server [hdlr]
  (fn [req]
    (assoc-in (hdlr req) [:headers "Server"] "Listronica 9000")))

(def app
  (wrap-server
   (wrap-file-info
    (wrap-resource
     (wrap-db
      (wrap-params
       routes))
     "static"))))

(defn -main [port]
  (items/create-table db)
  (jetty/run-jetty app
                   {:port (Integer. port)}))

(defn -dev-main [port]
  (items/create-table db)
  (jetty/run-jetty (wrap-reload #'app) {:port (Integer. port)}))