(ns riddle.components.server
  (:require [clojure.tools.logging :refer [info]]
            [ring.adapter.jetty :as jetty]
            [com.stuartsierra.component :as component]
            [riddle.components.routes :as routes]))

(defrecord Server [server]
  component/Lifecycle

  (start [this]
    (let [configuration (-> this :configuration :configuration :server)
          server (jetty/run-jetty (routes/build-routes this) configuration)]
      (info "Starting server on" (str (:host configuration) ":" (:port configuration)))
      (assoc this :server server)))

  (stop [this]
    (when-let [server (:server this)]
      (.stop server))
    (info "Stopping server")
    (assoc this :server nil)))
