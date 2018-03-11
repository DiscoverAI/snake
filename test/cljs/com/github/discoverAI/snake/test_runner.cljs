(ns com.github.discoverAI.snake.test-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [com.github.discoverAI.snake.events-test]
            [com.github.discoverAI.snake.subs-test]
            [com.github.discoverAI.snake.views-test]))

(doo-tests 'com.github.discoverAI.snake.events-test
           'com.github.discoverAI.snake.subs-test
           'com.github.discoverAI.snake.views-test)