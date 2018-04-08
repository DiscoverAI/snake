(ns com.github.discoverAI.snake.events-test
  (:require [cljs.test :refer-macros [deftest is testing]]
            [com.github.discoverAI.snake.events :as events]))

(deftest start-game-test
  (testing "Start the game and change state with game values"
    (with-redefs [events/attach-on-key-listener (constantly nil)]
                 (is (= {:board  [24 24]
                         :score  0
                         :state  :started
                         :tokens {:snake {:position  [[13 12] [12 12] [11 12]]
                                          :direction [1 0]
                                          :speed     1}}}
                        (events/start-game {} [:start-game]))))))
