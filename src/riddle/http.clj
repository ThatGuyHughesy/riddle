(ns riddle.http)

(defn create-forward-url [request]
  (cond-> (:uri request)
          (:query-string request) (str "?" (:query-string request))))

(defn forward-request
  ([client http-fn original-request]
   (forward-request client http-fn original-request {}))
  ([client http-fn original-request new-request]
   (http-fn (create-forward-url original-request)
            (assoc new-request
              :connection-manager (:connection-manager client)
              :headers (-> original-request :headers (dissoc "content-length"))
              :content-type :json
              :throw-exceptions false))))
