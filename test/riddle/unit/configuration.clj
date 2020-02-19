(ns riddle.unit.configuration
  (:require [clojure.test :refer :all]
            [riddle.components.configuration :refer :all]))

(def valid-configuration
  {:server {:host "localhost"
            :port 8080}
   :rules [{:when {:type :equal?
                   :path [:request-method]
                   :value :get}
            :then {:type :insert
                   :path [:uri]
                   :value "http://localhost:5000/status"}}]})

(deftest test-valid-configuration
  (is (true? (valid? valid-configuration))))

(deftest test-invalid-configuration-server
  (is (false? (valid? (assoc-in valid-configuration [:server :port] "8080")))))

(deftest test-invalid-configuration-rules
  (is (false? (valid? (assoc-in valid-configuration [:rules 0 :when :type] "equal?")))))
