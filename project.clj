(def phantomjs-bin (or (System/getenv "PHANTOMJSBIN")
                     "./dev-resources/phantomjs-2.1.1-linux-x86_64"))

(def java-opts (if (= (System/getProperty "java.version") "10") ["--add-modules" "java.xml.bind"] []))

(defproject com.github.discoverAI/snake "0.1.1-SNAPSHOT"
  :description "A Clojure/ClojureScript Snake game for your browser"
  :url "https://github.com/DiscoverAI/snake"
  :license {:name "MIT"}
  :scm {:name "git"
        :url  "https://github.com/DiscoverAI/snake.git"}

  :min-lein-version "2.5.3"
  :test-paths ["test/clj" "test/cljs"]
  :jvm-opts ~java-opts

  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.10.145"]
                 [reagent "0.7.0"]
                 [re-frame "0.10.5"]
                 [com.taoensso/sente "1.12.0"]
                 [de.otto/tesla-microservice "0.11.25"]
                 [de.otto/tesla-httpkit "1.0.1"]
                 [org.clojure/tools.logging "0.4.0"]
                 [ch.qos.logback/logback-classic "1.2.3"]
                 [hiccup "1.0.5"]]

  :plugins [[lein-cljsbuild "1.1.5"]
            [lein-doo "0.1.8"]
            [deraen/lein-sass4clj "0.3.1"]]
  :aliases {"test-all" ["do" "test" ["doo" "once"]]
            "uberjar"  ["do" "clean" ["sass4clj" "once"] ["cljsbuild" "once" "min"] "uberjar"]}

  :auto-clean false
  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target" "resources/public/css"]

  :main ^:skip-aot com.github.discoverAI.snake.core
  :source-paths ["src/clj/" "src/cljc"]
  :uberjar-name "snake-deploy-standalone.jar"
  :profiles {:uberjar {:aot :all}
             :dev     {:dependencies [[binaryage/devtools "0.9.9"]
                                      [ring/ring-mock "0.3.2"]]
                       :plugins      [[lein-figwheel "0.5.13"]
                                      [lein-release/lein-release "1.0.9"]]}}
  :doo {:build "test"
        :alias {:default [:phantom]}
        :paths {:phantom ~phantomjs-bin}}
  :lein-release {:deploy-via :clojars}
  :sass {:source-paths ["src/scss/"]
         :target-path  "resources/public/css"}
  :figwheel {:css-dirs ["resources/public/css"]}

  :cljsbuild {:builds [{:id           "dev"
                        :source-paths ["src/cljs" "src/cljc"]
                        :figwheel     {:on-jsload "com.github.discoverAI.snake.core/mount-root"}
                        :compiler     {:main                 com.github.discoverAI.snake.core
                                       :output-to            "resources/public/js/compiled/app.js"
                                       :output-dir           "resources/public/js/compiled/out"
                                       :asset-path           "js/compiled/out"
                                       :source-map-timestamp true
                                       :preloads             [devtools.preload]
                                       :external-config      {:devtools/config {:features-to-install :all}}}}

                       {:id           "min"
                        :source-paths ["src/cljs" "src/cljc"]
                        :compiler     {:main            com.github.discoverAI.snake.core
                                       :output-to       "resources/public/js/compiled/app.js"
                                       :optimizations   :advanced
                                       :closure-defines {goog.DEBUG false}
                                       :pretty-print    false}}
                       {:id           "test"
                        :source-paths ["src/cljs" "test/cljs" "src/cljc"]
                        :compiler     {:main          com.github.discoverAI.snake.test-runner
                                       :output-to     "resources/public/js/compiled/tests.js"
                                       :output-dir    "resources/public/js/compiled/test/out"
                                       :optimizations :none}}]})
