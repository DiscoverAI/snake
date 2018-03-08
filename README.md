# Snake

[![Build Status](https://travis-ci.org/DiscoverAI/snake.svg?branch=master)](https://travis-ci.org/DiscoverAI/snake)

A [re-frame](https://github.com/Day8/re-frame) Snake game for the browser.
Interact with your keyboard or with its api.

The purpose of this game is to be used as a test bed for running
Reinforcement Learning algorithms.

## Development Mode

### Run Frontend
```
lein clean
lein figwheel dev
```
Figwheel will automatically push cljs changes to the browser.

Wait a bit, then browse to [http://localhost:3449](http://localhost:3449).
### Run Backend
```
lein run
```
The Backend will be accessible on [http://localhost:8080](http://localhost:8080).

## Production Build
To compile clojurescript to javascript:
```
lein clean
lein cljsbuild once min
```  

## Test
To run the frontend and the backend tests:
```bash
lein test-all
```

For testing reframe apps read [this](https://github.com/Day8/re-frame/wiki/Testing).

### Frontend tests
```bash
lein doo once
```
This will run all tests that are passed within `test/snake/test_runner.cljs`.

Currently only phantom.js tests are supported. Phantom.js comes within the `./dev-resources`
folder. There is only a phantom.js version for linux included as of now. 

### Backend tests
```bash
lein test
```
