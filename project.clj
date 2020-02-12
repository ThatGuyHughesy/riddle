(defproject riddle "0.1.0-SNAPSHOT"
  :author "Conor Hughes <hello@conorhughes.me>"
  :description "Rule based HTTP request proxy and filter written in Clojure"
  :url "https://thatguyhughesy.github.io/riddle"
  :license {:name "MIT"
            :url "https://choosealicense.com/licenses/mit/"
            :distribution :repo}
  :min-lein-version "2.3.3"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/tools.logging "0.5.0"]
                 [org.clojure/core.async "0.7.559"]
                 [com.stuartsierra/component "0.4.0"]
                 [aero "1.1.3"]
                 [http-kit "2.3.0"]
                 [compojure "1.6.1"]
                 [medley "1.2.0"]]
  :repl-options {:init-ns riddle.core}
  :profiles {:test {:dependencies [[clj-http-fake "1.0.3"]
                                   [ring/ring-mock "0.4.0"]]}})
