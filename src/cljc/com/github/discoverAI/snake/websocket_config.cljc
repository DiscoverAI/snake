(ns com.github.discoverAI.snake.websocket-config)

(def INIT_ROUTE "/chsk")

(def CLIENT_CONFIG
  {:type :auto
   :host (str "localhost:" (or (System/getenv "PORT") "8080"))})
