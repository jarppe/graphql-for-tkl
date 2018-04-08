(defproject jarppe/graphql-for-tkl "0.0.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.9.0"]

                 ; Syksy:
                 [jarppe/syksy "0.0.3"]

                 ; Common stuff:
                 [org.clojure/core.async "0.4.474"]
                 [prismatic/schema "1.1.9"]
                 [potemkin "0.4.5"]
                 [metosin/potpuri "0.5.1"]

                 ; GraphQL:
                 [com.walmartlabs/lacinia "0.25.0"]

                 [metosin/reitit "0.1.1-SNAPSHOT"]
                 [metosin/reitit-ring "0.1.1-SNAPSHOT"]
                 [metosin/reitit-schema "0.1.1-SNAPSHOT"]

                 ; HTTP and HTML
                 [clj-http "3.8.0"]

                 ; nrepl:
                 [org.clojure/tools.nrepl "0.2.13"]

                 ; ClojureScript:
                 [org.clojure/clojurescript "1.10.238"]
                 [binaryage/devtools "0.9.10"]
                 [metosin/reagent-dev-tools "0.2.0"]
                 [reagent "0.8.0-alpha2"]
                 [cljs-http "0.1.45"]]

  :min-lein-version "2.8.1"
  :source-paths ["src/clj" "src/cljs"]
  :test-paths ["test/clj"]
  :target-path "target/dev"
  :auto-clean false

  :sass {:source-paths ["src/sass"]
         :source-map true
         :output-style :compressed}

  :figwheel {:css-dirs ["target/dev/resources/public/css"]
             :repl false}

  :plugins [[lein-pdo "0.1.1"]
            [deraen/lein-sass4clj "0.3.1"]
            [lein-figwheel "0.5.15"]
            [lein-cljsbuild "1.1.7"]]

  :profiles {:dev {:dependencies [[metosin/testit "0.2.1"]]
                   :resource-paths ["target/dev/resources"]
                   :sass {:target-path "target/dev/resources/public/css"}}
             :uberjar {:target-path "target/prod"
                       :source-paths ^:replace ["src/clj" "src/cljs"]
                       :resource-paths ["target/prod/resources"]
                       :sass {:target-path "target/prod/resources/public/css"}
                       :main syksy.main
                       :aot [syksy.main app.components]
                       :uberjar-name "app.jar"}}

  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src/cljs"]
                        :figwheel true
                        :compiler {:main "app.front.main"
                                   :asset-path "asset/js/out"
                                   :external-config {:devtools/config {:features-to-install :all}}
                                   :closure-defines {goog.DEBUG true}
                                   :preloads [devtools.preload]
                                   :output-to "target/dev/resources/public/js/main.js"
                                   :output-dir "target/dev/resources/public/js/out"
                                   :source-map true
                                   :optimizations :none
                                   :cache-analysis true
                                   :pretty-print true}}
                       {:id "prod"
                        :source-paths ["src/cljc" "src/cljs"]
                        :compiler {:main "app.front.main"
                                   :optimizations :advanced
                                   :output-to "target/prod/resources/public/js/main.js"
                                   :closure-defines {goog.DEBUG false}}}]}

  :aliases {"dev" ["do"
                   ["clean"]
                   ["pdo"
                    ["sass4clj" "auto"]
                    ["figwheel"]]]
            "uberjar" ["with-profile" "uberjar" "do"
                       ["clean"]
                       ["sass4clj" "once"]
                       ["cljsbuild" "once" "prod"]
                       ["uberjar"]]})
