(ns com.github.discoverAI.snake.core
  (:require [de.otto.tesla.system :as system]
            [de.otto.goo.goo :as goo]
            [iapetos.collector.jvm :as jvm]
            [de.otto.tesla.serving-with-httpkit :as httpkit]
            [clojure.tools.logging :as log]
            [com.stuartsierra.component :as c]
            [com.github.discoverAI.snake.engine :as eg]
            [com.github.discoverAI.snake.endpoint :as ep])
  (:gen-class))

(defn snake-system [runtime-config]
  (-> (system/base-system runtime-config)
      (assoc
        :engine (c/using (eg/new-engine) [:app-status :scheduler])
        :endpoint (c/using (ep/new-endpoint) [:handler :engine]))
      (httpkit/add-server :endpoint)))

(defonce _ (jvm/initialize (goo/snapshot)))
(defonce _ (Thread/setDefaultUncaughtExceptionHandler
             (reify Thread$UncaughtExceptionHandler
               (uncaughtException [_ thread ex]
                 (log/error ex "Uncaught exception on " (.getName thread))))))

(defn -main [& _]
  (system/start (snake-system {})))
