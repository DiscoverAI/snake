(ns com.github.discoverAI.snake.board
  (:require [clojure.set :as set]))

(defn- median [x]
  (int (Math/ceil (/ x 2))))

(defn initial-snake-position-base-vector
  ([snake-length]
   (reverse (range (+ (* -1 snake-length) (median snake-length))
                   (- snake-length (Math/floor (/ snake-length 2))))))
  ([snake-length [x-direction y-direction]]
   (map (fn [base] [(* base x-direction) (* base y-direction)])
        (initial-snake-position-base-vector snake-length))))

(defn initial-snake-position [snake-length [x-middle y-middle] direction]
  (map (fn [[x-base y-base]] [(+ x-base x-middle) (+ y-base y-middle)])
       (initial-snake-position-base-vector snake-length direction)))

(defn initial-state [board-width board-height snake-length]
  {:board  [board-width board-height]
   :tokens {:snake {:position  (initial-snake-position snake-length [(median board-width) (median board-height)] [1 0])
                    :direction [1 0]
                    :speed     1.0}}
   :score  0
   :game-over false})

(defn- cartesian [v1 v2]
  (for [x v1
        y v2]
    [x y]))

(defn random-vector [[x y] blacklist]
  (rand-nth (vec (set/difference
                   (set (cartesian (range x) (range y)))
                   (set blacklist)))))

(defn place-food [game-state]
  (assoc-in game-state [:tokens :food :position]
            [(random-vector (:board game-state) (get-in game-state [:tokens :snake :position]))]))
