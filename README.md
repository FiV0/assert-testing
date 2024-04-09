# Assert-testing

This repository contains essentially just two files. A Java file

```java
package testing;

public class Util {

    public static void different(int a, int b) {
        assert(a != b);
    }

}
```

and a Clojure file

```clj
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
```

the only goal is to check when which assertion is enabled.

### At the REPL
Assuming we execute the following commands in the `main` namespace above without any further tweaks.

```clj
(Util/different 1 1)
```
passes without any complains (Java assertions are disabled by default) where as

```clj
(different 1 1)
```
results in `java.lang.AssertionError`.
If we pass the java option `-ea` (or `-enableassertions`) as `:jvm-opts` when starting the REPL
both of the above result in an assertion error.

With `(set! *assert* true/false)` you can enable/disable assertions in Clojure. Beware that you need to
recompile your functions (in this case `different`) for this to have an effect.

### Jars
Let's compile an uberjar
```bash
clj -T:build uber
```
Excecuting
```bash
java -jar target/assert-testing-0.1.2-standalone.jar java 1 1
```
still results in no assertion getting thrown. As previously Java assertions are disabled by default.
You can enable them by passing `-ea` to the `java` command.
```bash
java -jar target/assert-testing-0.1.2-standalone.jar clj 1 1
```
The above on the contrary throws. To disable Clojure assertions you need to compile your Clojure files
with something like
```clj
(with-bindings {#'clojure.core/*assert* false}
    compile-your-stuff
    )
```
or use something like [clojure.tools.build.api/compile-clj](https://clojure.github.io/tools.build/clojure.tools.build.api.html#var-compile-clj) with the same bindings.
