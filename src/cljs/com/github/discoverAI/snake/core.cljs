(ns com.github.discoverAI.snake.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [com.github.discoverAI.snake.events :as events]
            [com.github.discoverAI.snake.views :as views]
            [com.github.discoverAI.snake.config :as config]))

(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (events/start)
  (reagent/render [views/base-template]
                  (.getElementById js/document "app")))

(defn ^:export init [game-id]
  (re-frame/dispatch-sync [::events/initialize-db game-id])
  (dev-setup)
  (mount-root))
