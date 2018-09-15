(ns com.github.discoverAI.snake.websocket-api
  (:require [com.github.discoverAI.snake.websocket-config :as ws]
            [taoensso.sente :as sente]))

(def CLIENT_CONFIG
  {:type :auto
   :host "localhost:8080"})

(let [{:keys [chsk ch-recv send-fn state]} (sente/make-channel-socket! ws/INIT_ROUTE CLIENT_CONFIG)]
  (def chsk chsk)
  (def ch-chsk ch-recv)                                     ; ChannelSocket's receive channel
  (def chsk-send! send-fn)                                  ; ChannelSocket's send API fn
  (def chsk-state state))

(def KEY_CODE->DIRECTION_VECTOR
  {
   ;Arrows
   37 [-1 0]
   38 [0 -1]
   39 [1 0]
   40 [0 1]
   ;WASD
   65 [-1 0]
   87 [0 -1]
   68 [1 0]
   83 [0 1]})

(defn send-key-pressed [game-id keycode]
  (if (some #{keycode} (keys KEY_CODE->DIRECTION_VECTOR))
    (chsk-send! [::key-pressed {:game-id game-id :direction (get KEY_CODE->DIRECTION_VECTOR keycode)}])))

(defn start-new-game [game-params callback]
  (chsk-send! [::start-new-game game-params] 3000 callback))
