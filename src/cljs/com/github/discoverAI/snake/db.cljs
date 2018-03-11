(ns com.github.discoverAI.snake.db)

(def started :started)
(def not-started :not-started)
(def game-over :game-over)

(def default-db
  {:board  []
   :state  not-started
   :tokens {}})

(def mock-start-state
  {:board  [42 42]
   :score  0
   :state  started
   :tokens {:snake {:position  [[11 10] [10 10] [9 10]]
                    :direction [1 0]
                    :speed     1.0}}})
