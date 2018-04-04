(ns app.components
  (:require [syksy.core :as syksy]
            [syksy.web.index :as index]
            [app.gql.handler :as graphql]))

(defn components []
  (syksy/default-components {:index-body (index/index {:title "GraphQL API for TKL"})
                             :routes graphql/handler}))
