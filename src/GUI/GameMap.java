package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class GameMapScreen extends JFrame {

    public GameMapScreen() {

        setTitle("Fungorium - Játéktérkép");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Csak ezt az ablakot zárja be
        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();

        // menu
        JMenu fileMenu = new JMenu("Fájl");
        JMenuItem backToMainMenuItem = new JMenuItem("Vissza a főmenübe");
        JMenuItem exitGameItem = new JMenuItem("Kilépés");

        fileMenu.add(backToMainMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitGameItem);

        menuBar.add(fileMenu);

        setJMenuBar(menuBar);

        // main panel
        JLabel mapLabel = new JLabel("Játék térkép");
        mapLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mapLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(mapLabel, BorderLayout.CENTER);

        //fomenube vissza gomb
        backToMainMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MainMenu().setVisible(true);
                dispose();
            }
        });

        //exit
        exitGameItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
}