(ns com.github.discoverAI.snake.endpoint
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

(def ARROW_KEY_CODES {37 :left
                      39 :right})

(defn send-key-pressed [keycode]
  (print "send key pressed")
  (if (some #{keycode} (keys ARROW_KEY_CODES))
    (print "send key pressed" keycode)
    (chsk-send! [::key-pressed {:direction (get ARROW_KEY_CODES keycode)}])))
