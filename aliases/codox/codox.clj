(ns ^{:clojure.tools.namespace.repl/load false} codox
  (:require 
    [codox.main :refer [generate-docs]]))

(println "Generating docs with codox")

(generate-docs
  {:metadata {:doc/format :markdown}
   :namespaces '[marge.core]})

(System/exit 0)
