(ns com.github.discoverAI.snake.views
  (:require [re-frame.core :refer [subscribe dispatch]]
            [com.github.discoverAI.snake.subs :as subs]))

(def table [:table.container])

(def container-view
  [:table.container])

(def container-body
  [:tbody])

(defn row-container
  [key]
  [:tr {:key key}])


(defn pos-is-snake [snake pos]
  (boolean ((into #{} snake) pos)))

(defn cell-item-for [current-pos snake]
  (cond
    (pos-is-snake snake current-pos)
    [:td.snake]
    :else
    [:td.cell]))

(defn create-row
  [width y snake]
  (into (row-container y)
        (for [x (range width) :let [current-pos [x y]]]
          (cell-item-for current-pos snake))
        ))

(defn render-board
  [width height snake]
  (print snake)
  (let [cells  (for [y (range height)]
                     (create-row width y snake))]
    (conj container-view (into container-body cells))
    ))

(defn board []
  (let [[grid-width grid-height] @(subscribe [::subs/game-board])
        snake @(subscribe [::subs/snake])]
    (render-board grid-width grid-height (:position snake))))

(defn info-panel []
  [:header "Snake"])

(defn hints []
  [:footer "footer"])

(defn base-template []
  [:div.wrapper
   [info-panel]
   [board]
   [hints]])
