(ns snake.core
  (:require [de.otto.tesla.system :as system]
            [de.otto.goo.goo :as goo]
            [iapetos.collector.jvm :as jvm]
            [de.otto.tesla.serving-with-httpkit :as httpkit]
            [clojure.tools.logging :as log]))

(defn peppermint-butler-system [runtime-config]
  (-> (system/base-system (merge {:name "snake-backend"} runtime-config))
      #_(assoc
          :endpoint (c/using (new-endpoint) [:dependencies])
                    )
      (httpkit/add-server                                   ;:endpoint
        )))

(defonce _ (jvm/initialize (goo/snapshot)))
(defonce _ (Thread/setDefaultUncaughtExceptionHandler
             (reify Thread$UncaughtExceptionHandler
               (uncaughtException [_ thread ex]
                 (log/error ex "Uncaught exception on " (.getName thread))))))

(defn -main [& _]
  (system/start (peppermint-butler-system {})))
