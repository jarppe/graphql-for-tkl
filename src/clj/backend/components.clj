(ns backend.components
  (:require [integrant.core :as ig]
            [backend.web.server :as server]
            [backend.web.routes :as routes]
            [backend.gql.handler :as graphql]))

(def components
  {::server/server {:handler (ig/ref ::routes/handler)}
   ::routes/handler {:graphql-handler (ig/ref ::graphql/handler)}
   ::graphql/handler {}})
