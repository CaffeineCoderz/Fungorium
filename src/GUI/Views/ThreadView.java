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
    public void drawThreads(Graphics2D g2d, 
                            Map<String, Point> objectPositions,
                            Map<String, Object> createdObjects,
                            Map<String, List<Point>> tektonCardinalPoints,
                            CommandProcessor commandProcessor,
                            Map<String, Point> threadEndpoints) {
        for (Map.Entry<String, Object> entry : createdObjects.entrySet()) {
            if (entry.getValue() instanceof FungusThread) {
                FungusThread thread = (FungusThread) entry.getValue();
                String threadName = entry.getKey();

                try {
                    if (thread.isBridge()) {
                        // System.out.println("Drawing bridge: " + threadName);
                        drawBridgeThread(g2d, thread, objectPositions, tektonCardinalPoints, commandProcessor, 
                                threadEndpoints);
                    } else {
                        // System.out.println("Drawing NON-bridge: " + threadName);
                        drawNonBridgeThread(g2d, thread, objectPositions, tektonCardinalPoints, commandProcessor, threadEndpoints);
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

    private void drawBridgeThread(
        Graphics2D g2d, 
        FungusThread thread, 
        Map<String, Point> objectPositions,
        Map<String, List<Point>> tektonCardinalPoints, 
        CommandProcessor commandProcessor, 
        Map<String, Point> threadEndpoints
    ) {
        List<Tekton> tektons = thread.getTektons();
        if (tektons.size() == 2) {
            // Point firstControlPoint = findClosestCardinalPoint(
            //     tektons.get(0), tektonCardinalPoints, objectPositions.get(commandProcessor.findByObject(tektons.get(1))), commandProcessor
            // );
            // Point secondControlPoint = findClosestCardinalPoint(
            //     tektons.get(1), tektonCardinalPoints, objectPositions.get(commandProcessor.findByObject(tektons.get(0))), commandProcessor
            // );
            String threadName = commandProcessor.findByObject(thread);
            Point startPoint = threadEndpoints.get(threadName+"_start");
            Point endPoint = threadEndpoints.get(threadName+"_end");
            drawLine(g2d, startPoint, endPoint);
        }
    }

    private void drawNonBridgeThread(
        Graphics2D g2d, 
        FungusThread thread, 
        Map<String, Point> objectPositions,
        Map<String, List<Point>> tektonCardinalPoints, 
        CommandProcessor commandProcessor,
        Map<String, Point> threadEndpoints
    ) {
        if (thread.getTektons().isEmpty()) return;
        Tekton tekton = thread.getTektons().get(0);
        
        String tektonName = commandProcessor.findByObject(tekton);
        
        if(tektonName == null || !objectPositions.containsKey(tektonName + "_center")){ 
            System.out.println(tektonName + "_center");
            System.out.println(!objectPositions.containsKey(tektonName + "_center"));
            for (Map.Entry<String, Point> entry : objectPositions.entrySet()) {
                Point objectPos = entry.getValue();
                String objectName = entry.getKey();
                // if(objectName.contains("_center"))
                System.out.println(objectName + " at: " + objectPos);
            }
            System.out.println("TektonName or Tekton center is missing in drawNonBridgeThread");
            return;
        }

        Point tektonPos = objectPositions.get(tektonName+ "_center");
        
        // TODO try to find another control point if a non bridge thread is here, and try to put it to another controlpoint(Not sure if its neccessary cos the player will choose direction on growthread but would be nice on init)
        //! soon would be nice to check which species has a thread there and would decline growth if species not equal
        Point controlPoint = findClosestCardinalPoint(
            tekton, 
            tektonCardinalPoints, 
            tektonPos,
            commandProcessor
        );
        
        if (controlPoint != null) {
            drawLine(g2d, controlPoint, tektonPos);
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