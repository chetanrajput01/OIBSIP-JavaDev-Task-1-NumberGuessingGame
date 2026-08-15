import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Number Guessing Game - Java Swing GUI Application
 * OIBSIP Task 2
 *
 * Features:
 *  - Difficulty levels (Easy / Medium / Hard) with different ranges & attempt limits
 *  - Random number generation each round
 *  - Higher / Lower / Correct feedback
 *  - Visible attempt counter
 *  - Max attempts limit -> "You Lost!" + reveal number
 *  - Play Again option
 *  - Round-by-round score/history tracking
 */
public class NumberGuessingGame extends JFrame {

    // ---------- Difficulty settings ----------
    private enum Difficulty {
        EASY("Easy (1-50, 10 attempts)", 1, 50, 10),
        MEDIUM("Medium (1-100, 7 attempts)", 1, 100, 7),
        HARD("Hard (1-200, 5 attempts)", 1, 200, 5);

        final String label;
        final int min, max, maxAttempts;

        Difficulty(String label, int min, int max, int maxAttempts) {
            this.label = label;
            this.min = min;
            this.max = max;
            this.maxAttempts = maxAttempts;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    // ---------- Game state ----------
    private final Random random = new Random();
    private Difficulty currentDifficulty = Difficulty.MEDIUM;
    private int targetNumber;
    private int attemptsUsed;
    private int roundNumber = 0;
    private final List<String> history = new ArrayList<>();

    // ---------- UI components ----------
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel = new JPanel(cardLayout);

    // Setup screen
    private JComboBox<Difficulty> difficultyBox;

    // Game screen
    private JLabel roundLabel;
    private JLabel rangeLabel;
    private JLabel attemptsLabel;
    private JLabel feedbackLabel;
    private JTextField guessField;
    private JButton guessButton;
    private JTextArea historyArea;

    public NumberGuessingGame() {
        setTitle("Number Guessing Game - OIBSIP Task 2");
        setSize(480, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        mainPanel.add(buildSetupPanel(), "SETUP");
        mainPanel.add(buildGamePanel(), "GAME");

        add(mainPanel);
        cardLayout.show(mainPanel, "SETUP");
    }

    // ---------------- Setup Panel ----------------
    private JPanel buildSetupPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel title = new JLabel("Number Guessing Game");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Choose a difficulty to begin");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        difficultyBox = new JComboBox<>(Difficulty.values());
        difficultyBox.setSelectedItem(Difficulty.MEDIUM);
        difficultyBox.setMaximumSize(new Dimension(300, 30));
        difficultyBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton startButton = new JButton("Start Game");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        startButton.addActionListener(this::onStartGame);
        startButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(title);
        panel.add(subtitle);
        panel.add(difficultyBox);
        panel.add(Box.createVerticalStrut(20));
        panel.add(startButton);

        return panel;
    }

    private void onStartGame(ActionEvent e) {
        currentDifficulty = (Difficulty) difficultyBox.getSelectedItem();
        startNewRound();
        cardLayout.show(mainPanel, "GAME");
    }

    // ---------------- Game Panel ----------------
    private JPanel buildGamePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top info panel
        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 4, 4));
        roundLabel = new JLabel();
        rangeLabel = new JLabel();
        attemptsLabel = new JLabel();
        roundLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        rangeLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        attemptsLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        infoPanel.add(roundLabel);
        infoPanel.add(rangeLabel);
        infoPanel.add(attemptsLabel);

        // Center: guess input + feedback
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        JPanel inputRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        guessField = new JTextField(8);
        guessButton = new JButton("Guess");
        guessButton.addActionListener(this::onGuess);
        guessField.addActionListener(this::onGuess); // allow Enter key
        inputRow.add(guessField);
        inputRow.add(guessButton);

        feedbackLabel = new JLabel(" ", SwingConstants.CENTER);
        feedbackLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        feedbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        feedbackLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        centerPanel.add(inputRow);
        centerPanel.add(feedbackLabel);

