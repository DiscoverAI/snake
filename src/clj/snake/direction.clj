(ns snake.direction)


(defn change-direction
  [current-direction change]
  (if (= :left change)
    [(* (last current-direction) -1) (* (first current-direction) -1)])
  )