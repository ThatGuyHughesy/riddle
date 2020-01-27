(ns riddle.components.client
  (:require [clojure.tools.logging :refer [info]]
            [com.stuartsierra.component :as component]
            [clj-http.conn-mgr :as conn-mgr]))

(defrecord Client [client]
  component/Lifecycle

  (start [this]
    (let [configuration (-> this :configuration :configuration :client)
          connection-manager (conn-mgr/make-reusable-conn-manager (:client configuration))]
      (info "Starting client for" (:url configuration))
      (-> this
          (assoc :url (:url configuration))
          (assoc :connection-manager connection-manager))))

  (stop [this]
    (when-let [connection-manager (:connection-manager client)]
      (conn-mgr/shutdown-manager connection-manager))
    (info "Stopping client")
    (assoc this :connection-manager nil)))
