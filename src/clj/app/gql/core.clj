(ns app.gql.core
  (:require [clojure.java.io :as io]
            [com.walmartlabs.lacinia :as gql]
            [com.walmartlabs.lacinia.schema :as gql.schema]
            [com.walmartlabs.lacinia.parser.schema :as gql.parse]
            [com.walmartlabs.lacinia.util :as gql.util]
            [app.journey.api :as journey]))

(defn wrap-in-map [schema-str]
  (str "{
  " schema-str "
  }"))

(def schema (-> "journey.graphql"
                (io/resource)
                (slurp)
                (wrap-in-map)
                (gql.parse/parse-schema {:resolvers {:Query {:line :line}}})
                (gql.util/attach-resolvers {:line journey/line})
                (gql.schema/compile)))

(comment

  (./aprint (gql/execute schema
                         "{
                            line(id: \"1A\") {
                              id
                              namez
                              description
                            }
                          }"
                         nil nil))

  (= (gql/execute schema
                  "{
                     characters(episode: NEWHOPE) {
                       id
                       name
                       episodes
                     }
                   }"
                  nil nil)
     {:data {:characters [{:id "100"
                           :name "FoFo"
                           :episodes [:NEWHOPE :EMPIRE]}
                          {:id "101"
                           :name "BaBa"
                           :episodes [:NEWHOPE :JEDI]}]}})

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