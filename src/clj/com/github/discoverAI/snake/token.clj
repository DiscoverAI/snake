(ns com.github.discoverAI.snake.token)
(defn- filtered-range [max blacklist]
  (->> (range max)
       (remove (set blacklist))))

(defn random-food-position [[board-width board-height] snake]
  (let [blacklist-x (map first snake)
        blacklist-y (map second snake)]
    [[(rand-nth (filtered-range board-width blacklist-x))
      (rand-nth (filtered-range board-height blacklist-y))]]))