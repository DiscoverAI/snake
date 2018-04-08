(ns com.github.discoverAI.snake.endpoint-test
  (:require [clojure.test :refer :all]
            [com.github.discoverAI.snake.core :as co]
            [com.github.discoverAI.snake.engine :as eg]
            [de.otto.tesla.util.test-utils :as tu]
            [com.github.discoverAI.snake.websocket-api :as ws-api]))

(deftest change-direction-endpoint-test
  (testing "if the change direction endpoint works"
    (let [dir-changed? (atom false)]
      (with-redefs [eg/change-direction (fn [_ _ direction]
                                          (is (= direction :right))
                                          (reset! dir-changed? true))]
        (tu/with-started [system (co/snake-system {})]
                         (ws-api/event-msg-handler (:engine system)
                                                   {:id    :com.github.discoverAI.snake.websocket-api/key-pressed
                                                    :event [:com.github.discoverAI.snake.websocket-api/key-pressed {:direction :right}]
                                                    :?data {:direction :right}})
                         (tu/eventually (is (= true @dir-changed?))))))))
