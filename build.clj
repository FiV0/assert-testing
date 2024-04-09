(ns build
  (:require [clojure.tools.build.api :as b]))

(def lib 'fiv0/assert-testing)
(def version (format "0.1.%s" (b/git-count-revs nil)))
(def class-dir "target/classes")
(def basis (b/create-basis {:project "deps.edn"}))
(def uber-file (format "target/%s-%s-standalone.jar" (name lib) version))
(def main 'main)

(defn clean
  "Cleans the target path."
  [_]
  (b/delete {:path "target"}))

(defn java
  "Compiles the java classes under `src/java`."
  [_]
  (b/javac {:src-dirs ["src/java"]
            :class-dir "target/classes"
            :basis basis
            ;; ignore Unsafe warnings
            :javac-opts ["-XDignore.symbol.file", "-Xlint:-options"]}))

(defn uber [_]
  (clean nil)
  (b/copy-dir {:src-dirs ["src" "resources"]
               :target-dir class-dir})
  (java nil)
  (b/compile-clj {:basis basis
                  :ns-compile '[main]
                  :class-dir class-dir
                  ;; to disable clojure assertions in an artifact
                  #_#_:bindings {#'clojure.core/*assert* false}})
  (b/uber {:class-dir class-dir
           :uber-file uber-file
           :basis basis
           :main 'main}))

(comment
  (clean nil)
  (java nil)
  (uber nil))
