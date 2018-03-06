(defproject com.github.discoverAI/snake "0.1.0-SNAPSHOT"
  :description "A Clojure/ClojureScript Snake game for your browser"
  :url "https://github.com/DiscoverAI/snake"
  :license {:name "MIT"}
  :scm {:name "git"
        :url  "https://github.com/DiscoverAI/snake"}

  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.10.126"]
                 [reagent "0.7.0"]
                 [re-frame "0.10.5"]
                 [com.taoensso/sente "1.12.0"]
                 [de.otto/tesla-microservice "0.11.25"]
                 [de.otto/tesla-httpkit "1.0.1"]]

  :plugins [[lein-cljsbuild "1.1.5"]]
  :min-lein-version "2.5.3"
  :source-paths ["src/clj"]
  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]
  :figwheel {:css-dirs ["resources/public/css"]}

  :exclusions [org.slf4j/slf4j-nop commons-logging log4j/log4j org.slf4j/slf4j-log4j12]
  :main ^:skip-aot snake.core

  :lein-release {:deploy-via :clojars}

  :profiles {:dev {:dependencies [[binaryage/devtools "0.9.9"]]
                   :plugins      [[lein-figwheel "0.5.13"]
                                  [lein-release/lein-release "1.0.9"]]}}

  :cljsbuild {:builds [{:id           "dev"
                        :source-paths ["src/cljs"]
                        :figwheel     {:on-jsload "snake.core/mount-root"}
                        :compiler     {:main                 snake.core
                                       :output-to            "resources/public/js/compiled/app.js"
                                       :output-dir           "resources/public/js/compiled/out"
                                       :asset-path           "js/compiled/out"
                                       :source-map-timestamp true
                                       :preloads             [devtools.preload]
                                       :external-config      {:devtools/config {:features-to-install :all}}
                                       }}
                       {:id           "min"
                        :source-paths ["src/cljs"]
                        :compiler     {:main            snake.core
                                       :output-to       "resources/public/js/compiled/app.js"
                                       :optimizations   :advanced
                                       :closure-defines {goog.DEBUG false}
                                       :pretty-print    false}}]})
