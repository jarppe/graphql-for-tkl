(ns backend.journey.core
  (:require [jsonista.core :as json]
            [clj-http.client :as http]))

(def mapper (json/object-mapper {:decode-key-fn keyword}))

(comment

  (-> (http/get "http://data.itsfactory.fi/journeys/api/1/lines" {:accept :json})
      :body
      (json/read-value mapper)
      :body
      (->> (take 5)))

  )