package GUI;

import javax.imageio.ImageIO;
import javax.swing.*;

import commands.CommandProcessor;
import fungus.*;
import insect.*;
import tektonTypes.*;
import sporeTypes.*;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.awt.image.BufferedImage;

public class FungoriumGamePanel extends JPanel {
    // Handlers
    private CommandProcessor commandProcessor;
    private RenderMap renderMap;

    // Positions and cells
    private Map<String, Point> objectPositions = new HashMap<>();
    private Set<Point> occupiedCells = new HashSet<>();
    
    // Sizes
    private static final int TEKTON_SIZE = 80;
    private static final int BODY_SIZE = 30;
    private static final int SPORE_SIZE = 10;
    private static final int INSECT_SIZE = 15;
    private static final int THREAD_WIDTH = 3;
    
    // Number of cells occupied by a Tekton
    private static final int TEKTON_CELLS = 3; // 3x3 cella (9 cella)

    // Background image
    private Image backgroundImage;

    // Tekton images
    private Image defTektonBg;
    private Image decomposingTektonBg;
    private Image decreasingTektonBg;
    private Image feedThreadTektonBg;
    private Image oneThreadTektonBg;
    private Image onlyThreadTektonBg;
    // Circular tekton images
    private Image defTektonBgCircular;
    private Image decomposingTektonBgCircular;
    private Image decreasingTektonBgCircular;
    private Image feedThreadTektonBgCircular;
    private Image oneThreadTektonBgCircular;
    private Image onlyThreadTektonBgCircular;

    private Image fungusBodyImg;

    public FungoriumGamePanel(CommandProcessor commandProcessor) {
        this.commandProcessor = commandProcessor;
        setPreferredSize(new Dimension(800, 800));

        renderMap = new RenderMap(RenderMap.MapSize.MEDIUM);

        // Load the background image
        try {
            backgroundImage = ImageIO.read(new File("src/resources/bgdark.jpg"));
        } catch (Exception e) {
            System.err.println("Error loading background image: " + e.getMessage());
        }

        try {
            // * Load SQARE tekton images
            defTektonBg = ImageIO.read(new File("src/resources/tektons/defaultTekton.jpg"));
            decomposingTektonBg = ImageIO.read(new File("src/resources/tektons/decomposingTekton.jpg"));
            decreasingTektonBg = ImageIO.read(new File("src/resources/tektons/decreasingTekton.jpg"));
            feedThreadTektonBg = ImageIO.read(new File("src/resources/tektons/feedThreadTekton.jpg"));
            oneThreadTektonBg = ImageIO.read(new File("src/resources/tektons/oneThreadTekton.jpg"));
            onlyThreadTektonBg = ImageIO.read(new File("src/resources/tektons/onlyThreadTekton.jpg"));

            // * Load CIRCULAR tekton images
            defTektonBgCircular = createCircularImage(
                    ImageIO.read(new File("src/resources/tektons/defaultTekton.jpg")));
            decomposingTektonBgCircular = createCircularImage(
                    ImageIO.read(new File("src/resources/tektons/decomposingTekton.jpg")));
            decreasingTektonBgCircular = createCircularImage(
                    ImageIO.read(new File("src/resources/tektons/decreasingTekton.jpg")));
            feedThreadTektonBgCircular = createCircularImage(
                    ImageIO.read(new File("src/resources/tektons/feedThreadTekton.jpg")));
            oneThreadTektonBgCircular = createCircularImage(
                    ImageIO.read(new File("src/resources/tektons/oneThreadTekton.jpg")));
            onlyThreadTektonBgCircular = createCircularImage(
                    ImageIO.read(new File("src/resources/tektons/onlyThreadTekton.jpg")));

            // Load fungus body image
            fungusBodyImg = ImageIO.read(new File("src/resources/fungusBody.png"));

        } catch (Exception e) {
            System.err.println("Error loading tekton images: " + e.getMessage());
        }
    }

