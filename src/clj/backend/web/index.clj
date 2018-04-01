(ns backend.web.index
  (:require [ring.util.http-response :as resp]
            [hiccup.core :as hiccup]
            [hiccup.page :as page]))

;;
;; Index page
;;

(def index-response
  (-> (hiccup/html
        (page/html5
          [:head
           [:title "GraphQL API for TKL"]
           [:meta {:charset "utf-8"}]
           [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge"}]
           [:meta {:name "viewport" :content "width=device-width, initial-scale=1, shrink-to-fit=no"}]
           (page/include-css "/css/style.css")]
          [:body
           [:div#app
            [:div.loading
             [:h1 "Loading..."]]]
           [:div#dev]
           (page/include-js "/js/main.js")]))
      (resp/ok)
      (resp/content-type "text/html; charset=utf-8")))

(defn handler [request]
  (when (and (-> request :request-method (= :get))
             (-> request :uri (= "/")))
    index-response))
