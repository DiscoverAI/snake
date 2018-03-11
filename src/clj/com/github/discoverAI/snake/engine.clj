(ns com.github.discoverAI.snake.engine
  (:require [clojure.tools.logging :as log]
            [com.stuartsierra.component :as c]
            [com.github.discoverAI.snake.board :as b]
            [de.otto.status :as st]
            [de.otto.tesla.stateful.app-status :as as]))

(defn game-id [game-state]
  (->> (hash game-state)
       (+ (System/nanoTime))
       (str "G_")
       (keyword)))

(defn on-tick [game-state]
  (let [pos-path [:tokens :snake :position]
        position (get-in game-state pos-path)
        dir (get-in game-state [:tokens :snake :direction])
        new-head (vec (map + dir (first position)))]
    (cond
      ;TODO check if we run out of bounds or snake eats itself (seperate task)
      :else (assoc-in game-state pos-path
              (into
                [new-head] ;the next snake head will be appended at beginning of the list
                (vec (butlast position)))) ;the tail will be cut off
    )
  ))

(defn new-game [width height snake-length]
  (let [game-state (b/initial-state width height snake-length)]
    {(game-id game-state) game-state}))

(defn register-new-game [{:keys [games]} width height snake-length]
  (let [game (new-game width height snake-length)]
    (swap! games merge game)
    (first (keys game))))

(defn games-state-status [games-state-atom]
  (if (and (map? @games-state-atom) (<= 0 (count @games-state-atom)))
    (st/status-detail :engine :ok (str (count @games-state-atom) " games registered"))
    (st/status-detail :engine :error "Engines games are corrupt")))

(defrecord Engine [app-status]
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
