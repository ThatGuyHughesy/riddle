(ns riddle.http
  (:require [clojure.core.async :refer [chan put!]]
            [org.httpkit.client :as client]))

(defn get-request-type [{:keys [request-method]}]
  "Return HTTP client function based on request type"
  (case request-method
    :get client/get
    :post client/post
    :put client/put
    :options client/options
    :delete client/delete
    client/get))

(defn create-forward-url [request]
  "Generate destination URL for request"
  (cond-> (:uri request)
          (:query-string request) (str "?" (:query-string request))))

(defn forward-request [request]
  "Send request on to destination"
  (let [c (chan)]
    (if (= :deny request)
      (put! c {:status 403})
      (let [http-fn (get-request-type request)]
        (http-fn
          (create-forward-url request)
          (assoc request
            :headers (-> request :headers (dissoc "content-length"))
            :content-type :json
            :throw-exceptions false)
          (partial put! c))))
    c))