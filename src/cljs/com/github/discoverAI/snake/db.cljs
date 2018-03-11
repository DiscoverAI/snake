(ns com.github.discoverAI.snake.db)

(def default-db
  {:board  []
   :state  :not-started
   :tokens {}})

(def mock-start-state
  {:board  [42 42]
   :state  :started
   :tokens {:snake {:position  [[11 10] [10 10] [9 10]]
                    :direction [1 0]
                    :speed     1.0}}})