    private BufferedImage createCircularImage(BufferedImage input) {
        int size = Math.min(input.getWidth(), input.getHeight());
        BufferedImage circleBuffer = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = circleBuffer.createGraphics();

        // Minőség javítása
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Kör maszkolás
        Ellipse2D.Double circle = new Ellipse2D.Double(0, 0, size, size);
        g2.setClip(circle);
        g2.drawImage(input, 0, 0, size, size, null);

        g2.dispose();
        return circleBuffer;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Rajzoljuk a térképet

        // Draw the tiled background
        if (backgroundImage != null) {
            int originalWidth = backgroundImage.getWidth(this);
            int originalHeight = backgroundImage.getHeight(this);

            // Scale the image to a smaller size (e.g., 50% of the original size)
            int scaledWidth = originalWidth / 7;
            int scaledHeight = originalHeight / 7;

            if (scaledWidth > 0 && scaledHeight > 0) {
                for (int x = 0; x < getWidth(); x += scaledWidth) {
                    for (int y = 0; y < getHeight(); y += scaledHeight) {
                        g2d.drawImage(backgroundImage, x, y, scaledWidth, scaledHeight, this);
                    }
                }
            }
        }


        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        calculateObjectPositions();

        // Draw the grid (optional) RED
        // drawGrid(g2d);

        // Draw all objects
        drawTektons(g2d);
        drawBodies(g2d);
        drawSpores(g2d);
        // drawInsects(g2d);
        // drawThreads(g2d);
    }

    private void drawGrid(Graphics2D g2d) {
        // A RenderMap által definiált cellák kirajzolása
        g2d.setColor(Color.RED);
        int cellWidth = getWidth() / renderMap.getCols();
        int cellHeight = getHeight() / renderMap.getRows();

        for (Point tile : renderMap.getTiles()) {
            int x = tile.y * cellWidth;
            int y = tile.x * cellHeight;
            g2d.drawRect(x, y, cellWidth, cellHeight);
        }
    }

