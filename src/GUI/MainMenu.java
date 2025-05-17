package GUI;

import javax.swing.*;

import logic.GameLogic;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.imageio.ImageIO;

public class MainMenu extends JFrame {

    protected GameLogic gameLogic;
    protected GameStateHandler saver;
    protected FungoriumGUIBuilder gameGUIBuilder;
    public MainMenu(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
        this.saver = new GameStateHandler(gameLogic);
        this.gameGUIBuilder = new FungoriumGUIBuilder(gameLogic, saver);
        setTitle("Fungorium - Főmenü");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        BackgroundPanel mainPanel = new BackgroundPanel("src/resources/PanelBg/menuPanel.jpg");
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Fungorium") {
        /**
         * Custom paintComponent method to draw the title text on the main menu
         * with a black outline to make it more visible.
         *
         * This method is called whenever the component needs to be redrawn.
         * It draws the title text of the main menu with a black outline to make
         * it more visible. The outline is drawn with a stroke of 2 pixels and
         * the text is drawn with the foreground color of the label.
         */
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawString(getText(), 2, getHeight() - 10);

                g2d.setColor(getForeground());
                g2d.drawString(getText(), 0, getHeight() - 12);

                g2d.dispose();
            }
        };
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        
        // Gombok panel
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10)); // 3 sor, 1 oszlop, kisebb térköz
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100)); // Margók a gombok körül
        buttonPanel.setOpaque(false);

        JButton newGameButton = createStyledButton("Új játék indítása");
        JButton rulesButton = createStyledButton("Játékszabályok");
        JButton settingsButton = createStyledButton("Beállítások");
        JButton exitButton = createStyledButton("Kilépés");

        buttonPanel.add(newGameButton);
        buttonPanel.add(rulesButton);
        buttonPanel.add(settingsButton);
        buttonPanel.add(exitButton);

        // Középre helyező panel
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(buttonPanel);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel);

        newGameButton.addActionListener(e -> startGameScreen());
        rulesButton.addActionListener(e -> showRules());
        settingsButton.addActionListener(e -> {
            Settings.showSettings(this,gameLogic); 
            dispose();
        });
        exitButton.addActionListener(e -> System.exit(0));
    }

    /**
     * Creates a styled JButton with the given text.
     * The button has a SansSerif font, white foreground, no focus painting, white background, 5px margin, and a preferred size of 250x40.
     * @param text the text to be displayed on the button
     * @return the created JButton
     */
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.PLAIN, 16));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setMargin(new Insets(5, 15, 5, 15));
        button.setBackground(new Color(30, 144, 255));
        button.setPreferredSize(new Dimension(250, 40)); 
        return button;
    }

/**
 * Opens the Choose Species screen and disposes of the current menu.
 * 
 * @param commandP The CommandProcessor instance used to handle game commands.
 */

    private void startGameScreen() {
        gameGUIBuilder.createAndShowGUI();
        dispose();
    }

    /**
     * Shows the game rules in a dialog box using a JTextArea within a JScrollPane.
     * The rules are loaded from the "src/GUI/DATA/GameRules.txt" file.
     * If the file cannot be read, an error message dialog box is shown.
     */
    private void showRules() {
        String currentWorkingDirectory = System.getProperty("user.dir");
        System.out.println("Current Working Directory: " + currentWorkingDirectory);

        StringBuilder rulesText = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/GUI/DATA/GameRules.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                rulesText.append(line).append("\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Hiba történt a játékszabályok fájl beolvasása közben!\nKeresett hely: " + currentWorkingDirectory + "\\GameRules.txt", "Hiba", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        JTextArea textArea = new JTextArea(rulesText.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(350, 250));

        JOptionPane.showMessageDialog(this, scrollPane, "Játékszabályok", JOptionPane.INFORMATION_MESSAGE);
    }

    private static class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            try {
                backgroundImage = ImageIO.read(new File(imagePath));
            } catch (IOException e) {
                System.err.println("Hiba a háttérkép betöltése közben: " + e.getMessage());
            }
        }

        /**
         * Custom paintComponent method to draw the background image on the panel.
         * 
         * This method is called whenever the component needs to be redrawn.
         * It draws the background image of the panel with its current size.
         * If the background image is null, it does nothing.
         * 
         * @param g The Graphics object to draw on.
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
}