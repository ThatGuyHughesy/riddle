(ns riddle.http)

(defn create-forward-url [request]
  (cond-> (:uri request)
          (:query-string request) (str "?" (:query-string request))))

(defn forward-request
  [client http-fn request]
  (http-fn (create-forward-url request)
           (assoc request
             :connection-manager (:connection-manager client)
             :headers (-> request :headers (dissoc "content-length"))
             :content-type :json
             :throw-exceptions false)))
