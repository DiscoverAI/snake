(ns com.github.discoverAI.snake.websocket-api
  (:require [com.github.discoverAI.snake.websocket-config :as ws]

            [taoensso.sente :as sente]))

(let [{:keys [chsk ch-recv send-fn state]} (sente/make-channel-socket! ws/INIT_ROUTE ws/CLIENT_CONFIG)]
  (def chsk chsk)
  (def ch-chsk ch-recv)                                     ; ChannelSocket's receive channel
  (def chsk-send! send-fn)                                  ; ChannelSocket's send API fn
  (def chsk-state state))

(def ARROW_KEY_CODES {37 :left
                      39 :right})

(defn send-key-pressed [keycode]
  (if (some #{keycode} (keys ARROW_KEY_CODES))
    (chsk-send! [::key-pressed {:direction (get ARROW_KEY_CODES keycode)}])))

(defn start-new-game [game-params callback]
  (chsk-send! [::start-new-game game-params] 3000 callback))
