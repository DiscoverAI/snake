(ns com.github.discoverAI.snake.events
  (:require [re-frame.core :as re-frame]
            [com.github.discoverAI.snake.db :as db]
            [com.github.discoverAI.snake.websocket-api :as ws-api]
            [taoensso.sente :as sente]
            [taoensso.encore :refer-macros (have)]))

(defn init-db [_db [_event game-id]]
  (if (not (nil? game-id))
    (db/start-game (db/register-game db/default-db game-id))
    db/default-db))

(defn send-spectate-request [db _event]
  (if (and (= db/started (:state db)) (not (nil? (:game-id db))))
    (ws-api/spectate-game (:game-id db)
                          (fn [_] (println "spectate registered"))))
  db)

(re-frame/reg-event-db
  ::socket-connection-ready
  send-spectate-request)

(re-frame/reg-event-db
  ::initialize-db
  init-db)

(defn key-pressed [game-id event] (ws-api/send-key-pressed game-id (.-keyCode event)))

(defn attach-on-key-listener [game-id]
  (set! (.-onkeydown js/window) (partial key-pressed game-id)))

(defn start-game [db event]
  (ws-api/start-new-game (second event) (fn [callback-reply]
                                          (when (sente/cb-success? callback-reply)
                                            (re-frame/dispatch [::register-game-id callback-reply]))))
  (db/start-game db))

(defn end-game [db event]
  (db/end-game db))


(re-frame/reg-event-db
  ::start-game
  start-game)

(defn register-game [db event]
  (attach-on-key-listener (second event))
  (db/register-game db (second event)))

(re-frame/reg-event-db
  ::register-game-id
  register-game)

(defn update-game-state [db event]
  (db/update-game db (second event)))

(re-frame/reg-event-db
  ::update-game-state
  update-game-state)

(re-frame/reg-event-db
  ::end-game
  end-game)

;; Websocket stuff

(defmulti -event-msg-handler :id)

(defn event-msg-handler [{:as ev-msg}]
  (-event-msg-handler ev-msg))

(defmethod -event-msg-handler :default
  [{:keys [event]}]
  (println "Unhandled event: " event))

(defmethod -event-msg-handler :chsk/state
  [{:keys [?data]}]
  (if (:first-open? (second ?data))
    (do
      (re-frame/dispatch [::socket-connection-ready])
      (println "Channel socket successfully established!"))))

(defmethod -event-msg-handler :chsk/recv
  [{:keys [?data]}]
  (let [[id data] ?data]
    (when (= :game/update-game-state id)
      (re-frame/dispatch [::update-game-state data])
      (if (= true (:game-over data)) (re-frame/dispatch [::end-game])))))

(defmethod -event-msg-handler :chsk/handshake
  [{:keys [?data]}]
  (println "Handshake:" ?data))

(defn start []
  (sente/start-client-chsk-router! ws-api/ch-chsk event-msg-handler))
