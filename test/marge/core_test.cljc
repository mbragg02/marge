(ns marge.core-test
  (:require #?(:cljs [cljs.test :as t]
               :clj  [clojure.test :as t])
             [marge.core :refer [markdown]]))

(t/deftest paragraph
  (t/testing "p produces expected string"
    (t/is (= "Paragraph\n"
             (markdown [:p "Paragraph"])))))

(t/deftest rulers
  (t/testing "hr produces expected string"
    (t/is (= "---"
             (markdown [:hr])))))

(t/deftest headers
  (t/testing "h1 headers produces expected string"
    (t/is (= "# Refactoring\n"
             (markdown [:h1 "Refactoring"]))))

  (t/testing "h2 headers produces expected string"
    (t/is (= "## Testing\n"
             (markdown [:h2 "Testing"]))))

  (t/testing "h3 headers produces expected string"
    (t/is (= "### Documenting\n"
             (markdown [:h3 "Documenting"]))))
  
  (t/testing "h4 headers produces expected string"
    (t/is (= "#### Supporting\n"
             (markdown [:h4 "Supporting"]))))
  
  (t/testing "h5 headers produces expected string"
    (t/is (= "##### Bug Fixing\n"
             (markdown [:h5 "Bug Fixing"]))))
  
  (t/testing "h6 headers produces expected string"
    (t/is (= "###### Features\n"
             (markdown [:h6 "Features"])))))

(t/deftest quotes
  (t/testing "blockquote produces expected string"
    (t/is (= "> Blockquotes are very handy"
             (markdown [:blockquote "Blockquotes are very handy"])))))

(t/deftest lists
  (t/testing "ordered list produces expected string"
    (t/is (= "1. First item\n2. Second item\n"
             (markdown [:ol ["First item" "Second item"]]))))
    
  (t/testing "nested unordered list in ordered list produces expected string"
    (t/is (= "1. First item\n  + First Sub Item\n  + Second Sub Item\n2. Second Item\n"
             (markdown [:ol ["First item" 
                             [:ul ["First Sub Item" "Second Sub Item"]] 
                             "Second Item"]]))))

  (t/testing "nested ordered list in ordered list produces expected string"
    (t/is (= "1. First item\n  1. First Sub Item\n  2. Second Sub Item\n2. Second Item\n"
             (markdown [:ol ["First item" 
                             [:ol ["First Sub Item" "Second Sub Item"]] 
                             "Second Item"]]))))

  (t/testing "nested ordered list in unordered list produces expected string"
    (t/is (= "+ First item\n  1. First Sub Item\n  2. Second Sub Item\n+ Second Item\n"
             (markdown [:ul ["First item" 
                             [:ol ["First Sub Item" "Second Sub Item"]] 
                             "Second Item"]]))))

  (t/testing "nested unordered list in unordered list produces expected string"
    (t/is (= "+ First item\n  + First Sub Item\n  + Second Sub Item\n+ Second Item\n"
             (markdown [:ul ["First item" 
                             [:ul ["First Sub Item" "Second Sub Item"]] 
                             "Second Item"]]))))
  
  (t/testing "unordered list produces expected string"
    (t/is (= "+ First item\n+ Second item\n"
             (markdown [:ul ["First item" "Second item"]])))))

(t/deftest links
  (t/testing "inline link returns expected string"
    (t/is (= "[I'm an inline-style link](https://www.google.com)"
             (markdown [:link {:text "I'm an inline-style link" :url "https://www.google.com"}]))))
  
  (t/testing "inline link with title returns expected string"
    (t/is (= "[I'm an inline-style link](https://www.google.com \"Google Homepage\")"
             (markdown [:link 
                        {:text "I'm an inline-style link" 
                         :url "https://www.google.com" 
                         :title "Google Homepage"}])))))

(t/deftest code
  (t/testing "block of code returns expected string"
    (t/is (= "```\n<b>Some code</b>\n```"
             (markdown [:code
                        "<b>Some code</b>"]))))
  
  (t/testing "block of code specifying syntax returns expected string"
    (t/is (= "```clojure\n(def data [1 2 3])\n```"
             (markdown [:code
                        {:clojure "(def data [1 2 3])"}])))))

(t/deftest table
  (t/testing "table returns expected string"
    (t/is (= (str "| Tables | Are | Cool |\n"
                  "| ------ | --- | ---- |\n"
                  "| 0 1    | 0 2 | 0 3  |\n"
                  "| 1 1    | 1 2 | 1 3  |\n")
             (markdown [:table
                        ["Tables" 
                         ["0 1" "1 1"] 
                         "Are" 
                         ["0 2" "1 2"]
                         "Cool"
                         ["0 3" "1 3"]]]))))

  (t/testing "table with one column returns expected string"
    (t/is (= (str "| Title |\n"
                  "| ----- |\n"
                  "| link  |\n")
             (markdown [:table
                        ["Title" 
                         ["link"]]]))))

  (t/testing "table with nested structures returns expected string"
    (t/is (= (str "| Title | Links       |\n"
                  "| ----- | ----------- |\n"
                  "| link  | [text](url) |\n")
             (markdown [:table
                        ["Title" 
                         ["link"]
                         "Links" 
                         [:link {:url "url" :text "text"}]]]))))
  
  (t/testing "table with varying data returns expected string"
    (t/is (= (str "| Product | Quantity | Price    |\n"
                  "| ------- | -------- | -------- |\n"
                  "| Coke    | 100      | $70      |\n"
                  "| Fanta   | 10000000 | $7000000 |\n"
                  "| Lilt    | 1        | $2       |\n")
             (markdown [:table
                        ["Product" 
                         ["Coke" "Fanta" "Lilt"] 
                         "Quantity" 
                         ["100" "10000000" "1"]
                         "Price"
                         ["$70" "$7000000" "$2"]]]))))

  (t/testing "table with non string data returns expected string"
    (t/is (= (str "| Product | Quantity | Price    |\n"
                  "| ------- | -------- | -------- |\n"
                  "| Coke    | 100      | $70      |\n"
                  "| Fanta   | 10000000 | $7000000 |\n"
                  "| Lilt    | 1        | $2       |\n")
             (markdown [:table
                        ["Product" 
                         ["Coke" "Fanta" "Lilt"] 
                         "Quantity" 
                         [100 10000000 1]
                         "Price"
                         ["$70" "$7000000" "$2"]]]))))
  
  (t/testing "table with no rows returns expected result"
    (t/is (= (str "| Product | Quantity | Price |\n"
                  "| ------- | -------- | ----- |\n")
             (markdown [:table
                        ["Product" 
                         [] 
                         "Quantity" 
                         []
                         "Price"
                         []]])))))

(t/deftest composing-nodes
  (t/testing "composing multiple nodes with line breaks"
    (t/is (= "# Header\n\n\n---1. First item\n2. Second item\n\n---## Header 2\n"
             (markdown [:h1 "Header"
                        :br :br
                        :hr
                        :ol ["First item" "Second item"]
                        :br
                        :hr
                        :h2 "Header 2"])))))

#?(:cljs
    (do
      (enable-console-print!)
      (set! *main-cli-fn* #(t/run-tests))))

#?(:cljs
    (defmethod t/report [:cljs.test/default :end-run-tests]
      [m]
      (if (t/successful? m)
        (set! (.-exitCode js/process) 0)
        (set! (.-exitCode js/process) 1))))
