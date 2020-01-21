(ns riddle.components.routes-test
  (:require [clojure.test :refer :all]
            [riddle.components.routes :refer :all]
            [ring.mock.request :as mock]))

(deftest get-handler
  (is (= (handler (mock/request :get "/test") nil)
         "GET")))

(deftest post-handler
  (is (= (handler (mock/request :post "/test") nil)
         "POST")))

(deftest put-handler
  (is (= (handler (mock/request :put "/test") nil)
         "PUT")))

(deftest option-handler
  (is (= (handler (mock/request :options "/test") nil)
         "OPTIONS")))

(deftest delete-handler
  (is (= (handler (mock/request :delete "/test") nil)
         "DELETE")))
