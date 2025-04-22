package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class GameMapScreen extends JFrame {
    private static final int MAP_WIDTH = 20;
    private static final int MAP_HEIGHT = 20;
    private static final int CELL_SIZE = 40; // Alapértelmezett cellaméret pixelben
    private static final double ZOOM_SPEED = 0.02;

    private MapPanel mapPanel;

    public GameMapScreen() {

        setTitle("Fungorium - Játéktérkép");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        mapPanel = new MapPanel();
        add(mapPanel);

        // Menüsor létrehozása
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Fájl");
        JMenuItem backToMainMenuItem = new JMenuItem("Vissza a főmenübe");
        JMenuItem exitGameItem = new JMenuItem("Kilépés");

        fileMenu.add(backToMainMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitGameItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        //fomenube vissza gomb
        backToMainMenuItem.addActionListener(e -> {
            new MainMenu().setVisible(true);
            dispose();
        });

        //exit
        exitGameItem.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    private class MapPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {
        private List<Point> tektonok;
        private List<Point> rovarok;
        private AffineTransform transform = new AffineTransform();
        private BufferedImage backgroundImage;
        private Point dragStartPoint;

        public MapPanel() {
            setPreferredSize(new Dimension(MAP_WIDTH * CELL_SIZE, MAP_HEIGHT * CELL_SIZE));
            setBackground(Color.LIGHT_GRAY);
            addMouseListener(this);
            addMouseMotionListener(this);
            addMouseWheelListener(this);

            try {
                backgroundImage = ImageIO.read(new File("src/GUI/DATA/background.png"));
            } catch (IOException e) {
                System.err.println("Hiba a háttérkép betöltése közben: " + e.getMessage());
                backgroundImage = null; // Kezeld a hibát megfelelően
            }
    
            // Tektonok és rovarok véletlenszerű elhelyezése
            Random random = new Random();
            tektonok = new ArrayList<>();
            rovarok = new ArrayList<>();
            int numEntities = 20; // Például 20 tekton és 20 rovar
            for (int i = 0; i < numEntities; i++) {
                tektonok.add(new Point(random.nextInt(MAP_WIDTH), random.nextInt(MAP_HEIGHT)));
                rovarok.add(new Point(random.nextInt(MAP_WIDTH), random.nextInt(MAP_HEIGHT)));
            }
    
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            if (backgroundImage != null) {
                g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            } else {
                // Ha nincs háttérkép, rajzolhatsz egy alapszínt
                g2d.setColor(Color.gray);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
            g2d.transform(transform);

            int panelWidth = getWidth();
            int panelHeight = getHeight();
            int mapPixelWidth = MAP_WIDTH * CELL_SIZE;
            int mapPixelHeight = MAP_HEIGHT * CELL_SIZE;

            // Kezdeti eltolás a térkép közepére igazításához
            AffineTransform initialTransform = new AffineTransform();
            initialTransform.translate(panelWidth / 2.0 - mapPixelWidth / 2.0,
                                        panelHeight / 2.0 - mapPixelHeight / 2.0);

            // Alkalmazzuk a zoom és mozgatás transzformációkat az eredeti után
            AffineTransform combinedTransform = new AffineTransform(initialTransform);
            combinedTransform.concatenate(transform);
            g2d.transform(combinedTransform);

            // Koordináta-rendszer rajzolása (opcionális)
            g2d.setColor(Color.white);
            for (int i = 0; i <= MAP_WIDTH; i++) {
                g2d.drawLine(i * CELL_SIZE, 0, i * CELL_SIZE, MAP_HEIGHT * CELL_SIZE);
            }
            for (int j = 0; j <= MAP_HEIGHT; j++) {
                g2d.drawLine(0, j * CELL_SIZE, MAP_WIDTH * CELL_SIZE, j * CELL_SIZE);
            }

            // Tektonok rajzolása
            g2d.setColor(Color.BLUE);
            for (Point tekton : tektonok) {
                g2d.fillRect(tekton.x * CELL_SIZE, tekton.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }

            // Rovarok rajzolása
            g2d.setColor(Color.RED);
            int insectSize = CELL_SIZE / 2;
            for (Point rovar : rovarok) {
                g2d.fillOval(rovar.x * CELL_SIZE + CELL_SIZE / 4, rovar.y * CELL_SIZE + CELL_SIZE / 4, insectSize, insectSize);
            }

            g2d.dispose();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            dragStartPoint = e.getPoint();
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            dragStartPoint = null;
            setCursor(Cursor.getDefaultCursor());
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (dragStartPoint != null) {
                int dx = e.getX() - dragStartPoint.x;
                int dy = e.getY() - dragStartPoint.y;
                transform.translate(dx, dy);
                dragStartPoint = e.getPoint();
                repaint();
            }
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            double zoomFactor = 1.0 - e.getWheelRotation() * ZOOM_SPEED;
            Point mousePoint = e.getPoint();
        
            transform.translate(mousePoint.x, mousePoint.y);
            transform.scale(zoomFactor, zoomFactor);
            transform.translate(-mousePoint.x, -mousePoint.y);
        
            repaint();
        }

        // Implement unused mouse event methods
        @Override
        public void mouseClicked(MouseEvent e) {}
        @Override
        public void mouseEntered(MouseEvent e) {}
        @Override
        public void mouseExited(MouseEvent e) {}
        @Override
        public void mouseMoved(MouseEvent e) {}
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameMapScreen());
    }
}