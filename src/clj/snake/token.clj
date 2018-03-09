(ns snake.token)

(defn change-direction
  [current-direction change]
  (if (= :left change)
    [(last current-direction) (* (first current-direction) -1)]
    [(* (last current-direction) -1) (first current-direction)]
    )
  )