(ns com.github.discoverAI.snake.views
  (:require [re-frame.core :as re-frame]
            [com.github.discoverAI.snake.subs :as subs]))

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div "Hello from " @name]))
