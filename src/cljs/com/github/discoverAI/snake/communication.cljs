(ns com.github.discoverAI.snake.communication
  (:require-macros
    [cljs.core.async.macros :as asyncm :refer (go go-loop)])
  (:require
    [cljs.core.async :as async :refer (<! >! put! chan)]
    [taoensso.sente  :as sente :refer (cb-success?)]))

(let [{:keys [chsk ch-recv send-fn state]}
      (sente/make-channel-socket! "/chsk"                   ; Note the same path as before
                                  {:type :auto              ; e/o #{:auto :ajax :ws}
                                   :host "localhost:8080"})]
  (def chsk chsk)
  (def ch-chsk ch-recv)                                     ; ChannelSocket's receive channel
  (def chsk-send! send-fn)                                  ; ChannelSocket's send API fn
  (def chsk-state state))                                   ; Watchable, read-only atom

(defmulti -event-msg-handler :id)

(defn event-msg-handler
  [{:as ev-msg}]
  (-event-msg-handler ev-msg))

(defmethod -event-msg-handler
  ::register-game
  [{:keys [event]}]
  (println "Registered Event: " event))

(defmethod -event-msg-handler
  :default
  [{:keys [event]} _engine])

(def ARROW_KEY_CODES {37 :left
                      39 :right})

(defn send-register-request []
  (println "requesting register")
  (chsk-send! [:com.github.discoverAI.snake.communication/register-game
               {:board-width 20 :board-height 20 :snake-length 20}]))

(defn send-key-pressed [keycode]
  (if (some #{keycode} (keys ARROW_KEY_CODES))
    (chsk-send! [::key-pressed {:direction (get ARROW_KEY_CODES keycode)}])))

(defn start-router []
  (println "starting router")
  (sente/start-client-chsk-router! ch-chsk event-msg-handler))

