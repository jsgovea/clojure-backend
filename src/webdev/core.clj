(ns webdev.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :refer [not-found]]
            [ring.handler.dump :refer [handle-dump]]))

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



(defroutes app
  (GET "/" [] greet)
  (GET "/goodbye" [] goodbye)
  (GET "/about" [] about)
  (GET "/yo/:name" [] yo)
  (GET "/calc/:first-value/:symbol/:second-value" [] calc)
  (GET "/request" [] handle-dump)
  (not-found "Page not found."))

(defn -main [port]
  (jetty/run-jetty app
                   {:port (Integer. port)}))

(defn -dev-main [port]
  (jetty/run-jetty (wrap-reload #'app) {:port (Integer. port)}))