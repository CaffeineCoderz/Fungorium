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
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.awt.image.BufferedImage;

public class FungoriumGamePanel extends JPanel {
    // Handlers
    private GameLogic gameLogic;
    private RenderMap renderMap;
    private static GameStateHandler saver;
    private JPanel controlPanel;
    private FungoriumGUIBuilder guiBuilder;

    // Positions and cells
    private Map<String, Point> objectPositions = new TreeMap<>();
    private Set<Point> occupiedCells = new HashSet<>();
    public String pickedObject; // Currently selected object
    // Flag to indicate if positions need recalculation
    private boolean shouldRecalculatePositions = true;
    
    // Sizes
    private static final int THREAD_WIDTH = 3;
    
    // Number of cells occupied by a Tekton
    private static final int TEKTON_CELLS = 3; // 3x3 cella (9 cella)

    private static int[][] directions = {
        // Cardinal directions (4-way)
        {4,0}, {0,4}, {-4,0}, {0,-4},
        // Diagonal directions (8-way)
        {3,3}, {3,-3}, {-3,3}, {-3,-3},
        // 12-way directions for polygon support
        {4,2}, {4,-2}, {-4,2}, {-4,-2},
        {2,4}, {2,-4}, {-2,4}, {-2,-4}
    };
    // Background image
    private Image backgroundImage;
    private Boolean initialPaint = true; // Flag to indicate if it's the first paint

    // Thread grow
    private Map<String, List<Point>> tektonCardinalPoints = new HashMap<>(); // Tekton égtáji pontjai
    private Map<String, Point> threadEndpoints = new HashMap<>(); // Thread végpontok tárolása
    private List<Line2D> possibleGrowthLines = new ArrayList<>(); // Lehetséges növekedési irányok
    private Map<FungusThread, String> threadDirections = new HashMap<>();

    //! View osztályok
    private TektonView tektonView = new TektonView();
    private SporeView sporeView = new SporeView();
    private BodyView bodyView = new BodyView();
    private InsectView insectView = new InsectView();
    private ThreadView threadView = new ThreadView();
    private StatusView statusView;

