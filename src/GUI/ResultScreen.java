package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

class ResultScreen extends JFrame {

    private BufferedImage crownImage;
    private List<String> fungusWinners;
    private List<String> insectWinners;

    public ResultScreen() {
        loadWinnersFromFile("src/GUI/DATA/winners.txt");

        setTitle("Fungorium - Eredmények");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Ablak maximalizálása induláskor
        setResizable(true);

        try {
            crownImage = ImageIO.read(new File("src/GUI/DATA/crown.png"));
        } catch (IOException e) {
            System.err.println("Hiba a korona kép betöltése közben: " + e.getMessage());
            crownImage = null;
        }

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(30, 30, 45));
                g2d.fillRect(0, 0, getWidth(), getHeight());

                int crownWidth = 80;
                int crownHeight = 80;
                if (crownImage != null) {
                    int crownX = getWidth() / 2 - crownWidth / 2;
                    int crownY = 30;
                    g2d.drawImage(crownImage, crownX, crownY, crownWidth, crownHeight, this);
                }

                g2d.setFont(new Font("Arial", Font.BOLD, 36));
                g2d.setColor(new Color(255, 204, 0));
                String titleText = "Győztesek";
                FontMetrics fmTitle = g2d.getFontMetrics();
                int titleX = getWidth() / 2 - fmTitle.stringWidth(titleText) / 2;
                int titleY = crownImage != null ? 30 + crownHeight + 40 : 80;
                g2d.drawString(titleText, titleX, titleY);

                g2d.setFont(new Font("Arial", Font.BOLD, 24));
                g2d.setColor(Color.WHITE);
                int startY = titleY + 60;
                int lineHeight = 30;
                int leftStartX = getWidth() / 4 - 50;
                int rightStartX = getWidth() * 3 / 4 - 50;

                g2d.drawString("FungusSpecies:", leftStartX, startY);
                for (int i = 0; i < fungusWinners.size(); i++) {
                    g2d.drawString((i + 1) + ". " + fungusWinners.get(i), leftStartX, startY + (i + 1) * lineHeight);
                }

                g2d.drawString("InsectSpecies:", rightStartX, startY);
                for (int i = 0; i < insectWinners.size(); i++) {
                    g2d.drawString((i + 1) + ". " + insectWinners.get(i), rightStartX, startY + (i + 1) * lineHeight);
                }

                g2d.dispose();
            }
        };
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        JButton backToMenuButton = new JButton("Vissza a főmenübe");
        styleButton(backToMenuButton);
        buttonPanel.add(backToMenuButton);
        add(buttonPanel, BorderLayout.SOUTH);

        backToMenuButton.addActionListener((ActionEvent e) -> {
            new MainMenu().setVisible(true);
            dispose();
        });
    }

    private void loadWinnersFromFile(String filePath) {
        fungusWinners = new ArrayList<>();
        insectWinners = new ArrayList<>();
        boolean readingFungus = false;
        boolean readingInsect = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.equalsIgnoreCase("<fungusspecies>")) {
                    readingFungus = true;
                    readingInsect = false;
                } else if (line.equalsIgnoreCase("<insectspecies>")) {
                    readingInsect = true;
                    readingFungus = false;
                } else if (readingFungus && !line.isEmpty()) {
                    fungusWinners.add(line);
                } else if (readingInsect && !line.isEmpty()) {
                    insectWinners.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Hiba a nyertesek fájl beolvasása közben: " + e.getMessage());
            // Kezeld a hibát megfelelően, pl. üres listák beállítása
        }
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("SansSerif", Font.BOLD, 18));
        button.setBackground(new Color(30, 144, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new RoundedBorder(10));
        button.setMargin(new Insets(8, 16, 8, 16));
    }

    private static class RoundedBorder implements javax.swing.border.Border {
        private int radius;

        public RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius / 2, radius / 2, radius / 2, radius / 2);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(Color.BLACK);
            g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2d.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ResultScreen());
    }
}