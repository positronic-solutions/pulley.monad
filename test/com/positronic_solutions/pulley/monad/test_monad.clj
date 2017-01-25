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
                (fn [k & xs]
                  (apply k v xs)))
   ::m/bind (fn [mv f]
              (fn [k & xs]
                (apply mv
                       (fn [v & xs']
                         (thunk (apply (f v) k xs')))
                       xs)))})

;; Runs given generic monadic value in cps-m and trampoline
(defn run-cps [mv]
  (trampoline (m/run cps-m mv) identity))

;; Operation for altering a computation's state
(defn update-state [f & args]
  (m/m-let [s (m/get-state)]
    (m/bind (apply f s args)
            m/set-state)))

;; Quick and dirty CPS + State monad
(def cps+state-m
  (assoc cps-m
         ::m/get-state (fn get-state []
                         (m/value (fn [k s & xs]
                                    (apply k s s xs))))
         ::m/set-state (fn set-state [s']
                         (m/value (fn [k s & xs]
                                    (apply k s' s' xs))))))

;; Runs mv in cps+state monad with inital state s0
(defn run-cps+state [mv s0]
  (trampoline (m/run cps+state-m mv)
              (fn [final-value final-state]
                [final-state final-value])
              s0))

(deftest test->>=
  (testing "TCO compatibility"
    (doseq [n [10 100 1000 10000 100000 1000000]]
      (testing (str "n = " n)
        (let [fs   (repeat n m-inc)
              expr (apply m/>>= m-0 fs)]
          (is (= n
                 (run-cps expr))))))))

(deftest test->>
  (testing "TCO compatibility"
    (doseq [n [10 100 1000 10000 100000 1000000]]
      (testing (str "n = " n)
        (let [fs   (repeat n (update-state m-inc))
              expr (apply m/>> fs)]
          (is (= [n n]
                 (run-cps+state expr 0))))))))
