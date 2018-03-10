(ns com.github.discoverAI.snake.subs-test
  (:require [cljs.test :refer-macros [deftest is testing]]
            [com.github.discoverAI.snake.subs :as subs]))

(def mock-db
  {:name "re-frame"
   :board  [10 10]
   :tokens {:snake {:position  [[3 5] [2 5] [1 5]]
                    :direction [1 0]
                    :speed     1.0}}})

(deftest get-board-test
  (is (= [10 10] (subs/get-board mock-db)))
  )

(deftest get-position-test
  (is (= [[3 5] [2 5] [1 5]] (subs/get-snake-position mock-db)))
  )