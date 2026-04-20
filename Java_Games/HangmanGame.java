import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class HangmanGame extends JFrame {
    private String[] words = {"DOCKER", "KUBERNETES", "JAVA", "SPRINGBOOT", "MICROSERVICE"};
    private String targetWord;
    private StringBuilder displayWord;
    private int attemptsLeft = 6;
    private JLabel wordLabel, statusLabel;
    private JPanel drawingPanel;

    public HangmanGame() {
        targetWord = words[new Random().nextInt(words.length)];
        displayWord = new StringBuilder("_".repeat(targetWord.length()));

        setTitle("NIIT Java Cloud-Ready Hangman");
        setSize(500, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top Area: Word Display
        wordLabel = new JLabel(formatWord(displayWord.toString()), SwingConstants.CENTER);
        wordLabel.setFont(new Font("Monospaced", Font.BOLD, 30));
        add(wordLabel, BorderLayout.NORTH);

        // Center Area: Drawing the Hangman
        drawingPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawMan(g);
            }
        };
        add(drawingPanel, BorderLayout.CENTER);

        // Bottom Area: Input
        JPanel southPanel = new JPanel(new GridLayout(2, 1));
        statusLabel = new JLabel("Attempts Left: " + attemptsLeft, SwingConstants.CENTER);
        JTextField inputField = new JTextField();
        
        inputField.addActionListener(e -> {
            processGuess(inputField.getText().toUpperCase());
            inputField.setText("");
        });

        southPanel.add(statusLabel);
        southPanel.add(inputField);
        add(southPanel, BorderLayout.SOUTH);
    }

    private void processGuess(String guess) {
        if (guess.length() != 1 || attemptsLeft <= 0) return;
        char c = guess.charAt(0);

        if (targetWord.indexOf(c) >= 0) {
            for (int i = 0; i < targetWord.length(); i++) {
                if (targetWord.charAt(i) == c) displayWord.setCharAt(i, c);
            }
        } else {
            attemptsLeft--;
        }

        wordLabel.setText(formatWord(displayWord.toString()));
        statusLabel.setText("Attempts Left: " + attemptsLeft);
        drawingPanel.repaint();
        checkGameOver();
    }

    private void checkGameOver() {
        if (displayWord.toString().equals(targetWord)) {
            JOptionPane.showMessageDialog(this, "GENIUS! You saved the app!");
            System.exit(0);
        } else if (attemptsLeft <= 0) {
            JOptionPane.showMessageDialog(this, "CRASHED! The word was: " + targetWord);
            System.exit(0);
        }
    }

    private String formatWord(String s) {
        return s.replace("", " ").trim();
    }

    private void drawMan(Graphics g) {
        g.setColor(Color.BLACK);
        // Gallows
        g.drawLine(50, 250, 150, 250); // Base
        g.drawLine(100, 250, 100, 50); // Post
        g.drawLine(100, 50, 200, 50);  // Top
        g.drawLine(200, 50, 200, 80);  // Rope

        if (attemptsLeft < 6) g.drawOval(180, 80, 40, 40);      // Head
        if (attemptsLeft < 5) g.drawLine(200, 120, 200, 180);   // Body
        if (attemptsLeft < 4) g.drawLine(200, 140, 170, 160);   // Left Arm
        if (attemptsLeft < 3) g.drawLine(200, 140, 230, 160);   // Right Arm
        if (attemptsLeft < 2) g.drawLine(200, 180, 170, 220);   // Left Leg
        if (attemptsLeft < 1) g.drawLine(200, 180, 230, 220);   // Right Leg
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HangmanGame().setVisible(true));
    }
}
