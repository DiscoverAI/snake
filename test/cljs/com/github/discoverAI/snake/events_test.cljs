(ns com.github.discoverAI.snake.events-test
  (:require [cljs.test :refer-macros [deftest is testing]]
            [com.github.discoverAI.snake.events :as events]
            [com.github.discoverAI.snake.websocket-api :as ws-api]))

(def expected-started-game {:board   []
                            :state   :started
                            :game-id :foo-bar-game-id
                            :tokens  {}})

(deftest start-game-test
  (testing "Start the game, get game id from backend"
    (with-redefs [events/attach-on-key-listener (constantly nil)
                  ws-api/start-new-game (fn [game-params callback]
                                          (is (= {:width 24 :height 24 :snake-length 3}
                                                 game-params))
                                          (is (not (nil? callback))))]
                 (let [init-db (events/init-db nil nil)
                       start-event [::events/start-game {:width 24 :height 24 :snake-length 3}]
                       db-with-started-game (events/start-game init-db start-event)

                       register-game-id-event [::events/register-game-id :foo-bar-game-id]
                       db-with-game-id (events/register-game db-with-started-game register-game-id-event)]

                   (is (= {:board  []
                           :state  :started
                           :tokens {}}
                          db-with-started-game))

                   (is (= expected-started-game
                          db-with-game-id))))))

(deftest spectate-game-test
  (testing "spectate-game-id was passed in a header"
    (let [db (events/init-db nil [nil :foo-bar-game-id])]
      (is (= db expected-started-game)))))

(deftest test-send-spectate-request
  (testing "that spectate requests are sent out if appropiate"
    (with-redefs [ws-api/spectate-game
                  (fn [game_id _callback]
                    (is (= :foo-bar-game-id game_id)))]
                 (events/send-spectate-request
                   expected-started-game
                   nil))))