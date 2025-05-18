package GUI;
import javax.swing.*;
import logic.GameLogic;
import java.awt.*;

public class Settings {

    public static void showSettings(JFrame parent, GameLogic gameLogic) {
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
        JButton PlayerCountButton = createStyledButton("Jatékosok száma: " + gameLogic.getPlayersCount());
        JButton SetTypeAndNameButton = createStyledButton("Játekosok típusa és neve");
        JButton LoadButton = createStyledButton("Load Game");
        JButton Rounds = createStyledButton("Körök száma: " + gameLogic.getGameTime());
        JButton MapSize = createStyledButton("Pálya mérete: " + gameLogic.getMapSize());
        // JButton button5 = createStyledButton("Gomb 5");
        // JButton button6 = createStyledButton("Gomb 6");

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

        gbc.gridx = 1;
        gbc.gridy = 1;
        buttonPanel.add(Rounds, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        buttonPanel.add(MapSize, gbc);

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
            
            String input = JOptionPane.showInputDialog(settingsFrame, "Add meg a játékosok számát(4-8):", gameLogic.getPlayersCount());
            int playersCount = Integer.parseInt(input.trim());
            if(playersCount < 4 || playersCount > 8) {
                JOptionPane.showMessageDialog(settingsFrame, "A játékosok száma legyen 4 és 8 között!", "Hiba", JOptionPane.ERROR_MESSAGE);
                return;
            }
            gameLogic.setPlayersCount(playersCount);
            PlayerCountButton.setText("Játékosok száma: " + playersCount);
        });

        Rounds.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(settingsFrame, "Add meg a körök számát:", gameLogic.getGameTime());
            if (input != null) {
                try {
                    int newRounds = Integer.parseInt(input.trim());
                    if (newRounds > 0 && newRounds <= 40) {
                        gameLogic.setGameTime(newRounds);
                        Rounds.setText("Körök száma: " + newRounds);
                    } else {
                        JOptionPane.showMessageDialog(settingsFrame, "A körök száma legyen pozitív egész szám 0 és 40 között!", "Hiba", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(settingsFrame, "Érvénytelen szám!", "Hiba", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        MapSize.addActionListener(e -> {
            String[] options = {"small", "medium", "large"};
            String current = gameLogic.getMapSize().name().toLowerCase();
            String selected = (String) JOptionPane.showInputDialog(
                settingsFrame,
                "Válaszd ki a pályaméretet:",
                "Pályaméret",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                current
            );
            if (selected != null) {
                RenderMap.MapSize chosen = RenderMap.MapSize.valueOf(selected.toUpperCase());
                gameLogic.setMapSize(chosen);
                MapSize.setText("Pályaméret: " + selected);
            }
        });

        LoadButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
                String filename = fileChooser.getSelectedFile().getAbsolutePath();
                SaveLoadHandler saveLoadHandler = new SaveLoadHandler(gameLogic);
                saveLoadHandler.loadGame(filename);

                // Játék GUI indítása betöltés után:
                settingsFrame.dispose(); // Bezárja a Settings ablakot
                FungoriumGUIBuilder builder = new FungoriumGUIBuilder(gameLogic, null);
                builder.createAndShowGUI();

                JOptionPane.showMessageDialog(parent, "Játék betöltve: " + filename);
            }
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
                        gameLogic.handleSpeciesCreation(type.equals("FUNGUS") ? "Fungus" : "Insect", name);
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
                    if (fungusCount+insectCount > 8) {
                        JOptionPane.showMessageDialog(settingsFrame,
                            "Maximum 8 játékos engedélyezett!",
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