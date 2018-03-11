(ns com.github.discoverAI.snake.subs
  (:require [re-frame.core :as re-frame]))

(defn game-board [db]
  (:board db))

(re-frame/reg-sub
  ::game-board
  game-board)

(defn score [db]
  (:score db))

(re-frame/reg-sub
  ::score
  score)

(defn current-state [db]
  (:state db))

(re-frame/reg-sub
  ::current-state
  current-state)

(defn snake [db]
  (get-in db [:tokens :snake]))

(re-frame/reg-sub
  ::snake
  snake)
