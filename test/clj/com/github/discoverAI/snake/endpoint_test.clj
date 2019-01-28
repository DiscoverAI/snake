(ns com.github.discoverAI.snake.endpoint-test
  (:require [clojure.test :refer :all]
            [com.github.discoverAI.snake.core :as co]
            [de.otto.tesla.util.test-utils :as tu]
            [com.github.discoverAI.snake.engine :as eg]
            [com.github.discoverAI.snake.endpoint :as ep]
            [clojure.string :as s]))

(def fake-game-state
  {:board  [20 20]
   :score  0
   :tokens {:snake {:position  [[0 0]]
                    :direction [1 0]
                    :speed     1.0}
            :food  {:position [[1 2]]}}})

(def system
  {:games (atom {:foo fake-game-state})})

(deftest get-game-handler-test
  (testing "should get existing game"
    (is (= {:body    fake-game-state
            :headers {}
            :status  200}
           (ep/get-game-handler system {:params {:id :foo}}))))

  (testing "should return 404 when game not existing"
    (is (= {:body    nil
            :headers {}
            :status  404}
           (ep/get-game-handler system "asd")))))

(def mock-game-id :foo)

(deftest post-game-handler-test
  (testing "should create new game"
    (with-redefs [eg/register-game-without-timer (fn [engine width height snake-length]
                                                   (is (= @(:games system) @engine))
                                                   (is (= 10 width))
                                                   (is (= 5 height))
                                                   (is (= 4 snake-length))
                                                   mock-game-id)]
      (let [add-game-result (ep/add-game-handler system {:body-params {:width 10 :height 5 :snakeLength 4}})]
        (is (= 201 (:status add-game-result)))

        (is (s/ends-with?
              (get-in add-game-result [:headers "Location"])
              (str ":8080/games/" (name mock-game-id))))))))

(deftest test-change-dir-handler
  (testing "should change direction"
    (tu/with-started
      [mock-system (co/snake-system {})]
      (let [{:keys [gameId]} (:body (ep/add-game-handler (:engine mock-system)
                                                         {:body-params {:width 20 :height 20 :snakeLength 4}}))
            created-game (gameId @(get-in mock-system [:engine :games]))
            direction-changed (assoc-in created-game [:tokens :snake :direction] [0 1])
            expected-state (eg/make-move direction-changed)]
        (is (= expected-state
               (ep/change-dir-handler (:engine mock-system) {:direction [0 1]} gameId)))))))
