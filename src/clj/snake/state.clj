(ns snake.state)


(def init-state
  {:field-width 10
   :field-height 10
   :snake [[5,3],[5,2], [5,1]] ; x,y coordinates for the snake. First is head, last is tail.
   :movement "right"
   :speed 1.0
   })