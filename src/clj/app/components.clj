(ns app.components
  (:require [integrant.core :as ig]
            [potpuri.core :as p]
            [syksy.core :as syksy]
            [syksy.web.index :as index]
            [syksy.web.resource :as resource]
            [syksy.web.resources :as resources]
            [app.gql.handler :as graphql]
            [app.nrepl :as nrepl]))

(defn components []
  (p/deep-merge
    (syksy/default-components {:host "0.0.0.0"
                               :port (or (some-> (System/getenv "PORT")
                                                 (Integer/parseInt))
                                         3000)
                               :index-body (index/index {:title "GraphQL API for TKL Journey API"})
                               :routes graphql/handler
                               :addon-handlers [(ig/ref [::resource/handler ::app])
                                                (ig/ref [::resources/handler ::app])]})
    {[::resource/handler ::app] {:match? {"/graphiql" "graphiql/index.html"
                                          "/graphiql/" "graphiql/index.html"
                                          "/favicon.ico" "public/favicon.ico"}}
     [::resources/handler ::app] {:asset-prefix "/graphiql/"
                                  :asset-dir "graphiql/"}
     ::nrepl/nrepl {:port (some-> (System/getenv "NREPL") (Integer/parseInt))}}))
