(ns com.github.discoverAI.snake.endpoint-test
  (:require [clojure.test :refer :all]
            [com.github.discoverAI.snake.core :as co]
            [de.otto.tesla.util.test-utils :as tu]
            [com.github.discoverAI.snake.engine :as eg]
            [com.github.discoverAI.snake.endpoint :as ep]
            [clojure.data.json :as json]))

(def fake-game-state
  {:board  [20 20]
   :score  0
   :tokens {:snake {:position  [[0 0]]
                    :direction [1 0]
                    :speed     1.0}
            :food  {:position [[1 2]]}}})

(def system
  {:engine {:games (atom {:foo fake-game-state})}})

(deftest get-game-handler-test
  (testing "should get existing game"
    (is (= {:body    (json/write-str fake-game-state)
            :headers {"content-type" "application/json"}
            :status  200}
           (ep/handle-get-game-request system {:params {:id "foo"}}))))

  (testing "should return 404 when game not existing"
    (is (= {:status 404}
           (ep/handle-get-game-request system {:params {:id "asd"}})))))

(deftest post-game-handler-test
  (testing "should create new game"
    (with-redefs [eg/register-new-game (fn [engine width height snake-length _]
                                         (is (= (:engine system) engine))
                                         (is (= 10 width))
                                         (is (= 5 height))
                                         (is (= 4 snake-length))
                                         :foo)]
      (is (= {:body    "{\"gameId\":\"foo\"}"
              :headers {"content-type" "application/json"}
              :status  201}
             (ep/handle-post-game-request system {:params {:width "10" :height "5" :snakeLength "4"}}))))))
