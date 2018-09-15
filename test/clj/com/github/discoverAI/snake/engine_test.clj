(ns com.github.discoverAI.snake.engine-test
  (:require [clojure.test :refer :all]
            [de.otto.tesla.util.test-utils :as tu]
            [com.github.discoverAI.snake.core :as co]
            [com.github.discoverAI.snake.engine :as eg]
            [clojure.string :as s]
            [com.github.discoverAI.snake.board-test :as bt]
            [com.github.discoverAI.snake.board :as b]
            [overtone.at-at :as at-at]))

(def game-20-20-3
  {:board     [20 20]
   :score     0
   :tokens    {:snake {:position  [[11 10] [10 10] [9 10]]
                       :direction [1 0]
                       :speed     1.0}
               :food  {:position [[1 2]]}}
   :game-over false})

(def game-20-20-3-id :foobar)

(def game-head-on-tail
  {:board     [20 20]
   :score     0
   :tokens    {:snake {:position  [[11 10] [10 10] [10 9] [11 9] [11 10]]
                       :direction [1 0]
                       :speed     1.0}
               :food  {:position [[1 2]]}}
   :game-over false})

(deftest game-id-test
  (testing "Should calculate a unique game id"
    (is (s/starts-with? (name (eg/game-id game-20-20-3)) "G_"))

    (is (< 2
           (count (name (eg/game-id game-20-20-3)))))

    (is (not= (name (eg/game-id {:board  [21 21]
                                 :tokens {:snake {:position  [[11 10] [10 10] [9 10]]
                                                  :direction [1 0]
                                                  :speed     1.0}}}))
              (name (eg/game-id game-20-20-3))))

    (is (not= (name (eg/game-id game-20-20-3))
              (name (eg/game-id game-20-20-3))))))

(deftest new-game-test
  (testing "Should create a new game with game id"
    (with-redefs [eg/game-id (constantly game-20-20-3-id)
                  b/random-vector (constantly [1 2])]
      (is (= {game-20-20-3-id game-20-20-3}
             (eg/new-game 20 20 3)))

      (is (not= {game-20-20-3-id game-20-20-3}
                (eg/new-game 21 19 7))))))

(deftest register-new-game-test
  (let [moved? (atom false)]
    (with-redefs [b/random-vector (constantly [1 2])
                  eg/game-id (fn [game-state]
                               (is (= game-20-20-3 game-state))
                               game-20-20-3-id)
                  eg/make-move (fn [game-state]
                                 (is (= game-20-20-3 game-state))
                                 (reset! moved? true)
                                 game-state)]

      (testing "Should add a new game to component and register move function with scheduler"
        (tu/with-started [system (co/snake-system {})]
                         (is (= game-20-20-3-id
                                (eg/register-new-game (:engine system) 20 20 3 (constantly nil))))
                         (is (= game-20-20-3
                                (game-20-20-3-id @(get-in system [:engine :games]))))
                         (is (not (nil? (game-20-20-3-id @(get-in system [:engine :game-id->scheduled-job-id])))))
                         (tu/eventually (true? @moved?) 2000 200))))))

(deftest test-concat-to-snake-head
  (testing "On a tick, the snake should move one pixel into the given direction"
    (is (= [1 4]
           (eg/concat-to-snake-head [1 2] [0 2] [9 9]))))

  (testing "should move snake head not outside of game field"
    (is (= [1 1]
           (eg/concat-to-snake-head [9 1] [1 0] [9 9])))))

(deftest test-new-direction-vector
  (testing "turning into the same direction/opposite should do nothing"
    (is (= [1 0] (eg/new-direction-vector [1 0] [-1 0])))
    (is (= [1 0] (eg/new-direction-vector [1 0] [1 0]))))

  (testing "turn direction"
    (is (= [0 1] (eg/new-direction-vector [1 0] [0 1])))
    (is (= [0 -1] (eg/new-direction-vector [1 0] [0 -1])))))

(deftest test-vector-modulo
  (testing "On board overflow, apply modulo operation to each elements of first vector with second one."
    (is (= [0 0]
           (eg/modulo-vector [4 0] [4 4])))

    (is (= [3 0]
           (eg/modulo-vector [3 4] [4 4])))))

