(ns app.gql.resolvers
  (:require [app.journey.api :as journey]))

(def query->resolver {:line :line})

(def resolvers {:line journey/line})
