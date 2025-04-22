package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

class ResultScreen extends JFrame {
        // ez is eleg ha egyszeruen kiirja a nyertest
        // majd visszater a fo menube
        //esetleg majd scoreboard nevvel
    public ResultScreen(String winnerName) {
        setTitle("Fungorium - Eredmények");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // result panel
        JLabel resultLabel = new JLabel("A győztes: " + winnerName);
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(resultLabel, BorderLayout.CENTER);

        // Gomb panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton backToMenuButton = new JButton("Vissza a főmenübe");
        buttonPanel.add(backToMenuButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Gomb esemeny
        backToMenuButton.addActionListener((ActionEvent e) -> {
            new MainMenu().setVisible(true);
            dispose();
        });

        add(mainPanel);
    }
}
