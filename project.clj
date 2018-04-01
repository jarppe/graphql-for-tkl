(defproject jarppe/graphql-for-tkl "0.0.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.9.0"]

                 ; Common stuff:
                 [org.clojure/core.async "0.4.474"]
                 [prismatic/schema "1.1.9"]
                 [potemkin "0.4.5"]

                 ; GraphQL:
                 [com.walmartlabs/lacinia "0.25.0"]

                 ; Integrant:
                 [integrant "0.6.3"]
                 [integrant/repl "0.3.1"]

                 ; Web:
                 [org.immutant/web "2.1.10"]
                 [ring/ring-core "1.6.3" :exclusions [commons-io]]
                 [ring/ring-defaults "0.3.1"]
                 [metosin/ring-http-response "0.9.0"]
                 [metosin/muuntaja "0.5.0"]
                 [metosin/jsonista "0.1.1"]

                 ; HTTP and HTML
                 [clj-http "3.8.0"]
                 [hiccup "2.0.0-alpha1"]
                 [enlive "1.1.6"]

                 ; ClojureScript:
                 [org.clojure/clojurescript "1.10.238"]
                 [binaryage/devtools "0.9.9"]
                 [metosin/reagent-dev-tools "0.2.0"]
                 [reagent "0.8.0-alpha2"]
                 [cljs-http "0.1.44"]

                 ; Logging:
                 [org.clojure/tools.logging "0.4.0"]
                 [org.slf4j/jcl-over-slf4j "1.7.25"]
                 [org.slf4j/jul-to-slf4j "1.7.25"]
                 [org.slf4j/log4j-over-slf4j "1.7.25"]
                 [ch.qos.logback/logback-classic "1.2.3" :exclusions [org.slf4j/slf4j-api]]]

  :source-paths ["src/dev" "src/clj" "src/cljs"]
  :test-paths ["test/clj"]
  :java-source-paths ["src/java"]
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

  :profiles {:dev {:resource-paths ["target/dev/resources"]
                   :sass {:target-path "target/dev/resources/public/css"}}
             :prod {:source-paths ^:replace ["src/clj" "src/cljs"]
                    :resource-paths ["target/prod/resources"]
                    :sass {:target-path "target/prod/resources/public/css"}}
             :uberjar {:main backend.main
                       :aot [backend.main]
                       :uberjar-name "app.jar"}}

  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src/cljs"]
                        :figwheel true
                        :compiler {:main "frontend.main"
                                   :asset-path "js/out"
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
                        :compiler {:main "frontend.main"
                                   :optimizations :advanced
                                   :output-to "target/prod/resources/public/js/main.js"
                                   :output-dir "target/prod/resources/public/js/out"
                                   :closure-defines {goog.DEBUG false}}}]}

  :aliases {"dev" ["do" "clean"
                   ["pdo" ["sass4clj" "auto"] ["figwheel"]]]
            "prod" ["with-profile" "prod" "do"
                    "clean"
                    ["sass4clj" "once"]
                    ["cljsbuild" "once" "prod"]
                    "uberjar"]})

