(ns com.github.discoverAI.snake.events
  (:require [re-frame.core :as re-frame]
            [com.github.discoverAI.snake.db :as db]))

(re-frame/reg-event-db
  ::initialize-db
  (fn [_ _]
    db/default-db))

(defn start-state [_db _event]
  db/mock-start-state)

(re-frame/reg-event-db
  ::start-game
  start-state)
