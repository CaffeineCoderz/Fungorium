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
import java.awt.geom.Point2D;


class GameMapScreen extends JFrame {
    private static final int MAP_WIDTH = 800; // Például a térkép pixel szélessége
    private static final int MAP_HEIGHT = 600; // Például a térkép pixel magassága
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
        private List<Point2D.Double> tektonok; // Folytonos koordináták
        private List<Point2D.Double> rovarok; // Folytonos koordináták
        private AffineTransform transform = new AffineTransform();
        private BufferedImage backgroundImage;
        private Point dragStartPoint;

        public MapPanel() {
            setPreferredSize(new Dimension(MAP_WIDTH, MAP_HEIGHT));
            setBackground(Color.LIGHT_GRAY);
            addMouseListener(this);
            addMouseMotionListener(this);
            addMouseWheelListener(this);

            try {
                backgroundImage = ImageIO.read(new File("src/GUI/DATA/background.png"));
            } catch (IOException e) {
                System.err.println("Hiba a háttérkép betöltése közben: " + e.getMessage());
                backgroundImage = null;
            }

            Random random = new Random();
            tektonok = new ArrayList<>();
            rovarok = new ArrayList<>();
            int numEntities = 20;
            for (int i = 0; i < numEntities; i++) {
                tektonok.add(new Point2D.Double(random.nextDouble() * MAP_WIDTH, random.nextDouble() * MAP_HEIGHT));
                rovarok.add(new Point2D.Double(random.nextDouble() * MAP_WIDTH, random.nextDouble() * MAP_HEIGHT));
            }

            // Kezdeti középre igazítás
            int panelWidth = getWidth();
            int panelHeight = getHeight();
            transform.translate(panelWidth / 2.0 - MAP_WIDTH / 2.0,
                                 panelHeight / 2.0 - MAP_HEIGHT / 2.0);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            if (backgroundImage != null) {
                g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            } else {
                g2d.setColor(Color.gray);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
            g2d.transform(transform);

            int tektonSize = 20;
            g2d.setColor(Color.BLUE);
            for (Point2D.Double tekton : tektonok) {
                g2d.fillRect((int) tekton.getX() - tektonSize / 2, (int) tekton.getY() - tektonSize / 2, tektonSize, tektonSize);
            }

            int insectSize = 10;
            g2d.setColor(Color.RED);
            for (Point2D.Double rovar : rovarok) {
                g2d.fillOval((int) rovar.getX() - insectSize / 2, (int) rovar.getY() - insectSize / 2, insectSize, insectSize);
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
            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;

            transform.translate(centerX, centerY);
            transform.scale(zoomFactor, zoomFactor);
            transform.translate(-centerX, -centerY);

            repaint();
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            // Nem használjuk
        }
        @Override
        public void mouseMoved(MouseEvent e) {
            // Nem használjuk
        }
        @Override
        public void mouseEntered(MouseEvent e) {
            // Nem használjuk
        }
        @Override
        public void mouseExited(MouseEvent e) {
            // Nem használjuk
        }

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameMapScreen());
    }
}