package GUI;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

import commands.CommandProcessor;
import fungus.FungusBody;
import fungus.FungusThread;
import insect.Insect;
import logic.GameLogic;
import sporeTypes.Spore;
import tektonTypes.Tekton;
import GUI.Views.*;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.awt.image.BufferedImage;

public class FungoriumGamePanel extends JPanel {
    // Handlers
    private GameLogic gameLogic;
    private RenderMap renderMap;
    private static GameStateHandler saver = new GameStateHandler();
    // Positions and cells
    private Map<String, Point> objectPositions = new HashMap<>();
    private Set<Point> occupiedCells = new HashSet<>();

    // Flag to indicate if positions need recalculation
    private boolean shouldRecalculatePositions = true;
    
    // Sizes
    private static final int THREAD_WIDTH = 3;
    
    // Number of cells occupied by a Tekton
    private static final int TEKTON_CELLS = 3; // 3x3 cella (9 cella)

    // Background image
    private Image backgroundImage;
    private Boolean initialPaint = true; // Flag to indicate if it's the first paint
    // // Tekton images
    // private Image defTektonBg;
    // private Image decomposingTektonBg;
    // private Image decreasingTektonBg;
    // private Image feedThreadTektonBg;
    // private Image oneThreadTektonBg;
    // private Image onlyThreadTektonBg;
    // Circular tekton images
    private Image defTektonBgCircular;
    private Image decomposingTektonBgCircular;
    private Image decreasingTektonBgCircular;
    private Image feedThreadTektonBgCircular;
    private Image oneThreadTektonBgCircular;
    private Image onlyThreadTektonBgCircular;

    private Image[] tektonImages;
    // Entity images
    private Image defaultSporeImg;
    private Image fastSporeImg;
    private Image slowSporeImg;
    private Image stunSporeImg;
    private Image disableCutSporeImg;
    private Image multiplyInsectSporeImg;

    private Image[] sporeImages;

    private Image insectImg;
    private Image fungusBodyImg;

    // Thread grow
    private Map<String, Point> threadEndpoints = new HashMap<>(); // Thread végpontok tárolása
    private Map<String, List<Point>> tektonCardinalPoints = new HashMap<>(); // Tekton égtáji pontjai
    private List<Line2D> possibleGrowthLines = new ArrayList<>(); // Lehetséges növekedési irányok
    private Map<FungusThread, String> threadDirections = new HashMap<>();

    //! View osztályok
    private TektonView tektonView = new TektonView();
    private SporeView sporeView = new SporeView();
    private BodyView bodyView = new BodyView();
    private InsectView insectView = new InsectView();
    private ThreadView threadView = new ThreadView();
    private StatusView statusView;

