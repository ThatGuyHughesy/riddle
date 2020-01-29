(ns riddle.components.routes-test
  (:require [clojure.test :refer :all]
            [riddle.components.routes :refer :all]
            [ring.mock.request :as mock]
            [clj-http.fake :as fake]))

(def dummy-response {:status 200
                     :headers {}
                     :body "Riddle is the best!"})

(deftest test-handler
  (fake/with-fake-routes
    {"http://localhost:5000/test"
     (fn [_] dummy-response)}
    (->> (map
           (fn [request-type]
             (-> (mock/request request-type "test")
                 (assoc :uri "http://localhost:5000/test")
                 (handler {:client {:connection-manager nil}})
                 (dissoc :request-time :orig-content-encoding)))
           [:get :post :put :options :delete])
         (every? #(= % dummy-response)))))