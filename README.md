# ClojureQuest - 3D Maze Game (Alpha/POC)

A proof-of-concept first-person 3D maze navigation game built with Clojure and Quil (Processing).

## 🎮 Game Overview

Navigate through a procedurally generated maze from a first-person perspective. Find your way from the blue starting zone to the green goal zone!

## ✨ Features

- **Procedural Maze Generation**: Each game generates a unique solvable maze using depth-first search
- **First-Person 3D Perspective**: Immersive view from inside the maze
- **Smooth Movement**: Vehicle-like controls with turning and forward/backward movement
- **Collision Detection**: Realistic wall boundaries with close approach
- **Visual Markers**: Blue start zone and green goal beacon

## 🎯 Controls

- **W** - Move forward
- **S** - Move backward  
- **A** - Turn left
- **D** - Turn right

## 🚀 Running the Game

### With Java 21+ (Recommended)
```bash
JAVA_CMD=/path/to/java21/bin/java clojure -M:run
```

### With Default Java
```bash
clojure -M:run
```

## 📋 Requirements

- Clojure 1.11.1 or higher
- Java 21+ recommended (Java 17+ minimum for latest Quil)
- Dependencies managed via deps.edn

## 🏗️ Project Structure

```
ClojureQuest/
├── deps.edn              # Dependencies and configuration
├── src/
│   └── maze_game/
│       ├── core.clj      # Main game loop and rendering
│       ├── maze.clj      # Maze generation algorithm
│       └── player.clj    # Player movement and collision
└── README.md
```

## 🔧 Development Status

**Alpha Release / Proof of Concept**

This is an early proof-of-concept demonstrating:
- 3D rendering in Clojure using Quil
- Procedural maze generation
- First-person navigation mechanics
- Basic collision detection

### Known Limitations
- Basic graphics and textures
- No game menu or UI
- No score or timer system
- Fixed maze size (10x10)
- No sound effects or music

### Potential Enhancements
- Multiple difficulty levels
- Minimap display
- Timer and scoring system
- Texture mapping for walls
- Sound effects
- Power-ups or collectibles
- Multiple maze algorithms

## 🛠️ Technology Stack

- **Clojure**: Functional programming language
- **Quil**: Clojure wrapper for Processing
- **Processing**: Graphics and visualization framework

## 📝 License

MIT License - See LICENSE file for details

## 🤝 Contributing

This is a proof-of-concept project. Feel free to fork and experiment!

---

*Built as a demonstration of 3D game development in Clojure*