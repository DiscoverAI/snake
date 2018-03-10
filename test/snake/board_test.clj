(ns snake.board-test
  (:require [clojure.test :refer :all]
            [snake.board :as b]))

(deftest initial-snake-position-base-vector-test
  (testing "base vector for snake length 3"
    (is (= [1 0 -1]
           (b/initial-snake-position-base-vector 3))))

  (testing "base vector for snake length 4"
    (is (= [1 0 -1 -2]
           (b/initial-snake-position-base-vector 4))))

  (testing "base vector for snake length 5"
    (is (= [2 1 0 -1 -2]
           (b/initial-snake-position-base-vector 5))))

  (testing "base vector for snake length 5 and direction (0,1)"
    (is (= [[0 2] [0 1] [0 0] [0 -1] [0 -2]]
           (b/initial-snake-position-base-vector 5 [0 1]))))

  (testing "base vector for snake length 4 and direction (1,0)"
    (is (= [[1 0] [0 0] [-1 0] [-2 0]]
           (b/initial-snake-position-base-vector 4 [1 0]))))

  (testing "base vector for snake length 4 and direction (1,0)"
    (is (= [[1 1] [0 0] [-1 -1] [-2 -2]]
           (b/initial-snake-position-base-vector 4 [1 1])))))

(deftest initial-snake-position-test
  (testing "placing snake of size 3 at position (3,3)"
    (is (= [[4 3] [3 3] [2 3]]
           (b/initial-snake-position 3 [3 3] [1 0]))))

  (testing "placing snake of size 2 at position (2,2)"
    (is (= [[2 2] [1 2]]
           (b/initial-snake-position 2 [2 2] [1 0])))))

(deftest initial-state-test
  (testing "initiating 5x5 game board with snake of size 3 in the middle"
    (is (= {:board  [5 5]
            :tokens {:snake {:position  [[4 3] [3 3] [2 3]]
                             :direction [1 0]
                             :speed     1.0}}}
           (b/initial-state 5 5 3))))

  (testing "initiating 3x3 game board with snake of size 2 in the middle"
    (is (= {:board  [3 3]
            :tokens {:snake {:position  [[2 2] [1 2]]
                             :direction [1 0]
                             :speed     1.0}}}
           (b/initial-state 3 3 2)))))
