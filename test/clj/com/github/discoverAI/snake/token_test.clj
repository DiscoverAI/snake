(ns com.github.discoverAI.snake.token-test
  (:require [clojure.test :refer :all]
            [com.github.discoverAI.snake.token :as t]))

(deftest change-direction-test
  (testing "Turning left should yield the correct direction vector"
    (is (= [-1 0]
           (t/change-direction [0 1] t/LEFT)))
    (is (= [0 1]
           (t/change-direction [1 0] t/LEFT)))
    (is (= [1 0]
           (t/change-direction [0 -1] t/LEFT)))
    (is (= [0 -1]
           (t/change-direction [-1 0] t/LEFT))))

  (testing "Turning right should yield the correct direction vector"
    (is (= [1 0]
           (t/change-direction [0 1] t/RIGHT)))
    (is (= [0 -1]
           (t/change-direction [1 0] t/RIGHT)))
    (is (= [-1 0]
           (t/change-direction [0 -1] t/RIGHT)))
    (is (= [0 1]
           (t/change-direction [-1 0] t/RIGHT)))))
