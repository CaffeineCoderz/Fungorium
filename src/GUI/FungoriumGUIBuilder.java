package GUI;

import javax.swing.*;

import logic.GameLogic;

import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;


public class FungoriumGUIBuilder {

    private final GameLogic gameLogic;
    private static GameStateHandler saver;
    private JButton growThreadButton;
    private JButton growBodyButton;
    private JButton sporulateButton;
    private JButton eatInsectButton;
    private JButton cutThreadButton;
    private JButton moveButton;
    private JButton eatSporeButton;

    private Color[] colors = {
        new Color(255, 182, 193), // Light Pink
        new Color(144, 238, 144), // Light Green
        new Color(173, 216, 230), // Light Blue
        new Color(255, 255, 153), // Light Yellow
        new Color(255, 204, 153), // Light Orange
        new Color(221, 160, 221), // Plum (Light Purple)
        new Color(224, 255, 255), // Light Cyan
        new Color(255, 222, 173)  // Navajo White (Light Beige)
    };
    private HashMap<String, Color> playerColors = new HashMap<>();

    public FungoriumGUIBuilder(GameLogic gameLogic, GameStateHandler saver) {
        this.gameLogic = gameLogic;
        this.saver = saver;
    }

    public void createAndShowGUI() {
        JFrame frame = createMainFrame();

        // Panel overlay layouttal
        JPanel loadingPanel = new JPanel();
        loadingPanel.setLayout(new OverlayLayout(loadingPanel));
        loadingPanel.setPreferredSize(new Dimension(800, 800));

        // Szöveg
        JLabel loadingLabel = new JLabel("Loading Fungorium...");
        loadingLabel.setFont(new Font("Arial", Font.BOLD, 32));
        loadingLabel.setForeground(Color.WHITE);
        loadingLabel.setOpaque(false); // Nincs háttér
        loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loadingLabel.setVerticalAlignment(SwingConstants.CENTER);
        loadingLabel.setAlignmentX(0.5f);
        loadingLabel.setAlignmentY(0.5f);
        loadingPanel.add(loadingLabel);

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(loadingPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Kép
        try {
            BufferedImage loadingImage = ImageIO.read(new File("src/resources/loading_screen.png"));
            JLabel imageLabel = new JLabel(new ImageIcon(loadingImage.getScaledInstance(800, 800, Image.SCALE_SMOOTH)));
            imageLabel.setAlignmentX(0.5f);
            imageLabel.setAlignmentY(0.5f);
            imageLabel.setOpaque(false); // Átlátszó legyen, ha szükséges
            loadingPanel.add(imageLabel);
        } catch (IOException e) {
            System.err.println("Could not load loading image.");
        }

        // Háttérszál a betöltéshez
        new Thread(() -> {
            FungoriumGamePanel gamePanel = new FungoriumGamePanel(gameLogic, this);
            gameLogic.setGamePanel(gamePanel);
            JPanel controlPanel = createControlPanel(frame, gamePanel);

            gamePanel.setControlPanel(controlPanel);

            SwingUtilities.invokeLater(() -> {
                frame.getContentPane().removeAll();
                frame.getContentPane().add(gamePanel, BorderLayout.CENTER);
                frame.getContentPane().add(controlPanel, BorderLayout.SOUTH);
                frame.revalidate();
                frame.repaint();
                frame.pack();

                // Itt már biztosan létezik a gamePanel!
                Map<String, Object> players = gameLogic.getPlayers();
                if (!players.isEmpty()) {
                    String firstPlayer = players.keySet().iterator().next();
                    Color color = getPlayerColor(firstPlayer);
                    gamePanel.setCurrentPlayerName(firstPlayer);
                    gamePanel.setPlayerBorderColor(color);
                }
            });
        }).start();
    }

    public Color getPlayerColor(String playerName) {
        return playerColors.getOrDefault(playerName, Color.GRAY);
    }

    public void assignPlayerColors() {
        System.out.println("Assigning player colors...");
        playerColors.clear();
        Map<String, Object> players = gameLogic.getPlayers();
        if (players == null || players.isEmpty()) {
            System.out.println("No players found.");
            return;
        }
        int colorIndex = 0;
        for (Map.Entry<String, Object> entry : players.entrySet()) {
            if (playerColors.size() < colors.length) {
                playerColors.put(entry.getKey(), colors[colorIndex++]);
                System.out.println("Player: " + entry.getKey() + ", Color: " + colors[colorIndex - 1]);
            } else {
                playerColors.put(entry.getKey(), Color.BLACK);
            }
        }
    }


    private JFrame createMainFrame() {
        JFrame frame = new JFrame("Fungorium Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ToolTipManager.sharedInstance().setInitialDelay(100);
        return frame;
    }

    private JPanel createControlPanel(JFrame frame, FungoriumGamePanel gamePanel) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            panel.setBackground(Color.LIGHT_GRAY);

            growThreadButton = createImageButton("src/resources/buttons/growThread1.png", 140, 260);
            growThreadButton.setToolTipText("Grow Thread");
            growBodyButton = createImageButton("src/resources/buttons/growBody1.png", 130, 260);
            growBodyButton.setToolTipText("Grow Body");
            sporulateButton = createImageButton("src/resources/buttons/sporulate1.png", 130, 260);
            sporulateButton.setToolTipText("Sporulate");
            eatInsectButton = createImageButton("src/resources/buttons/eatInsect1.png", 110, 240);
            eatInsectButton.setToolTipText("Eat Insect");
            cutThreadButton = createImageButton("src/resources/buttons/cutThread1.png", 110, 220);
            cutThreadButton.setToolTipText("Cut Thread");
            moveButton = createImageButton("src/resources/buttons/move1.png", 110, 220);
            moveButton.setToolTipText("Move");
            eatSporeButton = createImageButton("src/resources/buttons/eat1.png", 110, 220);
            eatSporeButton.setToolTipText("Eat Spore");

            panel.add(createUpdateButton(gamePanel));
            panel.add(createSaveButton(gamePanel));
            panel.add(createLoadButton(gamePanel));

            return panel;
    }


    private JButton createUpdateButton(FungoriumGamePanel gamePanel) {
        JButton button = createImageButton("src/resources/buttons/endTurn1.png", 110, 230);
        button.setToolTipText("End Turn");
        button.addActionListener(e -> gamePanel.endTurnLogic());
        return button;
    }

    private JButton createSaveButton(FungoriumGamePanel gamePanel) {
        JButton button = createImageButton("src/resources/buttons/save1.png", 110, 230);
        button.setToolTipText("Save Game");
        button.addActionListener(e -> {
            saver.saveGameState(gamePanel.getObjectPositions(), "gameState.xml");
            JOptionPane.showMessageDialog(gamePanel, "Game state saved to gameState.xml");
        });
        return button;
    }

    private JButton createLoadButton(FungoriumGamePanel gamePanel) {
        JButton button = createImageButton("src/resources/buttons/load.png", 64, 64);
        button.setToolTipText("Load Game");
        button.addActionListener(e -> {
            Map<String, Point> loadedPositions = saver.loadGameState("gameState.xml");
            gamePanel.setObjectPositions(loadedPositions);
            gamePanel.updateGameState();
            JOptionPane.showMessageDialog(gamePanel, "Game state loaded from gameState.xml");
        });
        return button;
    }

    private JButton createImageButton(String path, int width, int height) {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(64, 64));
        try {
            Image img = ImageIO.read(new File(path));
            button.setIcon(new ImageIcon(img.getScaledInstance(width, height, Image.SCALE_SMOOTH)));
        } catch (IOException e) {
            System.out.println("Error loading image: " + path);
        }
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setBackground(Color.LIGHT_GRAY);
        return button;
    }

