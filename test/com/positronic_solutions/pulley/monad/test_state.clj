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

(ns com.positronic-solutions.pulley.monad.test-state
  (:require [clojure.test :refer :all]
            [com.positronic-solutions.pulley.monad :as m]))

(deftest test-get-state
  (testing "return state"
    (is (= [1 1]
           ((m/run m/state-m (m/get-state)) 1))))
  (testing "use state in computation"
    (is (= [1 2]
           ((m/run m/state-m
              (m/bind (m/get-state)
                      (comp m/return inc)))
            1))))
  (testing "running with invalid context throws exception"
    (is (thrown? IllegalStateException
                 (m/run m/identity-m
                   (m/get-state))))))
