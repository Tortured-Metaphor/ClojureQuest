(ns maze-game.player)

(declare can-move?)

(defn create-player [x y]
  {:x (+ (* x 2.5) 1.25)  ;; Center in cell
   :y (+ (* y 2.5) 1.25)
   :angle 0.0})

(defn update-position [player maze keys-pressed]
  (let [speed (cond 
                (keys-pressed :w) 0.12
                (keys-pressed :s) -0.12
                :else 0)
        turn-speed (cond
                     (keys-pressed :a) -0.04
                     (keys-pressed :d) 0.04
                     :else 0)
        new-angle (+ (:angle player) turn-speed)
        ;; Calculate movement based on current angle
        dx (* speed (Math/cos (:angle player)))
        dy (* speed (Math/sin (:angle player)))
        ;; Try smaller steps for smoother collision
        steps 4
        step-dx (/ dx steps)
        step-dy (/ dy steps)
        cell-size 2.5
        margin 0.08]  ;; Very minimal buffer - can get very close to walls
    ;; Move in small steps for smoother collision
    (loop [i 0
           current-x (:x player)
           current-y (:y player)]
      (if (or (>= i steps) (= speed 0))
        {:x current-x :y current-y :angle new-angle}
        (let [next-x (+ current-x step-dx)
              next-y (+ current-y step-dy)]
          (cond
            ;; Try full step
            (can-move? maze next-x next-y margin cell-size)
            (recur (inc i) next-x next-y)
            
            ;; Try sliding along X
            (can-move? maze next-x current-y margin cell-size)
            (recur (inc i) next-x current-y)
            
            ;; Try sliding along Y
            (can-move? maze current-x next-y margin cell-size)
            (recur (inc i) current-x next-y)
            
            ;; Stop here
            :else
            {:x current-x :y current-y :angle new-angle}))))))

(defn can-move? [maze x y margin cell-size]
  (let [;; Check the cell we're in
        grid-x (int (/ x cell-size))
        grid-y (int (/ y cell-size))
        width (count (first maze))
        height (count maze)]
    ;; Bounds check
    (if (or (< grid-x 0) (>= grid-x width)
            (< grid-y 0) (>= grid-y height))
      false
      ;; Check walls in current cell only
      (let [cell (get-in maze [grid-y grid-x])
            local-x (- x (* grid-x cell-size))
            local-y (- y (* grid-y cell-size))]
        (and
         ;; Check current cell walls with margin
         (or (not (:north cell)) (> local-y margin))
         (or (not (:south cell)) (< local-y (- cell-size margin)))
         (or (not (:west cell)) (> local-x margin))
         (or (not (:east cell)) (< local-x (- cell-size margin))))))))

