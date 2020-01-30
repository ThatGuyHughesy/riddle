(ns riddle.components.routes
  (:require [compojure.core :refer [routes make-route GET PUT POST DELETE OPTIONS ANY]]
            [clj-http.client :as client]
            [riddle.http :as http]
            [riddle.rules :as rules]))

(defn process-request [fowarding-fn rules request]
  (if-let [processed (rules/process rules request)]
    (fowarding-fn processed)
    {:status 403}))

(defmulti handler
  (fn [request & _]
    (:request-method request)))

(defmethod handler :get [request client rules]
  (process-request
    (partial http/forward-request client client/get)
    rules
    request))

(defmethod handler :post [request client rules]
  (process-request
    (partial http/forward-request client client/post)
    rules
    request))

(defmethod handler :put [request client rules]
  (process-request
    (partial http/forward-request client client/put)
    rules
    request))

(defmethod handler :options [request client rules]
  (process-request
    (partial http/forward-request client client/options)
    rules
    request))

(defmethod handler :delete [request client rules]
  (process-request
    (partial http/forward-request client client/delete)
    rules
    request))

(defn build-routes [component]
  (let [client (:client component)
        rules (get-in component [:configuration :configuration :rules])]
    (routes
      (make-route :get "/*" #(handler % client rules))
      (make-route :post "/*" #(handler % client rules))
      (make-route :put "/*" #(handler % client rules))
      (make-route :options "/*" #(handler % client rules))
      (make-route :delete "/*" #(handler % client rules)))))
