# Number Guessing Game — OIBSIP Task 2

A Java Swing GUI application where the computer picks a random number and the
player tries to guess it, with real-time Higher/Lower/Correct feedback.

## Features

- Random number generation each round (range depends on difficulty)
- Live feedback: **Too High!**, **Too Low!**, **Correct!**
- Visible attempt counter (`Attempts used: X / MaxAttempts`)
- Max attempts limit — game ends with **"You Lost!"** and reveals the number
- **Play Again** prompt after every round (Yes/No)
- Round-by-round score history panel: `Round X — guessed in Y attempts`
- **Bonus:** Difficulty levels
  - Easy → 1–50, 10 attempts
  - Medium → 1–100, 7 attempts
  - Hard → 1–200, 5 attempts

## Tech Stack

- Java (JDK 8+)
- Swing (GUI) — `JFrame`, `CardLayout`, `JTextField`, `JOptionPane`, `JTextArea`

## Project Structure

```
OIBSIP-Java-Task2-NumberGuessingGame/
├── src/
│   └── NumberGuessingGame.java
├── README.md
└── screenshots/
```

## How to Run

### Using VS Code
1. Install the "Extension Pack for Java" (Microsoft) if not already installed.
2. Open this folder in VS Code.
3. Open `src/NumberGuessingGame.java`.
4. Click **Run** above the `main` method (or press `F5`).

### Using Eclipse / STS
1. `File → New → Java Project` → name it `NumberGuessingGame`.
2. Right-click `src` → `New → Package` (optional) or place the file directly under `src`.
3. Copy `NumberGuessingGame.java` into the `src` folder.
4. Right-click the file → `Run As → Java Application`.

### Using command line
```bash
javac NumberGuessingGame.java
java NumberGuessingGame
```

## How to Play

1. Launch the app — pick a difficulty from the dropdown and click **Start Game**.
2. Enter a guess in the text field and click **Guess** (or press Enter).
3. Read the feedback: Too High / Too Low / Correct.
4. Keep guessing until you run out of attempts or guess correctly.
5. Choose **Play Again** to start a new round (you can pick a new difficulty),
   or **No** to end the session and see your full score history.

## Author

Chetan — OIBSIP Java Development Internship
