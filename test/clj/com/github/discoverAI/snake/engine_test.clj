(ns com.github.discoverAI.snake.engine-test
  (:require [clojure.test :refer :all]
            [de.otto.tesla.util.test-utils :as tu]
            [com.github.discoverAI.snake.core :as co]
            [com.github.discoverAI.snake.engine :as e]
            [clojure.string :as s]))

(def game-20-20-3
  {:board  [20 20]
   :tokens {:snake {:position  [[11 10] [10 10] [9 10]]
                    :direction [1 0]
                    :speed     1.0}}})
(def game-20-20-3-id
  :foobar)

(deftest game-id-test
  (testing "Should calculate a unique game id"
    (is (s/starts-with? (name (e/game-id game-20-20-3)) "G_"))

    (is (< 2
           (count (name (e/game-id game-20-20-3)))))

    (is (not= (name (e/game-id {:board  [21 21]
                                :tokens {:snake {:position  [[11 10] [10 10] [9 10]]
                                                 :direction [1 0]
                                                 :speed     1.0}}}))
              (name (e/game-id game-20-20-3))))

    (is (not= (name (e/game-id game-20-20-3))
              (name (e/game-id game-20-20-3))))))

(deftest new-game-test
  (testing "Should create a new game with game id"
    (with-redefs [e/game-id (constantly game-20-20-3-id)]
      (is (= {game-20-20-3-id game-20-20-3}
             (e/new-game 20 20 3)))

      (is (not= {game-20-20-3-id game-20-20-3}
                (e/new-game 21 19 7))))))

(deftest register-new-game-test
  (testing "Should add a new game to component"
    (with-redefs [e/game-id (fn [game-state]
                              (is (= game-20-20-3
                                     game-state))
                              game-20-20-3-id)]
      (tu/with-started [system (co/snake-system {})]
                       (is (not= nil
                                 (:engine system)))

                       (is (= game-20-20-3-id
                              (e/register-new-game (:engine system) 20 20 3)))

                       (is (= game-20-20-3
                              (game-20-20-3-id @(get-in system [:engine :games]))))))))
