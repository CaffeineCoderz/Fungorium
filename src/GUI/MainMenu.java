package GUI;

import javax.swing.*;

import commands.CommandProcessor;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.imageio.ImageIO;

public class MainMenu extends JFrame {

    protected CommandProcessor commandProcessor;
    public MainMenu(CommandProcessor commandP) {
        commandProcessor = commandP;
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

        newGameButton.addActionListener(e -> openChooseSpeciesScreen(commandP));
        rulesButton.addActionListener(e -> showRules());
        settingsButton.addActionListener(e -> Settings.showSettings(this));
        exitButton.addActionListener(e -> System.exit(0));
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.PLAIN, 16));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setMargin(new Insets(5, 15, 5, 15));
        button.setBackground(new Color(30, 144, 255));
        return button;
    }

    private void openChooseSpeciesScreen(CommandProcessor commandP) {
        FungoriumGamePanel.createAndShowGUI(commandP);
        dispose();
    }

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

    public static void main(String[] args) {
        CommandProcessor commandProcessor = new CommandProcessor();
        SwingUtilities.invokeLater(() -> new MainMenu(commandProcessor).setVisible(true));
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

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
}