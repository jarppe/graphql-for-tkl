(ns backend.web.routes
  (:require [integrant.core :as ig]
            [ring.util.http-response :as resp]
            [muuntaja.middleware :as muuntaja]
            [backend.web.cache :as cache]
            [backend.web.index :as index]
            [backend.web.resources :as resources]))

;;
;; API handler:
;;

(defn make-graphql-handler [handler]
  (-> (fn [request]
        (when (-> request :uri (= "/graphql"))
          (handler request)))
      (muuntaja/wrap-format)
      (cache/wrap-smart-cache)))

;;
;; HTTP request handler:
;;

(defmethod ig/init-key ::handler [_ {:keys [graphql-handler]}]
  (some-fn (make-graphql-handler graphql-handler)
           index/handler
           resources/handler
           (constantly (resp/not-found "Say wha?"))))
