(ns snake.token)

(def LEFT (/ Math/PI 2))
(def RIGHT (* -1 (/ Math/PI 2)))

(defn change-direction [[x y] angle]
  [(Math/round (- (* x (Math/cos angle)) (* y (Math/sin angle))))
   (Math/round (+ (* x (Math/sin angle)) (* y (Math/cos angle))))])