    private void calculateObjectPositions() {
        // Tekton init
        List<Point> tiles = renderMap.getTiles();
        int maxRetries = 100; // Maximum number of retries to find a free spot
        occupiedCells.clear(); // Initialize the set of occupied cells

        for (Map.Entry<String, Object> entry : commandProcessor.getCreatedObjects().entrySet()) {
            if (entry.getValue() instanceof Tekton) {
                boolean placed = false;
                int retries = 0;

                while (!placed && retries < maxRetries) {
                    int randomIndex = (int) (Math.random() * tiles.size());
                    Point topLeft = tiles.get(randomIndex);
        
        
                    if (isWithinBounds(topLeft, TEKTON_CELLS, renderMap.getCols(), renderMap.getRows()) && isAreaFree(topLeft, TEKTON_CELLS)) {
                        objectPositions.put(entry.getKey(), topLeft);
                        occupyArea(topLeft, TEKTON_CELLS); // Mark cells as occupied
                        placed = true;
                    } else {
                        retries++;
                    }
                }

                if (!placed) {
                    System.err.println(
                            "Could not place Tekton: " + entry.getKey() + " after " + maxRetries + " retries.");
                    // Optionally handle this situation (e.g., skip placing the Tekton)
                }
            }
        }

        // Position other objects relative to their tektons
        for (Map.Entry<String, Object> entry : commandProcessor.getCreatedObjects().entrySet()) {
            String name = entry.getKey();
            Object obj = entry.getValue();

            if (obj instanceof FungusBody) {
                FungusBody body = (FungusBody) obj;
                Tekton tekton = body.getTekton();
                if (tekton != null) {
                    Point tektonCenter = getTektonCenter(tekton);
                    if (tektonCenter != null) {
                        // Set the FungusBody position to the center of the Tekton
                        objectPositions.put(name, tektonCenter);
                    }
                }
            } else if (obj instanceof FungusThread) {
                FungusThread thread = (FungusThread) obj;
                // Position threads between their connected objects
                if (!thread.getTektons().isEmpty()) {
                    Tekton firstTekton = thread.getTektons().get(0);
                    String firstTektonName = commandProcessor.findByObject(firstTekton);

                    if (thread.getNextBody() != null) {
                        String nextBodyName = commandProcessor.findByObject(thread.getNextBody());
                        if (firstTektonName != null && nextBodyName != null &&
                                objectPositions.containsKey(firstTektonName) &&
                                objectPositions.containsKey(nextBodyName)) {

                            Point start = objectPositions.get(firstTektonName);
                            Point end = objectPositions.get(nextBodyName);
                            // Store thread position as midpoint
                            objectPositions.put(name, new Point(
                                    (start.x + end.x) / 2,
                                    (start.y + end.y) / 2));
                        }
                    } else if (thread.getNext() != null) {
                        String nextThreadName = commandProcessor.findByObject(thread.getNext());
                        if (firstTektonName != null && nextThreadName != null &&
                                objectPositions.containsKey(firstTektonName) &&
                                objectPositions.containsKey(nextThreadName)) {

                            Point start = objectPositions.get(firstTektonName);
                            Point end = objectPositions.get(nextThreadName);
                            // Store thread position as midpoint
                            objectPositions.put(name, new Point(
                                    (start.x + end.x) / 2,
                                    (start.y + end.y) / 2));
                        }
                    }
                }
            } else if (obj instanceof Spore) {
                Spore spore = (Spore) obj;
                Tekton tekton = spore.getTekton();
                Point tektonPos = getTektonPosition(tekton);
                if (tektonPos != null) {

                        // Get cell dimensions
                        int cellWidth = getWidth() / renderMap.getCols();
                        int cellHeight = getHeight() / renderMap.getRows();

                        // Calculate Tekton's center coordinates
                        int tektonCenterX = tektonPos.y * cellWidth + (cellWidth * TEKTON_CELLS / 2);
                        int tektonCenterY = tektonPos.x * cellHeight + (cellHeight * TEKTON_CELLS / 2);

                        // Calculate Tekton's radius (use min to handle non-square cells)
                        int radius = Math.min(cellWidth, cellHeight) * TEKTON_CELLS / 2;

                        // Add 10% margin so spores don't touch the edge
                        int margin = (int) (radius * 0.1);
                        radius -= margin;

                        int offsetX, offsetY;
                        do {
                            offsetX = (int) (Math.random() * radius * 2 - radius);
                            offsetY = (int) (Math.random() * radius * 2 - radius);
                        } while (offsetX * offsetX + offsetY * offsetY > radius * radius);

                        // Set final spore position
                        objectPositions.put(name, new Point(
                                tektonCenterX + offsetX,
                                tektonCenterY + offsetY));
                    }
            } else if (obj instanceof Insect) {
                Insect insect = (Insect) obj;
                FungusThread thread = insect.getThread();
                if (thread != null) {
                    String threadName = commandProcessor.findByObject(thread);
                    if (threadName != null && objectPositions.containsKey(threadName)) {
                        Point threadPos = objectPositions.get(threadName);
                        // Position insect near the thread
                        objectPositions.put(name, new Point(
                                threadPos.x + 10,
                                threadPos.y + 10));
                    }
                }
            }
        }
    }

    private boolean isWithinBounds(Point topLeft, int size, int maxCols, int maxRows) {
        return topLeft.x + size <= maxRows && topLeft.y + size <= maxCols;
    }

