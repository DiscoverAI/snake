(ns com.github.discoverAI.snake.views-test
  (:require [cljs.test :refer-macros [deftest is testing]]
            [com.github.discoverAI.snake.views :as views]))

(deftest board-field-class-test
  (testing "Should only be field class"
    (is (= "field"
           (views/board-field-class [0 0] {:position [[3 20] [2 20] [1 20]]})))

    (is (= "field"
           (views/board-field-class [3 5] {:position [[3 20] [2 20] [1 20]]})))

    (is (= "field"
           (views/board-field-class [9 20] {:position [[3 20] [2 20] [1 20]]}))))

  (testing "Should be field and snake class"
    (is (= "field snake"
           (views/board-field-class [3 20] {:position [[3 20] [2 20] [1 20]]})))

    (is (= "field snake"
           (views/board-field-class [2 20] {:position [[3 20] [2 20] [1 20]]})))

    (is (= "field snake"
           (views/board-field-class [1 20] {:position [[3 20] [2 20] [1 20]]})))))
