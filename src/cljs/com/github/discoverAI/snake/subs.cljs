(ns com.github.discoverAI.snake.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  ::name
  (fn [db]
    (:name db)))

(defn get-board [db] (:board db))

(reg-sub
  :game-board-bounds
  get-board)

(defn get-snake-position [db] (:position (:snake (:tokens db))))

(reg-sub
  :snake-position
  get-snake-position)