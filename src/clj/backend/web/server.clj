(ns backend.web.server
  (:require [integrant.core :as ig]
            [immutant.web :as immutant]))

;;
;; Web server:
;;

(def defaults {:host "localhost"
               :port 3000
               :path "/"})

(defmethod ig/init-key ::server [_ {:keys [handler] :as opts}]
  (immutant/run handler (-> defaults
                            (merge opts)
                            (dissoc :handler))))

(defmethod ig/halt-key! ::server [_ server]
  (immutant/stop server))
