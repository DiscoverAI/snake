(ns com.github.discoverAI.snake.test-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [com.github.discoverAI.snake.core-test]))

(doo-tests 'snake.core-test)