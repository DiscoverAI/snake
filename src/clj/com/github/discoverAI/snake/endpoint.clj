(ns com.github.discoverAI.snake.endpoint
  (:require [com.stuartsierra.component :as c]
            [clojure.tools.logging :as log]
            [clojure.data.json :as json]
            [de.otto.tesla.stateful.handler :as handler]
            [ring.middleware.params :as params]
            [ring.middleware.keyword-params :as kparams]
            [de.otto.goo.goo :as metrics]
            [compojure.core :as cc]
            [taoensso.sente :as sente]
            [taoensso.sente.server-adapters.http-kit :refer (get-sch-adapter)]))

(defn response [{:keys [engine]} _]
  (if (= 0 (count @(:games engine)))
    {:status 204}
    {:status  200
     :headers {"content-type" "application/json"}
     :body    (json/write-str @(:games engine))}))

(let [{:keys [ch-recv send-fn connected-uids ajax-post-fn ajax-get-or-ws-handshake-fn]}
      (sente/make-channel-socket! (get-sch-adapter) {})]
  (def ring-ajax-post ajax-post-fn)
  (def ring-ajax-get-or-ws-handshake ajax-get-or-ws-handshake-fn)
  (def ch-chsk ch-recv)                                     ; ChannelSocket's receive channel
  (def chsk-send! send-fn)                                  ; ChannelSocket's send API fn
  (def connected-uids connected-uids))                       ; Watchable, read-only atom

(defn event-msg-handler
  [{:as ev-msg :keys [id ?data event]}]
  (println ?data))

(sente/start-server-chsk-router!
  ch-chsk event-msg-handler)

(defn endpoint-filter [handler]
  (cc/routes
    (cc/GET "/games" req (handler req))
    (cc/GET "/games/" req (handler req))
    (cc/GET "/chsk" req (ring-ajax-get-or-ws-handshake req))
    (cc/POST "/chsk" req (ring-ajax-post req))))

(defn create-routes [self]
  (->> (partial response self)
       (metrics/timing-middleware)
       (endpoint-filter)
       kparams/wrap-keyword-params
       params/wrap-params))

(defrecord Endpoint [handler engine]
  c/Lifecycle
  (start [self]
    (log/info "-> starting Endpoint")
    (handler/register-handler handler (create-routes self))
    self)
  (stop [_]
    (log/info "<- stopping Endpoint")))

(defn new-endpoint []
  (map->Endpoint {}))
