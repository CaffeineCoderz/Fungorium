package GUI;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {

    // szerintem ilyen szimpla boven elegendo ahoz hogy valasszunk nem kell jol kineznie
    // de ha megis akkor majd atirjuk
    public MainMenu() {
        setTitle("Fungorium - Főmenü");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        //main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // cim panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Fungorium");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titlePanel.add(titleLabel);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // gombok panel
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 15)); // 3 sor, 1 oszlop
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 0, 50));

        // gomb
        JButton newGameButton = createStyledButton("Új játék indítása");
        JButton rulesButton = createStyledButton("Játékszabályok");
        JButton exitButton = createStyledButton("Kilépés");

        // gomb+panel
        buttonPanel.add(newGameButton);
        buttonPanel.add(rulesButton);
        buttonPanel.add(exitButton);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        add(mainPanel);

        // Gombok esemenyei
        newGameButton.addActionListener(e -> openChooseSpeciesScreen());
        rulesButton.addActionListener(e -> showRules());
        exitButton.addActionListener(e -> System.exit(0));
    }

    // oszinten nem tudom milyen gomb kell egyenlore ez elegendo
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.PLAIN, 16));
        button.setBackground(new Color(240, 240, 240));
        button.setForeground(new Color(60, 60, 60));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        return button;
    }

    private void openChooseSpeciesScreen() {
        new ChooseSpecies().setVisible(true);
        dispose();
    }

    private void showRules() {
        // itt majd rendesen ossze kell rakni a jatekszabalyokat
        JOptionPane.showMessageDialog(this, "Játékszabályok:\n1. ...\n2. ...\n3. ...", "Játékszabályok", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenu().setVisible(true));
    }
}