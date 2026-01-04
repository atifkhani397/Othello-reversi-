# 🟢 Othello / Reversi - JavaFX Academic Project

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-4285F4?style=for-the-badge&logo=openjdk&logoColor=white)
![Status](https://img.shields.io/badge/Status-Completed-success?style=for-the-badge)

A fully functional, object-oriented implementation of the classic board game **Othello (Reversi)** built with **Java** and **JavaFX**. This project demonstrates advanced Software Engineering concepts including **MVC Architecture**, **AI Algorithms**, and **Modern UI Design**.



---

## ✨ Key Features

### 🎮 Game Modes
* **Player vs Player (PvP):** Classic local multiplayer.
* **Player vs Computer (PvE):** Challenge an AI opponent that uses a greedy/heuristic strategy to control the board.

### 🧠 Smart Features
* **Move Validation:** The game engine automatically calculates valid moves in all 8 directions.
* **Visual Hints:** Valid moves are highlighted on the board to assist new players.
* **Turn Management:** Handles complex rules like "Turn Skipping" if a player has no valid moves.
* **Score Tracking:** Real-time updates of disc counts for Black and White.

### 🎨 Modern UI/UX
* **Glassmorphism Effects:** Custom transparency and shadows for a modern aesthetic.
* **Custom Dialogs:** Replaced standard system alerts with styled, non-intrusive game-over overlays.
* **Theme Support:** Toggle between **Light Mode** and **Dark Mode** instantly.
* **Responsive Feedback:** Hover effects, glowing player cards, and smooth animations.

### 🛠 Tools
* **Undo Functionality:** Stack-based history allowing players to revert moves.
* **Restart & Reset:** Instant board reset without restarting the application.

---

## 🏗 Architecture & Design Pattern

This project strictly follows the **Model-View-Controller (MVC)** architectural pattern to ensure separation of concerns, making the code modular and testable.

### 1. Model (`com.othello.reversigame.model`)
* **`Board.java`**: Manages the 2D grid, logic for flipping discs, and valid move calculations.
* **`GameModel.java`**: Manages game state, player turns, undo stack, and rules.
* **`ComputerPlayer.java`**: Implements the AI logic.

### 2. View (`JavaFX` + `CSS`)
* **`CellView.java`**: A custom JavaFX component representing a single board tile.
* **`style.css`**: CSS styling for the "Forest Green" board, pieces, and glassmorphism effects.

### 3. Controller (`GameController.java`)
* Acts as the bridge between Logic and UI. It handles clicks, updates the model, and refreshes the view.

---

## 🚀 Getting Started

### Prerequisites
* **Java Development Kit (JDK)** 17 or higher.
* **IntelliJ IDEA** (Recommended) or Eclipse.
* **JavaFX SDK** (If not bundled with your JDK).

### Installation
1.  **Clone the repository**
    ```bash
    git clone [https://github.com/YourUsername/Othello-Reversi-JavaFX.git](https://github.com/YourUsername/Othello-Reversi-JavaFX.git)
    ```
2.  **Open in IntelliJ IDEA**
    * File -> Open -> Select the project folder.
    * Let Maven/Gradle sync (if applicable).
3.  **Run the Project**
    * Navigate to `src/main/java/com/othello/reversigame/Main.java`.
    * Right-click -> **Run 'Main.main()'**.

---

## 🧩 Algorithms Used

### 1. Directional Scanning (Move Validation)
The board logic scans all **8 directions** (horizontal, vertical, diagonal) from a placed piece to determine if it "flips" opponent discs.
`Direction Vectors: {-1, 0, 1}`

### 2. AI Strategy (Greedy Heuristic)
The Computer player evaluates moves based on a positional weight system:
* **Corners:** High Value (+100 points) - Stable positions that cannot be flipped.
* **Edges:** Medium Value - Good for stability.
* **Inner Board:** Low Value.

### 3. Stack Data Structure (Undo)
A `Stack<Board>` stores deep copies of the board state after every move. Clicking "Undo" pops the stack, reverting the game to the previous state.

---

## 📂 Project Structure

```text
src
└── main
    ├── java
    │   └── com.othello.reversigame
    │       ├── Main.java           # Entry Point & Scene Switching
    │       ├── GameController.java # Logic <-> UI Bridge
    │       ├── GameModel.java      # State Management
    │       ├── Board.java          # Core Grid Logic
    │       ├── Piece.java          # Enum (BLACK, WHITE, EMPTY)
    │       ├── CellView.java       # Custom UI Component
    │       └── ComputerPlayer.java # AI Logic
    └── resources
        └── com.othello.reversigame
            └── style.css           # Custom Styling
```
## 👥 Authors / Team Members

| Name | Student ID | Role |
| :--- | :--- | :--- |
| **Muhammad Atif** | FA24-BSE-011 | Lead Developer & UI/UX |
| **Hassan Khan** | FA24-BSE-004 | Backend Logic & Testing |

* **Course:** Object Oriented Programming (OOP)
* **University:** COMSATS University Islamabad, Abbottabad Campus

---

*This project was developed for academic purposes to demonstrate proficiency in Java OOP and GUI development.*
