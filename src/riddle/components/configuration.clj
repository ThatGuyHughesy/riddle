(ns riddle.components.configuration
  (:require [clojure.pprint :as pp]
            [clojure.tools.logging :refer [error]]
            [clojure.spec.alpha :as s]
            [aero.core :refer [read-config]]
            [com.stuartsierra.component :as component]))

(s/def ::host string?)
(s/def ::port number?)
(s/def ::daemon? boolean?)
(s/def ::join? boolean?)
(s/def ::server (s/keys :req-un [::host ::port ::daemon? ::join?]))

(s/def ::threads number?)
(s/def ::timeout number?)
(s/def ::client (s/keys :opt-un [::threads ::timeout]))

(s/def ::path (s/coll-of keyword?))
(s/def ::value (s/or :value keyword? :value string? :value number? :value boolean?))
(s/def :when/type #{:equals :greater-than :less-than})
(s/def ::when (s/keys :req-un [:when/type ::path ::value]))
(s/def :then/type #{:replace :increment :decrement :allow :deny})
(s/def ::then (s/keys :req-un [:then/type] :opt-un [::path ::value]))
(s/def ::rule (s/keys :req-un [::when ::then]))
(s/def ::rules (s/* ::rule))

(s/def ::configuration (s/keys :req-un [::server ::client ::rules]))

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
