(ns riddle.rules-test
  (:require [clojure.test :refer :all]
            [riddle.rules :refer :all]))

(def dummy-request {:uri "http://localhost:8080/test"
                    :headers {}
                    :body "Riddle is the best!"
                    :request-method :post
                    :server-port 80})

(deftest when-test
  ":type :equals matches"
  (is true? (rule-when {:type :equals :path [:request-method] :value :post}
                       dummy-request))
  ":type :equals does matches"
  (is true? (rule-when {:type :equals :path [:request-method] :value :get}
                       dummy-request))
  ":type :greater-than matches"
  (is true? (rule-when {:type :greater-than :path [:server-port] :value 79}
                       dummy-request))
  ":type :greater-than does not matches"
  (is true? (rule-when {:type :greater-than :path [:server-port] :value 81}
                       dummy-request))
  ":type :less-than matches"
  (is true? (rule-when {:type :less-than :path [:server-port] :value 81}
                       dummy-request))
  ":type :less-than does not matches"
  (is true? (rule-when {:type :less-than :path [:server-port] :value 79}
                       dummy-request)))

(deftest then-test
  ":type :replace"
  (is (= :get (:request-method
                (rule-then
                  {:type :replace :path [:request-method] :value :get}
                  dummy-request))))
  ":type :increment"
  (is (= 81 (:server-port
              (rule-then
                {:type :increment :path [:server-port] :value 1}
                dummy-request))))
  ":type :increment non-integer"
  (is (= 80 (:server-port
              (rule-then
                {:type :increment :path [:server-port] :value "one"}
                dummy-request))))
  ":type :decrement"
  (is (= 79 (:server-port
              (rule-then
                {:type :decrement :path [:server-port] :value 1}
                dummy-request))))
  ":type :decrement non-integer"
  (is (= 80 (:server-port
              (rule-then
                {:type :decrement :path [:server-port] :value "one"}
                dummy-request)))))

(deftest process-test
  "allow"
  (is (= dummy-request
         (process [{:when {:type :equals :path [:request-method] :value :post}
                    :then {:type :allow}}
                   {:when {:type :equals :path [:request-method] :value :post}
                    :then {:type :replace :path [:request-method] :value :get}}]
                  dummy-request)))
  "deny"
  (is nil? (process [{:when {:type :equals :path [:request-method] :value :post}
                      :then {:type :replace :path [:request-method] :value :get}}
                     {:when {:type :equals :path [:request-method] :value :get}
                      :then {:type :deny}}]
                    dummy-request)))