(deftest move-the-snake
  (testing "does not drop last node when the head is on a food"
    (with-redefs [clojure.core/drop-last (fn [_] (throw (Exception. "No no no")))]
      (eg/make-move {:board  [4 4]
                     :tokens {:snake {:position [[0 0] [1 0] [2 0]]}
                              :food  {:position [[0 0]]}}
                     :score  1})))

  (testing "drops the last node when the head is not on a food"
    (let [call-count (atom 0)]
      (with-redefs [clojure.core/drop-last (fn [_] (swap! call-count inc))]
        (eg/make-move {:board  [4 4]
                       :tokens {:snake {:position [[0 0] [1 0] [2 0]]}
                                :food  {:position [[5 5]]}}})
        (is (= @call-count 1)))))

  (testing "move snake one pixel into the given direction"
    (is (= {:board     [20 20]
            :score     0
            :tokens    {:snake {:position  [[12 10] [11 10] [10 10]]
                                :direction [1 0]
                                :speed     1.0}
                        :food  {:position [[1 2]]}}
            :game-over false}
           (eg/make-move game-20-20-3))))

  (testing "move snake back to the left side of the field, when it overflows the field on the right side"
    (is (= {:board  [4 4]
            :tokens {:snake {:position  [[0 0] [3 0] [2 0]]
                             :direction [1 0]
                             :speed     1.0}}}
           (eg/make-move {:board  [4 4]
                          :tokens {:snake {:position  [[3 0] [2 0] [1 0]]
                                           :direction [1 0]
                                           :speed     1.0}}}))))

  (testing "snake is on food and should grow and food disappear and respawn"
    (let [moved-game-state (eg/make-move {:board  [4 4]
                                          :score  42
                                          :tokens {:snake {:position  [[3 0] [2 0] [1 0]]
                                                           :direction [1 0]
                                                           :speed     1.0}
                                                   :food  {:position [[3 0]]}}})]
      (is (= {:board  [4 4]
              :score  43
              :tokens {:snake {:position  [[0 0] [3 0] [2 0] [1 0]]
                               :direction [1 0]
                               :speed     1.0}}}
             (bt/without-food-position moved-game-state)))

      (is (not= [[3 0]]
                (bt/extract-food-position moved-game-state))))))

(deftest snake-on-food?-test
  (testing "should return true if snake head coincident with food"
    (is (eg/snake-on-food? {:board  [4 4]
                            :tokens {:snake {:position [[0 0] [1 0] [2 0]]}
                                     :food  {:position [[0 0]]}}})))
  (testing "should return false if snake is not on a food token"
    (is (not (eg/snake-on-food? {:board  [4 4]
                                 :tokens {:snake {:position [[1 0] [2 0] [3 0]]}
                                          :food  {:position [[0 0]]}}})))))

(deftest change-direction-test
  (testing "should update game state with new direction"
    (let [game-state-atom (atom {:foobar {:tokens {:snake {:position  [13 37]
                                                           :direction [1 0]
                                                           :speed     1.0}}}})]
      (eg/change-direction game-state-atom :foobar [0 -1])

      (is (= {:foobar {:tokens {:snake {:position  [13 37]
                                        :direction [0 -1]
                                        :speed     1.0}}}}
             @game-state-atom)))))

(deftest game-over?-test
  (testing "game over is returned when snake overlaps"
    (is (= true (eg/game-over? {:tokens {:snake {:position [[0 0] [1 0] [2 0] [2 1] [1 1] [1 0]]}}})))
    (is (= true (eg/game-over? game-head-on-tail)))
    (is (= false (eg/game-over? game-20-20-3)))))

(deftest update-game-state!-test
  (let [games (atom {:foo            game-head-on-tail
                     game-20-20-3-id game-20-20-3})
        scheduled-jobs (atom {:foo 1337})]
    (with-redefs [at-at/stop (fn [id]
                               (is (= 1337 id)))]
      (testing "should set given game over when game lost"
        (is (not (get-in @games [:foo :game-over])))
        (eg/update-game-state! games scheduled-jobs :foo (constantly nil))
        (is (get-in @games [:foo :game-over]))
        (is (empty? @scheduled-jobs))))

    (testing "should move snake when not game over"
      (is (not (get-in @games [game-20-20-3-id :game-over])))
      (eg/update-game-state! games nil game-20-20-3-id (constantly nil))
      (is (not (get-in @games [game-20-20-3-id :game-over])))
      (is (not= game-20-20-3
                (game-20-20-3-id @games))))))
