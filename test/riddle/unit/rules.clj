(ns riddle.unit.rules
  (:require [clojure.test :refer :all]
            [riddle.rules :refer :all]))

(def dummy-request {:uri "http://localhost:8080/test"
                    :headers {}
                    :body "Riddle is the best!"
                    :request-method :post
                    :server-port 80})

(deftest when-test
  ":equal? matches"
  (is true? (rule-when
              {:type :equal? :path [:request-method] :value :post}
              dummy-request))
  ":equal? does not match"
  (is true? (rule-when
              {:type :equal? :path [:request-method] :value :get}
              dummy-request))
  ":not-equal? matches"
  (is true? (rule-when
              {:type :not-equal? :path [:request-method] :value :get}
              dummy-request))
  ":not-equal? does not match"
  (is true? (rule-when
              {:type :not-equal? :path [:request-method] :value :post}
              dummy-request))
  ":greater-than? matches"
  (is true? (rule-when
              {:type :greater-than? :path [:server-port] :value 79}
              dummy-request))
  ":greater-than? does not match"
  (is false? (rule-when
               {:type :greater-than? :path [:server-port] :value 81}
               dummy-request))
  ":greater-than? invalid"
  (is true? (rule-when
              {:type :greater-than? :path [:server-port] :value "eighty-one"}
              dummy-request))
  ":less-than? matches"
  (is true? (rule-when
              {:type :less-than? :path [:server-port] :value 81}
              dummy-request))
  ":less-than? does not match"
  (is false? (rule-when
               {:type :less-than? :path [:server-port] :value 79}
               dummy-request))
  ":less-than? invalid"
  (is true? (rule-when
              {:type :less-than? :path [:server-port] :value "seventy-nine"}
              dummy-request))
  ":exist? matches"
  (is true? (rule-when
              {:type :exist? :path [:server-port]}
              dummy-request))
  ":exist? does not match"
  (is true? (rule-when
              {:type :exist? :path [:hostname]}
              dummy-request))
  ":not-exist? matches"
  (is true? (rule-when
              {:type :not-exist? :path [:hostname]}
              dummy-request))
  ":not-exist? does not match"
  (is true? (rule-when
              {:type :not-exist? :path [:server-port]}
              dummy-request)))

(deftest then-test
  ":insert"
  (is (= :get (:request-method
                (rule-then
                  {:type :insert :path [:request-method] :value :get}
                  dummy-request))))
  ":remove"
  (is (nil? (:request-method
              (rule-then
                {:type :remove :path [:request-method]}
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
  (is (= (assoc dummy-request :action :allow)
         (process [{:when {:type :equal? :path [:request-method] :value :post}
                    :then {:type :allow}}
                   {:when {:type :equal? :path [:request-method] :value :post}
                    :then {:type :insert :path [:request-method] :value :get}}]
                  dummy-request)))
  ":deny"
  (is :deny (process [{:when {:type :equal? :path [:request-method] :value :post}
                       :then {:type :insert :path [:request-method] :value :get}}
                      {:when {:type :equal? :path [:request-method] :value :get}
                       :then {:type :deny}}]
                     dummy-request)))
