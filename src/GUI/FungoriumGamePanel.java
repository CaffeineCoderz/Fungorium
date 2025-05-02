package GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.*;
import java.util.List;

import commands.CommandProcessor;
import fungus.*;
import insect.*;
import sporeTypes.*;
import tektonTypes.*;
import GUI.Views.*;
import GUI.Views.GameObjectView;

public class FungoriumGamePanel extends JPanel {
    // Handlers
    private CommandProcessor commandProcessor;
    private RenderMap renderMap;
    private static GameStateHandler saver = new GameStateHandler();
    
    // Views and positions
    private Map<String, GameObjectView> gameObjectViews = new HashMap<>();
    private Map<String, Point> objectPositions = new HashMap<>();
    private Set<Point> occupiedCells = new HashSet<>();
    
    // Constants
    private static final int TEKTON_CELLS = 3;
    
    // Background
    private Image backgroundImage;
    private JTextArea statusTextArea;

    public FungoriumGamePanel(CommandProcessor commandProcessor) {
        this.commandProcessor = commandProcessor;
        setPreferredSize(new Dimension(800, 800));
        renderMap = new RenderMap(RenderMap.MapSize.MEDIUM);
        initializeUI();
        loadResources();
    }
    
    private void initializeUI() {
        // Status text area
        statusTextArea = new JTextArea(5, 20);
        statusTextArea.setEditable(false);
        statusTextArea.setLineWrap(true);
        statusTextArea.setWrapStyleWord(true);
        statusTextArea.setBackground(new Color(240, 240, 240));
        statusTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        statusTextArea.setFont(new Font("Arial", Font.PLAIN, 12));
        setLayout(null);
        add(statusTextArea);
        statusTextArea.setBounds(600, 10, 180, 100);
        
        // Mouse listener
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                GameObjectView clickedView = getViewAtPoint(e.getPoint());
                if (clickedView != null) {
                    showStatus(clickedView.getObjectName());
                }
            }
        });
    }
    
    private void loadResources() {
        try {
            backgroundImage = ImageIO.read(new File("src/resources/PanelBg/gamePanel.jpg"));
        } catch (Exception e) {
            System.err.println("Error loading background image: " + e.getMessage());
        }
    }
    
    private GameObjectView getViewAtPoint(Point point) {
        for (GameObjectView view : gameObjectViews.values()) {
            if (view.contains(point)) {
                return view;
            }
        }
        return null;
    }
    
    private void showStatus(String objectName) {
        String command = "/status " + objectName;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        try {
            System.setOut(new PrintStream(outputStream));
            commandProcessor.process(command);
            System.out.flush();
            String status = outputStream.toString();
            statusTextArea.setText(status);
        } finally {
            System.setOut(originalOut);
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        drawTiledBackground(g2d);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        calculateObjectPositions();
        
        // Draw all views
        Collection<GameObjectView> tempViews = gameObjectViews.values();
        for (GameObjectView view : tempViews) {
            // Debug: Draw cardinal points for Tekton views
            if (view instanceof TektonView) {
                view.draw(g2d);
                g2d.setColor(Color.GREEN);
                for (Point p : ((TektonView)view).getCardinalPoints()) {
                    g2d.fill(new Ellipse2D.Double(p.x, p.y, 10, 10));
                }
            }
        }
        for (GameObjectView view : tempViews) {
            // Debug: Draw cardinal points for Tekton views
            if (view instanceof ThreadView) {
                view.draw(g2d);
            }
        }
        for (GameObjectView view : tempViews) {
            // Debug: Draw cardinal points for Tekton views
            if (view instanceof BodyView) {
                view.draw(g2d);
            }
        }
        for (GameObjectView view : tempViews) {
            // Debug: Draw cardinal points for Tekton views
            if (view instanceof SporeView) {
                view.draw(g2d);
            }
        }
        for (GameObjectView view : tempViews) {
            // Debug: Draw cardinal points for Tekton views
            if (view instanceof InsectView) {
                view.draw(g2d);
            }
        }
        
    }
    
    private void drawTiledBackground(Graphics2D g2d) {
        if (backgroundImage != null) {
            int originalWidth = backgroundImage.getWidth(this);
            int originalHeight = backgroundImage.getHeight(this);
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
    
    private void calculateObjectPositions() {
        List<Point> tiles = renderMap.getTiles();
        int maxRetries = 100;
        occupiedCells.clear();
        gameObjectViews.clear();
        
        int cellWidth = getWidth() / renderMap.getCols();
        int cellHeight = getHeight() / renderMap.getRows();

        // First pass - place tektons
        for (Map.Entry<String, Object> entry : commandProcessor.getCreatedObjects().entrySet()) {
            if (entry.getValue() instanceof Tekton) {
                placeTekton(entry.getKey(), (Tekton)entry.getValue(), tiles, maxRetries, cellWidth, cellHeight);
            }
        }
        
        // Second pass - place other objects
        for (Map.Entry<String, Object> entry : commandProcessor.getCreatedObjects().entrySet()) {
            String name = entry.getKey();
            Object obj = entry.getValue();
            
            if (obj instanceof FungusBody) {
                placeFungusBody(name, (FungusBody)obj);
            } else if (obj instanceof FungusThread) {
                placeFungusThread(name, (FungusThread)obj);
            } else if (obj instanceof Spore) {
                placeSpore(name, (Spore)obj, cellWidth, cellHeight);
            } else if (obj instanceof Insect) {
                placeInsect(name, (Insect)obj);
            }
        }
    }
    
    private void placeTekton(String name, Tekton tekton, List<Point> tiles, int maxRetries, int cellWidth, int cellHeight) {
        boolean placed = false;
        int retries = 0;
        
        while (!placed && retries < maxRetries) {
            int randomIndex = (int)(Math.random() * tiles.size());
            Point topLeft = tiles.get(randomIndex);
            
            if (isWithinBounds(topLeft, TEKTON_CELLS, renderMap.getCols(), renderMap.getRows()) && 
                isAreaFree(topLeft, TEKTON_CELLS)) {
                
                // Calculate pixel position
                int x = topLeft.y * cellWidth;
                int y = topLeft.x * cellHeight;
                Point pixelPosition = new Point(x, y);
                
                objectPositions.put(name, pixelPosition);
                occupyArea(topLeft, TEKTON_CELLS);
                gameObjectViews.put(name, new TektonView(name, pixelPosition, tekton, cellWidth, cellHeight));
                placed = true;
            } else {
                retries++;
            }
        }
        
        if (!placed) {
            System.err.println("Could not place Tekton: " + name + " after " + maxRetries + " retries.");
        }
    }
    
    private void placeFungusBody(String name, FungusBody body) {
        Tekton tekton = body.getTekton();
        if (tekton != null) {
            String tektonName = commandProcessor.findByObject(tekton);
            if (tektonName != null && objectPositions.containsKey(tektonName)) {
                TektonView tektonView = (TektonView)gameObjectViews.get(tektonName);
                Point center = tektonView.getCenter();
                objectPositions.put(name, center);
                gameObjectViews.put(name, new BodyView(name, center, body));
            }
        }
    }
    
    private void placeFungusThread(String name, FungusThread thread) {
        if (!thread.getTektons().isEmpty()) {
            Tekton firstTekton = thread.getTektons().get(0);
            String firstTektonName = commandProcessor.findByObject(firstTekton);
            
            Point midpoint = calculateThreadMidpoint(name, thread, firstTektonName);
            if (midpoint != null) {
                objectPositions.put(name, midpoint);
                gameObjectViews.put(name, new ThreadView(name, midpoint, thread, gameObjectViews));
            }
        }
    }
    
    private Point calculateThreadMidpoint(String name, FungusThread thread, String firstTektonName) {
        if (thread.getNextBody() != null) {
            String nextBodyName = commandProcessor.findByObject(thread.getNextBody());
            if (firstTektonName != null && nextBodyName != null &&
                objectPositions.containsKey(firstTektonName) &&
                objectPositions.containsKey(nextBodyName)) {
                
                Point start = objectPositions.get(firstTektonName);
                Point end = objectPositions.get(nextBodyName);
                return new Point((start.x + end.x)/2, (start.y + end.y)/2);
            }
        } else if (thread.getNext() != null) {
            String nextThreadName = commandProcessor.findByObject(thread.getNext());
            if (firstTektonName != null && nextThreadName != null &&
                objectPositions.containsKey(firstTektonName) &&
                objectPositions.containsKey(nextThreadName)) {
                
                Point start = objectPositions.get(firstTektonName);
                Point end = objectPositions.get(nextThreadName);
                return new Point((start.x + end.x)/2, (start.y + end.y)/2);
            }
        }
        return null;
    }
    
    private void placeSpore(String name, Spore spore, int cellWidth, int cellHeight) {
        Tekton tekton = spore.getTekton();
        String tektonName = commandProcessor.findByObject(tekton);
        if (tektonName != null && objectPositions.containsKey(tektonName)) {
            Point tektonPos = objectPositions.get(tektonName);
            int tektonCenterX = tektonPos.x + (cellWidth * TEKTON_CELLS / 2);
            int tektonCenterY = tektonPos.y + (cellHeight * TEKTON_CELLS / 2);
            int radius = Math.min(cellWidth, cellHeight) * TEKTON_CELLS / 2;
            int margin = (int)(radius * 0.1);
            radius -= margin;
            
            int offsetX, offsetY;
            do {
                offsetX = (int)(Math.random() * radius * 2 - radius);
                offsetY = (int)(Math.random() * radius * 2 - radius);
            } while (offsetX * offsetX + offsetY * offsetY > radius * radius);
            
            Point sporePos = new Point(tektonCenterX + offsetX, tektonCenterY + offsetY);
            objectPositions.put(name, sporePos);
            gameObjectViews.put(name, new SporeView(name, sporePos, spore));
        }
    }
    
    private void placeInsect(String name, Insect insect) {
        FungusThread thread = insect.getThread();
        if (thread != null) {
            String threadName = commandProcessor.findByObject(thread);
            if (threadName != null && objectPositions.containsKey(threadName)) {
                Point threadPos = objectPositions.get(threadName);
                Point insectPos = new Point(threadPos.x + 10, threadPos.y + 10);
                objectPositions.put(name, insectPos);
                gameObjectViews.put(name, new InsectView(name, insectPos, insect));
            }
        }
    }
    
    // Helper methods
    private boolean isWithinBounds(Point topLeft, int size, int maxCols, int maxRows) {
        return topLeft.x + size <= maxRows && topLeft.y + size <= maxCols;
    }
    
    private boolean isAreaFree(Point topLeft, int size) {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Point cell = new Point(topLeft.x + row, topLeft.y + col);
                if (occupiedCells.contains(cell)) {
                    return false;
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
    
    public void updateGameState() {
        objectPositions.clear();
        occupiedCells.clear();
        repaint();
    }
    
    public void setObjectPositions(Map<String, Point> newPositions) {
        this.objectPositions = newPositions;
    }
    
    public Map<String, Point> getObjectPositions() {
        return objectPositions;
    }
    
    public static void createAndShowGUI(CommandProcessor commandProcessor) {
        JFrame frame = new JFrame("Fungorium Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        FungoriumGamePanel gamePanel = new FungoriumGamePanel(commandProcessor);
        frame.add(gamePanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        JPanel controlPanel = new JPanel();
        
        JButton updateButton = new JButton("Update View");
        updateButton.addActionListener(e -> gamePanel.updateGameState());
        controlPanel.add(updateButton);
        
        JButton saveButton = new JButton("Save Game");
        saveButton.addActionListener(e -> {
            saver.saveGameState(gamePanel.getObjectPositions(), "gameState.xml");
            JOptionPane.showMessageDialog(frame, "Game state saved to gameState.xml");
        });
        controlPanel.add(saveButton);
        
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