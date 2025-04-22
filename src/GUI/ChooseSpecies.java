package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

class ChooseSpecies extends JFrame {

    public ChooseSpecies() {

        // Alap settings
        setTitle("Fungorium - Fajválasztás");
        setSize(420, 280); // Nagyobb méret a Vissza gombnak
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // center
        setResizable(false);

        // main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // cim panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Válaszd ki a játékosok számát");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(titleLabel);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // middle panel
        JPanel inputPanel = new JPanel(new GridLayout(2, 1, 20, 15)); // 2 sor, 1 oszlop
        inputPanel.setBorder(new EmptyBorder(10, 50, 0, 50));

        JLabel playerCountLabel = new JLabel("Játékosok száma:");
        playerCountLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));

        JTextField playerCountField = new JTextField();
        playerCountField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        playerCountField.setHorizontalAlignment(JTextField.CENTER); // center number

        inputPanel.add(playerCountLabel);
        inputPanel.add(playerCountField);

        mainPanel.add(inputPanel, BorderLayout.CENTER);

        // Gomb panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10)); // Hozzáadtunk térközt
        JButton backButton = createStyledButton("Vissza"); // Vissza gomb
        JButton nextButton = createStyledButton("Tovább");
        buttonPanel.add(backButton); // Először a Vissza gomb
        buttonPanel.add(nextButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Gomb what to do
        nextButton.addActionListener(e -> {
            try {
                int playerCount = Integer.parseInt(playerCountField.getText());
                if (playerCount > 0) {
                    openGameInitScreen(playerCount);
                } else {
                    JOptionPane.showMessageDialog(this, "Kérlek, adj meg egy pozitív számot!", "Hiba", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Érvénytelen formátum! Számot kell megadnod.", "Hiba", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Vissza
        backButton.addActionListener(e -> {
            new MainMenu().setVisible(true);
            dispose();
        });
    }

    // gomb
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.PLAIN, 16));
        button.setBackground(new Color(240, 240, 240));
        button.setForeground(new Color(60, 60, 60));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                new EmptyBorder(10, 20, 10, 20)
        ));
        return button;
    }

    private void openGameInitScreen(int playerCount) {
        // itt van a jatek inicializalasa
        new GameInitScreen(playerCount).setVisible(true);
        dispose();
    }
}