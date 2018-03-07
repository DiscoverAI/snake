(ns snake.events
  (:require [re-frame.core :as re-frame]
            [snake.db :as db]
            [snake.handlers :as handlers]))

(re-frame/reg-event-db
 ::initialize-db
 (fn  [_ _]
   db/default-db))


(defn dispatch-tick
  []
  (let [now (js/Date.)]
    (re-frame/dispatch [:tick now])))

(defonce do-timer (js/setInterval dispatch-tick 1000))