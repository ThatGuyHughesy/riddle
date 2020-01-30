(ns riddle.integration.routes
  (:require [clojure.test :refer :all]
            [riddle.components.routes :refer :all]
            [ring.mock.request :as mock]
            [clj-http.fake :as fake]))

(def dummy-response {:status 200
                     :headers {}
                     :body "Riddle is the best!"})

(deftest test-handler
  (fake/with-fake-routes
    {"http://localhost:5000/api"
     (fn [_] dummy-response)}
    (->> (map
           (fn [request-type]
             (-> (mock/request request-type "/")
                 (assoc :uri "http://localhost:8080")
                 (handler {:connection-manager nil}
                          [{:when {:type :equals
                                   :path [:uri]
                                   :value "http://localhost:8080"}
                            :then {:type :replace
                                   :path [:uri]
                                   :value "http://localhost:5000/api"}}])
                 (dissoc :request-time :orig-content-encoding)))
           [:get :post :put :options :delete])
         (every? #(= % dummy-response)))))