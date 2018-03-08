(ns snake.state)


(def init-state
  {:board  [5 5]
   :tokens {:snake {:position  [[5 3] [5 2] [5 1]]
                    :direction [0 1]
                    :speed     1.0}}})