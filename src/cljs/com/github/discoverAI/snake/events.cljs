(ns com.github.discoverAI.snake.events
  (:require [re-frame.core :as re-frame]
            [com.github.discoverAI.snake.db :as db]
            [com.github.discoverAI.snake.communication :as c]))

(defn key-pressed [event] (c/send-key-pressed (.-keyCode event)))

(re-frame/reg-event-db
  ::initialize-db
  (fn [_ _]
    db/default-db))

(defn init-new-game []
  (c/start-router)
  (c/send-register-request)
  (set! (.-onkeydown js/window) key-pressed))

(defn start-game [_db _event]
  (init-new-game)
  ;TODO: wait syncronously here for backend?
  db/mock-start-state)

(re-frame/reg-event-db
  ::start-game
  start-game)
