(ns app.journey.api
  (:refer-clojure :exclude [find])
  (:require [app.journey.util :refer [GET]]))

;;
;; Access Journey API:
;;


; Map entity to URL part:

(def entity->uri {:line "lines"
                  :route "routes"
                  :journey-pattern "journey-patterns"
                  :journey "journeys"
                  :stop-point "stop-points"
                  :municipality "municipalities"
                  :vehicle-activity "vehicle-activity"})

;;
;; Query Journey API:
;;

(defn query
  ([entity params exclude-fields]
   (GET (entity->uri entity) params exclude-fields))
  ([uri] (query uri nil nil))
  ([uri params] (query uri params nil)))

;;
;; Find one entity by entity ID:
;;

(defn find
  ([entity entity-id exclude-fields]
   (let [url (str (entity->uri entity) "/" entity-id)]
     (-> (GET url nil exclude-fields)
         (first))))
  ([entity entity-id] (find entity entity-id nil)))

(comment

  (find :line "1A")
  ;=> {:name "1A"
  ;    :description "Vatiala - Pirkkala"
  ;    :url "http://178.217.134.14/journeys/api/1/lines/1A"}

  (query :line {:description "Vatiala - Pirkkala"})
  ;=> [{:description "Vatiala - Pirkkala", :name "1", :url "http://178.217.134.14/journeys/api/1/lines/1"}
  ;    {:description "Vatiala - Pirkkala", :name "1C", :url "http://178.217.134.14/journeys/api/1/lines/1C"}
  ;    {:description "Vatiala - Pirkkala", :name "1B", :url "http://178.217.134.14/journeys/api/1/lines/1B"}
  ;    {:description "Vatiala - Pirkkala", :name "1A", :url "http://178.217.134.14/journeys/api/1/lines/1A"}]

  (query :route
         {:description "Nokia - Tampere"}
         [:journeys :journeyPatterns :geographicCoordinateProjection])
  ;=> [{:name "Vatiala - Koskipuisto G", :lineUrl "http://178.217.134.14/journeys/api/1/lines/1", :url "http://178.217.134.14/journeys/api/1/routes/10599"}
  ;    {:name "Vatiala - Suupantori", :lineUrl "http://178.217.134.14/journeys/api/1/lines/1", :url "http://178.217.134.14/journeys/api/1/routes/10600"}
  ;    ...

  (->> (find :route "10869")
       keys)

  (find :route "10869" [:journeys :journeyPatterns :geographicCoordinateProjection])
  )
