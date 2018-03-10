(ns com.github.discoverAI.snake.core
  (:require [de.otto.tesla.system :as system]
            [de.otto.goo.goo :as goo]
            [iapetos.collector.jvm :as jvm]
            [de.otto.tesla.serving-with-httpkit :as httpkit]
            [clojure.tools.logging :as log]
            [com.stuartsierra.component :as c]
            [com.github.discoverAI.snake.engine :as e]))

(defn peppermint-butler-system [runtime-config]
  (-> (system/base-system runtime-config)
      (assoc :engine (c/using (e/new-engine) [:config :app-status]))
      (httpkit/add-server)))

(defonce _ (jvm/initialize (goo/snapshot)))
(defonce _ (Thread/setDefaultUncaughtExceptionHandler
             (reify Thread$UncaughtExceptionHandler
               (uncaughtException [_ thread ex]
                 (log/error ex "Uncaught exception on " (.getName thread))))))

(defn -main [& _]
  (system/start (peppermint-butler-system {})))
