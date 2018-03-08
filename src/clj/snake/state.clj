(ns snake.state)


(def init-state
  {:board  [5 5]
   :tokens {:snake {
                    ;[x y] coordinates for the snake
                    ; first list item is the head, last is tail
                    :position  [[5 3] [5 2] [5 1]]
                    :direction [0 1] ; change vector to [x y] position
                    :speed     1.0}}})