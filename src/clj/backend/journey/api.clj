(ns backend.journey.api
  (:require [backend.journey.util :as u]))

;;
;; Journey API:
;;

(defn line [_ctx {:keys [id]} _value]
  (-> (u/GET "/lines/" id)
      (first)
      (u/name-as-id)))

(comment

  (line nil {:id "1A"} nil)

  )