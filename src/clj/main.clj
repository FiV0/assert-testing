(ns main
  (:import testing.Util)
  (:gen-class))

(defn different [a b]
  (assert (not= a b)))

(defn -main [& args]
  (let [a (parse-long (nth args 1))
        b (parse-long (nth args 2))]
    (case (first args)
      "java" (Util/different a b)
      "clj" (different a b)
      (throw (UnsupportedOperationException.)))))
