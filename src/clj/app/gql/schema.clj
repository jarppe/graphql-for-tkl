(ns app.gql.schema
  (:require [clojure.java.io :as io]
            [com.walmartlabs.lacinia :as gql]
            [com.walmartlabs.lacinia.schema :as gql.schema]
            [com.walmartlabs.lacinia.parser.schema :as gql.parse]
            [com.walmartlabs.lacinia.util :as gql.util]
            [app.gql.resolvers :as resolvers]))

(defn wrap-in-map [schema-str]
  (str "{ " schema-str " }"))

(def schema (-> "journey.graphql"
                (io/resource)
                (slurp)
                (wrap-in-map)
                (gql.parse/parse-schema {:resolvers {:Query resolvers/query->resolver}})
                (gql.util/attach-resolvers resolvers/resolvers)
                (gql.schema/compile)))

(comment

  (gql/execute schema
               "{
                  __schema {
                    types {
                      name
                      kind
                      description
                      fields {
                        name
                      }
                    }
                  }
               }"
               nil nil)

  )