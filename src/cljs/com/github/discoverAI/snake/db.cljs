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

(defn start-game [db]
  (assoc db :state started))

(defn register-game [db game-id]
  (assoc db :game-id game-id))

(defn update-game [db new-game-state]
  (merge db new-game-state))
