(ns defworkshop.url-parser
  (:require [clojure.string :as string]
            [workshoplib.tdd :refer […]]))

;; Ok, enough basics for us! Let's move to something more interesting now.
;; Let's implement a URL parser that will call certain functions depending on what's coming into it.
;;
;; Of course, it won't be a full-blown url parser, since there're way too many options to cover in such
;; a short time, but be able to match url, extract params from them and run custom handlers based on which
;; url was called.
;;
;; We'll have an interface definition for you. You can check tests to see which arguments your functions
;; will be called with.

(defn get-parts
  "Gets parts of the URL, for example, for `\"/a/b/c\"`  it would return

     [\"a\", \"b\", \"c\"].

   You can use `clojure.string/split` for splitting the sequence.
   To create a regular expression from string, you can use `#\"\"` shortcut."
  [s]
  (string/split s #"/"))

(defn named-argument?
  "Checks wether the given route part is a named argument.

   Basically, checks wether the string starts with `:`."
  [part]
  (re-find #"^:" part))

(defn extract-named-argument
  "Converts url part (string) into key for the argument hash (keyword), stripping leading `:`.

     * You can use `subs` to get a part of string.
     * You can use `keyword` to create a keyword."
  [part]
  (keyword (subs part 1)))

(comment
  (extract-named-argument ":bar"))

(defn uri-matches?
  "Checks wether URI matches given pattern.

   Given you have a url `'/people/123'` and pattern `'/people/:id'`, their parts would look like:

      [\"people\", \"123\"]

   and

      [\"people\", \":id\"]

   Correspondingly.

   When the problem looks like one you would use `reduce` for, but you have more than 1 sequence,
   you can use 3 approaches:

   * stack-generating recursion
   * loop/recur
   * partition/interleave."
  [uri pattern]
  (let [uri-parts   (get-parts uri)
        pattern-parts (get-parts pattern)]
    (and (= (count uri-parts)
            (count pattern-parts))
         (comment (loop [[head & tail] (partition 2 (interleave uri-parts pattern-parts))]
           (let [[uri pattern] head]
             (if
               (or
                 (= uri pattern)
                 (re-find #"^:" pattern))
               (if (empty? tail)
                 true
                 (recur tail))
               false))))
         (loop [[u-head & u-tail] uri-parts
                [p-head & p-tail] pattern-parts]
           (if (or (= u-head p-head) (re-find #"^:" p-head))
             (if (empty? p-tail)
               true
               (recur u-tail p-tail))
             false)))))

(defn ^:not-implemented extract-arguments
  "Extracts named arguments from the string, based on a pattern."
  [uri pattern]
  (let [uri-parts   (get-parts uri)
        pattern-parts (get-parts pattern)]
    (and (= (count uri-parts)
            (count pattern-parts))
         (…))))

(defn ^:not-implemented dispatch
  "Dispatches the route based on route map"
  [uri route-map]
  (…))
