(ns riddle.rules
  (:require [clojure.tools.logging :refer [error]]))

(defmulti rule-when
  (fn [rule & _]
    (:type rule)))

(defmethod rule-when :equals [{:keys [path value]} request]
  (when-let [resolved-path (get-in request path)]
    (= resolved-path value)))

(defmethod rule-when :greater-than [{:keys [path value]} request]
  (when-let [resolved-path (get-in request path)]
    (when (and (int? resolved-path) (int? value))
      (> resolved-path value))))

(defmethod rule-when :less-than [{:keys [path value]} request]
  (when-let [resolved-path (get-in request path)]
    (when (and (int? resolved-path) (int? value))
      (< resolved-path value))))

(defmulti rule-then
  (fn [rule & _]
    (:type rule)))

(defmethod rule-then :replace [{:keys [path value]} request]
  (assoc-in request path value))

(defmethod rule-then :increment [{:keys [path value]} request]
  (update-in request path (fn [resolved-path]
                            (if (and (int? resolved-path) (int? value))
                              (+ resolved-path value)
                              resolved-path))))

(defmethod rule-then :decrement [{:keys [path value]} request]
  (update-in request path (fn [resolved-path]
                            (if (and (int? resolved-path) (int? value))
                              (- resolved-path value)
                              resolved-path))))

(defn process [rules request]
  (reduce
    (fn [request rule]
      (try
        (if (rule-when (:when rule) request)
          (rule-then (:then rule) request)
          request)
        (catch Exception e
          (error "Error applying rule: " rule "\n" e))))
    request
    rules))