(ns com.github.discoverAI.snake.events
  (:require [re-frame.core :as re-frame]
            [com.github.discoverAI.snake.db :as db]
            [com.github.discoverAI.snake.websocket-api :as ws-api]
            [taoensso.sente :as sente]))

(defn init-db [_db _event]
  db/default-db)

(re-frame/reg-event-db
  ::initialize-db
  init-db)

(defn key-pressed [event] (ws-api/send-key-pressed (.-keyCode event)))

(defn attach-on-key-listener []
  (set! (.-onkeydown js/window) key-pressed))

(defn start-game [db event]
  (attach-on-key-listener)
  (ws-api/start-new-game (second event) (fn [callback-reply]
                                          (when (sente/cb-success? callback-reply)
                                            (re-frame/dispatch [::register-game-id callback-reply]))))
  (db/start-game db))

(re-frame/reg-event-db
  ::start-game
  start-game)

(defn register-game [db event]
  (println "registering game with id: " (second event))
  (db/register-game db (second event)))

(re-frame/reg-event-db
  ::register-game-id
  register-game)
