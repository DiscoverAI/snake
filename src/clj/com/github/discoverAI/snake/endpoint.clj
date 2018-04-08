(ns com.github.discoverAI.snake.endpoint
  (:require [com.stuartsierra.component :as c]
            [clojure.tools.logging :as log]
            [clojure.data.json :as json]
            [de.otto.tesla.stateful.handler :as handler]
            [ring.middleware.params :as params]
            [ring.middleware.keyword-params :as kparams]
            [de.otto.goo.goo :as metrics]
            [compojure.core :as cc]
            [com.github.discoverAI.snake.websocket-config :as ws-config]
            [com.github.discoverAI.snake.websocket-api :as ws-api]
            [taoensso.sente :as sente]))

(defn response [{:keys [engine]} _]
  (if (= 0 (count @(:games engine)))
    {:status 204}
    {:status  200
     :headers {"content-type" "application/json"}
     :body    (json/write-str @(:games engine))}))

(defn endpoint-filter [handler]
  (cc/routes
    (cc/GET "/games" req (handler req))
    (cc/GET "/games/" req (handler req))
    (cc/GET ws-config/INIT_ROUTE req (ws-api/ring-ajax-get-or-ws-handshake req))
    (cc/POST ws-config/INIT_ROUTE req (ws-api/ring-ajax-post req))))

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
    (sente/start-server-chsk-router! ws-api/ch-chsk (fn [event] (ws-api/event-msg-handler engine event)))
    self)
  (stop [_]
    (log/info "<- stopping Endpoint")))

(defn new-endpoint []
  (map->Endpoint {}))
