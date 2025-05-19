package GUI.Views;

import fungus.FungusThread;
import tektonTypes.Tekton;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.List;
import java.util.Map;

import GUI.FungoriumGUIBuilder;
import commands.CommandProcessor;

public class ThreadView {
    private static final int THREAD_WIDTH = 3;
    private FungoriumGUIBuilder guiBuilder;

    public void setGuiBuilder(FungoriumGUIBuilder builder) {
        this.guiBuilder = builder;
    }
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
                    Point startPoint = threadEndpoints.get(threadName+"_start");
                    Point endPoint = threadEndpoints.get(threadName+"_end");
                    drawLine(g2d, startPoint, endPoint,
                    guiBuilder.getPlayerColor(commandProcessor.findByObject(thread.getSpecies())));


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
    private void drawLine(Graphics2D g2d, Point start, Point end, Color color) {
        if (start != null && end != null) {
            g2d.setColor(color);
            g2d.setStroke(new BasicStroke(THREAD_WIDTH));
            g2d.draw(new Line2D.Double(start.x, start.y, end.x, end.y));
        }
    }
}