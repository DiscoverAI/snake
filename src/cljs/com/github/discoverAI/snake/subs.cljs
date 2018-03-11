(ns com.github.discoverAI.snake.subs
  (:require [re-frame.core :as re-frame]))

(defn game-board [db]
  (:board db))

(re-frame/reg-sub
  ::game-board
  game-board)

(defn snake [db]
  (get-in db [:tokens :snake]))

(re-frame/reg-sub
  ::snake
  snake)
