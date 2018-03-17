(ns com.github.discoverAI.snake.engine-test
  (:require [clojure.test :refer :all]
            [de.otto.tesla.util.test-utils :as tu]
            [com.github.discoverAI.snake.core :as co]
            [com.github.discoverAI.snake.engine :as eg]
            [clojure.string :as s]
            [de.otto.tesla.stateful.scheduler :as sch]
            [overtone.at-at :as ot]))

(def game-20-20-3
  {:board  [20 20]
   :tokens {:snake {:position  [[11 10] [10 10] [9 10]]
                    :direction [1 0]
                    :speed     1.0}}})

(def game-20-20-3-id :foobar)

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
    (with-redefs [eg/game-id (constantly game-20-20-3-id)]
      (is (= {game-20-20-3-id game-20-20-3}
             (eg/new-game 20 20 3)))

      (is (not= {game-20-20-3-id game-20-20-3}
                (eg/new-game 21 19 7))))))

(deftest register-new-game-test
  (testing "Should add a new game to component"
    (with-redefs [eg/game-id (fn [game-state]
                               (is (= game-20-20-3
                                      game-state))
                               game-20-20-3-id)
                  eg/register-move-dispatch (fn [_ _ _])]
      (tu/with-started [system (co/snake-system {})]
                       (is (not= nil
                                 (:engine system)))

                       (is (= game-20-20-3-id
                              (eg/register-new-game (:engine system) 20 20 3)))

                       (is (= game-20-20-3
                              (game-20-20-3-id @(get-in system [:engine :games]))))))))

(deftest test-vector-add
  (testing "On a tick, the snake should move one pixel into the given direction"
    (is (= [1 4] (eg/vector-addition [1 2] [0 2])))))

(deftest on-tick-should-move-the-snake
  (testing "On a tick, the snake should move one pixel into the given direction"
    (is (= {:board  [20 20]
            :tokens {:snake {:position  [[12 10] [11 10] [10 10]]
                             :direction [1 0]
                             :speed     1.0}}} (eg/move game-20-20-3)))))

(deftest atomically-update-game-state
  (testing "If the game state is updated"
    (let [atom (atom {game-20-20-3-id game-20-20-3 :foobazz game-20-20-3})]
      (eg/atomically-update-game-state atom game-20-20-3-id)
      (is (= @atom {game-20-20-3-id (eg/move game-20-20-3) :foobazz game-20-20-3})))))

(deftest test-scheduling
  (testing "if the move is scheduled"
    (with-redefs
      [eg/atomically-update-game-state
       (fn [a _] (swap! a inc))]
      (tu/with-started [system (co/snake-system {})]
                       (let [a (atom 0)]
                         (eg/register-move-dispatch a :mock-id (:scheduler system))
                         (Thread/sleep (+ eg/move-speed 50))
                         (is (= @a 2)))))))