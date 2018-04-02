(ns app.front.dev
  (:require [reagent.core :as r]
            [reagent-dev-tools.state-tree :as dev-state]
            [reagent-dev-tools.core :as dev-tools]
            [app.front.state :as state]))

(defn init! []
  (when js/goog.DEBUG
    (js/console.log "Initialising dev tools...")
    (dev-state/register-state-atom "App state" state/state)
    (r/render [dev-tools/dev-tool {}] (js/document.getElementById "dev"))))
