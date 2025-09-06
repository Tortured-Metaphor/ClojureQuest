(ns maze-game.core
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [maze-game.maze :as maze]
            [maze-game.player :as player]))

(def maze-width 10)
(def maze-height 10)
(def cell-size 2.5)
(def wall-height 2.2)
(def wall-thickness 0.15)

(defn setup []
  (q/frame-rate 60)
  (q/background 135 206 235)
  {:maze (maze/generate-maze maze-width maze-height)
   :player (player/create-player 0 0)  ;; Start at entrance
   :keys-pressed #{}})

(defn update-state [state]
  (-> state
      (update :player player/update-position (:maze state) (:keys-pressed state))))

(defn draw-wall [x1 y1 x2 y2]
  (q/push-matrix)
  (q/fill 180 180 180)
  (let [mid-x (/ (+ x1 x2) 2)
        mid-y (/ (+ y1 y2) 2)
        wall-length (Math/sqrt (+ (Math/pow (- x2 x1) 2)
                                  (Math/pow (- y2 y1) 2)))]
    (q/translate mid-x mid-y (/ wall-height 2))
    (if (= x1 x2)
      (q/rotate-y (/ Math/PI 2)))
    ;; Wall thickness
    (q/box wall-length wall-thickness wall-height))
  (q/pop-matrix))

(defn draw-floor []
  (q/push-matrix)
  (q/fill 100 100 100)
  (q/translate (* maze-width cell-size 0.5) 
               (* maze-height cell-size 0.5) 
               -0.05)
  (q/box (* maze-width cell-size) 
         (* maze-height cell-size) 
         0.1)
  (q/pop-matrix))

(defn draw-ceiling []
  (q/push-matrix)
  (q/fill 150 150 150)
  (q/translate (* maze-width cell-size 0.5) 
               (* maze-height cell-size 0.5) 
               wall-height)
  (q/box (* maze-width cell-size) 
         (* maze-height cell-size) 
         0.1)
  (q/pop-matrix))

(defn draw-goal []
  (q/push-matrix)
  ;; Goal marker at bottom-right
  (let [goal-x (- (* maze-width cell-size) (/ cell-size 2))
        goal-y (- (* maze-height cell-size) (/ cell-size 2))]
    (q/translate goal-x goal-y 0.01)
    (q/fill 0 255 0)
    (q/box (* cell-size 0.8) (* cell-size 0.8) 0.02)
    ;; Goal beacon
    (q/translate 0 0 1.0)
    (q/fill 0 255 0 100)
    (q/box 0.3 0.3 2.0))
  (q/pop-matrix))

(defn draw-start []
  (q/push-matrix)
  ;; Start marker at top-left
  (q/translate (/ cell-size 2) (/ cell-size 2) 0.01)
  (q/fill 0 100 255)
  (q/box (* cell-size 0.8) (* cell-size 0.8) 0.02)
  (q/pop-matrix))

(defn draw-maze [maze]
  (doseq [y (range (count maze))
          x (range (count (first maze)))]
    (let [cell (get-in maze [y x])
          px (* x cell-size)
          py (* y cell-size)]
      (when (:north cell)
        (draw-wall px py (+ px cell-size) py))
      (when (:south cell)
        (draw-wall px (+ py cell-size) (+ px cell-size) (+ py cell-size)))
      (when (:west cell)
        (draw-wall px py px (+ py cell-size)))
      (when (:east cell)
        (draw-wall (+ px cell-size) py (+ px cell-size) (+ py cell-size))))))

(defn draw-state [state]
  (q/background 135 206 235)
  (q/lights)
  (q/ambient-light 100 100 100)
  (q/directional-light 255 255 255 -1 0.5 -1)
  
  ;; Set perspective before camera
  (q/perspective (/ Math/PI 2.2) 
                 (/ (float (q/width)) (float (q/height))) 
                 0.01 
                 100)
  
  ;; First-person camera that moves with player
  (let [player (:player state)
        eye-x (:x player)
        eye-y (:y player)
        eye-z 0.8  ;; Eye height
        ;; Look in the direction the player is facing
        look-x (+ eye-x (* (Math/cos (:angle player)) 1.0))
        look-y (+ eye-y (* (Math/sin (:angle player)) 1.0))
        look-z eye-z]  ;; Look straight ahead horizontally
    (q/camera eye-x eye-y eye-z
              look-x look-y look-z
              0 0 -1))
  
  (draw-floor)
  (draw-start)
  (draw-goal)
  (draw-ceiling)
  (draw-maze (:maze state)))

(defn key-pressed [state event]
  (let [k (or (:raw-key event) (:key event))]
    (cond
      (or (= k \w) (= k \W) (= k 87)) (update state :keys-pressed conj :w)
      (or (= k \s) (= k \S) (= k 83)) (update state :keys-pressed conj :s)
      (or (= k \a) (= k \A) (= k 65)) (update state :keys-pressed conj :a)
      (or (= k \d) (= k \D) (= k 68)) (update state :keys-pressed conj :d)
      :else state)))

(defn key-released [state event]
  (let [k (or (:raw-key event) (:key event))]
    (cond
      (or (= k \w) (= k \W) (= k 87)) (update state :keys-pressed disj :w)
      (or (= k \s) (= k \S) (= k 83)) (update state :keys-pressed disj :s)
      (or (= k \a) (= k \A) (= k 65)) (update state :keys-pressed disj :a)
      (or (= k \d) (= k \D) (= k 68)) (update state :keys-pressed disj :d)
      :else state)))

(defn -main []
  (q/defsketch maze-game
    :title "3D Maze Game"
    :size [800 600]
    :setup setup
    :update update-state
    :draw draw-state
    :key-pressed key-pressed
    :key-released key-released
    :middleware [m/fun-mode]
    :renderer :p3d))