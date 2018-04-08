(ns app.gql.resolvers
  (:require [app.journey.api :as journey]
            [app.journey.util :as u]))

(defn line-find-by-id [_ {:keys [id]} _]
  (some-> (journey/find :line id)
          (assoc :id id)))

(defn line-query [_ args _]
  (journey/query :line args))

(defn route-find-by-id [_ {:keys [id]} _]
  (journey/find :route id))

(defn route-query [_ args _]
  (journey/query :route args))

(defn route-line [ctx args {:keys [lineUrl]}]
  (line-find-by-id ctx {:id (u/url->id lineUrl)} nil))

(def resolvers {:Line/find-by-id line-find-by-id
                :Line/query line-query
                :Route/find-by-id route-find-by-id
                :Route/query route-query
                :Route/line route-line})
