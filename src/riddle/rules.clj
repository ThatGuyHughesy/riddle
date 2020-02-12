(ns riddle.rules
  (:require [clojure.core.async :refer [go]]
            [clojure.tools.logging :refer [error]]
            [medley.core :refer [dissoc-in]]))

(defmulti rule-when
  (fn [rule & _]
    (:type rule)))

(defmethod rule-when :equal? [{:keys [path value]} request]
  "Check if a field is equal to a specified value"
  (when-let [resolved-path (get-in request path)]
    (= resolved-path value)))

(defmethod rule-when :not-equal? [{:keys [path value]} request]
  "Check if a field is not equal to a specified value"
  (when-let [resolved-path (get-in request path)]
    (not= resolved-path value)))

(defmethod rule-when :greater-than? [{:keys [path value]} request]
  "Check if a field is greater than a specified value"
  (when-let [resolved-path (get-in request path)]
    (when (and (int? resolved-path) (int? value))
      (> resolved-path value))))

(defmethod rule-when :less-than? [{:keys [path value]} request]
  "Check if a field is less than a specified value"
  (when-let [resolved-path (get-in request path)]
    (when (and (int? resolved-path) (int? value))
      (< resolved-path value))))

(defmethod rule-when :exist? [{:keys [path]} request]
  "Check if a field exists"
  (if (get-in request path)
    true
    false))

(defmethod rule-when :not-exist? [{:keys [path]} request]
  "Check if a field does not exist"
  (if (get-in request path)
    false
    true))

(defmulti rule-then
  (fn [rule & _]
    (:type rule)))

(defmethod rule-then :insert [{:keys [path value]} request]
  "Add a new field to the request - will overwrite already exisiting field"
  (assoc-in request path value))

(defmethod rule-then :remove [{:keys [path]} request]
  "Remove field from the request"
  (dissoc-in request path))

(defmethod rule-then :increment [{:keys [path value]} request]
  "Increment a field by a specified amount - only if field value is an integer"
  (update-in request path (fn [resolved-path]
                            (if (and (int? resolved-path) (int? value))
                              (+ resolved-path value)
                              resolved-path))))

(defmethod rule-then :decrement [{:keys [path value]} request]
  "Decrement a field by a specified amount - only if field value is an integer"
  (update-in request path (fn [resolved-path]
                            (if (and (int? resolved-path) (int? value))
                              (- resolved-path value)
                              resolved-path))))

(defmethod rule-then :allow [_ request]
  "Forward request - skip all subsequent rules"
  (assoc request :action :allow))

(defmethod rule-then :deny [_ request]
  "Do not forward request - skip all subsequent rules"
  (assoc request :action :deny))

(defn allow-or-deny? [request]
  "Check if allow or deny action has been applied"
  (#{:allow :deny} (:action request)))

(defn deny? [request]
  "Check if request has been denied"
  (if (= :deny (:action request))
    :deny
    (dissoc request :action)))

(defn process [rules request]
  "Apply rules to request"
  (loop [[rule & remaining-rules] rules processed-request request]
    (if rule
      (if (allow-or-deny? processed-request)
        processed-request
        (let [processed (if (rule-when (:when rule) processed-request)
                          (rule-then (:then rule) processed-request)
                          processed-request)]
          (recur (rest remaining-rules) processed)))
      processed-request)))