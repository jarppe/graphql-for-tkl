(ns app.gql.handler
  (:require [clojure.tools.logging :as log]
            [ring.util.http-response :as resp]
            [com.walmartlabs.lacinia :as gql]
            [schema.core :as s]
            [reitit.core :as r]
            [reitit.ring :as ring]
            [reitit.coercion.schema]
            [reitit.ring.coercion :as rrc]
            [app.gql.schema :as schema]))

(s/defschema GraphQLRequest {:query s/Str
                             (s/optional-key :variables) (s/maybe s/Str)
                             (s/optional-key :operationName) (s/maybe s/Str)})

(s/defschema GraphQLResponse {(s/optional-key :data) s/Any
                              (s/optional-key :errors) [s/Any]})

(defn execute [req]
  (gql/execute schema/schema
               (-> req :gql :query)
               (-> req :gql :variables)
               (-> req :ctx)))

(defn gql-get [req]
  (assoc req :gql (-> req :parameters :query)))

(defn gql-post [req]
  (assoc req :gql (-> req :parameters :body)))

(def handler
  (ring/ring-handler
    (ring/router
      [["/graphql" {:name :graphql
                    :get {:handler (comp resp/ok execute gql-get)
                          :parameters {:query GraphQLRequest}
                          :responses {200 {:body GraphQLResponse}}}
                    :post {:handler (comp resp/ok execute gql-post)
                           :parameters {:body GraphQLRequest}
                           :responses {200 {:body GraphQLResponse}}}}]
       ["/info" {:name :info
                 :get {:handler (constantly (resp/ok "todo info"))}}]]
      {:data {:coercion reitit.coercion.schema/coercion
              :middleware [rrc/coerce-exceptions-middleware
                           rrc/coerce-request-middleware
                           rrc/coerce-response-middleware]}})))

