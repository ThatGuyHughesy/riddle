(ns riddle.components.routes
  (:require [compojure.core :refer [routes make-route GET PUT POST DELETE OPTIONS ANY]]
            [clj-http.client :as client]
            [riddle.http :as http]
            [riddle.rules :as rules]))

(defmulti handler
  (fn [request & _]
    (:request-method request)))

(defmethod handler :get [request {:keys [client configuration]}]
  (when-let [processed (rules/process (get-in configuration [:configuration :rules]) request)]
    (http/forward-request client client/get processed)))

(defmethod handler :post [request {:keys [client configuration]}]
  (when-let [processed (rules/process (get-in configuration [:configuration :rules]) request)]
    (http/forward-request client client/post processed {:body (:body request)})))

(defmethod handler :put [request {:keys [client configuration]}]
  (when-let [processed (rules/process (get-in configuration [:configuration :rules]) request)]
    (http/forward-request client client/put processed {:body (:body request)})))

(defmethod handler :options [request {:keys [client configuration]}]
  (when-let [processed (rules/process (get-in configuration [:configuration :rules]) request)]
    (http/forward-request client client/options processed)))

(defmethod handler :delete [request {:keys [client configuration]}]
  (when-let [processed (rules/process (get-in configuration [:configuration :rules]) request)]
    (http/forward-request client client/delete processed)))

(defn build-routes [component]
  (routes
    (make-route :get "/*" #(handler % component))
    (make-route :post "/*" #(handler % component))
    (make-route :put "/*" #(handler % component))
    (make-route :options "/*" #(handler % component))
    (make-route :delete "/*" #(handler % component))))
