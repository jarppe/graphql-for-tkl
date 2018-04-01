(ns backend.gql.handler
  (:require [integrant.core :as ig]
            [ring.util.http-response :as resp]))

(defmethod ig/init-key ::handler [_ _]
  (fn [request]
    (println "graphql:")
    (./aprint request)
    (resp/ok {:graphql "todo..."})))


