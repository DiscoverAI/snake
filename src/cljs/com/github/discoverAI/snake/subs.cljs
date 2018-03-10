(ns com.github.discoverAI.snake.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
  ::name
  (fn [db]
    (:name db)))

(defn get-board [db] (:board db))

(re-frame/reg-sub
  ::board
  get-board)

(defn get-snake-position [db] (:position (:snake (:tokens db))))

(re-frame/reg-sub
  ::snake-position
  get-snake-position)