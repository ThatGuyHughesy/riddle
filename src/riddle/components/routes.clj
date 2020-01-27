(ns riddle.components.routes
  (:require [compojure.core :refer [routes make-route GET PUT POST DELETE OPTIONS ANY]]
            [clj-http.client :as client]
            [riddle.http :as http]))

(defmulti handler
  (fn [request & _]
    (:request-method request)))

(defmethod handler :get
  [request {:keys [client]}]
  (http/forward-request client request {} client/get))

(defmethod handler :post
  [request {:keys [client]}]
  (http/forward-request client request {:body (:body request)} client/post))

(defmethod handler :put
  [request {:keys [client]}]
  (http/forward-request client request {:body (:body request)} client/put))

(defmethod handler :options
  [request {:keys [client]}]
  (http/forward-request client request {} client/options))

(defmethod handler :delete
  [request {:keys [client]}]
  (http/forward-request client request {} client/delete))

(defn build-routes [component]
  (routes
    (make-route :get "/*" #(handler % component))
    (make-route :post "/*" #(handler % component))
    (make-route :put "/*" #(handler % component))
    (make-route :options "/*" #(handler % component))
    (make-route :delete "/*" #(handler % component))))
