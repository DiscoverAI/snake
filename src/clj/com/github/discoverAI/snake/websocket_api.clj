(ns com.github.discoverAI.snake.websocket-api
  (:require [taoensso.sente :as sente]
            [taoensso.sente.server-adapters.http-kit :refer (get-sch-adapter)]
            [com.github.discoverAI.snake.engine :as eg]
            [clojure.tools.logging :as log]))

(let [{:keys [ch-recv send-fn connected-uids ajax-post-fn ajax-get-or-ws-handshake-fn]}
      (sente/make-channel-socket! (get-sch-adapter) {})]
  (def ring-ajax-post ajax-post-fn)
  (def ring-ajax-get-or-ws-handshake ajax-get-or-ws-handshake-fn)
  (def ch-chsk ch-recv)                                     ; ChannelSocket's receive channel
  (def chsk-send! send-fn)                                  ; ChannelSocket's send API fn
  (def connected-uids connected-uids))                      ; Watchable, read-only atom

(defmulti -event-msg-handler :id)

(defn event-msg-handler [engine {:as event-message}]
  (-event-msg-handler event-message engine))

(defmethod -event-msg-handler
  :default
  [{:keys [event]} _engine]
  (log/debug "Unhandled event: " event))

(defmethod -event-msg-handler
  ::key-pressed
  [{:keys [?data]} {:keys [games]}]
  (eg/change-direction games :mocked-game-id (:direction ?data)))

(defmethod -event-msg-handler
  ::start-new-game
  [{:keys [?data ?reply-fn]} engine]
  (?reply-fn (eg/register-new-game engine (:width ?data) (:height ?data) (:snake-length ?data))))
