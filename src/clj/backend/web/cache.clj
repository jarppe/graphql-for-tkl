(ns backend.web.cache
  (:require [clojure.string :as str]))

(def cache-header "cache-control")
(def cache-control-30d "public,max-age=2592000,s-maxage=2592000")
(def cache-control-no-cache "max-age=0,no-cache,no-store")
(def cache-control-revalidate "max-age=0,must-revalidate")

(def vary-header "Vary")
(def vary-value "Accept-Encoding")

(defn wrap-cache [handler cache-control-value]
  (fn [req]
    (let [response (handler req)]
      (if (and (map? response)
               (nil? (get-in response [:headers cache-header])))
        (update response :headers assoc cache-header cache-control-value
                                        vary-header vary-value)
        response))))

(def wrap-30d-cache (fn [handler] (wrap-cache handler cache-control-30d)))
(def wrap-no-cache (fn [handler] (wrap-cache handler cache-control-no-cache)))
(def wrap-allow-cache (fn [handler] (wrap-cache handler cache-control-revalidate)))

(defn wrap-smart-cache [handler]
  (fn [req]
    (let [response (handler req)]
      (if (and (map? response)
               (nil? (get-in response [:headers cache-header])))
        (update response :headers assoc cache-header (if (some-> req :query-string (str/starts-with? "v="))
                                                       cache-control-30d
                                                       cache-control-no-cache)
                                        vary-header vary-value)
        response))))
