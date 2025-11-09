# 💎 Miner - A Jetpack Compose Minesweeper

A simple and sleek Minesweeper-style game built for Android using modern Kotlin and Jetpack Compose. This project is a minimalist take on the classic game, focusing on a clean dark-mode UI, vibrant "eye-catching" colors, and quick, replayable rounds.

---

## 📸 Screenshots

| Game in Progress | Game Lost (Hit a Bomb) |
| :---: | :---: |
| ![A 3x3 grid with some hidden tiles and some revealed diamonds](<assets/ingame.jpeg>) | ![The game grid showing a red bomb tile and a 'You Lost!' message](<assets/bomb.jpeg>) |

---

## ✨ Features

* **Modern UI:** A sleek, dark-mode interface built entirely with Jetpack Compose.
* **Vibrant Feedback:** Uses bright, eye-catching colors (like neon green and vibrant blue) for a more engaging experience.
* **Simple Gameplay:** A 3x3 grid where you tap to find all the diamonds (💎).
* **Win/Loss States:** The game clearly tells you if you've won or hit a bomb (💣).
* **Quick Reset:** Instantly start a new game with the "Play Again" button.

---

## 🛠️ Tech Stack

* **Language:** [Kotlin](https://kotlinlang.org/)
* **UI Toolkit:** [Jetpack Compose](https://developer.android.com/jetpack/compose)
* **State Management:** `remember` and `mutableStateOf` for managing the game's state.

---

## 🚀 Getting Started

To get a local copy up and running, follow these simple steps.

1.  **Clone the repository:**
    ```sh
    git clone (https://github.com/Icey067/Minesweeper-Android-App.git)
    ```

2.  **Open in Android Studio:**
    * Open Android Studio (Hedgehog or newer recommended).
    * Select "Open" and navigate to the cloned project folder.

3.  **Build & Run:**
    * Wait for Gradle to sync and download all dependencies.
    * Select an Android emulator or connect a physical device.
    * Click the **Run** (▶️) button.

---

## 🗺️ Future Roadmap

This is a simple foundation. Future improvements could include:

* [ ] **Difficulty Levels:** Add options for different grid sizes (e.g., 5x5, 8x8) and bomb counts.
* [ ] **Flagging:** Implement a long-press feature to "flag" a tile you suspect is a bomb.
* [ ] **Neighboring Bomb Count:** Show a number on revealed tiles to indicate how many bombs are adjacent to it.
* [ ] **Game Timer:** Add a timer to track how fast you can win.
