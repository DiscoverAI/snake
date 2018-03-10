(ns com.github.discoverAI.snake.test-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [com.github.discoverAI.snake.core-test]
            [com.github.discoverAI.snake.events-test]
            [com.github.discoverAI.snake.subs-test]))

(doo-tests 'com.github.discoverAI.snake.core-test
           'com.github.discoverAI.snake.events-test
           'com.github.discoverAI.snake.subs-test)