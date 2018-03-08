(ns snake.direction_test
  (:require [clojure.test :refer :all]
            [snake.direction :as dir]))

(deftest change-direction-test
  (testing "given move diretion towards right, we go up if navigated left")
  (is (= [-1, 0] (dir/change-direction [0, 1] :left)))
  (is (= [0, -1] (dir/change-direction [1, 0] :left)))
  (is (= [1, 0] (dir/change-direction [0, -1] :left)))
  (is (= [0, 1] (dir/change-direction [-1, 0] :left)))
  )