    public void updateActionButtons(JPanel panel, FungoriumGamePanel gamePanel) {
        // Töröljük a régi gombokat
        panel.removeAll();
        panel.add(createUpdateButton(gamePanel));
        panel.add(createSaveButton(gamePanel));
        panel.add(createLoadButton(gamePanel));
        // Itt a gombok létrehozásának és hozzáadásának logikája
        boolean threadPicked = false;
        boolean bodyPicked = false;
        boolean sporePicked = false;
        boolean tektonPicked = false;
        boolean insectPicked = false;
        boolean fungusTurn = true; // Tesztelés
        boolean insectsTurn = true; // Tesztelés
        if(gameLogic.getCurrentSpecies()!= null) {
            if(gameLogic.getCurrentSpecies().equals("Fungus")) {
                fungusTurn = true;
                insectsTurn = false;
            }
            else if(gameLogic.getCurrentSpecies().equals("Insect")) {
                insectsTurn = true;
                fungusTurn = false;
            }
        }else {
            fungusTurn = true; // Tesztelés
            insectsTurn = true; // Tesztelés
        }

        if (gamePanel.pickedObject != null) {
            if (gamePanel.pickedObject.contains("Thread:")) {
                threadPicked = true;
            } else if (gamePanel.pickedObject.contains("Body:")) {
                bodyPicked = true;
            } else if (gamePanel.pickedObject.contains("Spore:")) {
                sporePicked = true;
            } else if (gamePanel.pickedObject.contains("Tekton:")) {
                tektonPicked = true;
            } else if (gamePanel.pickedObject.contains("Insect:")) {
                insectPicked = true;
            }
        }
        // Láthatósági logika – ide mehet külön osztály vagy állapotkezelő
        growThreadButton.setEnabled(false);
        growBodyButton.setEnabled(false);
        sporulateButton.setEnabled(false);
        eatInsectButton.setEnabled(false);
        cutThreadButton.setEnabled(false);
        moveButton.setEnabled(false);
        eatSporeButton.setEnabled(false);

        cutThreadButton.setVisible(true);
        moveButton.setVisible(true);
        eatSporeButton.setVisible(true);
        growThreadButton.setVisible(true);
        growBodyButton.setVisible(true);
        eatInsectButton.setVisible(true);
        sporulateButton.setVisible(true);

        if (fungusTurn) {
            cutThreadButton.setVisible(false);
            moveButton.setVisible(false);
            eatSporeButton.setVisible(false);
            if (threadPicked || tektonPicked) {
                growThreadButton.setEnabled(true);
                growBodyButton.setEnabled(true);
                eatInsectButton.setEnabled(true);
            }
            if (bodyPicked) {
                growThreadButton.setEnabled(true);
                sporulateButton.setEnabled(true);
            }
            if (sporePicked) {
                growThreadButton.setEnabled(true);
                growBodyButton.setEnabled(true);
            }
            if (insectPicked) {
                eatInsectButton.setEnabled(true);
            }
        }
        if (insectsTurn) {
            growThreadButton.setVisible(false);
            growBodyButton.setVisible(false);
            eatInsectButton.setVisible(false);
            sporulateButton.setVisible(false);
            if(insectPicked || tektonPicked) {
                cutThreadButton.setEnabled(true);
                moveButton.setEnabled(true);
                eatSporeButton.setEnabled(true);
            }
            if (sporePicked) {
                eatSporeButton.setEnabled(true);
            }
            if (bodyPicked) {
                moveButton.setEnabled(true);
            }
            if (threadPicked) {
                cutThreadButton.setEnabled(true);
                moveButton.setEnabled(true);
            }
        }

        panel.add(growThreadButton);
        panel.add(growBodyButton);
        panel.add(sporulateButton);
        panel.add(eatInsectButton);
        panel.add(cutThreadButton);
        panel.add(moveButton);
        panel.add(eatSporeButton);

        panel.revalidate();
        panel.repaint();
    }
}
