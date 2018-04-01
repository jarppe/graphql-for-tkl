(ns backend.gql.handler
  (:require [integrant.core :as ig]
            [ring.middleware.params :as params]
            [ring.util.http-response :as resp]
            [muuntaja.middleware :as muuntaja]
            [com.walmartlabs.lacinia :as gql]
            [schema.core :as s]
            [reitit.core :as r]
            [reitit.ring :as ring]
            [reitit.coercion.schema]
            [reitit.ring.coercion :as rrc]
            [backend.gql.core :as c]))


(s/defschema GraphQLResponse {(s/optional-key :data) s/Any
                              (s/optional-key :errors) [s/Any]})

(defn handler [req]
  (gql/execute c/schema
               (-> req :parameters :query :query)
               nil
               nil))

(def app
  (ring/ring-handler
    (ring/router
      ["/graphql" {:get {:parameters {:query {:query s/Str}}
                         :responses {200 {:body GraphQLResponse}}
                         :handler (comp resp/ok handler)}}]
      {:data {:coercion reitit.coercion.schema/coercion
              :middleware [rrc/coerce-exceptions-middleware
                           rrc/coerce-request-middleware
                           rrc/coerce-response-middleware]}})))


(defmethod ig/init-key ::handler [_ _]
  (-> app
      (params/wrap-params)
      (muuntaja/wrap-format)))


(comment

  (require '[clj-http.client :as http])
  (http/get "http://localhost:3000/graphql" {:query-params {:query "{line(id: \"1A\") {name}}"}
                                             :accept :json})

  )