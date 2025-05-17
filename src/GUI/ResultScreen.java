package GUI;
import javax.swing.*;

import commands.CommandProcessor;
import fungus.FungusSpecies;
import insect.InsectSpecies;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class ResultScreen extends JFrame {
    public ResultScreen(CommandProcessor commandProcessor) {
        List<FungusSpecies> fungusList = new ArrayList<>();
        List<InsectSpecies> insectList = new ArrayList<>();

        // Szétválogatás és gyűjtés
        for (Object obj : commandProcessor.getCreatedObjects().values()) {
            if (obj instanceof FungusSpecies) {
                fungusList.add((FungusSpecies) obj);
                System.out.println("Fungus bekerült: " + commandProcessor.findByObject(obj) + " pont: " + ((FungusSpecies)obj).getScore());
            }
            if (obj instanceof InsectSpecies) {
                insectList.add((InsectSpecies) obj);
                System.out.println("Insect bekerült: " + commandProcessor.findByObject(obj) + " pont: " + ((InsectSpecies)obj).getScore());
            }
        }

        // Rendezzük pontszám szerint csökkenőbe
        fungusList.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));
        insectList.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));

        // Csak az első 3-at vesszük (ha van annyi)
        List<FungusSpecies> topFungus = fungusList.subList(0, Math.min(3, fungusList.size()));
        List<InsectSpecies> topInsect = insectList.subList(0, Math.min(3, insectList.size()));

        setTitle("Fungorium - Eredmények");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(30, 30, 45));

        // Cím és kép panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);

        // Korona kép
        JLabel crownLabel = new JLabel();
        crownLabel.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            ImageIcon crownIcon = new ImageIcon("src/resources/crown.png");
            crownLabel.setIcon(new ImageIcon(crownIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
        } catch (Exception e) {
            System.err.println("Hiba a korona kép betöltése közben: " + e.getMessage());
        }
        titlePanel.add(crownLabel, BorderLayout.NORTH);

        // Cím
        JLabel titleLabel = new JLabel("Győztesek", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(new Color(255, 204, 0));
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Nyertesek fő panel
        JPanel winnersPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 0)); // FlowLayout a két oszlop egymás mellé rendezéséhez
        winnersPanel.setOpaque(false);

        // Fungus nyertesek panel
        JPanel fungusPanel = new JPanel(new BorderLayout()); // BorderLayout a címke és a nevek függőleges elrendezéséhez
        fungusPanel.setOpaque(false);
        JLabel fungusTitle = new JLabel("FungusSpecies", SwingConstants.CENTER);
        fungusTitle.setFont(new Font("Arial", Font.BOLD, 24));
        fungusTitle.setForeground(Color.lightGray);
        fungusPanel.add(fungusTitle, BorderLayout.NORTH);

        JPanel fungusWinnersSubPanel = new JPanel();
        fungusWinnersSubPanel.setLayout(new BoxLayout(fungusWinnersSubPanel, BoxLayout.Y_AXIS));
        fungusWinnersSubPanel.setOpaque(false);

        // --- Fungus nyertesek panel ---
        for (int i = 0; i < fungusList.size(); i++) {
            FungusSpecies fs = fungusList.get(i);
            String name = commandProcessor.findByObject(fs);
            JLabel winnerLabel = new JLabel((i+1) + ". " + name + " (" + fs.getScore() + " pont)");
            styleWinnerLabel(winnerLabel, i);
            winnerLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Középre igazítás
            fungusWinnersSubPanel.add(winnerLabel);
            if (i < fungusList.size() - 1) {
                fungusWinnersSubPanel.add(Box.createVerticalStrut(18));
            }
        }
        
        fungusPanel.add(fungusWinnersSubPanel, BorderLayout.CENTER);
        winnersPanel.add(fungusPanel);

        // Insect nyertesek panel
        JPanel insectPanel = new JPanel(new BorderLayout()); 
        insectPanel.setOpaque(false);
        JLabel insectTitle = new JLabel("InsectSpecies", SwingConstants.CENTER);
        insectTitle.setFont(new Font("Arial", Font.BOLD, 24));
        insectTitle.setForeground(Color.lightGray);
        insectPanel.add(insectTitle, BorderLayout.NORTH);

        JPanel insectWinnersSubPanel = new JPanel();
        insectWinnersSubPanel.setLayout(new BoxLayout(insectWinnersSubPanel, BoxLayout.Y_AXIS));
        insectWinnersSubPanel.setOpaque(false);

        // --- Insect nyertesek panel ---
        for (int i = 0; i < insectList.size(); i++) {
            InsectSpecies is = insectList.get(i);
            String name = commandProcessor.findByObject(is);
            JLabel winnerLabel = new JLabel((i+1) + ". " + name + " (" + is.getScore() + " pont)");
            styleWinnerLabel(winnerLabel, i);
            winnerLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Középre igazítás
            insectWinnersSubPanel.add(winnerLabel);
            if (i < insectList.size() - 1) {
                insectWinnersSubPanel.add(Box.createVerticalStrut(18));
            }
        }

        insectPanel.add(insectWinnersSubPanel, BorderLayout.CENTER);
        winnersPanel.add(insectPanel);

        // Hozzáadás a fő panelhez
        mainPanel.add(winnersPanel, BorderLayout.CENTER);

        // Vissza a főmenübe gomb
        JButton backToMenuButton = new JButton("Vissza a főmenübe");
        styleButton(backToMenuButton);
        backToMenuButton.addActionListener(e -> {
            //new MainMenu(commandP).setVisible(true);
            dispose();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(backToMenuButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("SansSerif", Font.BOLD, 18));
        button.setBackground(new Color(30, 144, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setMargin(new Insets(8, 16, 8, 16));
    }

    private void styleWinnerLabel(JLabel label, int index) {
        if (index == 0) {
            label.setFont(new Font("Arial", Font.BOLD, 28));
            label.setForeground(Color.white);
        } else {
            label.setFont(new Font("Arial", Font.PLAIN, 20));
            label.setForeground(Color.LIGHT_GRAY);
        }
        label.setHorizontalAlignment(SwingConstants.CENTER);
    }
}