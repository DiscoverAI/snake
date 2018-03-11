(ns com.github.discoverAI.snake.events-test
  (:require [cljs.test :refer-macros [deftest is testing]]
            [com.github.discoverAI.snake.events :as events]))

(deftest start-game-test
  (testing "Start the game and change state with game values"
    (is (= {:board  [42 42]
            :state  :started
            :tokens {:snake {:position  [[11 10] [10 10] [9 10]]
                             :direction [1 0]
                             :speed     1}}}
           (events/start-state {} [:start-game])))))
