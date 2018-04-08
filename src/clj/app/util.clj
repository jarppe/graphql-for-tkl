(ns app.util)

(defn lat-lon-point?
  "Predicate that returns truthy is `s` is a string containing
  lat-lon coordinate represented as two floating point numbers
  separated with comma."
  [s]
  (and (string? s)
       (re-matches #"[+-]?(?:\d*\.)?\d+,[+-]?(?:\d*\.)?\d+" s)))

(defn lat-lon-area?
  "Predicate that returns truthy is `s` is a string containing
  two lat-lon coordinates separated with semi-colon"
  [s]
  (and (string? s)
       (re-matches #"[+-]?(?:\d*\.)?\d+,[+-]?(?:\d*\.)?\d+:[+-]?(?:\d*\.)?\d+,[+-]?(?:\d*\.)?\d+" s)))

(defn lat-lon?
  "Predicate that returns truthy if `s` is either lat-lon
  point or lat-lon area."
  [s]
  (or (lat-lon-point? s)
      (lat-lon-area? s)))
