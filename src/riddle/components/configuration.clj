(ns riddle.components.configuration
  (:require [clojure.pprint :as pp]
            [clojure.tools.logging :refer [error]]
            [clojure.spec.alpha :as s]
            [aero.core :refer [read-config]]
            [com.stuartsierra.component :as component]))

(s/def ::host string?)
(s/def ::port int?)
(s/def ::daemon? boolean?)
(s/def ::join? boolean?)
(s/def ::server (s/keys :req-un [::host ::port ::daemon? ::join?]))

(s/def ::url string?)
(s/def ::threads int?)
(s/def ::timeout int?)
(s/def ::connection-manager (s/keys :req-un [::threads ::timeout]))
(s/def ::client (s/keys :req-un [::url ::connection-manager]))

(s/def ::configuration (s/keys :req-un [::server ::client]))

(defn valid? [configuration]
  (->> (s/conform ::configuration configuration)
       (not= ::s/invalid)))

(defn explain [configuration]
  (let [reason (s/explain-data ::configuration configuration)]
    (error "Invalid configuration:" (with-out-str (pp/pprint reason)))
    (throw (ex-info "Invalid configuration:" reason))))

(defrecord Configuration [filename]
  component/Lifecycle

  (start [this]
    (let [configuration (read-config filename {})]
      (if (valid? configuration)
        (assoc this :configuration configuration)
        (explain configuration))))

  (stop [this]
    (assoc this :configuration nil)))
