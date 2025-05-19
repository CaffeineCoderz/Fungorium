package GUI;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

import fungus.FungusBody;
import fungus.FungusThread;
import insect.Insect;
import logic.GameLogic;
import sporeTypes.*;
import tektonTypes.*;
import GUI.RenderMap.MapSize;
import GUI.Views.*;
import utils.*;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

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
    // Flag to indicate if positions need recalculation
    private boolean shouldRecalculatePositions = true;
    private boolean growthThreadCalled = false;
    private boolean eatInsectCalled = false;
    private boolean waitingForTarget = false;
    private boolean cutThreadCalled = false;
    private boolean moveCalled = false;
    public String origin;
    private boolean growThreadRepeated= false;
    private boolean eatSporeCalled = false;

    // Sizes
    private static final int THREAD_WIDTH = 3;
    
    // Number of cells occupied by a Tekton
    private static final int TEKTON_CELLS = 3; // 3x3 cella (9 cella)

    private static int[][] directions = {
        // Cardinal directions (4-way)
        {3,0}, {2,2},{0,3}, {-2,2}, {-3,0}, {-2,-2},{0,-3},{2,-2},
        // Diagonal directions (8-way)
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

    private List<String> selectedObjects = new ArrayList<>();
    private StatusView statusView1;
    private StatusView statusView2;


    private String currentPlayerName = "";
    private String currentRound = "";

    public FungoriumGamePanel(GameLogic gameLogic, FungoriumGUIBuilder guiBuilder) {
        this.gameLogic = gameLogic;
        saver = new GameStateHandler(gameLogic);
        this.guiBuilder = guiBuilder;
        setLayout(null); // Absolute positioning for overlay panels
        setPreferredSize(new Dimension(800, 800));
        renderMap = new RenderMap(gameLogic.getMapSize());

        // Set the size of the map size in each view
        
        // Body View Setup
        bodyView.setMapSize(gameLogic.getMapSize());
        bodyView.setCommandProcessor(gameLogic.getCommandProcessor());
        bodyView.setGuiBuilder(guiBuilder);
        
        // Spore View Setup
        sporeView.setMapSize(gameLogic.getMapSize());

        // Insect View Setup
        insectView.setMapSize(gameLogic.getMapSize());
        insectView.setCommandProcessor(gameLogic.getCommandProcessor());
        insectView.setGuiBuilder(guiBuilder);

        // Thread View Setup
        threadView.setGuiBuilder(guiBuilder);

        // Initialize the status view

        statusView1 = new StatusView();
        statusView2 = new StatusView();
        statusView1.setBounds(0, 0, 800, 800); // Bal oldal
        statusView2.setBounds(-200, 0, 800, 800); // Jobb oldal
        add(statusView1);
        add(statusView2);

        addMouseListener(
            new MouseAdapter() {
/**
 * Handles mouse click events on the game panel.
 * <p>
 * This method determines the object at the clicked point and executes
 * actions based on the current state of the game and user interaction.
 * If the game is in a "waiting for target" state due to a previous action
 * (such as growing a thread, eating an insect, cutting a thread, or moving),
 * it attempts to execute the corresponding game command after validating
 * the clicked target. If not in a waiting state, it updates the selection
 * for status panels and refreshes GUI buttons accordingly.
 *
 * @param e The MouseEvent that triggered this method.
 * <ul>
 * <li>If the clicked target is valid for the current action, a command is
 * sent to the game logic to execute the action.</li>
 * <li>If invalid, a message is shown to the user.</li>
 * <li>After executing or attempting the action, the game state and GUI are
 * updated to reflect any changes.</li>
 * <li>If not targeting an action, it updates the selected objects and
 * refreshes the status panels.</li>
 * </ul>
 */

                @Override
                public void mouseClicked(MouseEvent e) {
                    Point clickPoint = e.getPoint();
                    String clickedObjectName = getObjectAtPoint(clickPoint);

                    // --- Handle "waiting for target" actions first ---
                    if (waitingForTarget && growthThreadCalled) {
                        int num = gameLogic.getCommandProcessor().getCreatedObjects().size();
                        Object target = null;
                        boolean callednext= false;
                        try {
                            if(clickedObjectName!=null){
                                target = gameLogic.getCommandProcessor().getCreatedObjects().get(clickedObjectName);
                            }
                            if (target instanceof Tekton|| target instanceof OneThreadTekton || target instanceof DecomposingTekton || target instanceof DecreasingTekton|| target instanceof FeedThreadTekton|| target instanceof OnlyThreadTekton) {
                                String command = "growthread " + clickedObjectName + " " + origin;
                                gameLogic.getInputQueue().put(command);
                            
                            } else {
                                JOptionPane.showMessageDialog(FungoriumGamePanel.this, "Invalid target for thread growth.");
                            }
                        } catch (InterruptedException er) {
                            er.printStackTrace();
                        }
                        waitingForTarget = false;
                        growthThreadCalled = false;
                        try {
                            Thread.sleep(200); // Wait for 0.2 seconds (200 milliseconds)
                        } catch (InterruptedException ev) {
                            ev.printStackTrace();
                        }
                        System.out.println("Clicked object name: " + clickedObjectName);
                        if(gameLogic.getCommandProcessor().getCreatedObjects().size() > num){
                            //System.out.println("Thread grown.");
                            
                            updateStatusPanels();
                            positionDependentObjects();
                            revalidate();
                            repaint();
                            System.out.println("success");
                            Tekton tekton = (Tekton) target;
                            if(tekton.getSpores().size()>0&&!growThreadRepeated){
                                callednext = true;
                                growThreadRecalled();
                            }
                            if(!callednext){
                            try{
                                gameLogic.getInputQueue().put("next");
                            } catch (InterruptedException ev) {
                                ev.printStackTrace();
                            }}
                            
                        }
                        else{
                            JOptionPane.showMessageDialog(FungoriumGamePanel.this, "Thread not grown. Again");
                        }
                        SwingUtilities.invokeLater(() -> {
                            guiBuilder.updateActionButtons(controlPanel, FungoriumGamePanel.this);
                        });
                        return;
                        }
                        
                    
                    if(waitingForTarget && growThreadRepeated) {
                        int num = gameLogic.getCommandProcessor().getCreatedObjects().size();
                        origin = "th"+ gameLogic.getCommandProcessor().countObjectsOfType(FungusThread.class);
                        System.out.println("Origin: " + origin);
                        Object target = null;
                        try {
                            if(clickedObjectName!=null){
                                target = gameLogic.getCommandProcessor().getCreatedObjects().get(clickedObjectName);
                            }
                            if (target instanceof Tekton|| target instanceof OneThreadTekton || target instanceof DecomposingTekton || target instanceof DecreasingTekton|| target instanceof FeedThreadTekton|| target instanceof OnlyThreadTekton) {
                                String command = "growthread " + clickedObjectName + " " + origin;
                                gameLogic.getInputQueue().put(command);
                            } else {
                                JOptionPane.showMessageDialog(FungoriumGamePanel.this, "Invalid target for thread growth.");
                            }
                        } catch (InterruptedException er) {
                            er.printStackTrace();
                        }
                        try {
                            Thread.sleep(200); // Wait for 0.2 seconds (200 milliseconds)
                        } catch (InterruptedException ev) {
                            ev.printStackTrace();
                        }
                        if(gameLogic.getCommandProcessor().getCreatedObjects().size() > num){
                            //System.out.println("Thread grown.");
                            
                            updateStatusPanels();
                            positionDependentObjects();
                            revalidate();
                            repaint();
                            try{
                                gameLogic.getInputQueue().put("next");
                            } catch (InterruptedException ev) {
                                ev.printStackTrace();
                            }           
                        }
                        else{
                            JOptionPane.showMessageDialog(FungoriumGamePanel.this, "Thread not grown.");
                        }
                        SwingUtilities.invokeLater(() -> {
                            guiBuilder.updateActionButtons(controlPanel, FungoriumGamePanel.this);
                        });
                        
                        growThreadRepeated = false;
                        return;
                    }
                    if(waitingForTarget && eatSporeCalled){
                        Object target = null;
                        int num = gameLogic.getCommandProcessor().countObjectsOfType(Spore.class);
                        try {
                            if(clickedObjectName!=null){
                                target = gameLogic.getCommandProcessor().getCreatedObjects().get(clickedObjectName);
                            }
                            if (target instanceof Spore|| target instanceof SlowSpore || target instanceof FastSpore || target instanceof StunSpore|| target instanceof MultiplyInsectSpore|| target instanceof DisableCutSpore) {
                                String command = "eat " + clickedObjectName + " " + origin;
                                gameLogic.getInputQueue().put(command);
                            } else {
                                JOptionPane.showMessageDialog(FungoriumGamePanel.this, "Invalid target for spore eating.");
                            }
                        } catch (InterruptedException er) {
                            er.printStackTrace();
                        }
                        try {
                            Thread.sleep(500); // Wait for 0.2 seconds (200 milliseconds)
                        } catch (InterruptedException ev) {
                            ev.printStackTrace();
                        }
                        waitingForTarget = false;
                        eatSporeCalled = false;
                        SwingUtilities.invokeLater(() -> {
                            guiBuilder.updateActionButtons(controlPanel, FungoriumGamePanel.this);
                            updateStatusPanels();
                        });
                        revalidate();
                        positionInsects();
                        repaint();
                        if(gameLogic.getCommandProcessor().countObjectsOfType(Spore.class) < num){
                            //System.out.println("Spore eaten.");
                            objectPositions.remove(clickedObjectName);
                            positionDependentObjects();
                        }
                        else{
                            JOptionPane.showMessageDialog(FungoriumGamePanel.this, "Spore not eaten.");
                        }
                    }
                    if (waitingForTarget && eatInsectCalled) {
                        try {
                            if (clickedObjectName != null && gameLogic.getCommandProcessor().getCreatedObjects().get(clickedObjectName) instanceof Insect) {
                                String command = "eatinsect " + clickedObjectName + " " + origin;
                                gameLogic.getInputQueue().put(command);
                            } else {
                                JOptionPane.showMessageDialog(FungoriumGamePanel.this, "Invalid target for insect eating.");
                            }
                        } catch (InterruptedException er) {
                            er.printStackTrace();
                        }
                        waitingForTarget = false;
                        eatInsectCalled = false;
                        SwingUtilities.invokeLater(() -> {
                            guiBuilder.updateActionButtons(controlPanel, FungoriumGamePanel.this);
                            updateStatusPanels();
                        });
                        revalidate();
                        repaint();
                        try {
                            Thread.sleep(200); // Wait for 0.2 seconds (200 milliseconds)
                        } catch (InterruptedException ev) {
                            ev.printStackTrace();
                        }
                        if (!gameLogic.getCommandProcessor().getCreatedObjects().containsKey(clickedObjectName)) {
                            //System.out.println("Insect eaten.");
                            objectPositions.remove(clickedObjectName);
                            positionInsects();
                        } else {
                            JOptionPane.showMessageDialog(FungoriumGamePanel.this, "Insect not eaten.");
                        }
                        try{
                                gameLogic.getInputQueue().put("next");
                            } catch (InterruptedException ev) {
                                ev.printStackTrace();
                            }
                        return;
                    }
                    if (waitingForTarget && cutThreadCalled) {
                        Boolean cutted=false;
                        try {
                            if (clickedObjectName!=null&&gameLogic.getCommandProcessor().getCreatedObjects().get(clickedObjectName) instanceof FungusThread) {
                                String command = "cut " + clickedObjectName + " " + origin;
                                Object thr = gameLogic.getCommandProcessor().getCreatedObjects().get(clickedObjectName);
                                FungusThread thread = (FungusThread) thr;
                                if(thread.getIsDying()){
                                    JOptionPane.showMessageDialog(FungoriumGamePanel.this, "Thread is already cut.");
                                    return;
                                }
                                gameLogic.getInputQueue().put(command);
                            } else {
                                JOptionPane.showMessageDialog(FungoriumGamePanel.this, "Invalid target for thread cutting.");
                            }
                        } catch (InterruptedException er) {
                            er.printStackTrace();
                        }
                        waitingForTarget = false;
                        cutThreadCalled = false;
                        SwingUtilities.invokeLater(() -> {
                            guiBuilder.updateActionButtons(controlPanel, FungoriumGamePanel.this);
                            updateStatusPanels();
                        });
                        if(clickedObjectName!=null){
                            Object thr = gameLogic.getCommandProcessor().getCreatedObjects().get(clickedObjectName);
                            FungusThread thread = (FungusThread) thr;
                            if(thread.getIsDying()){
                                try{
                                    gameLogic.getInputQueue().put("next");
                                } catch (InterruptedException ev) {
                                    ev.printStackTrace();
                                }
                            }
                        }
                        
                        return;
                    }
                    if (waitingForTarget && moveCalled) {
                        try {
                            if (clickedObjectName!=null&&gameLogic.getCommandProcessor().getCreatedObjects().get(clickedObjectName) instanceof FungusThread) {
                                System.out.println("Clicked object name: " + clickedObjectName);
                                String command = "move " + origin + " " + clickedObjectName;
                                gameLogic.getInputQueue().put(command);
                            } else {
                                JOptionPane.showMessageDialog(FungoriumGamePanel.this, "Invalid target for moving.");
                            }
                        } catch (InterruptedException er) {
                            er.printStackTrace();
                        }
                        waitingForTarget = false;
                        moveCalled = false;
                        SwingUtilities.invokeLater(() -> {
                            guiBuilder.updateActionButtons(controlPanel, FungoriumGamePanel.this);
                            updateStatusPanels();
                            positionDependentObjects();
                            positionInsects();
                            revalidate();
                            repaint();
                        });
                        updateStatusPanels();
                        if(clickedObjectName!=null){
                            String st = getStatusText(origin);
                            if(st.contains(clickedObjectName)){
                                try{
                                    gameLogic.getInputQueue().put("next");
                                } catch (InterruptedException ev) {
                                    ev.printStackTrace();
                                }
                            }}
                        return;
                    }

                    // --- Selection logic for status panels ---
                    if (clickedObjectName != null) {
                        // Add to selection if not already present, max 2
                        if (!selectedObjects.contains(clickedObjectName)) {
                            if (selectedObjects.size() == 2) {
                                selectedObjects.remove(0); // Keep only last two
                            }
                            selectedObjects.add(clickedObjectName);
                        }
                        updateStatusPanels();
                        origin = clickedObjectName;
                        System.out.println(origin);
                    } else {
                        selectedObjects.clear();
                        updateStatusPanels();
                    }

                    // --- GUI gombok frissítése ---
                    if (controlPanel != null && guiBuilder != null) {
                        if (clickedObjectName != null) {
                            SwingUtilities.invokeLater(() -> {
                                guiBuilder.updateActionButtons(controlPanel, FungoriumGamePanel.this);
                            });
                        } else {
                            resetActionButtons();
                        }
                    }
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

    /**
     * Visszaadja a jelenleg kiválasztott objektumok nevét.
     * @return a kiválasztott objektumok nevét tartalmazó lista
     */
    public List<String> getSelectedObjects() {
        return selectedObjects;
    }

        // Setter a controlPanel beállításához
    public void setControlPanel(JPanel controlPanel) {
        this.controlPanel = controlPanel;
        // Azonnal frissíthetjük a gombokat az első megjelenítéskor, ha szükséges
        if (guiBuilder != null) {
            SwingUtilities.invokeLater(() -> {
                guiBuilder.updateActionButtons(controlPanel, this);
            });
        }
    }

/**
 * Sets the current player's name and repaints the panel.
 * 
 * @param name The name of the current player.
 */

    public void setCurrentPlayerName(String name) {
        this.currentPlayerName = name;
        repaint();
    }

    /**
     * Updates the status views according to the currently selected objects.
     * If there are no selected objects, the status views are hidden.
     * If there is one selected object, its status is displayed in the first
     * status view.
     * If there are two selected objects, their statuses are displayed in the
     * first and second status views.
     * If there are more than two selected objects, the status of the third
     * object is displayed in the first status view, and the second status
     * view is hidden.
     */
    private void updateStatusPanels() {
        // System.out.println("updateStatusPanels called. selectedObjects: " +
        // selectedObjects);
        if (selectedObjects.isEmpty()) {
            statusView1.clearStatus();
            statusView2.clearStatus();
            statusView1.setVisible(false);
            statusView2.setVisible(false);
            return;
        }

        if (selectedObjects.size() == 1) {
            String status1 = getStatusText(selectedObjects.get(0));
            statusView1.setVisible(true);
            statusView1.updateStatus(status1);
        }
        if (selectedObjects.size() == 2) {
            String status2 = getStatusText(selectedObjects.get(1));
            statusView2.setVisible(true);
            statusView2.updateStatus(status2);
        }

        if (selectedObjects.size() > 2) {
            String status1 = getStatusText(selectedObjects.get(2));
            selectedObjects.remove(0);
            selectedObjects.remove(0);
            statusView1.clearStatus();
            statusView2.clearStatus();
            statusView2.setVisible(false);
            statusView1.updateStatus(status1);
        }
        statusView1.repaint();
        statusView2.repaint();
    }

    /**
     * Executes the /status command for the given object name and returns the
     * output as a string.
     * 
     * @param objectName the name of the object for which to get the status
     * @return the status of the object as a string
     */
    protected String getStatusText(String objectName) {
        String command = "/status " + objectName;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        try {
            System.setOut(new PrintStream(outputStream));
            gameLogic.getCommandProcessor().process(command);
            System.out.flush();
            return outputStream.toString().trim();
        } finally {
            System.setOut(originalOut);
        }
    }
    

    // Metódus a gombok alaphelyzetbe állításához (pl. ha nincs kiválasztott objektum)
    private void resetActionButtons() {
        if (controlPanel != null && guiBuilder != null) {
            //controlPanel.removeAll();
            SwingUtilities.invokeLater(() -> {
                guiBuilder.updateActionButtons(controlPanel, this);
            }); // A builder metódusát hívjuk
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
        // Először nézzük a threadeket, mert ezek mennek minden felett
        for (Map.Entry<String, Point> entry : objectPositions.entrySet()) {
            String objectName = entry.getKey();
            Point objectPos = entry.getValue();

            // Alapértelmezett ellenőrzés
            if (point.distance(objectPos) <= 20) {
                if(gameLogic.getCommandProcessor().getCreatedObjects().get(objectName) instanceof FungusBody){
                    if(point.distance(objectPos) <= 15 && gameLogic.getMapSize() == MapSize.SMALL){
                        return objectName;
                    }
                    else if(point.distance(objectPos) <= 10 && gameLogic.getMapSize() == MapSize.MEDIUM){
                        return objectName;
                    }
                    else if(point.distance(objectPos) <= 7 && gameLogic.getMapSize() == MapSize.LARGE){
                        return objectName;
                    }
                }

                if(gameLogic.getCommandProcessor().getCreatedObjects().get(objectName) instanceof Insect){
                    if(point.distance(objectPos) <= 15 && gameLogic.getMapSize() == MapSize.SMALL){
                        return objectName;
                    }
                    else if(point.distance(objectPos) <= 10 && gameLogic.getMapSize() == MapSize.MEDIUM){
                        return objectName;
                    }
                    else if(point.distance(objectPos) <= 7 && gameLogic.getMapSize() == MapSize.LARGE){
                        return objectName;
                    }
                }

                if(gameLogic.getCommandProcessor().getCreatedObjects().get(objectName) instanceof Spore){
                    if(point.distance(objectPos) <= 6 && gameLogic.getMapSize() == MapSize.SMALL){
                        return objectName;
                    }
                    else if(point.distance(objectPos) <= 5 && gameLogic.getMapSize() == MapSize.MEDIUM){
                        return objectName;
                    }
                    else if(point.distance(objectPos) <= 3 && gameLogic.getMapSize() == MapSize.LARGE){
                        return objectName;
                    }
                }
            }
            // Thread kezelés
            if (objectName.startsWith("th")) {
                Object obj = gameLogic.getCommandProcessor().getCreatedObjects().get(objectName);
                if (obj instanceof FungusThread) {
                    Point start = threadEndpoints.get(objectName + "_start");
                    Point end = threadEndpoints.get(objectName + "_end");

                    if (start != null && end != null) {
                        // Extrém rövid thread speciális kezelése
                        if (start.distance(end) < 10) {
                            // Közel van-e bármelyik végponthoz
                            if (point.distance(start) <= THREAD_WIDTH * 2 ||
                                    point.distance(end) <= THREAD_WIDTH * 2) {
                                return objectName;
                            }
                            // Vagy a középponthoz
                            Point mid = new Point(
                                    (start.x + end.x) / 2,
                                    (start.y + end.y) / 2);
                            if (point.distance(mid) <= THREAD_WIDTH) {
                                return objectName;
                            }
                            continue;
                        }

                        // Normál threadek detektálása
                        double lineLength = start.distance(end);
                        double distanceToLine;

                        // Ha a thread túl rövid a szokásos módszerhez
                        if (lineLength < 20) {
                            distanceToLine = Math.min(
                                    point.distance(start),
                                    point.distance(end));
                        } else {
                            // Standard távolság számítás a vonaltól
                            distanceToLine = Line2D.ptSegDist(
                                    start.x, start.y, end.x, end.y,
                                    point.x, point.y);
                        }

                        if (distanceToLine <= THREAD_WIDTH * 1.5) {
                            return objectName;
                        }
                    }
                }
                continue;
            }
        }

        for (Map.Entry<String, Point> entry : objectPositions.entrySet()) {
            String objectName = entry.getKey();
            Point objectPos = entry.getValue();

            if (objectName.endsWith("_center")) {
                String tektonName = objectName.replace("_center", "");
                Object obj = gameLogic.getCommandProcessor().getCreatedObjects().get(tektonName);
                if (obj instanceof Tekton) {
                    int cellWidth = getWidth() / renderMap.getCols();
                    int cellHeight = getHeight() / renderMap.getRows();
                    int radius = Math.min(cellWidth, cellHeight) * TEKTON_CELLS / 2;
                    if (point.distance(objectPos) <= radius) {
                        return tektonName;
                    }
                }
                continue;
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

        if (shouldRecalculatePositions && !initialPaint || objectPositions.isEmpty()) {
            calculateObjectPositions();
            shouldRecalculatePositions = false;
        }
        /*if(objectPositions.isEmpty()){
            calculateObjectPositions();
        }*/

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
        
        // System.out.println("objectPositions size: " + objectPositions.size());

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

        if (currentPlayerName != null && !currentPlayerName.isEmpty()) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setFont(new Font("Arial", Font.BOLD, 15));
            Color borderColor = (getGuiBuilder() != null) ? getGuiBuilder().getPlayerColor(currentPlayerName)
                    : Color.GRAY;
            g2.setColor(borderColor);
            currentRound = " - " + (gameLogic.getRound() + 1) + " round";
            g2.drawString(currentPlayerName + currentRound, 10, 40);
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
        int tries = 0;
        while (initializeTektonPlacement() == false && tries < 1500) {
            objectPositions.clear(); // Force recalculation of positions
            tries++;
        }
        calculateTektonCardinalPoints(); // Maradhat külön, ha nem igényel változtatást
        positionDependentObjects();
        positionInsects();
    }

    /**
     * Initializes the positions of Tektons in the game world.
     * 
     * This method first clears the occupied cells and then positions all
     * Tektons in the order of the number of their neighbors. It uses the
     * placeTekton method to place each Tekton. If the positions do not satisfy
     * all the distance constraints, it returns false and forces recalculation
     * of positions.
     * 
     * @return true if the positions of Tektons satisfy all distance constraints,
     *         false otherwise
     */
    private boolean initializeTektonPlacement() {
        List<Point> tiles = renderMap.getTiles();
        occupiedCells.clear();

        List<Map.Entry<String, Object>> sortedTektons = gameLogic.getCommandProcessor().getCreatedObjects()
            .entrySet().stream()
            .filter(entry -> entry.getValue() instanceof Tekton)
            .sorted((e1, e2) -> Integer.compare(
                ((Tekton) e2.getValue()).getNeighbours().size(),
                ((Tekton) e1.getValue()).getNeighbours().size()))
            .collect(Collectors.toList());

        for (Map.Entry<String, Object> entry : sortedTektons) {
            if (entry.getValue() instanceof Tekton) {
                placeTekton(entry, tiles);
            }
        }
        if (!isAllDistancesCorrect()) {
             // System.out.println("Wrong calculation, so redraw");
             return false;
        }
        return true;
    }

    /**
     * Places a Tekton in the game world.
     * 
     * This method tries to place a Tekton in the game world by finding a free
     * area large enough to hold the Tekton. If the Tekton is placed
     * successfully, it marks the cells occupied by the Tekton as occupied.
     * Otherwise, it prints an error message.
     * 
     * @param entry the Map.Entry containing the Tekton object to be placed
     * @param tiles the list of available tiles in the game world
     */
    private void placeTekton(Map.Entry<String, Object> entry, List<Point> tiles) {
        boolean placed = false;
        int retries = 0;
        int maxRetries = 100;

        while (!placed && retries < maxRetries) {
            Point topLeft;
            Tekton tekton = (Tekton) entry.getValue();
                topLeft = calculateGroupedPosition(tekton, tiles);
            
            if (isWithinBounds(topLeft, TEKTON_CELLS, renderMap.getCols(), renderMap.getRows()) 
                && isAreaFree(topLeft, TEKTON_CELLS)) {

                objectPositions.put(entry.getKey(), topLeft);
                occupyArea(topLeft, TEKTON_CELLS);
                placed = true;

            } else {
                retries++;
            }
        }

        if (!placed) {
            System.err.println("Could not place Tekton: " + entry.getKey() + " after " + maxRetries + " retries.");
        }
    }

/**
 * Positions all dependent objects in the game world.
 * 
 * This method processes all created objects and assigns them positions based 
 * on their type and characteristics. It prioritizes positioning bridge threads 
 * first, followed by non-bridge threads. Spores and FungusBodies are positioned 
 * in their original order without additional sorting.
 */

    private void positionDependentObjects() {
        // 1. Bridge threadek először
        gameLogic.getCommandProcessor().getCreatedObjects().entrySet().stream()
            .filter(entry -> entry.getValue() instanceof FungusThread)
            .filter(entry -> ((FungusThread) entry.getValue()).isBridge())
            .forEach(entry -> positionFungusThread(entry.getKey(), (FungusThread) entry.getValue()));

        // 2. Nem-bridge threadek
        gameLogic.getCommandProcessor().getCreatedObjects().entrySet().stream()
            .filter(entry -> entry.getValue() instanceof FungusThread)
            .filter(entry -> !((FungusThread) entry.getValue()).isBridge())
            .forEach(entry -> positionFungusThread(entry.getKey(), (FungusThread) entry.getValue()));

        // 3. Spore-ok és Body-k eredeti sorrendben (nincs rendezés)
        gameLogic.getCommandProcessor().getCreatedObjects().forEach((name, obj) -> {
            if (obj instanceof Spore) {
                positionSpore(name, (Spore) obj);
            } else if (obj instanceof FungusBody) {
                positionFungusBody(name, (FungusBody) obj);
            }
        });
    }

    /**
     * Positions a FungusBody in the game world.
     * 
     * If the FungusBody is associated with a Tekton, it is positioned at the
     * center of that Tekton. If the Tekton is not found, the FungusBody is not
     * positioned.
     * 
     * @param name the name of the FungusBody to be positioned.
     * @param body the FungusBody to be positioned.
     */
    private void positionFungusBody(String name, FungusBody body) {
        Tekton tekton = body.getTekton();
        if (tekton != null) {
            Point center = getTektonCenter(tekton);
            if (center != null) {
                objectPositions.put(name, center);
            }
        }
    }

    
    /**
     * Positions a FungusThread in the game world.
     * 
     * This method processes a FungusThread object and assigns it a position based
     * on its type and characteristics. It takes into account whether the thread is
     * a bridge, whether its next or previous thread is a bridge, and assigns the
     * position accordingly.
     * 
     * @param name the name of the FungusThread to be positioned.
     * @param thread the FungusThread to be positioned.
     */
    private void positionFungusThread(String name, FungusThread thread) {
        List<Tekton> tektons = thread.getTektons();
        if (tektons.isEmpty()) return;

        // 1. Ha maga a szál híd
        if (thread.isBridge()) {
            handleBridgeCase(name, thread);
        }// 2. Ha következő szál híd
        else if (thread.getNext() != null && thread.getNext().isBridge()) {
            handleNextBridgeCase(name, thread);
        }
        // 2. Ha előző szál híd
        else if (thread.getPrev() != null && thread.getPrev().isBridge()) {
            handlePrevBridgeCase(name, thread);
        }
        // 4. Ha előző szál nem híd
        else if (thread.getPrev() != null && !thread.getPrev().isBridge()) {
            handlePrevNonBridgeCase(name, thread);
        }
        // 3. Ha következő szál nem híd
        else if (thread.getNext() != null && !thread.getNext().isBridge()) {
            handleNextNonBridgeCase(name, thread);
        }
        
        // 6. Alapértelmezett eset: sima szál
        else {
            handleDefaultCase(name, thread);
        }
    }

    // Segédmetódusok
    private void handleNextBridgeCase(String name, FungusThread thread) {
        FungusThread nextBridge = thread.getNext();
        String nextName = gameLogic.getCommandProcessor().findByObject(nextBridge);
        
        Point bridgeStart = threadEndpoints.get(nextName + "_start");
        Point bridgeEnd = threadEndpoints.get(nextName + "_end");
        
        Tekton tekton = thread.getTektons().get(0);
        Point center = getTektonCenter(tekton);
        
        Point bridgeConnectionPoint = (bridgeStart.distance(center) < bridgeEnd.distance(center)) 
            ? bridgeStart : bridgeEnd;
            
        setThreadPoints(name, center, bridgeConnectionPoint);
    }

    /**
     * Handles the case when the previous thread is a bridge.
     * 
     * It finds the two endpoints of the bridge and the center of the current
     * thread's tekton. It calculates which endpoint is closer to the center, and
     * assigns the position of the current thread to be halfway between that
     * endpoint and the center.
     * 
     * @param name the name of the current thread.
     * @param thread the current thread.
     */
    private void handlePrevBridgeCase(String name, FungusThread thread) {
        FungusThread prevBridge = thread.getPrev();
        String prevName = gameLogic.getCommandProcessor().findByObject(prevBridge);

        Point bridgeStart = threadEndpoints.get(prevName + "_start");
        Point bridgeEnd = threadEndpoints.get(prevName + "_end");
        
        Tekton tekton = thread.getTektons().get(0);
        Point targetCenter = getTektonCenter(tekton);
        
        Point closer = (bridgeStart.distance(targetCenter) < bridgeEnd.distance(targetCenter)) 
            ? bridgeStart : bridgeEnd;

        setThreadPoints(name, closer, targetCenter);
    }

    /**
     * Handles the case when the next thread is not a bridge.
     * 
     * It calculates the direction vector from the start to the end of the next
     * thread and adds this vector to the start of the next thread to get the
     * connection point. It then assigns the position of the current thread to
     * be halfway between the center of the current thread's tekton and this
     * connection point.
     * 
     * @param name the name of the current thread.
     * @param thread the current thread.
     */
    private void handleNextNonBridgeCase(String name, FungusThread thread) {
        FungusThread nextThread = thread.getNext();
        String nextName = gameLogic.getCommandProcessor().findByObject(nextThread);
        
        Point nextStart = threadEndpoints.get(nextName + "_start");
        Point nextEnd = threadEndpoints.get(nextName + "_end");
        
        Tekton tekton = thread.getTektons().get(0);
        Point center = getTektonCenter(tekton);
        
        if (nextStart == null || nextEnd == null) {
                //System.err.println("handleNextNonBridgeCase: nextStart or nextEnd is null for thread " + nextName);
                return;
            }

        Point dirPoint = new Point(nextEnd.x - nextStart.x, nextEnd.y - nextStart.y);

        Point connectionPoint = new Point(nextStart.x + dirPoint.x, nextStart.y+ dirPoint.y);
        setThreadPoints(name, center, connectionPoint);
    }

    /**
     * Handles the case when the previous thread is not a bridge.
     * 
     * It calculates the direction vector from the start to the end of the previous
     * thread and adds this vector to the end of the previous thread to get the
     * connection point. It then assigns the position of the current thread to
     * be at this connection point and the center of the current thread's tekton.
     * 
     * @param name the name of the current thread.
     * @param thread the current thread.
     */
    private void handlePrevNonBridgeCase(String name, FungusThread thread) {
        FungusThread prevThread = thread.getPrev();
        String prevName = gameLogic.getCommandProcessor().findByObject(prevThread);
        
        Point prevStart = threadEndpoints.get(prevName + "_start");
        Point prevEnd = threadEndpoints.get(prevName + "_end");
        
        Tekton tekton = thread.getTektons().get(0);
        Point center = getTektonCenter(tekton);

        if (prevStart == null || prevEnd == null) {
            //System.err.println("handlePrevNonBridgeCase: prevStart or prevEnd is null for thread " + prevName);
            return;
        }
        
        Point dirPoint = new Point(prevEnd.x - prevStart.x, prevEnd.y - prevStart.y);

        Point connectionPoint = new Point(prevEnd.x + dirPoint.x, prevEnd.y+ dirPoint.y);

        setThreadPoints(name, connectionPoint, center);
    }

/**
 * Handles the positioning of a FungusThread that acts as a bridge between two Tektons.
 * 
 * It retrieves the two Tektons associated with the bridge thread and calculates the closest
 * cardinal points for each Tekton. If valid points are found, it sets the thread's start 
 * and end positions to these points.
 * 
 * @param name the name of the bridge thread.
 * @param thread the FungusThread object representing the bridge.
 */

    private void handleBridgeCase(String name, FungusThread thread) {
    if (thread.getTektons().size() >= 2) {
        Tekton t1 = thread.getTektons().get(0);
        Tekton t2 = thread.getTektons().get(1);

        String t1Name = gameLogic.getCommandProcessor().findByObject(t1);
        String t2Name = gameLogic.getCommandProcessor().findByObject(t2);

        Point t2Pos = objectPositions.get(t2Name);
        Point t1Pos = objectPositions.get(t1Name);

        if (t1Pos == null || t2Pos == null) {
            System.err.println("handleBridgeCase: t1Pos or t2Pos is null for " + t1Name + " or " + t2Name);
            return;
        }

        Point p1 = findClosestCardinalPoint(t1, t2Pos);
        Point p2 = findClosestCardinalPoint(t2, t1Pos);

        if (p1 != null && p2 != null) {
            setThreadPoints(name, p1, p2);
        }
    }
}

/**
 * Handles the default positioning of a FungusThread that is not a bridge
 * and has no immediate bridge connections.
 * 
 * This method calculates the position for a standard FungusThread by 
 * finding the center of its associated Tekton and determining the 
 * closest cardinal control point. It then sets the thread's start 
 * and end points based on these calculated positions.
 * 
 * @param name the name of the FungusThread to be positioned.
 * @param thread the FungusThread to be positioned.
 */

    private void handleDefaultCase(String name, FungusThread thread) {
        Tekton tekton = thread.getTektons().get(0);
        Point center = getTektonCenter(tekton);
        Point control = findClosestCardinalPoint(tekton, center);
        
        setThreadPoints(name, center, control);
    }

    /**
     * Sets the start and end points of a FungusThread and updates the object positions
     * map with the center of the thread.
     * 
     * @param threadName the name of the FungusThread to be updated.
     * @param start the start point of the thread.
     * @param end the end point of the thread.
     */
    private void setThreadPoints(String threadName, Point start, Point end) {
        threadEndpoints.put(threadName + "_start", start);
        threadEndpoints.put(threadName + "_end", end);
        objectPositions.put(threadName, new Point(
            (start.x + end.x)/2, 
            (start.y + end.y)/2
        ));
    }
    // ehhez képi illusztráció #269 pullban
    
    /**
     * Calculates the position of a newly grown FungusThread based on its prev thread and the tekton it is connected to.
     * It handles 3 cases: 
     * 1. prev thread is a bridge and the new thread is connected to a new tekton. 
     *    In this case it calculates the closest endpoint to the new tekton and grows from there to the center of the new tekton.
     * 2. prev thread is not a bridge and the new thread is connected to a new tekton.
     *    In this case it calculates the closest endpoint to the new tekton and grows from there to the closest cardinal point of the new tekton.
     * 3. prev thread is not a bridge and the new thread is connected to the same tekton.
     *    In this case it grows from the center of the tekton to one of its cardinal points, but not the one it came from.
     * 
     * @param name the name of the newly grown FungusThread.
     * @param thread the FungusThread object representing the newly grown thread.
     */
    public void calculateGrownThreadPositions(String name, FungusThread thread){
        if (thread.getPrev() != null) {
            String prevThreadName = gameLogic.getCommandProcessor().findByObject(thread.getPrev());
            FungusThread prevThread = thread.getPrev();
            boolean prevIsBridge = prevThread.isBridge();

            // Céltekton meghatározása
            List<Tekton> currTektons = thread.getTektons();
            List<Tekton> prevTektons = prevThread.getTektons();
            // 1 eshetőség
            // prevthread bridge és tekton közepe felé növesztettünk
            // megkeressük a threadendpointsban melyik van közelebb a céltektonhoz és a
            // thread endpointból a tekton közepéhez növesztünk
            if (prevIsBridge) {
                // Megkeressük, melyik végpont van közelebb a céltektonhoz
                Tekton targetTekton = null;
                for (Tekton t : currTektons) {
                    if (!prevTektons.contains(t)) {
                        targetTekton = t;
                        break;
                    }
                }
                // ! Ezt még nem biztos hogy így kéne Ha nem találtunk céltekton-t, akkor a
                // legelső tektonhoz növesztünk
                if (targetTekton == null && !currTektons.isEmpty()) {
                    targetTekton = currTektons.get(0);
                }
                Point prevStart = threadEndpoints.get(prevThreadName + "_start");
                Point prevEnd = threadEndpoints.get(prevThreadName + "_end");
                Point targetCenter = getTektonCenter(targetTekton);

                // Válaszd ki a közelebbi végpontot
                Point closer = (prevStart.distance(targetCenter) < prevEnd.distance(targetCenter)) ? prevStart
                        : prevEnd;
                // Itt closer-től targetCenter-ig lehet növeszteni
                // Például:
                objectPositions.put(name, new Point((closer.x + targetCenter.x) / 2, (closer.y + targetCenter.y) / 2));
                threadEndpoints.put(name + "_start", closer);
                threadEndpoints.put(name + "_end", targetCenter);
            }
            // 2. eshetőség
            // prevthread nem bridge és egy másik tektonhoz növesztettünk, ekkor megkeressük
            // a thread endpointból meliyk vége a prevthreadnek van közelebb a céltektonhoz
            // és a kardinális pontból céltekton legközelebbi kardinális pontjához
            // növesztünk
            else if (!prevIsBridge && prevTektons.size() == 1 && currTektons.size() == 2) {
                // Megkeressük, melyik végpont van közelebb a céltektonhoz
                Tekton targetTekton = null;
                for (Tekton t : currTektons) {
                    if (!prevTektons.contains(t)) {
                        targetTekton = t;
                        break;
                    }
                }
                // ! Ezt még nem biztos hogy így kéne Ha nem találtunk céltekton-t, akkor a
                // legelső tektonhoz növesztünk
                if (targetTekton == null && !currTektons.isEmpty()) {
                    targetTekton = currTektons.get(0);
                }
                Point prevStart = threadEndpoints.get(prevThreadName + "_start");
                Point prevEnd = threadEndpoints.get(prevThreadName + "_end");
                Point targetCardinal = findClosestCardinalPoint(targetTekton,
                        objectPositions.get(gameLogic.getCommandProcessor().findByObject(prevTektons.get(0))));

                // Válaszd ki a közelebbi végpontot
                Point closer = (prevStart.distance(targetCardinal) < prevEnd.distance(targetCardinal)) ? prevStart
                        : prevEnd;
                // closer-től targetCardinal-ig lehet növeszteni
                // Például:
                objectPositions.put(name,
                        new Point((closer.x + targetCardinal.x) / 2, (closer.y + targetCardinal.y) / 2));
                threadEndpoints.put(name + "_start", closer);
                threadEndpoints.put(name + "_end", targetCardinal);
            }
            // 3. eshetőség
            // prevthread nem bridge és a céltekton a saját tektonja, ekkor a tekton
            // közepéből a jelenlegi tekton valamelyik kardinális pontjához növesztünk
            // lehetőleg nem ahhoz amiből növesztünk azaz kell melyik a másik vége a
            // prevthreadnek ami nem a tekton közepe
            else if (!prevIsBridge && prevTektons.size() == 1 && currTektons.size() == 1) {
                Tekton tekton = currTektons.get(0);
                Point tektonCenter = getTektonCenter(tekton);
                Point prevStart = threadEndpoints.get(prevThreadName + "_start");
                Point prevEnd = threadEndpoints.get(prevThreadName + "_end");

                // Megkeressük, melyik végpont NEM a tekton közepe
                Point notCenter = (prevStart.equals(tektonCenter)) ? prevEnd : prevStart;
                // tektonCenter-től valamelyik kardinális pontig növesztünk, de ne ahhoz, amiből
                // jöttünk
                List<Point> cardinals = tektonCardinalPoints.get(gameLogic.getCommandProcessor().findByObject(tekton));
                Point growTo = null;
                for (Point p : cardinals) {
                    if (!p.equals(notCenter)) {
                        growTo = p;
                        break;
                    }
                }
                // tektonCenter-től growTo-ig lehet növeszteni
                // Például:
                objectPositions.put(name, new Point((tektonCenter.x + growTo.x) / 2, (tektonCenter.y + growTo.y) / 2));
                threadEndpoints.put(name + "_start", tektonCenter);
                threadEndpoints.put(name + "_end", growTo);
            }
        }
    }

    /**
     * Positions a Spore object on the panel based on the position of its associated
     * Tekton object and the size of the panel.
     *
     * @param name the name of the Spore object to be positioned
     * @param spore the Spore object to be positioned
     */
    private void positionSpore(String name, Spore spore) {
        Tekton tekton = spore.getTekton();
        Point pos = getTektonPosition(tekton);
        //System.out.println("Spore: " + name + " tekton: " + tekton + " pos: " + pos);
        if (pos == null) return;

        int cellWidth = getWidth() / renderMap.getCols();
        int cellHeight = getHeight() / renderMap.getRows();

        int centerX = pos.y * cellWidth + (cellWidth * TEKTON_CELLS / 2);
        int centerY = pos.x * cellHeight + (cellHeight * TEKTON_CELLS / 2);

        int radius = Math.min(cellWidth, cellHeight) * TEKTON_CELLS / 2;
        radius -= radius * 0.2;

        int offsetX, offsetY;
        Point candidate;
        int maxTries = 100;
        int tries = 0;
        boolean valid;

        do {
            offsetX = (int) (Math.random() * radius * 2 - radius);
            offsetY = (int) (Math.random() * radius * 2 - radius);
            candidate = new Point(centerX + offsetX, centerY + offsetY);

            // Csak akkor fogadjuk el, ha elég messze van a többi spórától
            valid = true;
            for (Map.Entry<String, Point> entry : objectPositions.entrySet()) {
                if (entry.getKey().equals(name)) continue;
                Object obj = gameLogic.getCommandProcessor().getCreatedObjects().get(entry.getKey());
                if (obj instanceof Spore) {
                    if (candidate.distance(entry.getValue()) < sporeView.getSporeSize() + 1) {
                        valid = false;
                        break;
                    }
                }
            }
            tries++;
        } while ((offsetX * offsetX + offsetY * offsetY < radius * radius || !valid) && tries < maxTries);

        objectPositions.put(name, candidate);
    }

    /**
     * Positions Insect objects on the panel based on the positions of their associated
     * FungusThread objects.
     * 
     * <p>
     * The algorithm works as follows:
     * <ol>
     * <li>First, it groups all Insect objects by their associated FungusThread objects.
     * <li>Then, it iterates over all groups and positions each Insect object on the
     * panel by offsetting it from the center of its associated FungusThread object in
     * the direction of the FungusThread.
     * </ol>
     * 
     * <p>
     * The maximum number of Insect objects that can be positioned on a single
     * FungusThread is 3. If there are more than 3 Insect objects associated with a
     * single FungusThread, only the first 3 are positioned, and the rest are ignored.
     **/
       private void positionInsects() {
        // 1. Threadenként gyűjtsük a rovarokat
        Map<String, List<String>> threadToInsects = new HashMap<>();
        for (Map.Entry<String, Object> entry : gameLogic.getCommandProcessor().getCreatedObjects().entrySet()) {
            if (entry.getValue() instanceof Insect) {
                Insect insect = (Insect) entry.getValue();
                FungusThread thread = insect.getThread();
                if (thread != null) {
                    String threadName = gameLogic.getCommandProcessor().findByObject(thread);
                    if (threadName != null) {
                        threadToInsects.computeIfAbsent(threadName, k -> new ArrayList<>()).add(entry.getKey());
                    }
                }
            }
        }
    
        // 2. Minden threadre helyezzük el a rovarokat
        for (Map.Entry<String, List<String>> e : threadToInsects.entrySet()) {
            String threadName = e.getKey();
            List<String> insects = e.getValue();
            if (!objectPositions.containsKey(threadName) || insects.isEmpty()) continue;
    
            // Thread végpontjai
            Point start = threadEndpoints.get(threadName + "_start");
            Point end = threadEndpoints.get(threadName + "_end");
            Point center = objectPositions.get(threadName);
    
            if (start == null || end == null || center == null) continue;
    
            // Thread irányvektor
            double dx = end.x - start.x;
            double dy = end.y - start.y;
            double length = Math.hypot(dx, dy);
            if (length == 0)
                length = 1; // elkerülni a 0-val osztást

            // Irányvektor (unit vector) a thread mentén
            double dirX = dx / length;
            double dirY = dy / length;
            int offset = 18; // mennyire tolja el a rovart a középponttól

            // Max 3 rovar
            for (int i = 0; i < Math.min(3, insects.size()); i++) {
                String insectName = insects.get(i);
                int px = center.x;
                int py = center.y;
                if (i == 1) { // előre a thread mentén
                    px += (int) (dirX * offset);
                    py += (int) (dirY * offset);
                } else if (i == 2) { // hátra a thread mentén
                    px -= (int) (dirX * offset);
                    py -= (int) (dirY * offset);
                }
                objectPositions.put(insectName, new Point(px, py));
            }
        }
    }

/**
 * Finds the closest cardinal point associated with a given Tekton to the specified target point.
 *
 * <p>
 * The function calculates the pixel position of the target point based on the dimensions of the
 * render map and compares it against the list of cardinal points associated with the given
 * Tekton. It returns the cardinal point that is closest to the target point.
 * </p>
 *
 * @param tekton the Tekton object whose cardinal points are being evaluated
 * @param targetPoint the target point to which the distance is being measured
 * @return the closest cardinal point to the target point, or null if no valid points are found
 */

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
    
/**
 * Verifies the connections of all FungusThread objects in the game world, including
 * connections to Tekton objects and to other FungusThread and FungusBody objects.
 * 
 * This method is used for debugging purposes and is not part of the normal game
 * logic.
 */
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
/**
 * Generates an array of direction vectors representing the vertices of a 
 * regular polygon with a specified number of sides.
 * 
 * Each direction vector is calculated based on the angle from the center 
 * of the polygon, ensuring that the polygon is centered and oriented 
 * such that one vertex is at the top. The radius of the polygon increases 
 * slightly with the number of sides.
 * 
 * @param sides the number of sides of the polygon
 * @return a 2D array where each element is a pair of integers representing 
 *         the x and y components of a direction vector to a vertex
 */

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

    /**
     * Determines if the given number of sides is a special case for the
     * purposes of generating a regular polygon with the generatePolygonDirections
     * method. A special case is when the number of sides is 4 or less, or
     * exactly 8. In this case, special adjustments are needed to ensure that
     * the generated polygon is aesthetically pleasing and does not exceed the
     * boundaries of the game panel.
     *
     * @param sides the number of sides of the polygon
     * @return true if the given number of sides is a special case, false otherwise
     */
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
        }/*
        Collections.sort(neighbors, (e1, e2) -> {
            int n1 =  e1.getNeighbours().size();
            int n2 =  e2.getNeighbours().size();
            return Integer.compare(n2, n1); // Csökkenő sorrend
        });*/
        // Dinamikus irányok generálása
        int[][] dynamicDirections = isSpecialCase(neighbors.get(0).getNeighbours().size()) ?  directions :// Használjuk a fix mátrixot 4,8,12 esetén
        generatePolygonDirections(neighbors.get(0).getNeighbours().size());
        // Normál elhelyezési logika
        dynamicDirections = Arrays.stream(dynamicDirections)
            .sorted((a, b) -> ThreadLocalRandom.current().nextInt(-1, 2))
            .toArray(int[][]::new);
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
    
    /**
     * Ellenőrzi, hogy a Tektonok közötti távolságok megfelelnek-e a szabályoknak.
     * Ha bármelyik Tekton helytelenül van elhelyezve, akkor false értéket ad vissza.
     * A szabályok a következők:
     * - Szomszédok esetén: 4-6 cella
     * - Nem szomszédok esetén: minimum 7 cella
     * @return true, ha minden Tekton helyes pozícióban van, false egyébként
     */
    private Boolean isAllDistancesCorrect() {
        Map<String, Object> objects = gameLogic.getCommandProcessor().getCreatedObjects();
        List<Tekton> allTektons = new ArrayList<>();
        int problems = 0;
        // 1. Összes Tekton gyűjtése
        for (Object obj : objects.values()) {
            if (obj instanceof Tekton) {
                allTektons.add((Tekton) obj);
            }
        }

        // 2. Páronkénti ellenőrzés
        for (int i = 0; i < allTektons.size(); i++) {
            Tekton t1 = allTektons.get(i);
            Point p1 = getTektonPosition(t1);
            if (p1 == null) continue;

            for (int j = i + 1; j < allTektons.size(); j++) {
                Tekton t2 = allTektons.get(j);
                Point p2 = getTektonPosition(t2);
                if (p2 == null) continue;

                // 3. Szomszéd viszony ellenőrzése
                boolean areNeighbours = t1.getNeighbours().contains(t2) || t2.getNeighbours().contains(t1);
                
                // 4. Középpontok számítása
                Point c1 = new Point(p1.x + TEKTON_CELLS/2, p1.y + TEKTON_CELLS/2);
                Point c2 = new Point(p2.x + TEKTON_CELLS/2, p2.y + TEKTON_CELLS/2);
                double centerDistance = Math.hypot(c1.x - c2.x, c1.y - c2.y);
                String tekt1 = gameLogic.getCommandProcessor().findByObject(t1);
                String tekt2 = gameLogic.getCommandProcessor().findByObject(t2);
                // 5. Távolság szabályok alkalmazása
                if (areNeighbours) {
                    // Szomszédokra: 4-6 cella (9+ szomszédnál 5-6)
                    int min = (t1.getNeighbours().size() >=9 || t2.getNeighbours().size() >=9) ? 5 : 4;
                    if (centerDistance < min || centerDistance > 6) {
                       
                        /* System.err.println("Szomszéd távolsági hiba: " 
                            + tekt1 + " - " + tekt2 
                            + " (" + centerDistance + " cella)"); */

                        problems++;
                      }
                } else {
                    // Nem szomszédokra: minimum 7 cella (top-left pozíciók)
                    double edgeDistance = Math.hypot(p1.x - p2.x, p1.y - p2.y);
                    if (edgeDistance < 7) {
                        /* System.err.println("Nem szomszéd távolsági hiba: " 
                            + tekt1 + " - " + tekt2 
                            + " (" + edgeDistance + " cella)"); */
                        problems++;
                    }
                }
            }
        }
        if (problems > 1) {
            return false;
        }
        return true;
    }
/**
 * Calculates the placement distance for a given Tekton object based on its number of neighbors.
 * The distance is scaled up with the number of neighbors to ensure appropriate spacing.
 *
 * @param tekton The Tekton object for which to calculate the placement distance.
 * @return The calculated placement distance.
 */

    private int calculatePlacementDistance(Tekton tekton) {
        int neighborCount = tekton.getNeighbours().size();
        // Erősebb skálázás több szomszédnál
        return 2 + (int)(neighborCount * 0.2);
    }
    //?
    
    /**
     * Finds a fallback position for a Tekton object when the normal placement logic fails.
     * This method tries to find a valid position by moving in a series of concentric circles
     * around each of the Tekton's neighbors. If no valid position is found, it falls back to
     * finding an optimal random position.
     * 
     * @param tekton The Tekton object for which to find a fallback position.
     * @param neighbors The list of Tekton objects that are neighbors of the given Tekton.
     * @param minDistance The minimum distance from any other Tekton that the fallback position
     *                    must satisfy.
     * @return The fallback position for the given Tekton, or null if no valid position can be found.
     */
    private Point findFallbackPosition(Tekton tekton, List<Tekton> neighbors, int minDistance) {
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
    
    /**
     * Finds an optimal random position for a Tekton object in the game world.
     * This method first tries to find a position that is far enough from all other
     * Tektons, and if that fails, it falls back to finding any valid position.
     * If no valid position can be found, it returns the first tile in the list.
     * 
     * @param tiles The list of all tiles in the game world.
     * @param otherTektons The list of all other Tekton objects in the game world.
     * @return The optimal random position for the Tekton object, or the first tile
     *         in the list if no valid position can be found.
     */
    private Point findOptimalRandomPosition(List<Point> tiles, List<Tekton> otherTektons) {
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
    
/**
 * Checks if the given position is adjacent to any of the specified Tekton objects.
 *
 * This method determines adjacency by checking if the position is within a
 * certain distance of any Tekton in the provided list.
 *
 * @param pos The position to check for adjacency.
 * @param tektonsToCheck The list of Tekton objects to check against.
 * @return true if the position is adjacent to any Tekton in the list, false otherwise.
 */

    private boolean isAdjacentToOtherTekton(Point pos, List<Tekton> tektonsToCheck) {
        return isWithinDistance(pos, tektonsToCheck, TEKTON_CELLS + 1);
    }
    
    /**
     * Checks if the given position is within a certain distance of any Tekton
     * in the provided list.
     * 
     * This method determines adjacency by checking if the position is within a
     * certain distance of any Tekton in the provided list.
     * 
     * @param pos The position to check for adjacency.
     * @param tektonsToCheck The list of Tekton objects to check against.
     * @param minDistance The minimum distance required for adjacency.
     * @return true if the position is adjacent to any Tekton in the list, false
     *         otherwise.
     */
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

/**
 * Checks if the specified position is valid within the game world.
 *
 * This method verifies that the given position is within the bounds of the
 * game map and that the area around the position is free for placement.
 * The position is considered valid if it is non-negative, fits within the
 * map's dimensions considering an effective size buffer, and does not
 * overlap with any occupied areas.
 *
 * @param pos The position to validate.
 * @return true if the position is valid and free for use, false otherwise.
 */

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

/**
 * Calculates the center point of a given FungusThread.
 *
 * This method retrieves the position of the thread from the objectPositions map
 * and calculates its center based on the starting and ending points. If the
 * thread or its position cannot be found, it logs an appropriate message and
 * returns null.
 *
 * @param thread the FungusThread for which the center point is to be calculated.
 * @return the Point representing the center of the FungusThread, or null if the
 *         thread or its position cannot be found.
 */

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
    private void occupyArea(Point topLeft,int size) {
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
        this.objectPositions = newPositions != null ? new TreeMap<>(newPositions) : new TreeMap<>();
        this.occupiedCells.clear();
        this.shouldRecalculatePositions = false;
        repaint();
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
     * Ends the current turn and triggers the next one. This method is called
     * whenever the "End Turn" button is clicked. If the "Break Tekton" flag is
     * set, it triggers the breaking of a random Tekton and then sets the flag
     * to false. Finally, it updates the action buttons on the control panel to
     * reflect the new game state.
     */
    public void endTurnLogic(){
        try {
        if (gameLogic.getBreaking()) {
            breakTektonEvent();
            gameLogic.setBreak(false);
        }
        if (gameLogic.isNewRound()) {
            livingObjecstUpdate();
            gameLogic.setNewRound(false);
        }
        calculateTektonCardinalPoints();
        
        positionDependentObjects();
        
        positionInsects();
        
        revalidate();
        repaint();
        gameLogic.getInputQueue().put("next");
        SwingUtilities.invokeLater(() -> {
            guiBuilder.updateActionButtons(controlPanel, FungoriumGamePanel.this);
        });
        
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
    }
    /**
     * Triggers the breaking of a random Tekton.
     * This method first creates a list of all Tektons in the game world and
     * shuffles it. Then it selects the first Tekton from the list and removes its
     * threads and spores from the game world. Finally, it triggers the breaking
     * of the Tekton and updates the positions of all objects in the game world.
     */
    private void breakTektonEvent(){
        List<Map.Entry<String, Object>> listOfTektons = gameLogic.getCommandProcessor().getCreatedObjects()
            .entrySet().stream()
            .filter(entry -> entry.getValue() instanceof Tekton)
            .collect(Collectors.toList());
        Collections.shuffle(listOfTektons);

        int randomIndex = RandomGenerator.generateRandomNumber(0, listOfTektons.size()-1);
        String[] parts = new String[2];
        parts[1] = listOfTektons.get(randomIndex).getKey();
        //Törés a modellben
        Tekton origTekton = (Tekton) listOfTektons.get(randomIndex).getValue();
        List<FungusThread> threads = origTekton.getThreads();
        for (FungusThread thread : threads) {
            String objKey = gameLogic.getCommandProcessor().findByObject(thread);
            gameLogic.getCommandProcessor().getCreatedObjects().remove(objKey);
        }
        List<Spore> spores = origTekton.getSpores();
        for (Spore spore : spores) {
            String objKey = gameLogic.getCommandProcessor().findByObject(spore);
            gameLogic.getCommandProcessor().getCreatedObjects().remove(objKey);
        }
        gameLogic.getCommandProcessor().processBreakCommand(parts);

        Map<String, Object> currentObjects = gameLogic.getCommandProcessor().getCreatedObjects();
        List<Map.Entry<String, Object>> newTektons = currentObjects
            .entrySet().stream()
            .filter(entry -> entry.getValue() instanceof Tekton && !listOfTektons.contains(entry))
            .collect(Collectors.toList());
        //Új tekton egyikét az eredeti tekton helyére helyezés.
        Point newPointt1 = objectPositions.get(parts[1]);

        objectPositions.put(newTektons.get(0).getKey(), newPointt1);
        
        
        livingObjecstUpdate();

        List<Map.Entry<String, Object>> tttt = currentObjects.entrySet().stream()
            .filter(entry -> entry.getValue() instanceof Tekton)
            .collect(Collectors.toList());

        List<Tekton> othTektons = new ArrayList<>();
        for (Map.Entry<String, Object> entry : tttt){
            othTektons.add((Tekton) entry.getValue());
        }
        Point newPointt2 = findOptimalRandomPosition(renderMap.getTiles(), othTektons);
        occupyArea(newPointt2, TEKTON_CELLS);
        objectPositions.put(newTektons.get(1).getKey(), newPointt2);
        
         // Szomszédságok automatikus beállítása
        setNeighborsForNewTekton((Tekton) newTektons.get(1).getValue(), newPointt2,3, 6);
        
        calculateTektonCardinalPoints();
        positionDependentObjects();
        repaint();
    }
/**
 * Sets the neighbors for a newly created Tekton based on its position and
 * distance constraints.
 * 
 * This method iterates through all existing Tektons in the game, calculating
 * the distance from each to the new Tekton. If the distance is within the
 * specified minimum and maximum distance range, the Tektons are set as neighbors
 * of each other.
 * 
 * @param newTekton the newly created Tekton for which neighbors are to be set
 * @param newTektonPos the position of the new Tekton in the game world
 * @param minDist the minimum distance required for a Tekton to be considered a neighbor
 * @param maxDist the maximum distance allowed for a Tekton to be considered a neighbor
 */

    private void setNeighborsForNewTekton(Tekton newTekton, Point newTektonPos, int minDist, int maxDist) {
        List<Tekton> allTektons = gameLogic.getCommandProcessor()
            .getCreatedObjects()
            .values()
            .stream()
            .filter(obj -> obj instanceof Tekton && !((Tekton)obj == newTekton))
            .map(obj -> (Tekton)obj)
            .collect(Collectors.toList());

        double newCenterX = newTektonPos.x + TEKTON_CELLS/2.0;
        double newCenterY = newTektonPos.y - TEKTON_CELLS/2.0;
        for (Tekton existingTekton : allTektons) {
            Point existingPos = getTektonPosition(existingTekton);
            if (existingPos != null) {
                double existingCenterX = existingPos.x + TEKTON_CELLS/2.0;
                double existingCenterY = existingPos.y - TEKTON_CELLS/2.0;
                
                double distance = Math.hypot(
                    newCenterX - existingCenterX,
                    newCenterY - existingCenterY
                );  
                
                // Szomszédsági feltételek ellenőrzése
                if (distance >= minDist && distance <= maxDist) {
                    // Kölcsönös szomszédviszony létrehozása
                    newTekton.addNeighbour(existingTekton);
                    existingTekton.addNeighbour(newTekton);
                    
                    System.out.println("Automatikus szomszéd: " + newTekton 
                        + " <-> " + existingTekton
                        + " (" + (int)distance + " cella)");
                }
            }
        }
    }
    
    /**
     * Updates the objectPositions and threadEndpoints maps by removing any
     * entries that no longer exist in the currentObjects map.
     * 
     * This method is used to clean up the object positions and thread endpoints
     * after a game state has been loaded from a save file. It is called in the
     * constructor of the GamePanel class.
     */
    private void livingObjecstUpdate(){
        Map<String, Object> currentObjects = gameLogic.getCommandProcessor().getCreatedObjects();
        Iterator<Map.Entry<String, Point>> iterator = objectPositions.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Point> entry = iterator.next();
            if (!currentObjects.containsKey(entry.getKey())) {
                System.out.println("Remove: " + entry.getKey());
                iterator.remove();
            }
        }
        Iterator<Map.Entry<String, Point>> iterator2 = threadEndpoints.entrySet().iterator();
        while (iterator2.hasNext()) {
            Map.Entry<String, Point> entry = iterator2.next();
            if (!currentObjects.containsKey(entry.getKey())) {
                System.out.println("Remove: " + entry.getKey());
                iterator2.remove();
            }
        }
    }
/**
 * Starts the process of growing a new thread from the currently selected
 * object. This method is called when the user selects the "Grow thread" action
 * button.
 * 
 * The method sets the waitingForTarget flag to true, indicating that the
 * program is waiting for the user to select a target object in the game world.
 * It also displays a message dialog to the user, instructing them to select a
 * target object.
 */
    public void growThreadLogic(){ // Ezt az objektumot választotta ki kiindulásnak
        waitingForTarget = true; // Most várunk egy célkijelölést
        growthThreadCalled=true;
        // Esetleg üzenet a felhasználónak:
        JOptionPane.showMessageDialog(this, "Válassz ki egy cél objektumot a térképen!");
    }
    /**
     * Starts the process of growing a new body from the currently selected
     * object. This method is called when the user selects the "Grow body" action
     * button.
     * 
     * The method adds the "growbody" command to the input queue of the game
     * logic, with the currently selected object as the parameter. After that,
     * it waits for 0.5 seconds (500 milliseconds) to allow the game logic to
     * process the command. Finally, it updates the action buttons, status
     * panels, and positions of dependent objects, and repaints the panel.
     * 
     * @see #growThreadLogic()
     */
    public void growBodyLogic(){ // Ezt az objektumot választotta ki kiindulásnak
        try {
        //System.out.println("growbody called" + origin);
        gameLogic.getInputQueue().put("growbody "+ origin);
        try {
            Thread.sleep(200); // Wait for 0.5 seconds (500 milliseconds)
        } catch (InterruptedException ev) {
            ev.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            guiBuilder.updateActionButtons(controlPanel, FungoriumGamePanel.this);
            updateStatusPanels();
            positionDependentObjects();
            revalidate();
            repaint();
        });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    /**
     * Starts the process of sporing the currently selected object. This method
     * is called when the user selects the "Sporulate" action button.
     * 
     * The method adds the "sporulate" command to the input queue of the game
     * logic, with the currently selected object as the parameter. After that,
     * it waits for 0.5 seconds (500 milliseconds) to allow the game logic to
     * process the command. Finally, it updates the action buttons, status
     * panels, and positions of dependent objects, and repaints the panel.
     */
    public void sporulateLogic(){ // Ezt az objektumot választotta ki kiindulásnak
        try {
            gameLogic.getInputQueue().put("sporulate "+ origin);
            SwingUtilities.invokeLater(() -> {
                guiBuilder.updateActionButtons(controlPanel, FungoriumGamePanel.this);
                updateStatusPanels();
                positionDependentObjects();
                revalidate();
                repaint();
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * Starts the process of eating an insect from the currently selected
     * object. This method is called when the user selects the "Eat insect" action
     * button.
     * 
     * The method adds the "eatinsect" command to the input queue of the game
     * logic, with the currently selected object as the parameter. After that,
     * it waits for 0.5 seconds (500 milliseconds) to allow the game logic to
     * process the command. Finally, it updates the action buttons, status
     * panels, and positions of dependent objects, and repaints the panel.
     */
    public void eatInsectLogic(){
        waitingForTarget = true; // Most várunk egy célkijelölést
        eatInsectCalled=true;
        // Esetleg üzenet a felhasználónak:
        JOptionPane.showMessageDialog(this, "Válaszd ki a megevendő rovart a térképen!");
    }
    /**
     * Starts the process of cutting a FungusThread object from the currently
     * selected object. This method is called when the user selects the "Cut
     * thread" action button.
     * 
     * The method sets the waitingForTarget flag to true, indicating that the
     * program is waiting for the user to select a target object in the game
     * world. It also displays a message dialog to the user, instructing them to
     * select a target object.
     */
    public void cutThreadLogic(){
        waitingForTarget = true; // Most várunk egy célkijelölést
        cutThreadCalled=true;
        // Esetleg üzenet a felhasználónak:
        JOptionPane.showMessageDialog(this, "Válaszd ki a elvágandó fonalat a térképen!");
    }

/**
 * Initiates the process of moving an insect to a target location.
 * This method sets the waitingForTarget and moveCalled flags to true,
 * indicating that the program is waiting for the user to select a target
 * location on the game map. It also displays a message dialog to the user,
 * instructing them to select a target location.
 */

    public void moveLogic(){
        waitingForTarget = true; // Most várunk egy célkijelölést
        moveCalled=true;
        // Esetleg üzenet a felhasználónak:
        JOptionPane.showMessageDialog(this, "Válaszd ki a célhelyet a térképen!");
    }


    /**
     * Starts the process of growing a new FungusThread object from the currently
     * selected object, as if the user had clicked the "Grow thread" action button
     * again.
     * 
     * The method sets the waitingForTarget flag to true, indicating that the
     * program is waiting for the user to select a target object in the game world.
     * It also displays a message dialog to the user, instructing them to select a
     * target object.
     */
    public void growThreadRecalled(){
        waitingForTarget = true; // Most várunk egy célkijelölést
        growThreadRepeated=true;
        // Esetleg üzenet a felhasználónak:
        JOptionPane.showMessageDialog(this, "Válassz ki egy cél objektumot a térképen!");
    }

    public void eatSporeLogic(){
        waitingForTarget = true; // Most várunk egy célkijelölést
        eatSporeCalled=true;
        // Esetleg üzenet a felhasználónak:
        JOptionPane.showMessageDialog(this, "Válaszd ki a megevendő spórát a térképen!");
    }
  
/**
 * Sets the border color of the player's panel.
 *
 * This method changes the border color of the current player's panel
 * to the specified color and repaints the panel to reflect the change.
 *
 * @param color the new border color to be set.
 */

/**
 * Sets the border color of the player's panel.
 *
 * This method changes the border color of the current player's panel
 * to the specified color and repaints the panel to reflect the change.
 *
 * @param color the new border color to be set.
 */


    public void setPlayerBorderColor(Color color) {
        setBorder(BorderFactory.createLineBorder(color, 6));
        repaint();
    }

    /**
     * Returns the FungoriumGUIBuilder object used to build the game's UI.
     * This method is used to access the GUI builder object from outside the
     * FungoriumGamePanel class.
     * @return the FungoriumGUIBuilder object used to build the game's UI.
     */
    public FungoriumGUIBuilder getGuiBuilder() {
        return guiBuilder;
    }
    /**
     * Returns the map of thread endpoints. The keys of the map are the names of
     * the threads, and the values are the endpoints of the threads as Point
     * objects. The endpoints are stored as "threadName_start" and
     * "threadName_end".
     * 
     * @return the map of thread endpoints.
     */
    public Map<String, Point> getThreadEndpoints() {
        return threadEndpoints;
    }
    /**
     * Sets the map of thread endpoints. The keys of the map are the names of
     * the threads, and the values are the endpoints of the threads as Point
     * objects. The endpoints are stored as "threadName_start" and
     * "threadName_end". This method is used to update the thread endpoints
     * map when the game state is loaded from a file.
     * @param threadEndpoints the new map of thread endpoints.
     */
    public void setThreadEndpoints(Map<String, Point> threadEndpoints) {
        this.threadEndpoints = threadEndpoints;
    }
    /**
     * Returns the map of cardinal points of Tektons. The keys of the map are the
     * names of the Tektons, and the values are lists of Point objects
     * representing the cardinal points of the Tektons. This map is used to
     * position the Tektons in the game world.
     * @return the map of cardinal points of Tektons.
     */
    public Map<String, List<Point>> getTektonCardinalPoints() {
        return tektonCardinalPoints;
    }
    /**
     * Sets the map of cardinal points of Tektons. The keys of the map are the
     * names of the Tektons, and the values are lists of Point objects
     * representing the cardinal points of the Tektons. This map is used to
     * position the Tektons in the game world. This method is used to update the
     * tekton cardinal points map when the game state is loaded from a file.
     * @param tektonCardinalPoints the new map of cardinal points of Tektons.
     */
    public void setTektonCardinalPoints(Map<String, List<Point>> tektonCardinalPoints) {
        this.tektonCardinalPoints = tektonCardinalPoints;
    }
    /**
     * Returns the set of occupied cells in the game world. The occupied cells
     * are the cells where Tektons are placed. This set is used to position the
     * Tektons in the game world.
     * @return the set of occupied cells in the game world.
     */
    public Set<Point> getOccupiedCells() {
        return occupiedCells;
    }
/**
 * Sets the occupied cells in the game world.
 *
 * This method updates the set of occupied cells with the provided set.
 * The occupied cells are the locations where Tektons are placed, and
 * this set is used to track and manage their positions within the game
 * world.
 *
 * @param occupiedCells the new set of occupied cells to be set.
 */

    public void setOccupiedCells(Set<Point> occupiedCells) {
        this.occupiedCells = occupiedCells;
    }
/**
 * Sets the list of currently selected objects.
 * 
 * This method updates the list of selected objects with the provided list.
 * If the provided list is null, it initializes an empty list.
 *
 * @param selectedObjects the list of object names to be set as selected,
 *                        or null to clear the selection.
 */

    public void setSelectedObjects(List<String> selectedObjects) {
        this.selectedObjects = selectedObjects != null ? new ArrayList<>(selectedObjects) : new ArrayList<>();
    }
}