    public FungoriumGamePanel(GameLogic gameLogic, FungoriumGUIBuilder guiBuilder) {
        this.gameLogic = gameLogic;
        saver = new GameStateHandler(gameLogic);
        this.guiBuilder = guiBuilder;
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
                    pickedObject = null; 

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
                            
                            String[] lines = status.split(System.lineSeparator()); // Sorokra bontjuk a kimenetet
                            if (lines.length >= 2) {
                                pickedObject = lines[1].trim(); // A második sor a típus (index 1)
                            }
                            if(clickPoint.x < 400){
                                statusView.moveToRightPosition();
                            }else{
                                statusView.moveToLeftPosition();
                            }
                            statusView.updateStatus(status); // Update the status view
                            // Frissítjük a gombokat a vezérlőpanelen
                            if (controlPanel != null && guiBuilder != null) {
                                // Itt kellene meghívni egy metódust, ami frissíti a gombokat
                                // Ehhez az addActionButtons metódus logikáját ki kell szervezni
                                guiBuilder.updateActionButtons(controlPanel, FungoriumGamePanel.this);
                            }
                        } catch (Exception ex) {
                            System.err.println("Error executing command: " + ex.getMessage());
                        } finally {
                            System.setOut(originalOut); // Restore original System.out
                        }
                    } else {
                        statusView.clearStatus(); // Clear the status view if no valid object is clicked
                        if (controlPanel != null) {
                            resetActionButtons();
                        }
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
    }

        // Setter a controlPanel beállításához
    public void setControlPanel(JPanel controlPanel) {
        this.controlPanel = controlPanel;
        // Azonnal frissíthetjük a gombokat az első megjelenítéskor, ha szükséges
        if (guiBuilder != null) {
            guiBuilder.updateActionButtons(controlPanel, this); // Első frissítés
        }
    }

    

    // Metódus a gombok alaphelyzetbe állításához (pl. ha nincs kiválasztott objektum)
    private void resetActionButtons() {
        if (controlPanel != null && guiBuilder != null) {
            controlPanel.removeAll();
            guiBuilder.updateActionButtons(controlPanel, this); // A builder metódusát hívjuk
            controlPanel.revalidate();
            controlPanel.repaint();
        }
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

            // Check if the object is a Tekton center
            if (objectName.endsWith("_center")) {
                String tektonName = objectName.replace("_center", ""); // Get the Tekton name
                Object obj = gameLogic.getCommandProcessor().getCreatedObjects().get(tektonName);

                if (obj instanceof Tekton) {
                    // Calculate the Tekton's circle
                    int cellWidth = getWidth() / renderMap.getCols();
                    int cellHeight = getHeight() / renderMap.getRows();
                    int radius = Math.min(cellWidth, cellHeight) * TEKTON_CELLS / 2;

                    // Check if the click is within the Tekton's circle
                    if (point.distance(objectPos) <= radius) {
                        return tektonName; // Return the Tekton name
                    }
                }
            }

            if(objectName.startsWith("th")){
                Object obj = gameLogic.getCommandProcessor().getCreatedObjects().get(objectName);
                if(obj instanceof Thread){
                    Point start = threadEndpoints.get(objectName + "_start");
                    Point end = threadEndpoints.get(objectName + "_end");
                    if (start != null && end != null) {
                        Line2D line = new Line2D.Double(start, end);
                        if (line.ptSegDist(point) <= THREAD_WIDTH) {
                            return objectName; // Return the thread name
                        }
                    }
                }
            }

            // Check if the click is within the bounds of the object
            if (point.distance(objectPos) <= 20) { // Adjust the radius as needed
                return objectName;
            }
        }
        return null;
    }

    /**
     * Custom component method to draw the game state on the screen.
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
        if(objectPositions.isEmpty()){
            calculateObjectPositions();
        }

        // Draw the grid (optional) RED
        //drawGrid(g2d);

        // Draw all objects
        tektonView.drawTektons(g2d, objectPositions, gameLogic.getCommandProcessor().getCreatedObjects(),
        getWidth() / renderMap.getCols(), getHeight() / renderMap.getRows(), TEKTON_CELLS);

        // Égtáji pontok (zöld pontok) rajzolása
        g2d.setColor(Color.GREEN);
        for (List<Point> points : tektonCardinalPoints.values()) {
            for (Point p : points) {
                g2d.fill(new Ellipse2D.Double(p.x - 5, p.y - 5, 10, 10));
            }
        }
        
        System.out.println("objectPositions size: " + objectPositions.size());

        threadView.drawThreads(g2d, objectPositions, gameLogic.getCommandProcessor().getCreatedObjects(), tektonCardinalPoints, gameLogic.getCommandProcessor(), threadEndpoints);
        insectView.drawInsects(g2d, objectPositions, gameLogic.getCommandProcessor().getCreatedObjects());
        bodyView.drawBodies(g2d, objectPositions, gameLogic.getCommandProcessor().getCreatedObjects());
        sporeView.drawSpores(g2d, objectPositions, gameLogic.getCommandProcessor().getCreatedObjects());
        
        g2d.setColor(Color.RED);
        for (Map.Entry<String, Point> entry : objectPositions.entrySet()) {
            //System.out.println("Object: " + entry.getKey() + " at " + entry.getValue());
            if (entry.getKey().startsWith("th")) { // threads
                Point p = entry.getValue();
                // System.out.println("Thread: " + entry.getKey() + " at " + p);
                g2d.fillOval(p.x - 3, p.y - 3, 6, 6);
            }
        }

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
        initializeTektonPlacement();
        calculateTektonCardinalPoints(); // Maradhat külön, ha nem igényel változtatást
        positionDependentObjects();
        positionInsects();
    }

    private void initializeTektonPlacement() {
        List<Point> tiles = renderMap.getTiles();
        occupiedCells.clear();
        boolean first = true;

        List<Map.Entry<String, Object>> sortedTektons = gameLogic.getCommandProcessor().getCreatedObjects()
            .entrySet().stream()
            .filter(entry -> entry.getValue() instanceof Tekton)
            .sorted((e1, e2) -> Integer.compare(
                ((Tekton) e2.getValue()).getNeighbours().size(),
                ((Tekton) e1.getValue()).getNeighbours().size()))
            .collect(Collectors.toList());

        for (Map.Entry<String, Object> entry : sortedTektons) {
            if (entry.getValue() instanceof Tekton) {
                placeTekton(entry, tiles, first);
                first = false;
            }
        }
    }

    private void placeTekton(Map.Entry<String, Object> entry, List<Point> tiles, boolean first) {
        boolean placed = false;
        int retries = 0;
        int maxRetries = 100;

        while (!placed && retries < maxRetries) {
            Point topLeft;
            Tekton tekton = (Tekton) entry.getValue();

            if (first) {
                topLeft = new Point(renderMap.getCols()/2 - TEKTON_CELLS/2,
                                    renderMap.getRows()/2 - TEKTON_CELLS/2);
            } else {
                topLeft = calculateGroupedPosition(tekton, tiles);
            }

            if (isWithinBounds(topLeft, TEKTON_CELLS, renderMap.getCols(), renderMap.getRows()) 
                && isAreaFree(topLeft, TEKTON_CELLS)) {

                objectPositions.put(entry.getKey(), topLeft);
                occupyArea(topLeft, 0, TEKTON_CELLS);
                placed = true;

            } else {
                retries++;
            }
        }

        if (!placed) {
            System.err.println("Could not place Tekton: " + entry.getKey() + " after " + maxRetries + " retries.");
        }
    }

    private void positionDependentObjects() {
        for (Map.Entry<String, Object> entry : gameLogic.getCommandProcessor().getCreatedObjects().entrySet()) {
            Object obj = entry.getValue();
            String name = entry.getKey();

            if (obj instanceof Spore) {
                positionSpore(name, (Spore) obj);
            } else if (obj instanceof FungusThread) {
                positionFungusThread(name, (FungusThread) obj);
            } else if (obj instanceof FungusBody) {
                positionFungusBody(name, (FungusBody) obj);
            }
        }
    }

    private void positionFungusBody(String name, FungusBody body) {
        Tekton tekton = body.getTekton();
        if (tekton != null) {
            Point center = getTektonCenter(tekton);
            if (center != null) {
                objectPositions.put(name, center);
            }
        }
    }

    private void positionFungusThread(String name, FungusThread thread) {
        List<Tekton> tektons = thread.getTektons();
        if (tektons.isEmpty()) return;

        if (thread.isBridge()) {
            if (tektons.size() >= 2) {
                Tekton t1 = tektons.get(0);
                Tekton t2 = tektons.get(1);
                Point p1 = findClosestCardinalPoint(t1, objectPositions.get(gameLogic.getCommandProcessor().findByObject(t2)));
                Point p2 = findClosestCardinalPoint(t2, objectPositions.get(gameLogic.getCommandProcessor().findByObject(t1)));

                if (p1 != null && p2 != null) {
                    objectPositions.put(name, new Point((p1.x + p2.x) / 2, (p1.y + p2.y) / 2));
                    threadEndpoints.put(name + "_start", p1);
                    threadEndpoints.put(name + "_end", p2);
                } else {
                    System.err.println("Control points could not be calculated for thread: " + name);
                }
            }
        } else {
            Tekton tekton = tektons.get(0);
            FungusBody body = thread.getMyBody();
            if (body == null) return;

            Point center = getTektonCenter(tekton);
            Point control = findClosestCardinalPoint(tekton, center);

            if (center != null && control != null) {
                objectPositions.put(name, new Point((center.x + control.x) / 2, (center.y + control.y) / 2));
                threadEndpoints.put(name + "_start", center);
                threadEndpoints.put(name + "_end", control);
            } else {
                System.err.println("Control points could not be calculated for thread: " + name);
            }
        }
    }

    private void positionSpore(String name, Spore spore) {
        Tekton tekton = spore.getTekton();
        Point pos = getTektonPosition(tekton);
        if (pos == null) return;

        int cellWidth = getWidth() / renderMap.getCols();
        int cellHeight = getHeight() / renderMap.getRows();

        int centerX = pos.y * cellWidth + (cellWidth * TEKTON_CELLS / 2);
        int centerY = pos.x * cellHeight + (cellHeight * TEKTON_CELLS / 2);

        int radius = Math.min(cellWidth, cellHeight)* TEKTON_CELLS/2;
        radius -= radius * 0.2;

        int offsetX, offsetY;
        do {
            offsetX = (int) (Math.random() * radius * 2 - radius);
            offsetY = (int) (Math.random() * radius * 2 - radius);
        } while (offsetX * offsetX + offsetY * offsetY < radius * radius);

        objectPositions.put(name, new Point(centerX + offsetX, centerY + offsetY));
    }

    private void positionInsects() {
        for (Map.Entry<String, Object> entry : gameLogic.getCommandProcessor().getCreatedObjects().entrySet()) {
            if (entry.getValue() instanceof Insect) {
                Insect insect = (Insect) entry.getValue();
                FungusThread thread = insect.getThread();

                if (thread != null) {
                    String threadName = gameLogic.getCommandProcessor().findByObject(thread);
                    if (threadName != null && objectPositions.containsKey(threadName)) {
                        Point threadPos = objectPositions.get(threadName);
                        objectPositions.put(entry.getKey(), new Point(threadPos.x, threadPos.y - 13));
                    }
                }
            }
        }
    }

    private Point findClosestCardinalPoint(
            Tekton tekton,
            Point targetPoint) {
        String tektonName = gameLogic.getCommandProcessor().findByObject(tekton);
        if (tektonName == null || targetPoint == null) {
            return null;
        }
        
        List<Point> cardinalPoints = tektonCardinalPoints.get(tektonName);
        if (cardinalPoints == null || cardinalPoints.isEmpty()) {
            return null;
        }

        int cellWidth = 800 / renderMap.getCols();
        //int cellHeight = getHeight() / renderMap.getRows();
        int cellHeight = 800 / renderMap.getRows();
        Point pixelTarget = new Point(
                targetPoint.y * cellWidth + (cellWidth / 2),
                targetPoint.x * cellHeight + (cellHeight / 2));

        Point closest = cardinalPoints.get(0);
        double minDistance = closest.distance(pixelTarget);

        for (int i = 1; i < cardinalPoints.size(); i++) {
            double distance = cardinalPoints.get(i).distance(pixelTarget);
            if (distance < minDistance) {
                minDistance = distance;
                closest = cardinalPoints.get(i);
            }
        }

        return closest;
    }
    
    private void verifyThreadConnections() {
        System.out.println("\n=== THREAD CONNECTION VERIFICATION ===");
        for (Map.Entry<String, Object> entry : gameLogic.getCommandProcessor().getCreatedObjects().entrySet()) {
            if (entry.getValue() instanceof FungusThread) {
                FungusThread thread = (FungusThread) entry.getValue();
                System.out.println("\nThread: " + entry.getKey());

                // Verify tekton connections
                System.out.println("Connected to tektons:");
                for (Tekton tekton : thread.getTektons()) {
                    String tektonName = gameLogic.getCommandProcessor().findByObject(tekton);
                    System.out.println("- " + tektonName + " (exists: " +
                            objectPositions.containsKey(tektonName) + ")");
                }

                // Verify body connection
                if (thread.getNextBody() != null) {
                    String bodyName = gameLogic.getCommandProcessor().findByObject(thread.getNextBody());
                    System.out.println("Connects to body: " + bodyName + " (exists: " +
                            objectPositions.containsKey(bodyName) + ")");
                }

                // Verify thread connection
                if (thread.getNext() != null) {
                    String nextThreadName = gameLogic.getCommandProcessor().findByObject(thread.getNext());
                    System.out.println("Connects to thread: " + nextThreadName + " (exists: " +
                            objectPositions.containsKey(nextThreadName) + ")");
                }
            }
        }
    }
    private static int[][] generatePolygonDirections(int sides) {
        int[][] dirs = new int[sides][2];
        double angleStep = 2 * Math.PI / sides;
        double radius = 2.5 + sides * 0.15; // Alap sugár
        
        for (int i = 0; i < sides; i++) {
            double angle = i * angleStep + Math.PI/2;
            dirs[i][0] = (int) Math.round(radius * Math.cos(angle));
            dirs[i][1] = (int) Math.round(radius * Math.sin(angle));
        }
        return dirs;
    }

    private static boolean isSpecialCase(int sides) {
        return sides <= 4 || sides == 8;
    }
    //?
    private Point calculateGroupedPosition(Tekton tekton, List<Point> tiles) {
        List<Tekton> neighbors = tekton.getNeighbours();
        
        int placementDistance = calculatePlacementDistance(tekton);
        // Ha nincsenek szomszédok, véletlenszerű pozíció
        if (neighbors.isEmpty()) {
            return findOptimalRandomPosition(tiles, gameLogic.getCommandProcessor().getCreatedObjects().values().stream()
            .filter(obj -> obj instanceof Tekton)
            .map(obj -> (Tekton) obj)
            .filter(t -> t != tekton)
            .collect(Collectors.toList()));
        }
        Collections.sort(neighbors, (e1, e2) -> {
            int n1 =  e1.getNeighbours().size();
            int n2 =  e2.getNeighbours().size();
            return Integer.compare(n2, n1); // Csökkenő sorrend
        });
        // Dinamikus irányok generálása
        int[][] dynamicDirections = isSpecialCase(neighbors.get(0).getNeighbours().size()) ?  directions :// Használjuk a fix mátrixot 4,8,12 esetén
        generatePolygonDirections(neighbors.get(0).getNeighbours().size());
        // Normál elhelyezési logika
        for (Tekton neighbor : neighbors) {
            Point neighborPos = getTektonPosition(neighbor);
            if (neighborPos == null) continue;

            // Próbálkozás minden dinamikus irányban
            for (int[] dir : dynamicDirections) {
                Point newPos = new Point(
                    neighborPos.x + dir[0] * placementDistance,
                    neighborPos.y + dir[1] * placementDistance
                );
                
                if (isPositionValid(newPos) && !isAdjacentToOtherTekton(newPos, neighbors)) {
                    return newPos;
                }
            }
        }
    
        // Tartalék megoldás
        return findFallbackPosition(tekton, neighbors, placementDistance+1);
    }
    
    private int calculatePlacementDistance(Tekton tekton) {
        int neighborCount = tekton.getNeighbours().size();
        // Erősebb skálázás több szomszédnál
        return 2 + (int)(neighborCount * 0.05);
    }
    //?
    private Point findFallbackPosition(Tekton tekton, List<Tekton> neighbors, int minDistance) {
        System.out.println("FallBack");
        // Dinamikus irányok generálása
        int[][] dynamicDirections = isSpecialCase(neighbors.get(0).getNeighbours().size()) ?  directions : // Használjuk a fix mátrixot 4,8,12 esetén
        generatePolygonDirections(neighbors.get(0).getNeighbours().size());
        for (int distance = minDistance; distance < 8; distance++) {
            for (Tekton neighbor : neighbors) {
                Point neighborPos = getTektonPosition(neighbor);
                if (neighborPos == null) continue;
    
                for (int[] dir : dynamicDirections) {
                    Point newPos = new Point(
                        neighborPos.x + dir[0] * distance,
                        neighborPos.y + dir[1] * distance
                    );
                    
                    if (isPositionValid(newPos)) {
                        return newPos;
                    }
                }
            }
        }
        return findOptimalRandomPosition(renderMap.getTiles(), gameLogic.getCommandProcessor().getCreatedObjects().values().stream()
        .filter(obj -> obj instanceof Tekton)
        .map(obj -> (Tekton) obj)
        .filter(t -> t != tekton)
        .collect(Collectors.toList()));
    }
    //?
    private Point findOptimalRandomPosition(List<Point> tiles, List<Tekton> otherTektons) {
        System.out.println("FallBack");
        int maxAttempts = 100;
        List<Point> validPositions = new ArrayList<>();
        int minDistance = TEKTON_CELLS + 1; // 3+1=4 cella minimális távolság
        
        // Első körben olyan pozíciók amik elég távol vannak más Tektonektől
        for (int i = 0; i < maxAttempts; i++) {
            int randomIndex = (int) (Math.random() * tiles.size());
            Point candidate = tiles.get(randomIndex);
            if (isPositionValid(candidate) && !isWithinDistance(candidate, otherTektons, minDistance)) {
                validPositions.add(candidate);
            }
        }
        
        if (!validPositions.isEmpty()) {
            return validPositions.get((int)(Math.random() * validPositions.size()));
        }
        
        // Tartalék: bármilyen érvényes pozíció
        for (int i = 0; i < maxAttempts; i++) {
            int randomIndex = (int) (Math.random() * tiles.size());
            Point candidate = tiles.get(randomIndex);
            if (isPositionValid(candidate)) {
                return candidate;
            }
        }
        
        return tiles.get(0); // Vésztartalék
    }
    //?
    private boolean isAdjacentToOtherTekton(Point pos, List<Tekton> tektonsToCheck) {
        return isWithinDistance(pos, tektonsToCheck, TEKTON_CELLS + 1);
    }
    private boolean isWithinDistance(Point pos, List<Tekton> tektonsToCheck, int minDistance) {
        for (Tekton tekton : tektonsToCheck) {
            Point tektonPos = getTektonPosition(tekton);
            if (tektonPos != null) {
                int dx = Math.abs(pos.x - tektonPos.x);
                int dy = Math.abs(pos.y - tektonPos.y);
                if (dx < minDistance && dy < minDistance) {
                    return true;
                }
            }
        }
        return false;
    } 


    private boolean isPositionValid(Point pos) {
        // Szigorúbb távolságellenőrzés
        int buffer = 1; // Dinamikus buffer
        int effectiveSize = TEKTON_CELLS + buffer;
        
        return pos.x >= 0 && pos.y >= 0 
            && pos.x + effectiveSize <= renderMap.getCols()
            && pos.y + effectiveSize <= renderMap.getRows()
            && isAreaFree(pos, effectiveSize);
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

        Map<String, Point> newEntries = new HashMap<>();
        List<String> keysToRemove = new ArrayList<>();

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

                // Add the center position to objectPositions
                // Calculate the center of the Tekton's circle
                int centerX = topLeft.y * cellWidth + (cellWidth * TEKTON_CELLS / 2);
                int centerY = topLeft.x * cellHeight + (cellHeight * TEKTON_CELLS / 2);

                // Prepare modifications
                newEntries.put(name + "_center", new Point(centerX, centerY));
                
                // Remove the old center position if it exists, currently not used, but can be useful when it is needed
                //! keysToRemove.add(name);
            }
        }
        // Apply modifications after iteration
        /*
        ! for (String key : keysToRemove) {
        !    objectPositions.remove(key);
        ! }
        */
        objectPositions.putAll(newEntries);
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

        // Calculate the center of the thread based on its starting point and ending point
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
    private void occupyArea(Point topLeft, int start,int size) {
        // Pontosabb területfoglalás
        int expansion = size > 6 ? 1 : 0; // Csak nagy struktúrákhoz adjunk hozzá
        for (int row = -expansion; row < size + expansion; row++) {
            for (int col = -expansion; col < size + expansion; col++) {
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
}