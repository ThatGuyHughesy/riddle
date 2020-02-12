(ns riddle.components.routes
  (:require [clojure.core.async :refer [go chan <! <!!]]
            [compojure.core :refer [defroutes ANY]]
            [riddle.http :as http]
            [riddle.rules :as rules]))

(defn handle-request [request rules]
  (<!!
    (go
      (-> (rules/process rules request)
          (rules/deny?)
          (http/forward-request)
          <!))))

(defn build-routes [component]
  (let [rules (get-in component [:configuration :configuration :rules])]
    (defroutes build-routes
      (ANY "/" []
        (fn [request]
          (handle-request request rules))))))