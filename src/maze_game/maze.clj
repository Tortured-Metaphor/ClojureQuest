(ns maze-game.maze)

(defn create-grid [width height]
  (vec (for [y (range height)]
         (vec (for [x (range width)]
                {:north true :south true :east true :west true :visited false})))))

(defn get-neighbors [maze x y]
  (let [width (count (first maze))
        height (count maze)
        neighbors []]
    (cond-> neighbors
      (> y 0) (conj {:x x :y (dec y) :dir :north})
      (< y (dec height)) (conj {:x x :y (inc y) :dir :south})
      (> x 0) (conj {:x (dec x) :y y :dir :west})
      (< x (dec width)) (conj {:x (inc x) :y y :dir :east}))))

(defn unvisited-neighbors [maze x y]
  (filter #(not (get-in maze [(:y %) (:x %) :visited]))
          (get-neighbors maze x y)))

(defn opposite-dir [dir]
  (case dir
    :north :south
    :south :north
    :east :west
    :west :east))

(defn remove-wall [maze x1 y1 x2 y2 dir]
  (-> maze
      (assoc-in [y1 x1 dir] false)
      (assoc-in [y2 x2 (opposite-dir dir)] false)))

(defn generate-maze-dfs [maze x y]
  (let [maze (assoc-in maze [y x :visited] true)
        neighbors (unvisited-neighbors maze x y)]
    (if (empty? neighbors)
      maze
      (let [shuffled (shuffle neighbors)]
        (reduce (fn [m neighbor]
                  (if (not (get-in m [(:y neighbor) (:x neighbor) :visited]))
                    (-> m
                        (remove-wall x y (:x neighbor) (:y neighbor) (:dir neighbor))
                        (generate-maze-dfs (:x neighbor) (:y neighbor)))
                    m))
                maze
                shuffled)))))

(defn generate-maze [width height]
  (let [grid (create-grid width height)
        ;; Always start from top-left corner
        start-x 0
        start-y 0
        ;; Generate the maze
        maze (generate-maze-dfs grid start-x start-y)]
    ;; Open entrance and exit
    (-> maze
        ;; Open entrance at top-left
        (assoc-in [0 0 :west] false)
        ;; Open exit at bottom-right
        (assoc-in [(dec height) (dec width) :east] false))))