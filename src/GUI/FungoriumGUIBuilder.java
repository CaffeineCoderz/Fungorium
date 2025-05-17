package GUI;

import javax.swing.*;

import logic.GameLogic;

import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
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
            JPanel controlPanel = createControlPanel(frame, gamePanel);

            gamePanel.setControlPanel(controlPanel);

            SwingUtilities.invokeLater(() -> {
                frame.getContentPane().removeAll();
                frame.getContentPane().add(gamePanel, BorderLayout.CENTER);
                frame.getContentPane().add(controlPanel, BorderLayout.SOUTH);
                frame.revalidate();
                frame.repaint();
                frame.pack();
            });
        }).start();
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
            growThreadButton.addActionListener(e -> gamePanel.growThreadLogic());
            growBodyButton = createImageButton("src/resources/buttons/growBody1.png", 130, 260);
            growBodyButton.setToolTipText("Grow Body");
            growBodyButton.addActionListener(e -> gamePanel.growBodyLogic());
            sporulateButton = createImageButton("src/resources/buttons/sporulate1.png", 130, 260);
            sporulateButton.setToolTipText("Sporulate");
            sporulateButton.addActionListener(e -> gamePanel.sporulateLogic());
            eatInsectButton = createImageButton("src/resources/buttons/eatInsect1.png", 110, 240);
            eatInsectButton.setToolTipText("Eat Insect");
            eatInsectButton.addActionListener(e -> gamePanel.eatInsectLogic());
            cutThreadButton = createImageButton("src/resources/buttons/cutThread1.png", 110, 220);
            cutThreadButton.setToolTipText("Cut Thread");
            cutThreadButton.addActionListener(e -> gamePanel.cutThreadLogic());
            moveButton = createImageButton("src/resources/buttons/move1.png", 110, 220);
            moveButton.setToolTipText("Move");
            moveButton.addActionListener(e -> gamePanel.moveLogic());
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


        if (gamePanel.getSelectedObjects() != null && !gamePanel.getSelectedObjects().isEmpty()) {
            String objName = gamePanel.getSelectedObjects().get(0);
            String status = gamePanel.getStatusText(objName);
            if (status.contains("Thread:")) {
                threadPicked = true;
            } else if (status.contains("Body:")) {
                bodyPicked = true;
            } else if (status.contains("Spore:")) {
                sporePicked = true;
            } else if (status.contains("Tekton:")) {
                tektonPicked = true;
            } else if (status.contains("Insect:")) {
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
            if (threadPicked) {
                growThreadButton.setEnabled(true);
                growBodyButton.setEnabled(true);
                eatInsectButton.setEnabled(true);
            }
            if (bodyPicked) {
                growThreadButton.setEnabled(true);
                sporulateButton.setEnabled(true);
            }
            else if(!bodyPicked && !threadPicked) {
                growThreadButton.setEnabled(false);
                sporulateButton.setEnabled(false);
                growBodyButton.setEnabled(false);
                eatInsectButton.setEnabled(false);
            }
        }
        if (insectsTurn) {
            growThreadButton.setVisible(false);
            growBodyButton.setVisible(false);
            eatInsectButton.setVisible(false);
            sporulateButton.setVisible(false);
            System.out.println(insectPicked);
            if(insectPicked) {
                cutThreadButton.setEnabled(true);
                moveButton.setEnabled(true);
                eatSporeButton.setEnabled(true);
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
