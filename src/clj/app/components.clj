(ns app.components
  (:require [integrant.core :as ig]
            [syksy.core :as syksy]
            [syksy.web.resources :as resources]
            [syksy.web.index :as index]
            [syksy.web.redirect :as redirect]
            [syksy.web.server :as server]
            [potpuri.core :as p]
            [app.gql.handler :as graphql]))

(defn components []
  (p/deep-merge
    (syksy/default-components {:index-body (index/index {:title "GraphQL API for TKL Journey API"})
                               :routes graphql/handler
                               :addon-handlers [(ig/ref [::resources/handler ::app])
                                                (ig/ref [::redirect/handler ::app])]})
    ; Serve "/graphiql/" resources under /resources/graphiql and
    ; redirect "/graphiql" and "/graphiql/" -> "/graphiql/index.html"
    {[::server/server ::syksy/syksy] {:port (or (System/getenv "PORT") 3000)}
     [::resources/handler ::app] {:asset-prefix "/graphiql/"
                                  :asset-dir "graphiql/"}
     [::redirect/handler ::app] {:from (comp #{"/graphiql" "/graphiql/"} :uri)
                                 :location "/graphiql/index.html"}}))
