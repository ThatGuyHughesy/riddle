(ns riddle.components.routes
  (:require [compojure.core :refer [routes make-route GET PUT POST DELETE OPTIONS ANY]]))

(defmulti handler
  (fn [request & _]
    (:request-method request)))

(defmethod handler :get
  [request component]
  "GET")

(defmethod handler :post
  [request component]
  "POST")

(defmethod handler :put
  [request component]
  "PUT")

(defmethod handler :options
  [request component]
  "OPTIONS")

(defmethod handler :delete
  [request component]
  "DELETE")

(defn build-routes [component]
  (routes
    (make-route :get "/*" #(handler % component))
    (make-route :post "/*" #(handler % component))
    (make-route :put "/*" #(handler % component))
    (make-route :options "/*" #(handler % component))
    (make-route :delete "/*" #(handler % component))))
