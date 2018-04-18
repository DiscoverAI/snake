(ns com.github.discoverAI.snake.engine
  (:require [clojure.tools.logging :as log]
            [com.stuartsierra.component :as c]
            [com.github.discoverAI.snake.board :as b]
            [de.otto.status :as st]
            [de.otto.tesla.stateful.app-status :as as]
            [overtone.at-at :as at-at]
            [de.otto.tesla.stateful.scheduler :as scheduler]))

(defn game-id [game-state]
  (->> (hash game-state)
       (+ (System/nanoTime))
       (str "G_")
       (keyword)))

(defn new-game [width height snake-length]
  (let [game-state (b/place-food (b/initial-state width height snake-length))]
    {(game-id game-state) game-state}))

(defn modulo-vector [position-vector modulos]
  (map mod position-vector modulos))

(defn vector-addition [first second board]
  (-> (map + first second)
      (modulo-vector board)))

(def MOVE_UPDATE_INTERVAL 1000)

(defn snake-on-food? [games-state]
  (= (first (get-in games-state [:tokens :snake :position]))
     (first (get-in games-state [:tokens :food :position]))))

(defn move-snake [{:keys [board tokens] :as game-state}]
  (let [snake (:snake tokens)]
    (update snake :position
            (fn [snake-position]
              (concat [(vector-addition (first snake-position) (:direction snake) board)]
                      (if (snake-on-food? game-state)
                        snake-position
                      (drop-last snake-position)))))))

(defn move [game-state]
  (let [new-snake (move-snake game-state)]
    (assoc-in game-state [:tokens :snake] new-snake)))

(defn change-direction [games-state game-id direction]
  (log/info "Change direction: " direction)
  (swap! games-state assoc-in [game-id :tokens :snake :direction] direction)
  (log/debug "new state" (game-id @games-state)))

(defn update-game-state! [games-atom game-id callback-fn]
  (swap! games-atom update game-id move)
  (callback-fn (game-id @games-atom)))

(defn register-new-game [{:keys [games scheduler]} width height snake-length callback-fn]
  (let [game (new-game width height snake-length)
        game-id (first (keys game))]
    (swap! games merge game)
    (at-at/every MOVE_UPDATE_INTERVAL
                 #(update-game-state! games game-id callback-fn)
                 (scheduler/pool scheduler)
                 :desc "UpdateGameStateTask")
    game-id))

(defn games-state-status [games-state-atom]
  (if (and (map? @games-state-atom) (<= 0 (count @games-state-atom)))
    (st/status-detail :engine :ok (str (count @games-state-atom) " games registered"))
    (st/status-detail :engine :error "Engines games are corrupt")))

(defrecord Engine [app-status scheduler]
  c/Lifecycle
  (start [self]
    (log/info "-> starting Engine")
    (let [games-state (atom {})]
      (as/register-status-fun app-status (partial games-state-status games-state))
      (assoc self :games games-state)))
  (stop [_]
    (log/info "<- stopping Engine")))

(defn new-engine []
  (map->Engine {}))
