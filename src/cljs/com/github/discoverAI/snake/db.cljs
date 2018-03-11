(ns com.github.discoverAI.snake.db)

(def started :started)
(def not-started :not-started)
(def game-over :game-over)

(def default-db
  {:board  []
   :state  not-started
   :tokens {}})

(def mock-start-state
  {:board  [24 24]
   :score  0
   :state  started
   :tokens {:snake {:position  [[13 12] [12 12] [11 12]]
                    :direction [1 0]
                    :speed     1.0}}})
