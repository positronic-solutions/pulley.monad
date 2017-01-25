;; Copyright 2017 Positronic Solutions, LLC.
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

(ns com.positronic-solutions.pulley.monad.test-monad
  (:require [clojure.test :refer :all]
            [com.positronic-solutions.pulley.monad :as m]))

(def m-inc (comp m/return inc))
(def m-0 (m/return 0))

;; Quick and dirty macro for thunk generation
(defmacro thunk [& body]
  `(fn []
     ~@body))

;; Quick and dirty (thunking) CPS monad
(def cps-m
  {::m/return (fn [v]
                (fn [k]
                  (k v)))
   ::m/bind (fn [mv f]
              (fn [k]
                (mv (fn [v]
                      (thunk ((f v) k))))))})

;; Runs given generic monadic value in cps-m and trampoline
(defn run-cps [mv]
  (trampoline (m/run cps-m mv) identity))

(deftest test->>=
  (testing "TCO compatibility"
    (doseq [n [10 100 1000 10000 100000 1000000]]
      (testing (str "n = " n)
        (let [fs   (repeat n m-inc)
              expr (apply m/>>= m-0 fs)]
          (is (= n
                 (run-cps expr))))))))
