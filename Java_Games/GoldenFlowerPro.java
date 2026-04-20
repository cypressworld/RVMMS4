import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import javax.sound.sampled.*;
import java.io.File;

public class GoldenFlowerPro extends JFrame {
    // Game State
    private int balance = 10000;
    private int betA = 0, betB = 0, betC = 0;
    private List<String> history = new ArrayList<>();
    
    // UI Components
    private JLabel balanceLabel, statusLabel;
    private JPanel historyPanel, cardAreaA, cardAreaB;
    private JButton btnA, btnB, btnC;

    public GoldenFlowerPro() {
        setTitle("NIIT Golden Flower - Professional Edition");
        setSize(1000, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(25, 25, 25)); // Dark Sleek Theme
        setLayout(new BorderLayout());

        setupHistoryBar();
        setupGameTable();
        setupBettingControls();

        setVisible(true);
    }

    private void setupHistoryBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(new Color(45, 45, 45));
        
        JLabel title = new JLabel("  TREND ANALYSIS: ");
        title.setForeground(Color.LIGHT_GRAY);
        title.setFont(new Font("Arial", Font.BOLD, 12));

        historyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        historyPanel.setBackground(new Color(35, 35, 35));
        historyPanel.setPreferredSize(new Dimension(1000, 45));

        bar.add(title, BorderLayout.WEST);
        bar.add(historyPanel, BorderLayout.CENTER);
        add(bar, BorderLayout.NORTH);
    }

    private void setupGameTable() {
        JPanel table = new JPanel(new GridLayout(1, 2, 40, 0));
        table.setOpaque(false);
        table.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        cardAreaA = createPlayerUI("PLAYER A", Color.CYAN);
        cardAreaB = createPlayerUI("PLAYER B", new Color(255, 0, 255));

        table.add(cardAreaA);
        table.add(cardAreaB);
        add(table, BorderLayout.CENTER);
    }

    private JPanel createPlayerUI(String name, Color accent) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JLabel label = new JLabel(name, SwingConstants.CENTER);
        label.setForeground(accent);
        label.setFont(new Font("SansSerif", Font.BOLD, 28));

        JPanel cards = new JPanel(new FlowLayout());
        cards.setBackground(new Color(255, 255, 255, 20));
        cards.setPreferredSize(new Dimension(300, 180));
        
        for(int i=0; i<3; i++) {
            JLabel c = new JLabel("🂠"); // Back of card emoji
            c.setFont(new Font("Serif", Font.PLAIN, 120));
            c.setForeground(Color.GRAY);
            cards.add(c);
        }

        panel.add(label, BorderLayout.NORTH);
        panel.add(cards, BorderLayout.CENTER);
        return panel;
    }

    private void setupBettingControls() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(15, 15, 15));
        
        // Balance Display
        balanceLabel = new JLabel("💰 BALANCE: " + balance, SwingConstants.CENTER);
        balanceLabel.setForeground(Color.YELLOW);
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 22));
        footer.add(balanceLabel, BorderLayout.NORTH);

        // Buttons Panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        btnPanel.setOpaque(false);

        btnA = createStyledButton("BET A (2x)", Color.CYAN);
        btnB = createStyledButton("BET B (2x)", new Color(255, 0, 255));
        btnC = createStyledButton("LUCKY C (5x)", Color.ORANGE);
        
        JButton deal = new JButton("DEAL");
        deal.setBackground(Color.GREEN);
        deal.setPreferredSize(new Dimension(120, 50));
        
        JButton learn = new JButton("📖 RULES");
        learn.setBackground(Color.WHITE);

        btnA.addActionListener(e -> placeBet("A"));
        btnB.addActionListener(e -> placeBet("B"));
        btnC.addActionListener(e -> placeBet("C"));
        deal.addActionListener(e -> runGameLogic());
        learn.addActionListener(e -> showHelp());

        btnPanel.add(btnA);
        btnPanel.add(btnC);
        btnPanel.add(btnB);
        btnPanel.add(deal);
        btnPanel.add(learn);
        
        footer.add(btnPanel, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
    }

    private JButton createStyledButton(String text, Color border) {
        JButton b = new JButton(text);
        b.setPreferredSize(new Dimension(130, 50));
        b.setForeground(Color.WHITE);
        b.setBackground(new Color(40, 40, 40));
        b.setBorder(BorderFactory.createLineBorder(border, 2));
        return b;
    }

    private void placeBet(String type) {
        if (balance >= 100) {
            balance -= 100;
            if (type.equals("A")) betA += 100;
            else if (type.equals("B")) betB += 100;
            else betC += 100;
            updateStatus();
            playAudio("bet"); // Requires bet.wav
        }
    }

    private void runGameLogic() {
        if (betA + betB + betC == 0) return;

        Random r = new Random();
        int winChance = r.nextInt(100);
        String winner;

        if (winChance < 45) winner = "A";
        else if (winChance < 90) winner = "B";
        else winner = "C";

        // Update Balance - updated line
        //if (winner.equals("A")) balance += (betA * 2);
        //else if (winner.equals("B")) balance += (betB * 2);
        //else balance += (betC * 5);


        if (winner.equals("A")) balance += (betA * 2.89);
        else if (winner.equals("B")) balance += (betB * 2.89);
        else balance += (betC * 2.89);


     

        // Reset Bets and Log History
        betA = 0; betB = 0; betC = 0;
        updateHistory(winner);
        updateStatus();
        playAudio("win"); // Requires win.wav
    }

    private void updateHistory(String res) {
        JLabel l = new JLabel(res);
        l.setOpaque(true);
        l.setPreferredSize(new Dimension(25, 25));
        l.setHorizontalAlignment(SwingConstants.CENTER);
        l.setBackground(res.equals("A") ? Color.CYAN : res.equals("B") ? Color.MAGENTA : Color.ORANGE);
        historyPanel.add(l, 0);
        historyPanel.revalidate();
    }

    private void updateStatus() {
        balanceLabel.setText("💰 BALANCE: " + balance);
        btnA.setText("A: " + betA);
        btnB.setText("B: " + betB);
        btnC.setText("C: " + betC);
    }

    private void showHelp() {
        JOptionPane.showMessageDialog(this, 
            "HOW TO PLAY:\n1. Click A, B, or C to place bets (100 coins each).\n" +
            "2. Analysis: Review Trend History to guess the next win.\n" +
            "3. Payouts: A & B = 2x, Lucky C = 5x.", "Game Instructions", 1);
    }

    private void playAudio(String name) {
        try {
            AudioInputStream ai = AudioSystem.getAudioInputStream(new File(name + ".wav"));
            Clip c = AudioSystem.getClip();
            c.open(ai);
            c.start();
        } catch (Exception e) { /* Sound file missing */ }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GoldenFlowerPro::new);
    }
}