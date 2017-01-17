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

(ns com.positronic-solutions.pulley.monad.test-ops
  (:require [clojure.test :refer :all]
            [com.positronic-solutions.pulley.monad :as m]))

;; Test operations for test-defop
(m/defop foo)
(m/defop bar :bar)
(m/defop baz "Doc for baz")
(m/defop quux []
  (m/return "quux"))
(m/defop frobitz
  "Doc for frobitz"
  ([]
    (m/return "frobitz")))

(deftest test-defop
  (testing "docstrings"
    (is (= nil
           (:doc (meta #'foo))
           (:doc (meta #'bar))
           (:doc (meta #'quux))))
    (is (= "Doc for baz"
           (:doc (meta #'baz))))
    (is (= "Doc for frobitz"
           (:doc (meta #'frobitz)))))
  (testing "invoking defaults"
    (is (thrown? IllegalStateException
                 (m/run m/identity-m
                   (foo))))
    (is (thrown? IllegalStateException
                 (m/run m/identity-m
                   (bar))))
    (is (thrown? IllegalStateException
                 (m/run m/identity-m
                   (baz))))
    (is (= "quux"
           (m/run m/identity-m
             (quux))))
    (is (= "frobitz"
           (m/run m/identity-m
             (frobitz)))))
  (testing "invoking provided"
    (is (= (+ 1 2 3)
           (m/run (assoc m/identity-m
                         ::foo (comp m/return +))
             (foo 1 2 3))))
    (is (= (+ 1 2 3)
           (m/run (assoc m/identity-m
                         :bar (comp m/return +))
             (bar 1 2 3))))
    (is (= (+ 1 2 3)
           (m/run (assoc m/identity-m
                         ::baz (comp m/return +))
             (baz 1 2 3))))
    (is (= (+ 1 2 3)
           (m/run (assoc m/identity-m
                         ::frobitz (comp m/return +))
             (frobitz 1 2 3))))
    (is (= (+ 1 2 3)
           (m/run (assoc m/identity-m
                         ::quux (comp m/return +))
             (quux 1 2 3))))))
