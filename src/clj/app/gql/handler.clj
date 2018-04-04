(ns app.gql.handler
  (:require [ring.util.http-response :as resp]
            [com.walmartlabs.lacinia :as gql]
            [schema.core :as s]
            [reitit.core :as r]
            [reitit.ring :as ring]
            [reitit.coercion.schema]
            [reitit.ring.coercion :as rrc]
            [app.gql.core :as c]))

(s/defschema GraphQLResponse {(s/optional-key :data) s/Any
                              (s/optional-key :errors) [s/Any]})

(defn gql-get-handler [req]
  (gql/execute c/schema
               (-> req :parameters :query :query)
               nil
               nil))

(defn gql-post-handler [req]
  (gql/execute c/schema
               (-> req :parameters :query :query)
               nil
               nil))

(def handler
  (ring/ring-handler
    (ring/router
      ["/graphql" {:name :graphql
                   :get {:parameters {:query {:query s/Str}}
                         :handler (comp resp/ok gql-get-handler)
                         :responses {200 {:body GraphQLResponse}}}
                   :post {:parameters {:body {:query s/Str}}
                          :handler (comp resp/ok gql-post-handler)
                          :responses {200 {:body GraphQLResponse}}}}]
      {:data {:coercion reitit.coercion.schema/coercion
              :middleware [rrc/coerce-exceptions-middleware
                           rrc/coerce-request-middleware
                           rrc/coerce-response-middleware]}})))

(handler {:request-method :post
          :uri "/graphql"
          :body-params {:query "foo"}})

(comment

  (require '[clj-http.client :as http])
  (http/get "http://localhost:3000/graphql" {:query-params {:query "{line(id: \"1A\") {name}}"}
                                             :accept :json
                                             :as :json})

  ; http :3000/graphql query=='{line(id: "1A") {name description}}'

  )