(ns com.github.discoverAI.snake.views
  (:require [re-frame.core  :refer [subscribe dispatch]]))

(def table
  [:table.stage {:style {:height 377
                         :width  527}} ])

(defn pos-is-snake
  [snake pos]
  (boolean ((into #{} snake) pos))
  )

(defn create-row
  [width y snake]
  ; Without the key pro, we get an error
  ; I don't see why
  (into [:tr {:key y}]
        (for [x (range width)
              :let [current-pos [x y]]]
          (cond
            (pos-is-snake snake current-pos)
            [:td.snake-on-cell]
            :else
            [:td.point]
            )
          )
        )
  )

(defn render-board
  [width height snake]
  (let [cells (for [y (range height)]
                (create-row width y snake))
        ]
    (conj table [:tbody cells]))
  )

(defn board
  []
  (let [[grid-width grid-height] @(subscribe [:subs/board])
        [snake-positions] @(subscribe [:subs/snake-position])]
    (render-board grid-width grid-height snake-positions)
    )
  )

(defn main-panel
  []
   [board])