    private boolean isAreaFree(Point topLeft, int size) {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Point cell = new Point(topLeft.x + row, topLeft.y + col);
                if (occupiedCells.contains(cell)) {
                    return false; // Már foglalt cella
                }
            }
        }
        return true;
    }

    private void occupyArea(Point topLeft, int size) {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Point cell = new Point(topLeft.x + row, topLeft.y + col);
                occupiedCells.add(cell);
            }
        }
    }

    private Point getTektonCenter(Tekton tekton) {
        Point tektonPos = getTektonPosition(tekton);
        if (tektonPos != null) {
            int cellWidth = getWidth() / renderMap.getCols();
            int cellHeight = getHeight() / renderMap.getRows();

            // Calculate center in pixel coordinates
            int tektonCenterX = tektonPos.y * cellWidth + (cellWidth * TEKTON_CELLS / 2);
            int tektonCenterY = tektonPos.x * cellHeight + (cellHeight * TEKTON_CELLS / 2);

            return new Point(tektonCenterX, tektonCenterY);
        }
        return null;
    }

    private Point getTektonPosition(Tekton tekton) {
        String tektonName = commandProcessor.findByObject(tekton);
        if (tektonName != null && objectPositions.containsKey(tektonName)) {
            return objectPositions.get(tektonName);
        }
        return null;
    }


    private void drawTektons(Graphics2D g2d) {
        int cellWidth = getWidth() / renderMap.getCols();
        int cellHeight = getHeight() / renderMap.getRows();

        // Iterate over objectPositions with the correct type
        for (Map.Entry<String, Point> entry : objectPositions.entrySet()) {
            String name = entry.getKey(); // The key is a String
            Object obj = commandProcessor.getCreatedObjects().get(name); // Get the object by name
            if (obj == null || !(obj instanceof Tekton)) {
                continue; // Skip if not a Tekton
            }
            System.out.println("Drawing object: " + entry.getKey());

            Point topLeft = entry.getValue(); // The value is a Point

            // Calculate the Tekton's area
            int x = topLeft.y * cellWidth;
            int y = topLeft.x * cellHeight;
            int width = cellWidth * TEKTON_CELLS;
            int height = cellHeight * TEKTON_CELLS;

            // Draw shadow (offset by 5 pixels and with a semi-transparent black color)
            //g2d.setColor(new Color(139, 69, 19, 100)); // Semi-transparent brown
            g2d.setColor(new Color(139, 69, 19, 255)); // Fully-visible brown
            g2d.fill(new Ellipse2D.Double(x + 1, y + 5, width, height));

            // Draw the Tekton image
            drawTektonImage(g2d, name, x, y, width, height);

            // Draw the Tekton name
            g2d.setColor(Color.WHITE);
            g2d.drawString(name, x, y);
        }
    }

    private void drawTektonImage(Graphics2D g2d, String name, int x, int y, int width, int height) {
        Object tekton = commandProcessor.getCreatedObjects().get(name);

        if (tekton instanceof DecomposingTekton && decomposingTektonBgCircular != null) {
            g2d.drawImage(decomposingTektonBgCircular, x, y, width, height, this);
        } else if (tekton instanceof DecreasingTekton && decreasingTektonBgCircular != null) {
            g2d.drawImage(decreasingTektonBgCircular, x, y, width, height, this);
        } else if (tekton instanceof FeedThreadTekton && feedThreadTektonBgCircular != null) {
            g2d.drawImage(feedThreadTektonBgCircular, x, y, width, height, this);
        } else if (tekton instanceof OneThreadTekton && oneThreadTektonBgCircular != null) {
            g2d.drawImage(oneThreadTektonBgCircular, x, y, width, height, this);
        } else if (tekton instanceof OnlyThreadTekton && onlyThreadTektonBgCircular != null) {
            g2d.drawImage(onlyThreadTektonBgCircular, x, y, width, height, this);
        } else if (tekton instanceof Tekton && defTektonBgCircular != null) {
            g2d.drawImage(defTektonBgCircular, x, y, width, height, this);
        } else {
            // Fallback: Draw a gray circle if no image is available
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.fill(new Ellipse2D.Double(x, y, width, height));
            g2d.setColor(Color.WHITE);
            g2d.draw(new Ellipse2D.Double(x, y, width, height));
        }
    }

    private void drawThreads(Graphics2D g2d) {
        for (Map.Entry<String, Object> entry : commandProcessor.getCreatedObjects().entrySet()) {
            if (entry.getValue() instanceof FungusThread) {
                FungusThread thread = (FungusThread) entry.getValue();

                // Draw connection to previous body if exists
                if (thread.getPrevBody() != null) {
                    String prevBodyName = commandProcessor.findByObject(thread.getPrevBody());
                    if (prevBodyName != null && objectPositions.containsKey(prevBodyName)) {
                        Point bodyPos = objectPositions.get(prevBodyName);
                        Point threadPos = objectPositions.getOrDefault(entry.getKey(), bodyPos);

                        g2d.setColor(new Color(150, 75, 0));
                        g2d.setStroke(new BasicStroke(THREAD_WIDTH));
                        g2d.draw(new Line2D.Double(
                                bodyPos.x, bodyPos.y,
                                threadPos.x, threadPos.y));
                    }
                }

                // Draw connection to next body if exists
                if (thread.getNextBody() != null) {
                    String nextBodyName = commandProcessor.findByObject(thread.getNextBody());
                    if (nextBodyName != null && objectPositions.containsKey(nextBodyName)) {
                        Point bodyPos = objectPositions.get(nextBodyName);
                        Point threadPos = objectPositions.getOrDefault(entry.getKey(), bodyPos);

                        g2d.setColor(new Color(150, 75, 0));
                        g2d.setStroke(new BasicStroke(THREAD_WIDTH));
                        g2d.draw(new Line2D.Double(
                                threadPos.x, threadPos.y,
                                bodyPos.x, bodyPos.y));
                    }
                }

                // Draw connection to next thread if exists
                if (thread.getNext() != null) {
                    String nextThreadName = commandProcessor.findByObject(thread.getNext());
                    if (nextThreadName != null && objectPositions.containsKey(nextThreadName)) {
                        Point nextThreadPos = objectPositions.get(nextThreadName);
                        Point threadPos = objectPositions.getOrDefault(entry.getKey(), nextThreadPos);

                        g2d.setColor(new Color(150, 75, 0));
                        g2d.setStroke(new BasicStroke(THREAD_WIDTH));
                        g2d.draw(new Line2D.Double(
                                threadPos.x, threadPos.y,
                                nextThreadPos.x, nextThreadPos.y));
                    }
                }

                // Draw thread name if position is known
                Point pos = objectPositions.get(entry.getKey());
                if (pos != null) {
                    g2d.setColor(Color.WHITE);
                    g2d.drawString(entry.getKey(), pos.x, pos.y);
                }
            }
        }
    }

    private void drawBodies(Graphics2D g2d) {
        for (Map.Entry<String, Object> entry : commandProcessor.getCreatedObjects().entrySet()) {
            if (entry.getValue() instanceof FungusBody) {
                String name = entry.getKey();
                Point pos = objectPositions.getOrDefault(name, new Point(100, 100));

                // Draw the fungus body image if available
                if (fungusBodyImg != null) {
                    g2d.drawImage(fungusBodyImg, pos.x - BODY_SIZE / 2, pos.y - BODY_SIZE / 2, BODY_SIZE, BODY_SIZE,
                            this);
                } else {
                    // Fallback: Draw an ellipse if the image is not available
                    g2d.setColor(new Color(100, 50, 0));
                    g2d.fill(new Ellipse2D.Double(pos.x - BODY_SIZE / 2, pos.y - BODY_SIZE / 2, BODY_SIZE, BODY_SIZE));
                    g2d.setColor(Color.WHITE);
                    g2d.draw(new Ellipse2D.Double(pos.x - BODY_SIZE / 2, pos.y - BODY_SIZE / 2, BODY_SIZE, BODY_SIZE));
                }
                // Draw body name
                g2d.drawString(name, pos.x - BODY_SIZE / 2 + 5, pos.y - BODY_SIZE / 2 + 15);

                // Draw spore count if available
                FungusBody body = (FungusBody) entry.getValue();
                g2d.drawString("Spores: " + body.getSporeCount(), pos.x - BODY_SIZE / 2 + 5,
                        pos.y - BODY_SIZE / 2 + 30);
            }
        }
    }

    private void drawSpores(Graphics2D g2d) {
        for (Map.Entry<String, Object> entry : commandProcessor.getCreatedObjects().entrySet()) {
            if (entry.getValue() instanceof Spore) {
                String name = entry.getKey();
                Point pos = objectPositions.getOrDefault(name, new Point(150, 150));

                // Different colors for different spore types
                Color color = Color.WHITE;
                if (entry.getValue() instanceof FastSpore) {
                    color = new Color(255, 200, 200); // Light red
                } else if (entry.getValue() instanceof MultiplyInsectSpore) {
                    color = new Color(200, 255, 200); // Light green
                } else if (entry.getValue() instanceof SlowSpore) {
                    color = new Color(200, 200, 255); // Light blue
                } else if (entry.getValue() instanceof StunSpore) {
                    color = new Color(255, 255, 200); // Light yellow
                } else if (entry.getValue() instanceof DisableCutSpore) {
                    color = new Color(255, 200, 255); // Light purple
                }

                g2d.setColor(color);
                g2d.fill(new Ellipse2D.Double(pos.x - SPORE_SIZE / 2, pos.y - SPORE_SIZE / 2, SPORE_SIZE, SPORE_SIZE));
                g2d.setColor(Color.WHITE);
                g2d.draw(new Ellipse2D.Double(pos.x - SPORE_SIZE / 2, pos.y - SPORE_SIZE / 2, SPORE_SIZE, SPORE_SIZE));

                // Draw spore name (small font)
                Font originalFont = g2d.getFont();
                g2d.setFont(new Font(originalFont.getName(), originalFont.getStyle(), 8));
                g2d.drawString(name, pos.x - SPORE_SIZE / 2, pos.y - SPORE_SIZE / 2 - 2);
                g2d.setFont(originalFont);
            }
        }
    }

    private void drawInsects(Graphics2D g2d) {
        for (Map.Entry<String, Object> entry : commandProcessor.getCreatedObjects().entrySet()) {
            if (entry.getValue() instanceof Insect) {
                String name = entry.getKey();
                // Point pos = objectPositions.getOrDefault(name, new Point(200, 200));
                Insect insect = (Insect) entry.getValue();
                FungusThread thread = insect.getThread();

                Point pos;
                if (thread != null) {
                    String threadName = commandProcessor.findByObject(thread);
                    if (threadName != null && objectPositions.containsKey(threadName)) {
                        Point threadPos = objectPositions.get(threadName);
                        // Position insect near the thread
                        pos = new Point(threadPos.x + 10, threadPos.y + 10);
                    } else {
                        // Default position if thread position is not found
                        pos = new Point(200, 200);
                    }
                } else {
                    // Default position if thread is null
                    pos = new Point(200, 200);
                }

                // Base insect color
                g2d.setColor(new Color(150, 100, 50));
                g2d.fill(new Ellipse2D.Double(pos.x - INSECT_SIZE / 2, pos.y - INSECT_SIZE / 2, INSECT_SIZE,
                        INSECT_SIZE));
                g2d.setColor(Color.WHITE);
                g2d.draw(new Ellipse2D.Double(pos.x - INSECT_SIZE / 2, pos.y - INSECT_SIZE / 2, INSECT_SIZE,
                        INSECT_SIZE));

                // Draw effect indicator
                InsectEffects effect = insect.gEffect();
                if (effect != null) {
                    switch (effect) {
                        case STUN:
                            g2d.setColor(Color.YELLOW);
                            break;
                        case SLOW:
                            g2d.setColor(Color.BLUE);
                            break;
                        case FAST:
                            g2d.setColor(Color.RED);
                            break;
                        case NO_CUT:
                            g2d.setColor(Color.MAGENTA);
                            break;
                        default:
                            g2d.setColor(Color.WHITE);
                    }
                    g2d.fillOval(pos.x - INSECT_SIZE / 4, pos.y - INSECT_SIZE / 4, INSECT_SIZE / 2, INSECT_SIZE / 2);
                }

                // Draw insect name
                Font originalFont = g2d.getFont();
                g2d.setFont(new Font(originalFont.getName(), originalFont.getStyle(), 8));
                g2d.setColor(Color.WHITE);
                g2d.drawString(name, pos.x - INSECT_SIZE / 2, pos.y - INSECT_SIZE / 2 - 2);
                g2d.setFont(originalFont);

                // Position of insect should be based on its getThread() method and it should be
                // on the thread
            }
        }
    }

    public void updateGameState() {
        objectPositions.clear(); // Force recalculation of positions
        occupiedCells.clear(); // Clear occupied cells as positions are being recalculated
        repaint();
    }

    public static void createAndShowGUI(CommandProcessor commandProcessor) {
        JFrame frame = new JFrame("Fungorium Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        FungoriumGamePanel gamePanel = new FungoriumGamePanel(commandProcessor);
        frame.add(gamePanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Add a simple control panel
        JPanel controlPanel = new JPanel();
        JButton updateButton = new JButton("Update View");
        updateButton.addActionListener(e -> gamePanel.updateGameState());
        controlPanel.add(updateButton);

        frame.add(controlPanel, BorderLayout.SOUTH);
    }
}