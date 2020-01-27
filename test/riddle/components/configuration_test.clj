(ns riddle.components.configuration-test
  (:require [clojure.test :refer :all]
            [riddle.components.configuration :refer :all]))

(def valid-configuration
  {:server {:host "localhost"
            :port 8080
            :daemon? true
            :join? false}
   :client {:url "http://localhost:9200"
            :connection-manager {:threads 1
                                 :timeout 60}}})

(def invalid-configuration
  {:server {:host "localhost"
            :port 8080
            :daemon? true
            :join? "false"}
   :client {:url "http://localhost:9200"
            :connection-manager {:threads "1"
                                 :timeout 60}}})

(deftest test-valid-configuration
  (is (true? (valid? valid-configuration))))

(deftest test-invalid-configuration
  (is (false? (valid? invalid-configuration))))
