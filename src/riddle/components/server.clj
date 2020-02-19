(ns riddle.components.server
  (:require [clojure.tools.logging :refer [info]]
            [com.stuartsierra.component :as component]
            [org.httpkit.server :as server]
            [riddle.components.routes :as routes]))

(defrecord Server [server]
  component/Lifecycle

  (start [this]
    (let [configuration (-> this :configuration :configuration :server)
          server (server/run-server (routes/build-routes this) configuration)]
      (info "Starting server on" (str (:host configuration) ":" (:port configuration)))
      (assoc this :server server)))

  (stop [this]
    (when-let [server (:server this)]
      (server))
    (info "Stopping server")
    (assoc this :server nil)))
