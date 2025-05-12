package GUI;
import javax.swing.*;
import commands.CommandProcessor;
import java.awt.*;

public class Settings {

    public static void showSettings(JFrame parent, CommandProcessor commandProc) {
        // Új ablak létrehozása
        JFrame settingsFrame = new JFrame("Beállítások");
        settingsFrame.setSize(800, 800);
        settingsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        settingsFrame.setLocationRelativeTo(parent);

        // Fő panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Margók
        mainPanel.setBackground(new Color(30, 30, 30)); // Háttérszín

        // Gombok panelje (középső rész)
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Térköz a gombok körül
        gbc.fill = GridBagConstraints.NONE; // Ne nyújtsa ki a gombokat
        gbc.anchor = GridBagConstraints.CENTER; // Középre igazítás

        // Gombok létrehozása
        JButton PlayerCountButton = createStyledButton("Jatékosok száma: 4");
        JButton SetTypeAndNameButton = createStyledButton("Játekosok típusa és neve");
        JButton LoadButton = createStyledButton("Load Game");
        JButton button4 = createStyledButton("Gomb 4");
        JButton button5 = createStyledButton("Gomb 5");
        JButton button6 = createStyledButton("Gomb 6");

        // Gombok elhelyezése
        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(PlayerCountButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        buttonPanel.add(SetTypeAndNameButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        buttonPanel.add(LoadButton, gbc);

        // gbc.gridx = 1;
        // gbc.gridy = 1;
        // buttonPanel.add(button4, gbc);

        // gbc.gridx = 0;
        // gbc.gridy = 2;
        // buttonPanel.add(button5, gbc);

        // gbc.gridx = 1;
        // gbc.gridy = 2;
        // buttonPanel.add(button6, gbc);

        // Gombok panel hozzáadása a fő panel közepéhez
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Mentés és bezárás gomb (alsó rész)
        JButton saveAndCloseButton = createStyledButton("Mentés és vissza a főmenübe");
        saveAndCloseButton.addActionListener(e -> {
            settingsFrame.dispose(); // Bezárja a Settings ablakot
            new MainMenu(commandProc).setVisible(true); // Visszatér a MainMenu-hoz
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.add(saveAndCloseButton);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Panel hozzáadása az ablakhoz
        settingsFrame.add(mainPanel);

        // Ablak megjelenítése
        settingsFrame.setVisible(true);
        
        PlayerCountButton.addActionListener(e -> {
            new ResultScreen().setVisible(true); // Megnyitja a ResultScreen ablakot
        });
        SetTypeAndNameButton.addActionListener(e -> {
            // Felugró ablak létrehozása
            JOptionPane.showMessageDialog(
                settingsFrame, // Szülő ablak
                "A Player 2 köre következik.", // Üzenet
                "Kör információ", // Ablak címe
                JOptionPane.INFORMATION_MESSAGE // Információs ikon
            );
        });
    }

    private static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.PLAIN, 14)); // Kisebb betűméret
        button.setForeground(Color.WHITE); // Szöveg színe
        button.setBackground(new Color(30, 144, 255)); // Háttérszín
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2)); // Fehér szegély
        button.setPreferredSize(new Dimension(250, 40)); // Kisebb gombméret
        return button;
    }
}