(ns com.github.discoverAI.snake.views-test
  (:require [cljs.test :refer-macros [deftest is testing]]
            [com.github.discoverAI.snake.views :as views]))

(deftest test-pos-snake
  (testing "If a for a position it is returned true if it is within the snake"
    (is (= true
           (views/pos-is-snake [[3 5] [2 5] [1 5]] [1 5]))))

  (testing "If a for a position it is returned true if it is not within the snake"
    (is (= false
           (views/pos-is-snake [[3 5] [2 5] [1 5]] [1 42])))))

(deftest test-cell-class-for
  (testing "If the correct cell is returned for the position"
    (is (= [:td.snake]
           (views/cell-item-for [1 2] [[1 2]])))

    (is (= [:td.cell]
           (views/cell-item-for [1 3] [[1 2]])))))

(deftest create-row-test
  (testing "If a row of cells is created correctly"
    (is (= [:tr {:key 0} [:td.snake] [:td.snake] [:td.snake] [:td.cell]]
           (views/create-row 4 0 [[0 0] [1 0] [2 0]])))))

(deftest render-board-test
  (testing "If board is rendered correctly"
    (is (= (views/render-board 2 2 [[0 1] [1 1]])
           (into views/container-view [(into views/container-body [
                                                                   (into (views/row-container 0) [[:td.cell] [:td.cell]])
                                                                   (into (views/row-container 1) [[:td.snake] [:td.snake]])])])))
    (is (= (views/render-board 2 2 [[1 0]])
           (into views/container-view [(into views/container-body [
                                                                   (into (views/row-container 0) [[:td.cell] [:td.snake]])
                                                                   (into (views/row-container 1) [[:td.cell] [:td.cell]])])])))))
