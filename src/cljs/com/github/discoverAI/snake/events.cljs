(ns com.github.discoverAI.snake.events
  (:require [re-frame.core :as re-frame]
            [com.github.discoverAI.snake.db :as db]))

(defn handle-init-db
  [_coeffects _event]
  db/default-db
  )

(re-frame/reg-event-db
  ::initialize-db
  handle-init-db)
