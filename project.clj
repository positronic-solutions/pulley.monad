;; Copyright 2016-2017 Positronic Solutions, LLC.
;;
;; This file is part of pulley.monad.
;;
;; pulley.monad is free software: you can redistribute it and/or modify
;; it under the terms of the GNU Lesser General Public License as published by
;; the Free Software Foundation, either version 3 of the License, or
;; (at your option) any later version.
;;
;; pulley.monad is distributed in the hope that it will be useful,
;; but WITHOUT ANY WARRANTY; without even the implied warranty of
;; MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
;; GNU General Public License for more details.
;;
;; You should have received a copy of the GNU Lesser General Public License
;; along with pulley.monad.  If not, see <http://www.gnu.org/licenses/>.

(defproject com.positronic-solutions/pulley.monad "0.1.0-SNAPSHOT"
  :description "A generic monad library for Clojure"
  :url "https://github.com/positronic-solutions/pulley.monad"
  :license {:name "GNU Lesser General Public License, version 3 or later"
            :url "http://www.gnu.org/licenses/lgpl.html"
            :distribution :repo}
  :source-paths ["src/clj"]
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :plugins [[lein-ancient "0.6.10"]
            [lein-exec "0.3.6"]
            [lein-pprint "1.1.2"]]
  :profiles {:clojure-1.8 {:dependencies [[org.clojure/clojure "1.8.0"]]}
             :clojure-1.7 {:dependencies [[org.clojure/clojure "1.7.0"]]}
             :clojure-1.6 {:dependencies [[org.clojure/clojure "1.6.0"]]}
             :clojure-1.5 {:dependencies [[org.clojure/clojure "1.5.1"]]}
             :clojure-1.4 {:dependencies [[org.clojure/clojure "1.4.0"]]}
             :examples {:source-paths ["examples/src"]
                        :test-paths ["examples/src"]}})
