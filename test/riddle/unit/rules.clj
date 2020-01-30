(ns riddle.unit.rules
  (:require [clojure.test :refer :all]
            [riddle.rules :refer :all]))

(def dummy-request {:uri "http://localhost:8080/test"
                    :headers {}
                    :body "Riddle is the best!"
                    :request-method :post
                    :server-port 80})

(deftest when-test
  ":equals matches"
  (is true? (rule-when
              {:type :equals :path [:request-method] :value :post}
              dummy-request))
  ":equals does matches"
  (is true? (rule-when
              {:type :equals :path [:request-method] :value :get}
              dummy-request))
  ":greater-than matches"
  (is true? (rule-when
              {:type :greater-than :path [:server-port] :value 79}
              dummy-request))
  ":greater-than does not matches"
  (is false? (rule-when
              {:type :greater-than :path [:server-port] :value 81}
              dummy-request))
  ":greater-than invalid"
  (is true? (rule-when
              {:type :greater-than :path [:server-port] :value "eighty-one"}
              dummy-request))
  ":less-than matches"
  (is true? (rule-when
              {:type :less-than :path [:server-port] :value 81}
              dummy-request))
  ":less-than does not matches"
  (is false? (rule-when
              {:type :less-than :path [:server-port] :value 79}
              dummy-request))
  ":less-than invalid"
  (is true? (rule-when
              {:type :less-than :path [:server-port] :value "seventy-nine"}
              dummy-request)))

(deftest then-test
  ":replace"
  (is (= :get (:request-method
                (rule-then
                  {:type :replace :path [:request-method] :value :get}
                  dummy-request))))
  ":increment"
  (is (= 81 (:server-port
              (rule-then
                {:type :increment :path [:server-port] :value 1}
                dummy-request))))
  ":increment non-integer"
  (is (= 80 (:server-port
              (rule-then
                {:type :increment :path [:server-port] :value "one"}
                dummy-request))))
  ":decrement"
  (is (= 79 (:server-port
              (rule-then
                {:type :decrement :path [:server-port] :value 1}
                dummy-request))))
  ":decrement non-integer"
  (is (= 80 (:server-port
              (rule-then
                {:type :decrement :path [:server-port] :value "one"}
                dummy-request)))))

(deftest process-test
  ":allow"
  (is (= dummy-request
         (process [{:when {:type :equals :path [:request-method] :value :post}
                    :then {:type :allow}}
                   {:when {:type :equals :path [:request-method] :value :post}
                    :then {:type :replace :path [:request-method] :value :get}}]
                  dummy-request)))
  ":deny"
  (is nil? (process [{:when {:type :equals :path [:request-method] :value :post}
                      :then {:type :replace :path [:request-method] :value :get}}
                     {:when {:type :equals :path [:request-method] :value :get}
                      :then {:type :deny}}]
                    dummy-request)))
