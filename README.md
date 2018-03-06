# Snake

[![Build Status](https://travis-ci.org/DiscoverAI/snake.svg?branch=master)](https://travis-ci.org/DiscoverAI/snake)

A [re-frame](https://github.com/Day8/re-frame) Snake game for the browser.

Interact with your keyboard or with its api.

The purpose of this game is to be used as a test bed for running
Reinforcement Learning algorithms.

## Development Mode

### Run application:
```
lein clean
lein figwheel dev
```

Figwheel will automatically push cljs changes to the browser.

Wait a bit, then browse to [http://localhost:3449](http://localhost:3449).

## Production Build
To compile clojurescript to javascript:
```
lein clean
lein cljsbuild once min
```  

## Test
Read [this](https://github.com/Day8/re-frame/wiki/Testing)
