(ns com.github.discoverAI.snake.events-test
  (:require [cljs.test :refer-macros [deftest is testing]]
            [com.github.discoverAI.snake.events :as events]
            [com.github.discoverAI.snake.db :as db]))

(deftest init-test
  (testing "if the initialize event is correctly handled and returnes the (for now static) state map"
    (is (= {:name   "re-frame"
            :board  [20 20]
            :tokens {:snake {:position  [[3 5] [2 5] [1 5]]
                             :direction [1 0]
                             :speed     1.0}}}
           (events/handle-init-db {} :snake.events/initialize-db)))))