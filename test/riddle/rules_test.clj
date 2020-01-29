(ns riddle.rules-test
  (:require [clojure.test :refer :all]
            [riddle.rules :refer :all]))

(def dummy-request {:uri "http://localhost:8080/test"
                    :headers {}
                    :body "Riddle is the best!"
                    :request-method :post
                    :server-port 80})

(def rules-match [{:when {:type :equals
                          :path [:request-method]
                          :value :post}
                   :then {:type :replace
                          :path [:uri]
                          :value "http://localhost:5000/status"}}
                  {:when {:type :less-than
                          :path [:server-port]
                          :value 81}
                   :then {:type :increment
                          :path [:server-port]
                          :value 11}}
                  {:when {:type :greater-than
                          :path [:server-port]
                          :value 90}
                   :then {:type :decrement
                          :path [:server-port]
                          :value 1}}])

(deftest rules-test
  (is (= (assoc dummy-request :uri "http://localhost:5000/status" :server-port 90)
         (process rules-match dummy-request)))
  (is (= dummy-request
         (-> (assoc-in rules-match [0 :when :value] :get)
             (assoc-in [1 :when :value] 80)
             (assoc-in [2 :when :value] 80)
             (process dummy-request)))))
