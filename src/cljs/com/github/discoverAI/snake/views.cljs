(ns com.github.discoverAI.snake.views
  (:require [re-frame.core :refer [subscribe dispatch]]
            [com.github.discoverAI.snake.subs :as subs]
            [com.github.discoverAI.snake.db :as db]
            [com.github.discoverAI.snake.events :as events]))

(defn board-grid []
  [:span "asd"])

(defn board []
  [:section
   (let [current-state @(subscribe [::subs/current-state])]
     (cond
       (= db/not-started current-state) [:h2 "Press \"New Game\" button to start"]
       (= db/started current-state) [board-grid]))])

(defn info-panel []
  [:header
   [:div {:class "title"} [:h1 "Snake"]]
   (when-let [score @(subscribe [::subs/score])]
     [:div {:class "score"} [:h1 (str score " Points")]])])

(defn hints []
  [:footer
   (let [current-state @(subscribe [::subs/current-state])]
     (cond
       (= db/not-started current-state) [:a {:class "btn" :on-click #(dispatch [::events/start-game])} "New Game"]
       (= db/started current-state) [:span "Use your keyboard arrows to navigate"]
       :else [:span ""]))])

(defn base-template []
  [:div {:class "wrapper"}
   [info-panel]
   [:main [board]]
   [hints]])
