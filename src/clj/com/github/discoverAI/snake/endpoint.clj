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
            [taoensso.sente :as sente]
            [com.github.discoverAI.snake.engine :as eg]))

(defn handle-register-request [{:keys [engine]} request]
  (let [p (:params request)]
    {:status  200
     :headers {"content-type" "application/json"}
     :body    (json/write-str {:game-id (eg/register-new-game-without-timer
                                          engine
                                          (Integer/parseInt (:width p))
                                          (Integer/parseInt (:height p))
                                          (Integer/parseInt (:snake-length p))
                                          (fn []))})}))

(defn handle-move-request [{:keys [engine]} request]
  (let [p (:params request)
        id (:id p)
        x-dir (:x p)
        y-dir (:y p)]
    (eg/change-direction (:games engine)
                         (keyword id)
                         [(Integer/parseInt x-dir)
                          (Integer/parseInt y-dir)])
    {:status  200
     :headers {"content-type" "application/json"}
     :body    (json/write-str (eg/update-game-state!
                                (:games engine)
                                (keyword id)
                                (fn [_])
                                (:game-timer-tasks engine)))}))

(defn handle-get-game-request [{:keys [engine]} {:keys [params]}]
  (if (nil? (get @(:games engine) (keyword (:id params))))
    {:status 404}
    {:status  200
     :headers {"content-type" "application/json"}
     :body    (json/write-str (get @(:games engine) (keyword (:id params))))}))

(defn endpoint-filter [register-handler move-handler get-game-handler]
  (cc/routes
    (cc/POST "/register/" req (register-handler req))
    (cc/POST "/register" req (register-handler req))
    (cc/POST "/move/" req (move-handler req))
    (cc/POST "/move" req (move-handler req))
    (cc/GET "/games/:id" req (get-game-handler req))
    (cc/GET "/games/:id/" req (get-game-handler req))
    (cc/GET ws-config/INIT_ROUTE req (ws-api/ring-ajax-get-or-ws-handshake req))
    (cc/POST ws-config/INIT_ROUTE req (ws-api/ring-ajax-post req))))

(defn- timed-handler [handler self]
  (metrics/timing-middleware (partial handler self)))

(defn create-routes [self]
  (->> (endpoint-filter
         (timed-handler handle-register-request self)
         (timed-handler handle-move-request self)
         (timed-handler handle-get-game-request self))
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