(ns riddle.http)

(defn create-forward-url [client original-req]
  (cond-> (:url client)
          (:uri original-req) (str (:uri original-req))
          (:query-string original-req) (str "?" (:query-string original-req))))

(defn forward-request [client original-request new-request http-fn]
  (http-fn (create-forward-url client original-request)
           (assoc new-request
             :connection-manager (:connection-manager client)
             :headers (-> original-request :headers (dissoc "content-length"))
             :content-type :json
             :throw-exceptions false)))
