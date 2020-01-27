(ns riddle.core
  (:require [com.stuartsierra.component :as component]
            [clojure.tools.logging :refer [info error]]
            [riddle.components.configuration :as configuration]
            [riddle.components.server :as server]
            [riddle.components.client :as client]))

(def system nil)

(defn riddle-system [filename]
  (component/system-map
    :configuration (configuration/map->Configuration {:filename filename})
    :client (component/using (client/map->Client {}) [:configuration])
    :server (component/using (server/map->Server {}) [:configuration :client])))

(defn init [filename]
  (alter-var-root
    #'system
    (fn [_] (riddle-system filename))))

(defn start []
  (alter-var-root
    #'system
    component/start))

(defn stop []
  (alter-var-root
    #'system
    (fn [s] (when s (component/stop s)))))

(defn -main [filename & _]
  (.addShutdownHook (Runtime/getRuntime) (Thread. stop))
  (init filename)
  (start))
