(ns com.github.discoverAI.snake.websocket-api
  (:require [com.github.discoverAI.snake.websocket-config :as ws]
            [taoensso.sente :as sente]
            [re-frame.core :as re-frame]))

(let [{:keys [chsk ch-recv send-fn state]} (sente/make-channel-socket! ws/INIT_ROUTE ws/CLIENT_CONFIG)]
  (def chsk chsk)
  (def ch-chsk ch-recv)                                     ; ChannelSocket's receive channel
  (def chsk-send! send-fn)                                  ; ChannelSocket's send API fn
  (def chsk-state state))

(def KEY_CODE->DIRECTION_VECTOR
  {37 [-1 0]
   38 [0 -1]
   39 [1 0]
   40 [0 1]})

(defn send-key-pressed [keycode]
  (if (some #{keycode} (keys KEY_CODE->DIRECTION_VECTOR))
    (chsk-send! [::key-pressed {:direction (get KEY_CODE->DIRECTION_VECTOR keycode)}])))

(defn start-new-game [game-params callback]
  (chsk-send! [::start-new-game game-params] 3000 callback))
