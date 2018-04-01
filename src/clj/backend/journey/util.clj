(ns backend.journey.util
  (:require [clojure.string :as str]
            [jsonista.core :as json]
            [clj-http.client :as http]))

(def mapper (json/object-mapper {:decode-key-fn keyword}))

(defn ok! [{:keys [status] :as response}]
  (when-not (= status 200)
    (throw (ex-info (str "HTTP fail: " status) {})))
  response)

(defn success! [{:keys [status] :as response}]
  (when-not (= status "success")
    (throw (ex-info (str "API fail: " status) {})))
  response)

(defn GET [& uri]
  (-> (str/join "/" (cons "http://data.itsfactory.fi/journeys/api/1" uri))
      (http/get {:accept :json})
      (ok!)
      :body
      (json/read-value mapper)
      (success!)
      :body))

(defn name-as-id [{:keys [name] :as entity}]
  (assoc entity :id name))
