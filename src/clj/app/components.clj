(ns app.components
  (:require [integrant.core :as ig]
            [syksy.web.index :as index]
            [app.gql.handler :as graphql]))

(defn components []
  {:syksy.web.server/server {:handlers [(ig/ref :syksy.web.api/handler)
                                        (ig/ref :syksy.web.index/handler)
                                        (ig/ref :syksy.web.resources/handler)
                                        (ig/ref :syksy.web.not-found/handler)]}
   :syksy.web.api/handler {:routes (ig/ref ::graphql/handler)}
   :syksy.web.index/handler {:index-body (index/index {:title "GraphQL API for TKL"})}
   :syksy.web.resources/handler {}
   :syksy.web.not-found/handler {}
   ::graphql/handler {}})
