(ns com.github.discoverAI.snake.subs-test
  (:require [cljs.test :refer-macros [deftest is testing]]
            [com.github.discoverAI.snake.subs :as subs]))

(def game-20-20-3
  {:board  [20 20]
   :tokens {:snake {:position  [[11 10] [10 10] [9 10]]
                    :direction [1 0]
                    :speed     1.0}}})

(deftest subscribe-to-board-test
  (testing "Should yield the board"
    (is (= [20 20]
           (subs/game-board game-20-20-3)))))

(deftest subscribe-to-snake-test
  (is (= {:position  [[11 10] [10 10] [9 10]]
          :direction [1 0]
          :speed     1.0}
         (subs/snake game-20-20-3))))
