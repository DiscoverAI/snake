(ns snake.token_test
  (:require [clojure.test :refer :all]
            [snake.token :as dir]))

(deftest change-direction-test-move-left
  (testing "Turning left should yield the correct direction vector"
    (is (= [1 0]
           (dir/change-direction [0 1] :left)))
    (is (= [0 -1]
           (dir/change-direction [1 0] :left)))
    (is (= [-1 0]
           (dir/change-direction [0 -1] :left)))
    (is (= [0 1]
           (dir/change-direction [-1 0] :left)))
    ))

(deftest change-direction-test-move-right
  (testing "Turning right should yield the correct direction vector"
    (is (= [-1 0]
           (dir/change-direction [0 1] :right)))
    (is (= [0 1]
           (dir/change-direction [1 0] :right)))
    (is (= [1 0]
           (dir/change-direction [0 -1] :right)))
    (is (= [0 -1]
           (dir/change-direction [-1 0] :right)))
    ))