    public FungoriumGamePanel(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
        setLayout(null); // Absolute positioning for overlay panels
        setPreferredSize(new Dimension(800, 800));
        renderMap = new RenderMap(RenderMap.MapSize.MEDIUM);

        // Initialize the status view
        statusView = new StatusView();
        //statusView.setBounds(600, 10, 180, 100);

        statusView.setBounds(0, 0, 800, 800); // Position at the top-right corner
        add(statusView);

        // Add mouse listener to detect clicks on objects
        /**
         * Handles mouse clicks on the game panel. If the clicked point corresponds to a valid game object (tekton, fungus, insect, or spore), 
         * a "/status <objectName>" command is executed and the resulting status string is displayed in the status view. If the clicked point does not
         * correspond to a valid game object, the status view is cleared.
         * 
         * @param e the MouseEvent that triggered this method call
         */
        addMouseListener(
            new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Point clickPoint = e.getPoint();
                    String clickedObjectName = getObjectAtPoint(clickPoint);

                    if (clickedObjectName != null) {
                        String command = "/status " + clickedObjectName;

                        // Capture System.out output
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        PrintStream originalOut = System.out;
                        try {
                            System.setOut(new PrintStream(outputStream));
                            gameLogic.getCommandProcessor().process(command); // Execute the command
                            System.out.flush();
                            String status = outputStream.toString().trim(); // Get the captured output
                            statusView.updateStatus(status); // Update the status view
                        } finally {
                            System.setOut(originalOut); // Restore original System.out
                        }
                    } else {
                        statusView.clearStatus(); // Clear the status view if no valid object is clicked
                    }
                    statusView.repaint();
                }
            }
        );       
            
        // Load the background image
        try {
            backgroundImage = ImageIO.read(new File("src/resources/PanelBg/gamePanel3.jpg"));
        } catch (Exception e) {
            System.err.println("Error loading background image: " + e.getMessage());
        }

        loadResources();
    }

    /**
     * Retrieves the name of the object located at the specified point.
     *
     * This method checks if the given point is within a certain distance of any
     * object's position and returns the name of the first matching object.
     *
     * @param point The point to check for object presence.
     * @return The name of the object at the specified point, or null if no object is found.
     */
    private String getObjectAtPoint(Point point) {
        for (Map.Entry<String, Point> entry : objectPositions.entrySet()) {
            Point objectPos = entry.getValue();
            String objectName = entry.getKey();

            // Check if the click is within the bounds of the object
            if (point.distance(objectPos) <= 20) { // Adjust the radius as needed
                return objectName;
            }
        }
        return null;
    }

    /**
     * Loads all resources needed for the game panel, including background and
     * foreground images for the different types of Tektons, fungus bodies, spores
     * and insects.
     *
     * @throws IOException if any of the resources cannot be loaded.
     */
    private void loadResources() {
        try {
            // // * Load SQUARE tekton images
            // defTektonBg = ImageIO.read(new File("src/resources/tektons/defaultTekton1.jpg"));
            // decomposingTektonBg = ImageIO.read(new File("src/resources/tektons/decomposingTekton1.jpg"));
            // decreasingTektonBg = ImageIO.read(new File("src/resources/tektons/decreasingTekton1.jpg"));
            // feedThreadTektonBg = ImageIO.read(new File("src/resources/tektons/feedThreadTekton1.jpg"));
            // oneThreadTektonBg = ImageIO.read(new File("src/resources/tektons/oneThreadTekton1.jpg"));
            // onlyThreadTektonBg = ImageIO.read(new File("src/resources/tektons/onlyThreadTekton1.jpg"));

            // * Load CIRCULAR tekton images
            defTektonBgCircular = createCircularImage(
                    ImageIO.read(new File("src/resources/tektons/defaultTekton1.jpg")));
            decomposingTektonBgCircular = createCircularImage(
                    ImageIO.read(new File("src/resources/tektons/decomposingTekton1.jpg")));
            decreasingTektonBgCircular = createCircularImage(
                    ImageIO.read(new File("src/resources/tektons/decreasingTekton1.jpg")));
            feedThreadTektonBgCircular = createCircularImage(
                    ImageIO.read(new File("src/resources/tektons/feedThreadTekton1.jpg")));
            oneThreadTektonBgCircular = createCircularImage(
                    ImageIO.read(new File("src/resources/tektons/oneThreadTekton1.jpg")));
            onlyThreadTektonBgCircular = createCircularImage(
                    ImageIO.read(new File("src/resources/tektons/onlyThreadTekton1.jpg")));

            // Load fungus body image
            fungusBodyImg = ImageIO.read(new File("src/resources/fungusBody.png"));

            // Load spore images
            defaultSporeImg = ImageIO.read(new File("src/resources/spores/spore.png"));
            fastSporeImg = ImageIO.read(new File("src/resources/spores/fastSpore.png"));
            slowSporeImg = ImageIO.read(new File("src/resources/spores/slowSpore.png"));
            stunSporeImg = ImageIO.read(new File("src/resources/spores/stunSpore.png"));
            disableCutSporeImg = ImageIO.read(new File("src/resources/spores/disableCutSpore.png"));
            multiplyInsectSporeImg = ImageIO.read(new File("src/resources/spores/multiplyInsectSpore.png"));

            // Load insect image
            insectImg = ImageIO.read(new File("src/resources/insect.png"));

        } catch (Exception e) {
            System.err.println("Error loading resources: " + e.getMessage());
        }
    }

    /**
     * Creates a circular cropped version of the input BufferedImage.
     *
     * This method takes an input image and creates a new BufferedImage
     * containing only the circular region of the largest possible size
     * centered within the input image. The resulting image is drawn with
     * anti-aliasing for improved visual quality.
     *
     * @param input the original BufferedImage to be cropped to a circle
     * @return a new BufferedImage containing the circular cropped region
     */
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

        // Fényerő csökkentése: átlátszó fekete réteg hozzáadása
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f)); // 90% átlátszóság
        g2.setColor(new Color(0, 0, 0, 128)); // Fekete szín, 50% átlátszóság
        g2.fill(circle);

        g2.dispose();
        return circleBuffer;
    }

    /**
     * Custom paintComponent method to draw the game state on the screen.
     *
     * This method is called whenever the component needs to be redrawn.
     * It is responsible for drawing the background image, the grid, all
     * objects including tektons, spores, threads, insects and the fungus
     * body. It also draws the égtáji pontok (green points) for the tektons.
     * The method is optimized for performance by using a single Graphics2D
     * object for all drawing operations and by minimizing the number of
     * objects created during the drawing process.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        // Rajzoljuk a térképet
        drawTiledBackground(g2d);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (shouldRecalculatePositions && !initialPaint) {
            calculateObjectPositions();
            shouldRecalculatePositions = false;
        }

        // Draw the grid (optional) RED
        //drawGrid(g2d);

        // Draw all objects
        tektonImages = new Image[]{defTektonBgCircular, decomposingTektonBgCircular, decreasingTektonBgCircular,
            feedThreadTektonBgCircular, oneThreadTektonBgCircular, onlyThreadTektonBgCircular};
        tektonView.drawTektons(g2d, objectPositions, gameLogic.getCommandProcessor().getCreatedObjects(),
        getWidth() / renderMap.getCols(), getHeight() / renderMap.getRows(), TEKTON_CELLS, tektonImages);

        calculateTektonCardinalPoints();
        // Égtáji pontok (zöld pontok) rajzolása
        g2d.setColor(Color.GREEN);
        for (List<Point> points : tektonCardinalPoints.values()) {
            for (Point p : points) {
                g2d.fill(new Ellipse2D.Double(p.x - 5, p.y - 5, 10, 10));
            }
        }
        sporeImages = new Image[]{defaultSporeImg, fastSporeImg, slowSporeImg, stunSporeImg,
                disableCutSporeImg, multiplyInsectSporeImg};
        sporeView.drawSpores(g2d, objectPositions, gameLogic.getCommandProcessor().getCreatedObjects(), sporeImages);
        threadView.drawThreads(g2d, objectPositions, gameLogic.getCommandProcessor().getCreatedObjects(), tektonCardinalPoints, gameLogic.getCommandProcessor());
        insectView.drawInsects(g2d, objectPositions, gameLogic.getCommandProcessor().getCreatedObjects(), insectImg);
        bodyView.drawBodies(g2d, objectPositions, gameLogic.getCommandProcessor().getCreatedObjects(), fungusBodyImg);
        

        if (initialPaint) {
            initialPaint = false;
        }
    }

    // PIROS
    /**
     * Draws the grid of cells defined by the RenderMap.
     * 
     * This method draws a red grid on the screen, where each cell is of size
     * determined by the RenderMap. The grid is drawn by repeatedly drawing
     * rectangles of the appropriate size, with their positions determined by
     * the coordinates of the cells in the RenderMap.
     * 
     * @param g2d the Graphics2D object to draw the grid on
     */
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

    /**
     * Draws the background image of the game, scaled down to a smaller size
     * (e.g., 50% of the original size) and tiled to cover the entire screen.
     * 
     * @param g2d the Graphics2D object to draw the background on
     */
    private void drawTiledBackground(Graphics2D g2d) {
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
    }

    /**
     * Calculates the positions of all objects in the game world.
     * 
     * This method goes through all created objects and assigns them a position
     * on the screen based on their type and any other relevant information.
     * Positions are stored in the objectPositions map.
     * 
     * For Tektons, it randomly selects a free spot on the board and marks the
     * cells as occupied.
     * For FungusBodies, it sets the position to the center of the associated
     * Tekton.
     * For FungusThreads, it positions them between their connected objects.
     * For Spores, it randomly assigns a position around the associated Tekton.
     * For Insects, it positions them near their associated thread.
     */
    private void calculateObjectPositions() {
        // Tekton init
        List<Point> tiles = renderMap.getTiles();
        int maxRetries = 100; // Maximum number of retries to find a free spot
        occupiedCells.clear(); // Initialize the set of occupied cells

        for (Map.Entry<String, Object> entry : gameLogic.getCommandProcessor().getCreatedObjects().entrySet()) {
            if (entry.getValue() instanceof Tekton) {
                boolean placed = false;
                int retries = 0;

                while (!placed && retries < maxRetries) {
                    // Point topLeft = tiles.get(randomIndex);

                    Point topLeft;
                    Tekton tekton = (Tekton) entry.getValue();
                    
                    // Ha van szomszédja, kiszámítjuk a csoportosított pozíciót
                    topLeft = calculateGroupedPosition(tekton, tiles);

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
        for (Map.Entry<String, Object> entry : gameLogic.getCommandProcessor().getCreatedObjects().entrySet()) {
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
                    String firstTektonName = gameLogic.getCommandProcessor().findByObject(firstTekton);

                    if (thread.getNextBody() != null) {
                        String nextBodyName = gameLogic.getCommandProcessor().findByObject(thread.getNextBody());
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
                        String nextThreadName = gameLogic.getCommandProcessor().findByObject(thread.getNext());
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
            }
        }
    
        // Az insect a threadtől függ, ezért a threaderket számoljuk előbb és utánna megyünk végig az insecteken
        for (Map.Entry<String, Object> entry : gameLogic.getCommandProcessor().getCreatedObjects().entrySet()) {
            String name = entry.getKey();
            Object obj = entry.getValue();

            if (obj instanceof Insect) {
                Insect insect = (Insect) obj;
                FungusThread thread = insect.getThread();
                if (thread != null) {
                    String threadName = gameLogic.getCommandProcessor().findByObject(thread);
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
    
    private Point calculateGroupedPosition(Tekton tekton, List<Point> tiles) {
        List<Tekton> neighbors = tekton.getNeighbours();
        List<Tekton> allTektons = gameLogic.getCommandProcessor().getCreatedObjects().values().stream()
                .filter(obj -> obj instanceof Tekton)
                .map(obj -> (Tekton) obj)
                .filter(t -> t != tekton)
                .collect(Collectors.toList());

        // Ha nincsenek szomszédok, véletlenszerű pozíciót választunk
        if (neighbors.isEmpty()) {
            return findOptimalRandomPosition(tiles, allTektons);
        }

        // Minden szomszédot sorban megpróbálunk
        for (Tekton neighbor : neighbors) {
            String neighborName = gameLogic.getCommandProcessor().findByObject(neighbor);
            if (neighborName == null || !objectPositions.containsKey(neighborName)) {
                continue;
            }

            Point neighborPos = objectPositions.get(neighborName);
            
            // Irányok véletlenszerű sorrendben
            int[][] directions = {{1,0}, {0,1}, {-1,0}, {0,-1}};
            Collections.shuffle(Arrays.asList(directions));
            
            for (int[] dir : directions) {
                Point newPos = new Point(neighborPos.x + dir[0]*3, neighborPos.y + dir[1]*3);
                
                if (isPositionValid(newPos) && !isAdjacentToOtherTekton(newPos, allTektons)) {
                    return newPos;
                }
            }
        }

        // Ha nem találtunk ideális helyet, próbáljunk olyat ami csak valid
        for (Tekton neighbor : neighbors) {
            String neighborName = gameLogic.getCommandProcessor().findByObject(neighbor);
            if (neighborName == null || !objectPositions.containsKey(neighborName)) {
                continue;
            }

            Point neighborPos = objectPositions.get(neighborName);
            int[][] directions = {{1,0}, {0,1}, {-1,0}, {0,-1}};
            
            for (int[] dir : directions) {
                Point newPos = new Point(neighborPos.x + dir[0]*4, neighborPos.y + dir[1]*4);
                if (isPositionValid(newPos)) {
                    return newPos;
                }
            }
        }

        // Végső esetben véletlenszerű érvényes pozíció
        return findOptimalRandomPosition(tiles, allTektons);
    }

    private Point findOptimalRandomPosition(List<Point> tiles, List<Tekton> otherTektons) {
        int maxAttempts = 100;
        List<Point> validPositions = new ArrayList<>();
        
        // Első körben csak olyan pozíciók amik nem szomszédosak más Tektonekkel
        for (int i = 0; i < maxAttempts; i++) {
            int randomIndex = (int) (Math.random() * tiles.size());
            Point candidate = tiles.get(randomIndex);
            if (isPositionValid(candidate) && !isAdjacentToOtherTekton(candidate, otherTektons)) {
                validPositions.add(candidate);
            }
        }
        
        if (!validPositions.isEmpty()) {
            return validPositions.get((int)(Math.random() * validPositions.size()));
        }
        
        // Ha nem találtunk ilyet, akkor bármilyen érvényes pozíció
        for (int i = 0; i < maxAttempts; i++) {
            int randomIndex = (int) (Math.random() * tiles.size());
            Point candidate = tiles.get(randomIndex);
            if (isPositionValid(candidate)) {
                return candidate;
            }
        }
        
        return tiles.get(0); // Fallback
    }

    private boolean isAdjacentToOtherTekton(Point pos, List<Tekton> otherTektons) {
        for (Tekton tekton : otherTektons) {
            String tektonName = gameLogic.getCommandProcessor().findByObject(tekton);
            if (tektonName != null && objectPositions.containsKey(tektonName)) {
                Point tektonPos = objectPositions.get(tektonName);
                
                // Ellenőrizzük, hogy a két Tekton nem érintkezik-e (3 cella távolság + 3 cella méret = 1 cella rés)
                if (Math.abs(pos.x - tektonPos.x) < 6 && Math.abs(pos.y - tektonPos.y) < 6) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isPositionValid(Point pos) {
        int maxCol = renderMap.getCols() - TEKTON_CELLS;
        int maxRow = renderMap.getRows() - TEKTON_CELLS;
        
        return pos.x >= 0 && pos.y >= 0 && pos.x <= maxCol && pos.y <= maxRow && 
            isAreaFree(pos, TEKTON_CELLS);
    }
    
    /**
     * Calculates the cardinal points of all Tekton objects on the map.
     * These points are used for drawing the égtáji pontok (green points) for the Tektons.
     * The points are stored in the tektonCardinalPoints map.
     */
    private void calculateTektonCardinalPoints() {
        tektonCardinalPoints.clear();
        int cellWidth = getWidth() / renderMap.getCols();
        int cellHeight = getHeight() / renderMap.getRows();

        for (Map.Entry<String, Point> entry : objectPositions.entrySet()) {
            String name = entry.getKey();
            Object obj = gameLogic.getCommandProcessor().getCreatedObjects().get(name);

            if (obj instanceof Tekton) {
                Point topLeft = entry.getValue();
                int x = topLeft.y * cellWidth;
                int y = topLeft.x * cellHeight;
                int width = cellWidth * TEKTON_CELLS;
                int height = cellHeight * TEKTON_CELLS;

                // Égtáji pontok számítása
                Point center = new Point(x + width / 2, y + height / 2);
                int offset = Math.min(width, height) / 2;

                List<Point> points = new ArrayList<>();
                points.add(new Point(center.x + offset, center.y)); // East (E)
                points.add(new Point(center.x, center.y - offset)); // North (N)
                points.add(new Point(center.x - offset, center.y)); // West (W)
                points.add(new Point(center.x, center.y + offset)); // South (S)

                tektonCardinalPoints.put(name, points);
            }
        }
    }

    private Point calculateThreadCenter(FungusThread thread) {
        if (thread == null) {
            System.out.println("Thread is null");
            return null;
        }

        String threadName = gameLogic.getCommandProcessor().findByObject(thread);
        if (threadName == null) {
            System.out.println("Thread name not found");
            return null;
        }

        if (!objectPositions.containsKey(threadName)) {
            System.out.println("Thread position not found in objectPositions");
            return null;
        }

        Point threadPos = objectPositions.get(threadName);
        System.out.println("Thread position: " + threadPos);

        // Calculate the center of the thread based on its starting point and ending povint
        int x = threadPos.x + (THREAD_WIDTH / 2);
        int y = threadPos.y + (THREAD_WIDTH / 2);
        return new Point(x, y);
    }


    /**
     * Returns the cardinal point of the given Tekton in the given direction.
     * The direction should be given as a lowercase string ("e", "n", "w", "s").
     * If the Tekton does not exist or the direction is invalid, returns null.
     * @param tektonName the name of the Tekton
     * @param direction the direction of the cardinal point
     * @return the cardinal point of the given Tekton in the given direction
     */
    private Point getCardinalPoint(String tektonName, String direction) {
        List<Point> points = tektonCardinalPoints.get(tektonName);
        if (points == null)
            return null;

        switch (direction.toLowerCase()) {
            case "e":
                return points.get(0); // East
            case "n":
                return points.get(1); // North
            case "w":
                return points.get(2); // West
            case "s":
                return points.get(3); // South
            default:
                return null;
        }
    }

    /**
     * Checks if the given cell area (top-left corner and size) is within the
     * bounds of the map (maxCols and maxRows).
     *
     * @param topLeft the top-left corner of the cell area
     * @param size    the size of the cell area
     * @param maxCols the maximum number of columns of the map
     * @param maxRows the maximum number of rows of the map
     * @return true if the cell area is within the bounds of the map, false
     *         otherwise
     */
    private boolean isWithinBounds(Point topLeft, int size, int maxCols, int maxRows) {
        return topLeft.x + size <= maxRows && topLeft.y + size <= maxCols;
    }

    /**
     * Checks if the given cell area (top-left corner and size) is free of
     * any occupied cells.
     * @param topLeft the top-left corner of the cell area
     * @param size    the size of the cell area
     * @return true if all cells in the area are free, false otherwise
     */
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

    /**
     * Adds all cells in the given cell area (top-left corner and size) to
     * the occupiedCells set. This is used to keep track of which cells are
     * currently occupied by a Tekton.
     * @param topLeft the top-left corner of the cell area
     * @param size    the size of the cell area
     */
    private void occupyArea(Point topLeft, int size) {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Point cell = new Point(topLeft.x + row, topLeft.y + col);
                occupiedCells.add(cell);
            }
        }
    }

    /**
     * Calculates the center point of a Tekton object in pixel coordinates.
     * @param tekton the Tekton object to calculate the center for
     * @return the center point of the Tekton in pixel coordinates, or null if
     *         the Tekton doesn't have a position
     */
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

    /**
     * Returns the position of the given Tekton as a Point object in cell
     * coordinates, or null if the Tekton doesn't have a position.
     * @param tekton the Tekton object to get the position for
     * @return the position of the Tekton in cell coordinates, or null if
     *         it doesn't have a position
     */
    private Point getTektonPosition(Tekton tekton) {
        String tektonName = gameLogic.getCommandProcessor().findByObject(tekton);
        if (tektonName != null && objectPositions.containsKey(tektonName)) {
            return objectPositions.get(tektonName);
        }
        return null;
    }


    /**
     * Updates the game state by clearing the current object positions and
     * occupied cells and setting a flag to recalculate the positions of all
     * objects. Finally, the panel is repainted to reflect the changes.
     * This method should be called whenever the game state has changed, such as
     * after a command has been processed.
     */
    public void updateGameState() {
        objectPositions.clear(); // Force recalculation of positions
        occupiedCells.clear(); // Clear occupied cells as positions are being recalculated
        shouldRecalculatePositions = true; // Set the flag to recalculate positions
        repaint();
    }

    /**
     * Sets the positions of all objects in the game world.
     * 
     * @param newPositions a map containing the new positions of objects, 
     *                     where the key is the object name and the value is 
     *                     its position on the board.
     */

    public void setObjectPositions(Map<String, Point> newPositions) {
        this.objectPositions = newPositions;
    }

    /**
     * Returns the current positions of all objects in the game world.
     * 
     * @return a map containing the positions of objects, where the key is the
     *         object name and the value is its position on the board.
     */
    public Map<String, Point> getObjectPositions() {
        return objectPositions;
    }

    /**
     * Initializes and displays the main game window for the Fungorium Game.
     * This method creates a JFrame containing the game panel and a control panel 
     * with buttons to update the view, save the game state, and load the game state.
     * 
     * The game panel is initialized with the provided command processor and is added 
     * to the frame. The control panel buttons trigger actions on the game panel, 
     * such as updating the view, saving the current game state to a file, and 
     * loading the game state from a file.
     * 
     * The frame is set to be visible and centered on the screen.
     * 
     * @param commandProcessor the CommandProcessor used to handle game commands and logic.
     */

    public static void createAndShowGUI(GameLogic gameLogic) {
        JFrame frame = new JFrame("Fungorium Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        FungoriumGamePanel gamePanel = new FungoriumGamePanel(gameLogic);
        frame.add(gamePanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Add a simple control panel
        JPanel controlPanel = new JPanel();

        // Update View button
        JButton updateButton = new JButton("Update View");
        updateButton.addActionListener(e -> gamePanel.updateGameState());
        controlPanel.add(updateButton);

        // Save Game button
        JButton saveButton = new JButton("Save Game");
        saveButton.addActionListener(e -> {
            saver.saveGameState(gamePanel.getObjectPositions(), "gameState.xml");
            JOptionPane.showMessageDialog(frame, "Game state saved to gameState.xml");
        });
        controlPanel.add(saveButton);

        // Load Game button
        JButton loadButton = new JButton("Load Game");
        loadButton.addActionListener(e -> {
            Map<String, Point> loadedPositions = saver.loadGameState("gameState.xml");
            gamePanel.setObjectPositions(loadedPositions);
            gamePanel.updateGameState();
            JOptionPane.showMessageDialog(frame, "Game state loaded from gameState.xml");
        });
        controlPanel.add(loadButton);

        frame.add(controlPanel, BorderLayout.SOUTH);
    }
}