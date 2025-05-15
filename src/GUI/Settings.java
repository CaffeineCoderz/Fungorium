package GUI;
import javax.swing.*;
import commands.CommandProcessor;
import logic.GameLogic;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Settings {
    public class Player {
        String type;
        String name;
    }

    public static void showSettings(JFrame parent, GameLogic gameLogic) {
        List<Player> players = new ArrayList<>();
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
        JButton PlayerCountButton = createStyledButton("Jatékosok száma: " + players.size());
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
            new MainMenu(gameLogic).setVisible(true); // Visszatér a MainMenu-hoz
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
            // getter kell hozzá!
            java.util.Map<String, Object> playerMap = gameLogic.getPlayers(); 
            DefaultListModel<String> model = new DefaultListModel<>();
            for (String name : playerMap.keySet()) {
                Object obj = playerMap.get(name);
                String type = obj.getClass().getSimpleName().toUpperCase().contains("FUNGUS") ? "FUNGUS" : "INSECT";
                model.addElement(name + " (" + type + ")");
            }

            JList<String> playerList = new JList<>(model);

            JButton addButton = new JButton("Új játékos");
            addButton.addActionListener(ev -> {
                JTextField nameField = new JTextField();
                String[] types = {"FUNGUS", "INSECT"};
                JComboBox<String> typeBox = new JComboBox<>(types);
                int res = JOptionPane.showConfirmDialog(settingsFrame, new Object[]{
                    "Név:", nameField, "Típus:", typeBox
                }, "Játékos hozzáadása", JOptionPane.OK_CANCEL_OPTION);
                if (res == JOptionPane.OK_OPTION) {
                    String name = nameField.getText().trim();
                    String type = (String) typeBox.getSelectedItem();
                    if (!playerMap.containsKey(name) && !name.isEmpty()) {
                        if (type.equals("FUNGUS")) {
                            gameLogic.getCommandProcessor().process("/create fungusspecies " + name);
                        } else {
                            gameLogic.getCommandProcessor().process("/create insectspecies " + name);
                        }
                        gameLogic.addSpecies(name, gameLogic.getCommandProcessor().getCreatedObjects().get(name));
                        model.addElement(name + " (" + type + ")");
                        PlayerCountButton.setText("Játékosok száma: " + gameLogic.getPlayers().size());
                    } else {
                        JOptionPane.showMessageDialog(settingsFrame, "Ez a név már foglalt vagy üres!", "Hiba", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            JButton removeButton = new JButton("Törlés");
            removeButton.addActionListener(ev -> {
                int idx = playerList.getSelectedIndex();
                if (idx >= 0) {
                    String selected = model.get(idx);
                    String name = selected.split(" ")[0];
                    gameLogic.removeSpecies(name);
                    model.remove(idx);
                    PlayerCountButton.setText("Játékosok száma: " + gameLogic.getPlayers().size());
                }
            });

            JButton saveButton = new JButton("Mentés és bezárás");
                saveButton.addActionListener(ev -> {
                    int fungusCount = 0;
                    int insectCount = 0;
                    for (int i = 0; i < model.size(); i++) {
                        String entry = model.get(i);
                        if (entry.contains("(FUNGUS)")) fungusCount++;
                        if (entry.contains("(INSECT)")) insectCount++;
                    }
                    if (fungusCount < 2 || insectCount < 2) {
                        JOptionPane.showMessageDialog(settingsFrame,
                            "Legalább 2 FUNGUS és 2 INSECT játékos szükséges!",
                            "Hiba", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    ((JDialog)SwingUtilities.getWindowAncestor(playerList)).dispose();
                });

            JPanel btnPanel = new JPanel();
            btnPanel.add(addButton);
            btnPanel.add(removeButton);
            btnPanel.add(saveButton);

            JDialog dialog = new JDialog(settingsFrame, "Játékosok szerkesztése", true);
            dialog.setSize(350, 400);
            dialog.setLayout(new BorderLayout());
            dialog.add(new JScrollPane(playerList), BorderLayout.CENTER);
            dialog.add(btnPanel, BorderLayout.SOUTH);
            dialog.setLocationRelativeTo(settingsFrame);
            dialog.setVisible(true);
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