(ns com.github.discoverAI.snake.endpoint-test
  (:require [clojure.test :refer :all]
            [com.github.discoverAI.snake.endpoint :as e]
            [com.github.discoverAI.snake.core :as co]
            [de.otto.tesla.util.test-utils :as tu]))


(deftest change-direction-endpoint-test
  (testing "if the change direction endpoint works"
    ;TODO: This test should test in the future that the according game in the engine has been updated
    ;TODO: Like this the test just checks if the right method is dispatched.
    (tu/with-started [system (co/snake-system {})]
                     (is (= (e/event-msg-handler (:engine system)
                                                 {:id :com.github.discoverAI.snake.events/key-pressed
                                                  :event [:com.github.discoverAI.snake.events/key-pressed {:direction :right}]
                                                  :?data {:direction :right},})
                            {:direction :right})))))
