(ns com.github.discoverAI.snake.db)

(def started :started)
(def not-started :not-started)
(def game-over :game-over)

(def default-db
  {:board  []
   :state  not-started
   :tokens {}})

(defn start-game [db]
  (assoc db :state started))

(defn register-game [db game-id]
  (assoc db :game-id game-id))

(defn update-game [db new-game-state]
  (merge db new-game-state))
