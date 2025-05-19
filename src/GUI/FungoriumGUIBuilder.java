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
    private final SaveLoadHandler saveLoadHandler;


    // Színek 
    private Color[] fungusColors = {
            new Color(255, 182, 193), // Light Pink
            new Color(144, 238, 144), // Light Green
            new Color(55, 81, 250), // Light Blue
            new Color(221, 160, 221) // Light Purple
    };
    private Color[] insectColors = {
            new Color(255, 222, 173), // Light Beige
            new Color(222, 76, 73), // Light Red
            new Color(255, 204, 153), // Light Orange
            new Color(224, 255, 255) // Light Cyan
    };

    private final Color LIGHT_PINK   = fungusColors[0];
    private final Color LIGHT_GREEN  = fungusColors[1];
    private final Color LIGHT_BLUE   = fungusColors[2];
    private final Color LIGHT_PURPLE = fungusColors[3];
    private final Color LIGHT_BEIGE  = insectColors[0];
    private final Color LIGHT_RED    = insectColors[1];
    private final Color LIGHT_ORANGE = insectColors[2];
    private final Color LIGHT_CYAN   = insectColors[3];

    private HashMap<String, Color> playerColors = new HashMap<>();
    public FungoriumGamePanel gamePanel = null;

    public FungoriumGUIBuilder(GameLogic gameLogic, FungoriumGamePanel GP) {
        if(GP != null) {
            gamePanel = GP;
        }else {
            gamePanel = new FungoriumGamePanel(gameLogic, this);
        }
        this.gameLogic = gameLogic;
        this.saveLoadHandler = new SaveLoadHandler(gameLogic, gamePanel);
    }

    /**
     * Visszaadja a jelenlegi FungoriumGamePanel példányt.
     * @return a jelenlegi FungoriumGamePanel példány
     */
    public FungoriumGamePanel getGamePanel() {
        return gamePanel;
    }
    /**
     * Creates and shows the main GUI frame, using the given GameLogic
     * instance to get the game panel and set up the control panel.
     * <p>
     * First, a loading panel is displayed with a loading image and a
     * "Loading Fungorium..." label, while a separate thread is started
     * to load the game panel and set up the control panel. Once the
     * loading is complete, the loading panel is replaced with the game
     * panel and control panel in the main frame.
     * <p>
     * The game panel is set up with its control panel, and the first
     * player's name is set as the current player name, with their
     * corresponding color as the player border color.
     */
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
        frame.setResizable(false);
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
            gameLogic.setGamePanel(gamePanel);
            JPanel controlPanel = createControlPanel(frame, gamePanel);

            gamePanel.setControlPanel(controlPanel);

            SwingUtilities.invokeLater(() -> {
                frame.getContentPane().removeAll();
                frame.getContentPane().add(gamePanel, BorderLayout.CENTER);
                frame.getContentPane().add(controlPanel, BorderLayout.SOUTH);
                //frame.revalidate();
                //frame.repaint();
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

    /**
     * Returns the color associated with the given player name, or gray if there is no color associated with the player.
     * 
     * @param playerName The name of the player.
     * @return The color associated with the player, or gray.
     */
    public Color getPlayerColor(String playerName) {
        return playerColors.getOrDefault(playerName, Color.GRAY);
    }

/**
 * Retrieves the map of player names to their assigned colors.
 *
 * @return A HashMap where the keys are player names and the values are Colors assigned to each player.
 */

    public HashMap<String, Color> getPlayerColors() {
        return playerColors;
    }

    /**
     * Sets the player colors map to the provided map of player names and their associated colors.
     *
     * @param colors A HashMap where the keys are player names and the values are Colors assigned to each player.
     */

    public void setPlayerColors(HashMap<String, Color> colors) {
        this.playerColors = colors;
    }
    
    /**
     * Assigns colors to each player. If the number of players exceeds the number of colors, black is used.
     * 
     * @see #getPlayerColors()
     * @see #setPlayerColors(HashMap)
     */
    public void assignPlayerColors() {
        System.out.println("Assigning player colors...");
        playerColors.clear();
        Map<String, Object> players = gameLogic.getPlayers();
        if (players == null || players.isEmpty()) {
            System.out.println("No players found.");
            return;
        }
        int fungusIndex = 0;
        int insectIndex = 0;
        for (Map.Entry<String, Object> entry : players.entrySet()) {
            String name = entry.getKey();
            Color color;
            if (name.contains("Fungus")) {
                if (fungusIndex < fungusColors.length) {
                    color = fungusColors[fungusIndex++];
                } else {
                    System.out.println("Túl sok player, nincs ennyi szín az assignPlayerColors-ban.");
                    color = Color.BLACK;
                }
            } else if (name.contains("Insect")) {
                if (insectIndex < insectColors.length) {
                    color = insectColors[insectIndex++];
                } else {
                    System.out.println("Túl sok player, nincs ennyi szín az assignPlayerColors-ban.");
                    color = Color.BLACK;
                }
            } else {
                color = Color.GRAY;
            }
            playerColors.put(name, color);
            //System.out.println("Player: " + name + ", Color: " + color);
            //System.out.println("Ez a szín: " + getColorName(color));
        }
    }

    /**
     * Returns the name of the given color as a string, or "RGB(r,g,b)" if the color is not recognized.
     * 
     * @param color The color to get the name of.
     * @return The name of the given color.
     */
    private String getColorName(Color color) {
        if (color.equals(LIGHT_PINK))
            return "Light Pink";
        if (color.equals(LIGHT_BEIGE))
            return "Light Beige";
        if (color.equals(LIGHT_GREEN))
            return "Light Green";
        if (color.equals(LIGHT_RED))
            return "Light Red";
        if (color.equals(LIGHT_BLUE))
            return "Light Blue";
        if (color.equals(LIGHT_ORANGE))
            return "Light Orange";
        if (color.equals(LIGHT_PURPLE))
            return "Light Purple";
        if (color.equals(LIGHT_CYAN))
            return "Light Cyan";
        // Ha nem ismert, akkor RGB értékkel tér vissza
        return "RGB(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + ")";
    }

    /**
     * Creates a main frame for the game, with default settings.
     * @return A new JFrame for the game.
     */
    private JFrame createMainFrame() {
        JFrame frame = new JFrame("Fungorium Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ToolTipManager.sharedInstance().setInitialDelay(100);
        return frame;
    }

        /**
         * Creates a control panel with buttons for the player to control the game.
         * The panel contains buttons for the following actions: grow thread, grow body, sporulate, eat insect, cut thread, move, save, load, and end turn.
         * @param frame The frame to add the control panel to.
         * @param gamePanel The game panel to control.
         * @return A JPanel with the control buttons.
         */
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
            //panel.add(createLoadButton(gamePanel));

            return panel;
    }


    /**
     * Creates a button for the player to end their turn.
     * @param gamePanel The game panel to control.
     * @return A JButton with the end turn image and action listener.
     */
    private JButton createUpdateButton(FungoriumGamePanel gamePanel) {
        JButton button = createImageButton("src/resources/buttons/endTurn1.png", 110, 230);
        button.setToolTipText("End Turn");
        button.addActionListener(e -> gamePanel.endTurnLogic());
        return button;
    }

/**
 * Creates a button for saving the current game state.
 * The button displays a save icon and, when clicked, opens a file chooser dialog 
 * for the user to select a location to save the game. Upon confirmation, the game state
 * is saved to the selected file, and a message is displayed to the user.
 * 
 * @param gamePanel The game panel to which the button is associated.
 * @return A JButton configured to save the game state.
 */

    private JButton createSaveButton(FungoriumGamePanel gamePanel) {
        JButton button = createImageButton("src/resources/buttons/save1.png", 110, 230);
        button.setToolTipText("Save Game");
        button.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                String filename = fileChooser.getSelectedFile().getAbsolutePath();
                saveLoadHandler.saveGame(filename); // <-- EZT HASZNÁLD
                JOptionPane.showMessageDialog(gamePanel, "Game saved to: " + filename);
            }
        });
        return button;
    }

    // private JButton createLoadButton(FungoriumGamePanel gamePanel) {
    //     JButton button = createImageButton("src/resources/buttons/load.png", 64, 64);
    //     button.setToolTipText("Load Game");
    //     button.addActionListener(e -> {
    //         Map<String, Point> loadedPositions = saver.loadGameState("gameState.xml");
    //         gamePanel.setObjectPositions(loadedPositions);
    //         gamePanel.updateGameState();
    //         JOptionPane.showMessageDialog(gamePanel, "Game state loaded from gameState.xml");
    //     });
    //     return button;
    // }

    /**
     * Creates a JButton with an image from the given path, resized to the given width and height.
     * The button is configured with a margin of 0, and a light gray background.
     * @param path the path to the image file
     * @param width the width of the button
     * @param height the height of the button
     * @return a JButton with the image and the specified size
     */
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

    /**
     * Updates the action buttons in the given panel according to the game state.
     * The method first removes all existing buttons from the panel, then adds the
     * standard buttons (Update, Save, Load) and the action buttons, which are
     * enabled/disabled according to the game state.
     * If no objects are selected, all action buttons are disabled.
     * If the current player is the fungus, the action buttons are enabled/disabled
     * according to the selected object type (thread, body, spore, tekton, insect).
     * If the current player is an insect, the action buttons are enabled/disabled
     * according to the selected object type (tekton, insect, spore).
     * @param panel The panel in which the buttons are to be updated.
     * @param gamePanel The game panel to which the buttons are associated.
     */
    public void updateActionButtons(JPanel panel, FungoriumGamePanel gamePanel) {
        // Töröljük a régi gombokat
        panel.removeAll();
        panel.add(createUpdateButton(gamePanel));
        panel.add(createSaveButton(gamePanel));
        //panel.add(createLoadButton(gamePanel));
        // Itt a gombok létrehozásának és hozzáadásának logikája
        boolean threadPicked = false;
        boolean bodyPicked = false;
        boolean sporePicked = false;
        boolean tektonPicked = false;
        boolean insectPicked = false;
        boolean fungusTurn = true; // Tesztelés
        boolean insectsTurn = true; // Tesztelés
        boolean myTurn = false;
        if(gameLogic.getCurrentSpecies()!= null) {
            if(gameLogic.getCurrentSpecies().contains("Fungus")) {
                fungusTurn = true;
                insectsTurn = false;
            }
            else if(gameLogic.getCurrentSpecies().contains("Insect")) {
                insectsTurn = true;
                fungusTurn = false;
            }
        }else {
            fungusTurn = true; // Tesztelés
            insectsTurn = true; // Tesztelés
        }

        if (gamePanel.getSelectedObjects() != null && !gamePanel.getSelectedObjects().isEmpty()) {
            String objName = gamePanel.getSelectedObjects().get(0);
            if(gamePanel.getSelectedObjects().size() > 1) {
                objName = gamePanel.getSelectedObjects().get(1);
            }
            String status = gamePanel.getStatusText(objName);
            String[] lines = status.split("\\R"); // \R mindenféle sortörést kezel
            String secondLine = lines.length > 1 ? lines[1].trim() : "";
            if (secondLine.contains("Thread:")) {
                threadPicked = true;
            } else if (secondLine.contains("Body:")) {
                bodyPicked = true;
            } else if (secondLine.contains("Spore:")) {
                sporePicked = true;
            } else if (secondLine.contains("Tekton:")) {
                tektonPicked = true;
            } else if (secondLine.contains("Insect:")) {
                insectPicked = true;
            }
            if(status.contains(gameLogic.getCurrentSpecies())) {
                myTurn = true;
            }else {
                myTurn = false;
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
            //System.out.println(myTurn);
            if(myTurn){
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
        }
        if (insectsTurn) {
            growThreadButton.setVisible(false);
            growBodyButton.setVisible(false);
            eatInsectButton.setVisible(false);
            sporulateButton.setVisible(false);
            if(myTurn){
                if(insectPicked) {
                    cutThreadButton.setEnabled(true);
                    moveButton.setEnabled(true);
                    eatSporeButton.setEnabled(true);
                }
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


    /**
     * Returns the color of the given species as a string, based on the type of species and the color assigned to it.
     * The color is determined by the group of the species (Fungus or Insect) and the specific color assigned to it.
     * If the species is not recognized, the method prints an error message and returns "DID NOT MATCH".
     * 
     * @param species The species to get the color string for.
     * @return The color string for the given species.
     */
    public String getSpeciesStringColor(String species) {
        Color playerColor = playerColors.get(species);

        // Determine group
        String group = "Other";
        if(species != null){
            if (species.contains("Fungus")) {
                group = "Fungus";
            } else if (species.contains("Insect") && species != null) {
                group = "Insect";
            } 
        }else{
            System.out.println("Állítsd be a species-t, mert a getSpeciesStringColor-ban null: " + species);
        }

        // Map color to string for each group
        switch (group) {
            case "Fungus":
                if (playerColor.equals(LIGHT_PINK))
                    return "LightPink";
                if (playerColor.equals(LIGHT_GREEN))
                    return "LightGreen";
                if (playerColor.equals(LIGHT_BLUE))
                    return "LightBlue";
                if (playerColor.equals(LIGHT_PURPLE))
                    return "LightPurple";
                return "Fungus1";
            case "Insect":
                if (playerColor.equals(LIGHT_BEIGE))
                    return "LightBeige";
                if (playerColor.equals(LIGHT_CYAN))
                    return "LightCyan";
                if (playerColor.equals(LIGHT_ORANGE))
                    return "LightOrange";
                if (playerColor.equals(LIGHT_RED))
                    return "LightRed";
                return "Insect1";
            default:
                System.out.println("Unknown species: " + species);
                return "DID NOT MATCH";
        }
    }
}
