(ns riddle.components.routes
  (:require [compojure.core :refer [routes make-route GET PUT POST DELETE OPTIONS ANY]]
            [clj-http.client :as client]
            [riddle.http :as http]
            [riddle.rules :as rules]))

(defmulti handler
  (fn [request & _]
    (:request-method request)))

(defmethod handler :get [request {:keys [client configuration]}]
  (->> (rules/process (get-in configuration [:configuration :rules]) request)
       (http/forward-request client client/get)))

(defmethod handler :post [request {:keys [client configuration]}]
  (http/forward-request
    client
    client/post
    (rules/process (get-in configuration [:configuration :rules]) request)
    {:body (:body request)}))

(defmethod handler :put [request {:keys [client configuration]}]
  (http/forward-request
    client
    client/put
    (rules/process (get-in configuration [:configuration :rules]) request)
    {:body (:body request)}))

(defmethod handler :options [request {:keys [client configuration]}]
  (http/forward-request
    client
    client/options
    (rules/process (get-in configuration [:configuration :rules]) request)))

(defmethod handler :delete [request {:keys [client configuration]}]
  (http/forward-request
    client
    client/delete
    (rules/process (get-in configuration [:configuration :rules]) request)))

(defn build-routes [component]
  (routes
    (make-route :get "/*" #(handler % component))
    (make-route :post "/*" #(handler % component))
    (make-route :put "/*" #(handler % component))
    (make-route :options "/*" #(handler % component))
    (make-route :delete "/*" #(handler % component))))
