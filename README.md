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
Please note the section _**Frontend tests**_ for more details about running the
frontend tests.

For testing reframe apps read [this](https://github.com/Day8/re-frame/wiki/Testing).

### Frontend tests
```bash
lein doo once
```
This will run all tests that are passed within `test/snake/test_runner.cljs`.

The tests will be executed with phantom.js. By default the phantom.js binary found in 
`./dev-resources` will be used to execute the tests. The shipped version of phantom.js
only works for linux.
If you want to specify a custom phantom.js binary (e.g. you are working with MacOS and
need another phantom.js binary) export a environment variable `PHANTOMJSBIN` which has
the path to your custom phantom.js binary. 

For example:
```bash
export PHANTOMJSBIN="phantomjs" && lein doo once 
```

### Backend tests
```bash
lein test
```
