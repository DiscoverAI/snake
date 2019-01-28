(ns com.github.discoverAI.snake.endpoint
  (:require [com.stuartsierra.component :as c]
            [clojure.tools.logging :as log]
            [de.otto.tesla.stateful.handler :as handler]
            [compojure.core :as cc]
            [com.github.discoverAI.snake.websocket-config :as ws-config]
            [com.github.discoverAI.snake.websocket-api :as ws-api]
            [taoensso.sente :as sente]
            [compojure.api.sweet :refer :all]
            [schema.core :as s]
            [ring.util.http-response :refer :all]
            [com.github.discoverAI.snake.engine :as eg]))

(s/defschema GameInitialization
  {:width       s/Int
   :height      s/Int
   :snakeLength s/Int})

(s/defschema Game
  {:board     [s/Int]
   :score     s/Int
   :tokens    {:snake {:position  [[s/Int]]
                       :direction [s/Int]
                       :speed     s/Num}
               :food  {:position [[s/Int]]}}
   :game-over s/Bool})

(s/defschema DirectionChange
  {:direction [s/Int]})

(s/defschema GameId
  {:game-id s/Str})

(defn location-for-game-id [gameid]
  ; TODO: get port from config? Is there an easier way of returning the location?
  (str (.. java.net.InetAddress getLocalHost getHostName) ":8080/games/" (name gameid)))

(defn add-game-handler [engine {body :body-params}]
  (let [game-id (eg/register-game-without-timer
                  (:games engine)
                  (:width body)
                  (:height body)
                  (:snakeLength body))]
    (created (location-for-game-id game-id)
             {:gameId game-id})))

(defn get-game-handler [engine {:keys [params]}]
  (if-let [game (get @(:games engine) (keyword (:id params)))]
    (ok game)
    (not-found)))

(defn change-dir-handler [engine {:keys [direction]} id]
  (if (get @(:games engine) (keyword id))
    (do
      (eg/change-direction (:games engine) (keyword id) direction)
      (eg/update-game-state!
        (:games engine) (:game-id->scheduled-job-id engine) (keyword id) (fn [_])))))

(defn app [engine]
  (api
    {:swagger
     {:ui   "/api-docs"
      :spec "/swagger.json"
      :data {:info     {:title       "Snake API"
                        :description "This rest api for snake is intended for use in Machine learning clients."}
             :tags     [{:name "api", :description "game apis"}]
             :consumes ["application/json"]
             :produces ["application/json"]}}}
    (context "/games" []
      (resource
        {:tags ["games"]
         :post {:summary    "add's a game"
                :parameters {:body-params GameInitialization}
                :responses  {created {:schema      GameId
                                      :description "the id of the created game"}}
                :handler    (partial add-game-handler engine)}})
      (context "/:id" []
        :path-params [id :- s/Str]
        (resource
          {:tags ["games"]
           :get  {:summary   "gets a game state"
                  :responses {ok {:schema Game}}
                  :handler   (partial get-game-handler engine)}})
        (PUT "/tokens/snake/direction" []
          :tags ["games"]
          :summary "changes snake direction in game"
          :body [dir-change DirectionChange]
          :return Game
          (ok (change-dir-handler engine dir-change id)))))

    (GET ws-config/INIT_ROUTE req (ws-api/ring-ajax-get-or-ws-handshake req))
    (POST ws-config/INIT_ROUTE req (ws-api/ring-ajax-post req))))

(defrecord Endpoint [handler engine]
  c/Lifecycle
  (start [self]
    (log/info "-> starting Endpoint")
    (handler/register-timed-handler handler (app engine))
    (sente/start-server-chsk-router! ws-api/ch-chsk (fn [event] (ws-api/event-msg-handler engine event)))
    self)
  (stop [_]
    (log/info "<- stopping Endpoint")))

(defn new-endpoint []
  (map->Endpoint {}))

(defn transform-state-map-to-board-map [state-map]
  [[1]])
