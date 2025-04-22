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
import java.awt.Polygon;
import java.awt.geom.Point2D;

class GameMapScreen extends JFrame {
    private static final int MAP_WIDTH = 25;
    private static final int MAP_HEIGHT = 25;
    private static final double ZOOM_SPEED = 0.02;
    private static final int CELL_SIZE = 40; // cell size in pixels

    //hex
    private static final double HEX_RADIUS = CELL_SIZE / 2.0;
    private static final double HEX_SIDE = HEX_RADIUS * Math.sqrt(3) / 2.0;
    private static final double HEX_HEIGHT = HEX_RADIUS;
    private static final double HEX_WIDTH = 2 * HEX_SIDE;
    private static final double HORIZONTAL_SPACING = HEX_WIDTH * 1.7;
    private static final double VERTICAL_SPACING = HEX_HEIGHT * 0.9;


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
                g2d.setColor(Color.gray);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
            g2d.transform(transform);
        
            for (int y = 0; y < MAP_HEIGHT; y++) {
                for (int x = 0; x < MAP_WIDTH; x++) {
                    Point2D.Double center = getHexagonCenter(x, y);
                    Polygon hexagon = createHexagon(center.getX(), center.getY());
        
                    g2d.setColor(Color.WHITE);
                    g2d.draw(hexagon);
        
                    g2d.setColor(Color.BLUE);
                    for (Point tekton : tektonok) {
                        if (tekton.x == x && tekton.y == y) {
                            g2d.fill(hexagon);
                            break;
                        }
                    }
        
                    g2d.setColor(Color.RED);
                    int insectSize = (int) (HEX_RADIUS / 2);
                    for (Point rovar : rovarok) {
                        if (rovar.x == x && rovar.y == y) {
                            g2d.fillOval((int) (center.getX() - insectSize / 2), (int) (center.getY() - insectSize / 2), insectSize, insectSize);
                            break;
                        }
                    }
                }
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
    
    private Point2D.Double getHexagonCenter(int x, int y) {
        double centerX = x * HORIZONTAL_SPACING + HEX_SIDE;
        double centerY = y * VERTICAL_SPACING + HEX_HEIGHT / 2;
        if (y % 2 == 1) {
            centerX += HORIZONTAL_SPACING / 2;
        }
        return new Point2D.Double(centerX, centerY);
    }
    
    private Polygon createHexagon(double centerX, double centerY) {
        Polygon hexagon = new Polygon();
        for (int i = 0; i < 6; i++) {
            double angle = 2 * Math.PI / 6 * i;
            int px = (int) (centerX + HEX_RADIUS * Math.cos(angle));
            int py = (int) (centerY + HEX_RADIUS * Math.sin(angle));
            hexagon.addPoint(px, py);
        }
        return hexagon;
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameMapScreen());
    }
}