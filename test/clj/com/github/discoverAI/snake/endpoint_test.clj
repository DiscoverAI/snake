(ns com.github.discoverAI.snake.endpoint-test
  (:require [clojure.test :refer :all]
            [com.github.discoverAI.snake.core :as co]
            [de.otto.tesla.util.test-utils :as tu]
            [com.github.discoverAI.snake.engine :as eg]
            [com.github.discoverAI.snake.endpoint :as e]
            [com.github.discoverAI.snake.board :as b]
            [com.github.discoverAI.snake.engine-test :as egt]
            [clojure.data.json :as json]))

(def mock-register-request
  {:params {:width "2" :height "2" :snake-length "1"}})

(def mock-move-request
  {:params {:id "abc123" :x "1" :y "0"}})

(def mock-get-game-request
  {:params {:id "abc123"}})

(def game-id :abc123)

(defn fake-game-id [_] game-id)

(def fake-game-state
   {:board  [20 20]
    :score  0
    :tokens {:snake {:position  [[0 0]]
                     :direction [1 0]
                     :speed     1.0}
             :food  {:position [[1 2]]}}})

(defn- register-mock-game [{:keys [games]}]
  (swap! games merge   {game-id fake-game-state} (print games)))

(deftest register-game-handler-test
  (testing "If a new game is registered on the register request handler"
    (tu/with-started [system (co/snake-system {})]
                     (with-redefs [eg/game-id fake-game-id]
                       (is (= {:status  200
                               :headers {"content-type" "application/json"}
                               :body    (json/write-str {:game-id :abc123})}
                              (e/handle-register-request system mock-register-request)))))))

(deftest move-handler-test
  (testing "If a new game is registered on the register request handler"
    (tu/with-started [system (co/snake-system {})]
                     (register-mock-game (:engine system))
                     (is (= {:body (json/write-str (eg/move fake-game-state))
                             :headers {"content-type" "application/json"}
                             :status  200}
                            (e/handle-move-request
                              system
                              mock-move-request))))))

(deftest get-game-handler-test
  (testing "If a new game is registered on the register request handler"
    (tu/with-started [system (co/snake-system {})]
                     (register-mock-game (:engine system))
                     (is (= {:body    (json/write-str fake-game-state)
                             :headers {"content-type" "application/json"}
                             :status  200}
                            (e/handle-get-game-request
                              system
                              mock-get-game-request))))))