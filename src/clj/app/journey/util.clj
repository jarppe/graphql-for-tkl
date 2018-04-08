(ns app.journey.util
  (:require [clojure.string :as str]
            [clj-http.client :as http]
            [jsonista.core :as json]))

(def ^:private mapper (json/object-mapper {:decode-key-fn keyword}))

(defn- assert-ok! [{:keys [status] :as response}]
  (when-not (= status 200)
    (throw (ex-info (str "HTTP fail: " status) {:response response})))
  response)

(defn- assert-success! [{:keys [status] :as response}]
  (when-not (= status "success")
    (throw (ex-info (str "API fail: " status) {:response response})))
  response)

(def ^:private +journey-url+ "http://data.itsfactory.fi/journeys/api/1/")
(def ^:private +default-opts+ {:accept :json
                               :throw-exceptions false})

(defn GET [url params exclude-fields]
  (let [query-params (assoc params :exclude-fields (->> exclude-fields
                                                        (map name)
                                                        (str/join ",")))
        opts (assoc +default-opts+ :query-params query-params)]
    (-> (http/get (str +journey-url+ url) opts)
        (assert-ok!)
        :body
        (json/read-value mapper)
        (assert-success!)
        :body)))

(defn url->id [url]
  (second (re-matches #".*\/(\d+)" url)))
