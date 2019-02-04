(ns com.github.discoverAI.snake.frontend
  (:require [hiccup.page :as page]
            [clojure.tools.logging :as log]
            [de.otto.tesla.stateful.handler :as handler]
            [com.stuartsierra.component :as c]
            [ring.middleware.params :as params]
            [ring.middleware.keyword-params :as kparams]
            [ring.middleware.resource :as resource]
            [de.otto.goo.goo :as metrics]
            [compojure.api.sweet :refer :all]))

(defn pass-game-id-if-present [game-id]
  (if (not (nil? game-id)) (str \" game-id \")))

(defn frontend-page [game-id]
  (page/html5
    {:lang "en"}
    [:head
     [:meta {:name "viewport" :content "width=device-width, initial-scale=1, shrink-to-fit=no"}]
     (page/include-css "/css/snake.css")]
    [:body [:div#app]
     (page/include-js "/js/compiled/app.js")
     [:script (str "com.github.discoverAI.snake.core.init("
                   (pass-game-id-if-present game-id)
                   ");")]]))

(defn response [game-id]
  {:status  200
   :headers {"content-type" "text/html"}
   :body    (frontend-page game-id)})

(defn endpoint-filter [_handler]
  (api
    (GET "/" []
      :query-params [{spectate-game-id :- String nil}]
      (response spectate-game-id))))

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