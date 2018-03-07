(ns snake.handlers
  (:require [re-frame.core :as re-frame]
            [snake.db :as db]))

(defn handle-tick
  [db, v]
  (print (:field db))
  db
  )

(re-frame/reg-event-db
  :tick
  handle-tick)