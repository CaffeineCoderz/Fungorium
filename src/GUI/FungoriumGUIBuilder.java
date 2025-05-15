package GUI;

import javax.swing.*;

import logic.GameLogic;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Flow;

import javax.imageio.ImageIO;

public class FungoriumGUIBuilder {

    private final GameLogic gameLogic;
    private static GameStateHandler saver;

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
            FungoriumGamePanel gamePanel = new FungoriumGamePanel(gameLogic);
            JPanel controlPanel = createControlPanel(frame, gamePanel);

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

        panel.add(createUpdateButton(gamePanel));
        panel.add(createSaveButton(frame, gamePanel));
        panel.add(createLoadButton(frame, gamePanel));

        addActionButtons(panel, gamePanel);

        return panel;
    }

    private JButton createUpdateButton(FungoriumGamePanel gamePanel) {
        JButton button = new JButton("Update View");
        button.addActionListener(e -> gamePanel.updateGameState());
        return button;
    }

    private JButton createSaveButton(JFrame frame, FungoriumGamePanel gamePanel) {
        JButton button = createImageButton("src/resources/buttons/save1.png", 110, 230);
        button.setToolTipText("Save Game");
        button.addActionListener(e -> {
            saver.saveGameState(gamePanel.getObjectPositions(), "gameState.xml");
            JOptionPane.showMessageDialog(frame, "Game state saved to gameState.xml");
        });
        return button;
    }

    private JButton createLoadButton(JFrame frame, FungoriumGamePanel gamePanel) {
        JButton button = createImageButton("src/resources/buttons/load.png", 64, 64);
        button.setToolTipText("Load Game");
        button.addActionListener(e -> {
            Map<String, Point> loadedPositions = saver.loadGameState("gameState.xml");
            gamePanel.setObjectPositions(loadedPositions);
            gamePanel.updateGameState();
            JOptionPane.showMessageDialog(frame, "Game state loaded from gameState.xml");
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

    private void addActionButtons(JPanel panel, FungoriumGamePanel gamePanel) {
        // Itt ideiglenes logika van (fixen insectPicked = true), de ezt a logikát érdemes kiszervezni
        boolean threadPicked = false;
        boolean bodyPicked = false;
        boolean sporePicked = false;
        boolean tektonPicked = false;
        boolean insectPicked = false;
        boolean fungusTurn = true;
        boolean insectsTurn = false;

        JButton growThreadButton = createImageButton("src/resources/buttons/growThread1.png", 140, 260);
        growThreadButton.setToolTipText("Grow Thread");
        panel.add(growThreadButton);

        JButton growBodyButton = createImageButton("src/resources/buttons/growBody1.png", 130, 260);
        growBodyButton.setToolTipText("Grow Body");
        panel.add(growBodyButton);

        JButton sporulateButton = createImageButton("src/resources/buttons/sporulate1.png", 130, 260);
        sporulateButton.setToolTipText("Sporulate");
        panel.add(sporulateButton);

        JButton eatInsectButton = createImageButton("src/resources/buttons/eatInsect1.png", 110, 240);
        eatInsectButton.setToolTipText("Eat Insect");
        panel.add(eatInsectButton);

        JButton cutThreadButton = createImageButton("src/resources/buttons/cutThread1.png", 110, 220);
        cutThreadButton.setToolTipText("Cut Thread");
        panel.add(cutThreadButton);

        JButton moveButton = createImageButton("src/resources/buttons/move1.png", 110, 220);
        moveButton.setToolTipText("Move");
        panel.add(moveButton);

        JButton eatSporeButton = createImageButton("src/resources/buttons/eat1.png", 110, 220);
        eatSporeButton.setToolTipText("Eat Spore");
        panel.add(eatSporeButton);

        // Láthatósági logika – ide mehet külön osztály vagy állapotkezelő
        growThreadButton.setVisible(false);
        growBodyButton.setVisible(false);
        sporulateButton.setVisible(false);
        eatInsectButton.setVisible(false);

        if (fungusTurn) {
            if (threadPicked) {
                growThreadButton.setVisible(true);
                growBodyButton.setVisible(true);
                eatInsectButton.setVisible(true);
            } else if (bodyPicked) {
                growThreadButton.setVisible(true);
                sporulateButton.setVisible(true);
            } else if (sporePicked || tektonPicked) {
                growThreadButton.setVisible(true);
                growBodyButton.setVisible(true);
                eatInsectButton.setVisible(tektonPicked);
            } else if (insectPicked) {
                eatInsectButton.setVisible(true);
            }
        } else if (insectsTurn) {
            cutThreadButton.setVisible(true);
            moveButton.setVisible(true);
            eatSporeButton.setVisible(sporePicked || tektonPicked || insectPicked);
        }

        panel.add(cutThreadButton);
        panel.add(moveButton);
        panel.add(eatSporeButton);
    }
}
