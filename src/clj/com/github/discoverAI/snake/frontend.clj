(ns com.github.discoverAI.snake.frontend
  (:require [hiccup.page :as page]
            [clojure.tools.logging :as log]
            [de.otto.tesla.stateful.handler :as handler]
            [com.stuartsierra.component :as c]
            [ring.middleware.params :as params]
            [ring.middleware.keyword-params :as kparams]
            [ring.middleware.resource :as resource]
            [de.otto.goo.goo :as metrics]
            [compojure.core :as cc]))

(defn frontend-page []
  (page/html5
    {:lang "en"}
    [:head
     [:meta {:name "viewport" :content "width=device-width, initial-scale=1, shrink-to-fit=no"}]
     (page/include-css "/css/snake.css")]
    [:body [:div#app]
     (page/include-js "/js/compiled/app.js")
     [:script "com.github.discoverAI.snake.core.init();"]]))

(defn response [_ _]
  {:status  200
   :headers {"content-type" "text/html"}
   :body    (frontend-page)})

(defn endpoint-filter [handler]
  (cc/routes
    (cc/GET "/" req (handler req))))

(defn with-resource [handler]
  (resource/wrap-resource handler "public"))

(defn create-routes [self]
  (->> (partial response self)
       (metrics/timing-middleware)
       (endpoint-filter)
       (with-resource)
       kparams/wrap-keyword-params
       params/wrap-params))

(defrecord Frontend [handler]
  c/Lifecycle
  (start [self]
    (log/info "-> starting Endpoint")
    (handler/register-handler handler (create-routes self))
    self)
  (stop [_]
    (log/info "<- stopping Endpoint")))

(defn new-frontend []
  (map->Frontend {}))