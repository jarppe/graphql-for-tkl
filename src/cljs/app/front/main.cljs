(ns app.front.main
  (:require [reagent.core :as r]
            [app.front.state :as state]
            [app.front.dev :as dev]))

;;
;; Main view:
;;

(defn main-view []
  [:h1 "Here we go again..."])

;;
;; Main
;;

(defn init! []
  (js/console.log "Initialising front...")
  (r/render [main-view] (js/document.getElementById "app"))
  (dev/init!))

(init!)
