(ns riddle.components.configuration-test
  (:require [clojure.test :refer :all]
            [riddle.components.configuration :refer :all]))

(def valid-configuration
  {:server {:host "localhost"
            :port 8080
            :daemon? true
            :join? false}
   :client {:threads 1
            :timeout 60}
   :rules [{:when {:type :equals
                   :path [:request-method]
                   :value :get}
            :then {:type :replace
                   :path [:uri]
                   :value "http://localhost:5000/status"}}]})

(deftest test-valid-configuration
  (is (true? (valid? valid-configuration))))

(deftest test-invalid-configuration-server
  (is (false? (valid? (assoc-in valid-configuration [:server :port] "8080")))))

(deftest test-invalid-configuration-client
  (is (false? (valid? (assoc-in valid-configuration [:client :threads] "1")))))

(deftest test-invalid-configuration-rules
  (is (false? (valid? (assoc-in valid-configuration [:rules 0 :when :type] "equals")))))
