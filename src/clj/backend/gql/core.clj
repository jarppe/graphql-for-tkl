(ns backend.gql.core
  (:require [clojure.java.io :as io]
            [com.walmartlabs.lacinia :as gql]
            [com.walmartlabs.lacinia.schema :as gql.schema]
            [com.walmartlabs.lacinia.parser.schema :as gql.parse]
            [com.walmartlabs.lacinia.util :as gql.util]))

(defn character [ctx args value]
  (println "characters:" args value)
  {:id "100"
   :name "FoFo"
   :episodes [:NEWHOPE :EMPIRE]})

(defn characters [ctx args value]
  (println "characters:" args value)
  [{:id "100"
    :name "FoFo"
    :episodes [:NEWHOPE :EMPIRE]}
   {:id "101"
    :name "BaBa"
    :episodes [:NEWHOPE :JEDI]}])

(defn wrap-in-map [schema-str]
  (str "{
  " schema-str "
  }"))

(def schema (-> "journey.graphql"
                (io/resource)
                (slurp)
                (wrap-in-map)
                (gql.parse/parse-schema {:resolvers {:Query {:character :character
                                                             :characters :characters}}
                                         :documentation {:Character "A Star Wars character"
                                                         :Character/name "Character name"
                                                         :Query/characters "Find all characters in the given episode"
                                                         :Query.characters/episode "Episode for which to find characters."}})
                (gql.util/attach-resolvers {:character character
                                            :characters characters})
                (gql.schema/compile)))

(comment

  (= (gql/execute schema
                  "{
                   character(id: \"100\") {
                     id
                     name
                   }
                 }"
                  nil nil)
     {:data {:character {:id "100" :name "FoFo"}}})

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