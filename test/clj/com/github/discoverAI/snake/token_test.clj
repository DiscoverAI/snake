(ns com.github.discoverAI.snake.token-test
  (:require [clojure.test :refer :all]
            [com.github.discoverAI.snake.token :as t]
            [com.github.discoverAI.snake.board :as b]))

(deftest random-exclude
  (testing "should randomly produce a vector without blacklisted numbers"
    (let [called (atom false)]
      (with-redefs [rand-nth (fn [collection]
                               (is (or (= [0 3 4]
                                          collection)
                                       (= [0 1 4]
                                          collection)
                                       ))
                               (reset! called true)
                               :pseudo-random)]
        (is (= [[:pseudo-random :pseudo-random]]
               (t/random-food-position [5 5] [[1 2] [2 3]])))

        (is (true? @called))))))