(ns com.github.discoverAI.snake.events
  (:require [re-frame.core :as re-frame]
            [snake.db :as db]))

(re-frame/reg-event-db
 ::initialize-db
 (fn  [_ _]
   db/default-db))
