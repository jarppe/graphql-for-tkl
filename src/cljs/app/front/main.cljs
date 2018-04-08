(ns app.front.main
  (:require [reagent.core :as r]
            [app.front.state :as state]
            [app.front.dev :as dev]))

;;
;; Main view:
;;

(defn link-row [name url]
  [:tr
   [:td name]
   [:td [:a {:href url :target "_blank"} url]]])

(defn main-view []
  [:div
   [:h1 "GraphQL API for TKL Journey API"]
   [:div.info
    [:div "This is a GraphQL test project. I wanted to learn GraphQL and
           decided to use the Journey API as a data source. Journey API
           provides a RESTful API for public transport information in
           Tampere area."]
    [:div "This project is still under development and currently the
           API supports only a very limited subset of Journey API
           functionality."]
    [:div "You can test the GraphQL API with the interactive "
     "Graph" [:i "i"] "QL client "
     [:a {:href "/graphiql"} "here"]
     "."]]
   [:div.links
    "Here's some related links:"
    [:table
     [:tbody
      [link-row "GraphQL" "https://graphql.org/"]
      [link-row "GraphiQL" "https://github.com/graphql/graphiql/"]
      [link-row "Clojure" "https://clojure.org/"]
      [link-row "Journey API" "http://wiki.itsfactory.fi/index.php/Journeys_API"]
      [link-row "TKL" "http://joukkoliikenne.tampere.fi/"]
      [link-row "This project" "https://github.com/jarppe/graphql-for-tkl/"]]]]
   [:div.info
    [:div "This service is running on Heroku free instance. Sometimes
           the service is sleeping, please allow some time for wakeup."]]
   [:div.copy "Copyright © 2018 " [:a {:href "https://github.com/jarppe/"} "Jarppe Länsiö"]]])

;;
;; Main
;;

(defn init! []
  (js/console.log "Initialising front...")
  (r/render [main-view] (js/document.getElementById "app"))
  (dev/init!))

(init!)
