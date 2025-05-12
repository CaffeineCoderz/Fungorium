package GUI;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class ResultScreen extends JFrame {

    private List<String> fungusWinners;
    private List<String> insectWinners;

    public ResultScreen() {
        loadWinnersFromFile("src/GUI/DATA/winners.txt");

        setTitle("Fungorium - Eredmények");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(30, 30, 45));

        // Cím és kép panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);

        // Korona kép
        JLabel crownLabel = new JLabel();
        crownLabel.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            ImageIcon crownIcon = new ImageIcon("src/resources/crown.png");
            crownLabel.setIcon(new ImageIcon(crownIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
        } catch (Exception e) {
            System.err.println("Hiba a korona kép betöltése közben: " + e.getMessage());
        }
        titlePanel.add(crownLabel, BorderLayout.NORTH);

        // Cím
        JLabel titleLabel = new JLabel("Győztesek", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(new Color(255, 204, 0));
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Nyertesek fő panel
        JPanel winnersPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 0)); // FlowLayout a két oszlop egymás mellé rendezéséhez
        winnersPanel.setOpaque(false);

        // Fungus nyertesek panel
        JPanel fungusPanel = new JPanel(new BorderLayout()); // BorderLayout a címke és a nevek függőleges elrendezéséhez
        fungusPanel.setOpaque(false);
        JLabel fungusTitle = new JLabel("FungusSpecies", SwingConstants.CENTER);
        fungusTitle.setFont(new Font("Arial", Font.BOLD, 24));
        fungusTitle.setForeground(Color.lightGray);
        fungusPanel.add(fungusTitle, BorderLayout.NORTH);
        JPanel fungusWinnersSubPanel = new JPanel(new GridLayout(0, 1)); // GridLayout a nevek függőlegesen
        fungusWinnersSubPanel.setOpaque(false);
        for (int i = 0; i < fungusWinners.size(); i++) {
            JLabel winnerLabel = new JLabel((i + 1) + ". " + fungusWinners.get(i), SwingConstants.CENTER);
            styleWinnerLabel(winnerLabel, i);
            fungusWinnersSubPanel.add(winnerLabel);
        }
        fungusPanel.add(fungusWinnersSubPanel, BorderLayout.CENTER);
        winnersPanel.add(fungusPanel);

        // Insect nyertesek panel
        JPanel insectPanel = new JPanel(new BorderLayout()); 
        insectPanel.setOpaque(false);
        JLabel insectTitle = new JLabel("InsectSpecies", SwingConstants.CENTER);
        insectTitle.setFont(new Font("Arial", Font.BOLD, 24));
        insectTitle.setForeground(Color.lightGray);
        insectPanel.add(insectTitle, BorderLayout.NORTH);
        JPanel insectWinnersSubPanel = new JPanel(new GridLayout(0, 1)); 
        insectWinnersSubPanel.setOpaque(false);
        for (int i = 0; i < insectWinners.size(); i++) {
            JLabel winnerLabel = new JLabel((i + 1) + ". " + insectWinners.get(i), SwingConstants.CENTER);
            styleWinnerLabel(winnerLabel, i);
            insectWinnersSubPanel.add(winnerLabel);
        }
        insectPanel.add(insectWinnersSubPanel, BorderLayout.CENTER);
        winnersPanel.add(insectPanel);

        // Hozzáadás a fő panelhez
        mainPanel.add(winnersPanel, BorderLayout.CENTER);

        // Vissza a főmenübe gomb
        JButton backToMenuButton = new JButton("Vissza a főmenübe");
        styleButton(backToMenuButton);
        backToMenuButton.addActionListener(e -> {
            //new MainMenu(commandP).setVisible(true);
            dispose();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(backToMenuButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void loadWinnersFromFile(String filePath) {
        fungusWinners = new ArrayList<>();
        insectWinners = new ArrayList<>();
        boolean readingFungus = false;
        boolean readingInsect = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.equalsIgnoreCase("<fungusspecies>")) {
                    readingFungus = true;
                    readingInsect = false;
                } else if (line.equalsIgnoreCase("<insectspecies>")) {
                    readingInsect = true;
                    readingFungus = false;
                } else if (readingFungus && !line.isEmpty()) {
                    fungusWinners.add(line);
                } else if (readingInsect && !line.isEmpty()) {
                    insectWinners.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Hiba a nyertesek fájl beolvasása közben: " + e.getMessage());
        }
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("SansSerif", Font.BOLD, 18));
        button.setBackground(new Color(30, 144, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setMargin(new Insets(8, 16, 8, 16));
    }

    private void styleWinnerLabel(JLabel label, int index) {
        if (index == 0) {
            label.setFont(new Font("Arial", Font.BOLD, 28));
            label.setForeground(Color.white);
        } else {
            label.setFont(new Font("Arial", Font.PLAIN, 20));
            label.setForeground(Color.LIGHT_GRAY);
        }
        label.setHorizontalAlignment(SwingConstants.CENTER);
    }
}