        // Bottom: history
        historyArea = new JTextArea(8, 20);
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(historyArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Score History"));

        panel.add(infoPanel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(scrollPane, BorderLayout.SOUTH);

        return panel;
    }

    private void startNewRound() {
        roundNumber++;
        targetNumber = random.nextInt(currentDifficulty.max - currentDifficulty.min + 1) + currentDifficulty.min;
        attemptsUsed = 0;

        roundLabel.setText("Round " + roundNumber + "  (" + currentDifficulty.name() + ")");
        rangeLabel.setText("Guess a number between " + currentDifficulty.min + " and " + currentDifficulty.max);
        attemptsLabel.setText("Attempts used: 0 / " + currentDifficulty.maxAttempts);
        feedbackLabel.setText(" ");
        guessField.setText("");
        guessField.setEnabled(true);
        guessButton.setEnabled(true);
        guessField.requestFocusInWindow();
    }

    private void onGuess(ActionEvent e) {
        String text = guessField.getText().trim();
        if (text.isEmpty()) {
            feedbackLabel.setForeground(Color.ORANGE);
            feedbackLabel.setText("Enter a number!");
            return;
        }

        int guess;
        try {
            guess = Integer.parseInt(text);
        } catch (NumberFormatException ex) {
            feedbackLabel.setForeground(Color.ORANGE);
            feedbackLabel.setText("That's not a valid number!");
            return;
        }

        if (guess < currentDifficulty.min || guess > currentDifficulty.max) {
            feedbackLabel.setForeground(Color.ORANGE);
            feedbackLabel.setText("Out of range! (" + currentDifficulty.min + "-" + currentDifficulty.max + ")");
            return;
        }

        attemptsUsed++;
        attemptsLabel.setText("Attempts used: " + attemptsUsed + " / " + currentDifficulty.maxAttempts);

        if (guess == targetNumber) {
            feedbackLabel.setForeground(new Color(0, 150, 0));
            feedbackLabel.setText("Correct! 🎉");
            endRound(true);
        } else if (guess < targetNumber) {
            feedbackLabel.setForeground(Color.BLUE);
            feedbackLabel.setText("Too Low!");
            checkAttemptsExhausted();
        } else {
            feedbackLabel.setForeground(Color.RED);
            feedbackLabel.setText("Too High!");
            checkAttemptsExhausted();
        }

        guessField.setText("");
        guessField.requestFocusInWindow();
    }

    private void checkAttemptsExhausted() {
        if (attemptsUsed >= currentDifficulty.maxAttempts) {
            feedbackLabel.setForeground(Color.RED);
            feedbackLabel.setText("You Lost! Number was: " + targetNumber);
            endRound(false);
        }
    }

    private void endRound(boolean won) {
        guessField.setEnabled(false);
        guessButton.setEnabled(false);

        String summary = won
                ? "Round " + roundNumber + " — guessed in " + attemptsUsed + " attempts (" + currentDifficulty.name() + ")"
                : "Round " + roundNumber + " — LOST after " + attemptsUsed + " attempts, number was " + targetNumber + " (" + currentDifficulty.name() + ")";
        history.add(summary);
        historyArea.setText(String.join("\n", history));

        int choice = JOptionPane.showConfirmDialog(
                this,
                (won ? "You guessed it in " + attemptsUsed + " attempts!" : "Out of attempts! The number was " + targetNumber + ".")
                        + "\n\nPlay again?",
                won ? "You Won!" : "You Lost!",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            // Optionally let the player pick a new difficulty each round
            Difficulty selected = (Difficulty) JOptionPane.showInputDialog(
                    this,
                    "Choose difficulty for next round:",
                    "Difficulty",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    Difficulty.values(),
                    currentDifficulty
            );
            if (selected != null) {
                currentDifficulty = selected;
            }
            startNewRound();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Thanks for playing!\n\nFinal Score History:\n" + String.join("\n", history),
                    "Game Over",
                    JOptionPane.INFORMATION_MESSAGE);
            cardLayout.show(mainPanel, "SETUP");
            roundNumber = 0;
            history.clear();
            historyArea.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
            NumberGuessingGame game = new NumberGuessingGame();
            game.setVisible(true);
        });
    }
}
