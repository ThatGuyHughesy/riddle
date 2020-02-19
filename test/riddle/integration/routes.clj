(ns riddle.integration.routes
  (:require [clojure.test :refer :all]
            [riddle.components.routes :refer :all]
            [ring.mock.request :as mock]
            [clj-http.fake :as fake]))

(def dummy-response {:status 200
                     :headers {}
                     :body "Riddle is the best!"
                     :random :field})

(deftest test-handler
  (fake/with-fake-routes
    {"http://localhost:5000/api"
     (fn [_] dummy-response)}
    (->> (map
           (fn [request-type]
             (-> (mock/request request-type "/")
                 (assoc :uri "http://localhost:8080")
                 (handle-request [{:when {:type :equal?
                                          :path [:uri]
                                          :value "http://localhost:8080"}
                                   :then {:type :insert
                                          :path [:uri]
                                          :value "http://localhost:5000/api"}}
                                  {:when {:type :exist?
                                          :path [:random]}
                                   :then {:type :remove
                                          :path [:random]}}])
                 (dissoc :request-time :orig-content-encoding)))
           [:get :post :put :options :delete])
         (every? #(= % dummy-response)))))