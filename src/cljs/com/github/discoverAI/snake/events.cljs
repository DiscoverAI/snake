(ns com.github.discoverAI.snake.events
  (:require [re-frame.core :as re-frame]
            [com.github.discoverAI.snake.db :as db]
            [com.github.discoverAI.snake.endpoint :as ep]))

(defn key-pressed [event] (ep/send-key-pressed (.-keyCode event)))

(re-frame/reg-event-db
  ::initialize-db
  (fn [_ _]
    db/default-db))

(defn attach-on-key-listener []
  (set! (.-onkeydown js/window) key-pressed))

(defn start-game [_db _event]
  (attach-on-key-listener)
  db/mock-start-state)

(re-frame/reg-event-db
  ::start-game
  start-game)
