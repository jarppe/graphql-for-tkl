(ns app.nrepl
  (:require [integrant.core :as ig]
            [clojure.tools.logging :as log]
            [clojure.tools.nrepl.server :as nrepl]))

(defmethod ig/init-key ::nrepl [_ {:keys [port]}]
  (when port
    (log/info "starting nrepl server on port" port "...")
    (nrepl/start-server :port port :bind "localhost")))

(defmethod ig/halt-key! ::nrepl [_ server]
  (when server
    (log/info "stopping nrepl serber...")
    (nrepl/stop-server server)))
