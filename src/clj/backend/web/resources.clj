(ns backend.web.resources
  (:require [clojure.java.io :as io]
            [ring.util.http-response :as resp]))

;;
;; Resources:
;;

(defn handler [request]
  (when (-> request :request-method (= :get))
    (when-let [res (->> request :uri (str "public/") (io/resource))]
      (-> (io/input-stream res)
          (resp/ok)))))

