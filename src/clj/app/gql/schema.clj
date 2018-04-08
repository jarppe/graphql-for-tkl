(ns app.gql.schema
  (:require [com.walmartlabs.lacinia :as gql]
            [com.walmartlabs.lacinia.schema :as gql.schema]
            [com.walmartlabs.lacinia.util :as gql.util]
            [app.gql.resolvers :as resolvers]
            [app.util :as util])
  (:import (java.net URL)))

(defn re->conformer [re]
  (gql.schema/as-conformer
    (fn [v]
      (or (re-matches re v)
          (throw (ex-info (str "value does not match " re ": " v) {:re re, :v v})))
      v)))

(def noop-serializer (gql.schema/as-conformer str))

(def schema-data
  {:scalars {:Date {:parse (re->conformer #"\d{4}-\d{2}-\d{2}")
                    :serialize noop-serializer}
             :Time {:parse (re->conformer #"\d{2}:\d{2}:\d{2}")
                    :serialize noop-serializer}
             :URL {:parse (gql.schema/as-conformer (fn [v] (URL. v) v))
                   :serialize noop-serializer}
             :LatLon {:parse (gql.schema/as-conformer util/lat-lon?)
                      :serialize noop-serializer}}

   :enums {:WeekDay {:values [:monday
                              :tuesday
                              :wednesday
                              :thursday
                              :friday
                              :saturday
                              :sunday]}
           :TariffZone {:values [:A :B :C :D :E :F :G]}
           :Direction {:values [:toDest :toOrig]}}

   :objects '{:DayTypeException {:fields {:runs {:type Boolean}
                                          :from {:type Date}
                                          :to {:type Date}}}

              :GTFS {:fields {:tripId {:type String}}}

              :Municipality {:fields {:id {:type ID}
                                      :name {:type String}
                                      :shortName {:type String}}}

              :Stop {:fields {:id {:type ID}
                              :name {:type String}
                              :shortName {:type String}
                              :tariffZone {:type TariffZone}
                              :municipality {:type Municipality}
                              :location {:type LatLon}}}

              :Call {:fields {:departureLocalTime {:type Time}
                              :arrivalLocalTime {:type Time}
                              :stopPoint {:type Stop}}}

              :JourneyPattern {:fields {:id {:type ID}
                                        :name {:type String}
                                        :line {:type Line}
                                        :route {:type Route}
                                        :origin {:type Stop}
                                        :destination {:type Stop}
                                        :direction {:type Direction}
                                        :journeys {:type (list Journey)}
                                        :stops {:type (list Stop)}}}

              :Journey {:fields {:id {:type ID}
                                 :route {:type Route}
                                 :line {:type Line}
                                 ; TODO: :activity {:type Activity}
                                 :journeyPattern {:type JourneyPattern}
                                 :departureLocalTime {:type Time}
                                 :arrivalLocalTime {:type Time}
                                 :headSign {:type String}
                                 :dayTypes {:type (list WeekDay)}
                                 :dayTypeExceptions {:type (list DayTypeException)}
                                 :wheelchairAccessible {:type Boolean}
                                 :direction {:type Direction}
                                 :calls {:type (list Call)}
                                 :gtfs {:type GTFS}}}

              :Line {:fields {:id {:type ID}
                              :name {:type String}
                              :description {:type String}}}

              :Route {:fields {:id {:type ID}
                               :name {:type String}
                               :line {:type Line
                                      :resolve :Route/line}
                               :journeyPatterns {:type (list JourneyPattern)}
                               :journeys {:type (list Journey)}
                               :geographicCoordinateProjection {:type String}}}}

   :queries '{:line {:type :Line
                     :args {:id {:type ID}}
                     :resolve :Line/find-by-id}
              :lines {:type (list :Line)
                      :args {:description {:type String}}
                      :resolve :Line/query}
              :route {:type :Route
                      :args {:id {:type ID}}
                      :resolve :Route/find-by-id}
              :routes {:type (list :Route)
                       :args {:lineId {:type ID}
                              :name {:type String}}
                       :resolve :Route/query}}})

(def schema (-> schema-data
                (gql.util/attach-resolvers resolvers/resolvers)
                (gql.schema/compile)))

(comment

  (gql/execute schema
               "query { route(id: \"10869\") { name line { id name description } } }"
               nil
               nil)

  )