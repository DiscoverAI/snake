(ns com.github.discoverAI.snake.websocket-config)

(def INIT_ROUTE "/chsk")

(def BACKEND_PORT (or (System/getenv "BACKEND_PORT") "5001"))

(def CLIENT_CONFIG
  {:type :auto
   :host (str "localhost:" BACKEND_PORT) })
