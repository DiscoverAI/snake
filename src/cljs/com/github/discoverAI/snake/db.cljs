(ns com.github.discoverAI.snake.db)

(def default-db
  ;TODO get State from backend
  {:name "re-frame"
   :board  [30 30]
   :tokens {:snake {:position  [[4 5][3 5] [2 5] [1 5]]
                    :direction [1 0]
                    :speed     1.0}}})
