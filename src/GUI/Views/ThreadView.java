package GUI.Views;

import fungus.FungusBody;
import fungus.FungusThread;
import tektonTypes.Tekton;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.List;
import java.util.Map;

import commands.CommandProcessor;

public class ThreadView {
    private static final int THREAD_WIDTH = 3;
    private static final Color THREAD_COLOR = new Color(150, 75, 0);
    private Color[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA};
    
    /**
     * Draws all FungusThreads in the game world.
     *
     * @param g2d                 the Graphics2D object to draw the threads on
     * @param objectPositions     a map of object names to their positions
     * @param createdObjects      a map of object names to their instances
     * @param tektonCardinalPoints a map of Tekton names to their cardinal points
     * @param commandProcessor    the CommandProcessor to resolve object names
     */
    public void drawThreads(Graphics2D g2d, Map<String, Point> objectPositions,
                            Map<String, Object> createdObjects,
                            Map<String, List<Point>> tektonCardinalPoints,
                            CommandProcessor commandProcessor) {
        for (Map.Entry<String, Object> entry : createdObjects.entrySet()) {
            if (entry.getValue() instanceof FungusThread) {
                FungusThread thread = (FungusThread) entry.getValue();
                String threadName = entry.getKey();

                try {
                    if (thread.isBridge()) {
                        drawBridgeThread(g2d, thread, objectPositions, tektonCardinalPoints, commandProcessor);
                    } else if (thread.getNext() != null) {
                        drawConnectionToNextThread(g2d, thread, threadName, objectPositions, commandProcessor);
                    } else {
                        drawNonBridgeThread(g2d, thread, threadName, objectPositions, tektonCardinalPoints, commandProcessor);
                    }

                    // Draw thread name
                    Point pos = objectPositions.get(threadName);
                    if (pos != null) {
                        g2d.setColor(Color.WHITE);
                        g2d.drawString(threadName, pos.x, pos.y);
                    }
                } catch (Exception e) {
                    System.err.println("Error drawing thread: " + threadName + " - " + e.getMessage());
                }
            }
        }
    }

    private void drawBridgeThread(Graphics2D g2d, FungusThread thread, Map<String, Point> objectPositions,
                                  Map<String, List<Point>> tektonCardinalPoints, CommandProcessor commandProcessor) {
        List<Tekton> tektons = thread.getTektons();
        if (tektons.size() >= 2) {
            Point firstControlPoint = findClosestCardinalPoint(
                tektons.get(0), tektonCardinalPoints, objectPositions.get(commandProcessor.findByObject(tektons.get(1))), commandProcessor
            );
            Point secondControlPoint = findClosestCardinalPoint(
                tektons.get(1), tektonCardinalPoints, objectPositions.get(commandProcessor.findByObject(tektons.get(0))), commandProcessor
            );
            drawLine(g2d, firstControlPoint, secondControlPoint);
        }
    }

    private void drawConnectionToNextThread(Graphics2D g2d, FungusThread thread, String threadName,
                                            Map<String, Point> objectPositions, CommandProcessor commandProcessor) {
        String nextThreadName = commandProcessor.findByObject(thread.getNext());
        if (nextThreadName != null && objectPositions.containsKey(nextThreadName)) {
            Point threadPos = objectPositions.getOrDefault(threadName, objectPositions.get(nextThreadName));
            Point nextThreadPos = objectPositions.get(nextThreadName);
            drawLine(g2d, threadPos, nextThreadPos);
        }
    }

    private void drawNonBridgeThread(
        Graphics2D g2d, 
        FungusThread thread, 
        String threadName,
        Map<String, Point> objectPositions,
        Map<String, List<Point>> tektonCardinalPoints, 
        CommandProcessor commandProcessor
    ) {
        if (thread.getTektons().isEmpty()) return;
        Tekton tekton = thread.getTektons().get(0);
        
        FungusBody body = thread.getMyBody();
        if (body == null) return;
        
        String bodyName = commandProcessor.findByObject(body);
        if (bodyName == null || !objectPositions.containsKey(bodyName)) return;
        
        Point bodyPos = objectPositions.get(bodyName);
        
        Point controlPoint = findClosestCardinalPoint(
            tekton, 
            tektonCardinalPoints, 
            bodyPos,
            commandProcessor
        );
        
        if (controlPoint != null) {
            drawLine(g2d, controlPoint, bodyPos);
        }
    }

    private Point findClosestCardinalPoint(
        Tekton tekton, 
        Map<String, List<Point>> tektonCardinalPoints,
        Point targetPoint, 
        CommandProcessor commandProcessor
    ) {
        String tektonName = commandProcessor.findByObject(tekton);
        if (tektonName == null || targetPoint == null) {
            return null;
        }

        List<Point> cardinalPoints = tektonCardinalPoints.get(tektonName);
        if (cardinalPoints == null || cardinalPoints.isEmpty()) {
            return null;
        }

        // Convert targetPoint from grid coordinates to pixel coordinates
        int cellWidth = 800 / 25;
        int cellHeight = 800 / 25;
        Point pixelTarget = new Point(
            targetPoint.y * cellWidth + (cellWidth / 2),
            targetPoint.x * cellHeight + (cellHeight / 2)
        );

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

    private void drawLine(Graphics2D g2d, Point start, Point end) {
        if (start != null && end != null) {
            int randomIndex = (int) (Math.random() * colors.length);
            g2d.setColor(colors[randomIndex]);
            g2d.setStroke(new BasicStroke(THREAD_WIDTH));
            g2d.draw(new Line2D.Double(start.x, start.y, end.x, end.y));
        }
    }
}