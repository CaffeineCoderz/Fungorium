package GUI.Views;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Map;

import sporeTypes.*;

public class SporeView {
    private static final int SPORE_SIZE = 10;

    /**
     * Draws all Spore objects in the game.
     *
     * Iterates over the created objects and draws each Spore object based on its
     * position and available spore images. If a corresponding image is found for
     * the spore type, it is used; otherwise, a fallback visual representation is
     * drawn. Additionally, displays the name of each spore above its visual
     * representation.
     *
     * @param g2d the Graphics2D object used for drawing
     * @param objectPositions a mapping of object names to their positions on the board
     * @param createdObjects a mapping of object names to their objects
     * @param sporeImages an array of images representing different spore types
     */
    public void drawSpores(Graphics2D g2d, Map<String, Point> objectPositions, Map<String, Object> createdObjects, Image[] sporeImages) {
        for (Map.Entry<String, Object> entry : createdObjects.entrySet()) {
            if (entry.getValue() instanceof Spore) {
                String name = entry.getKey();
                Point pos = objectPositions.getOrDefault(name, new Point(150, 150));
                Object spore = entry.getValue();

                // Draw spore image
                drawSporeImage(g2d, spore, pos.x - SPORE_SIZE / 2, pos.y - SPORE_SIZE / 2, SPORE_SIZE, SPORE_SIZE, sporeImages);

                // Draw spore name
                g2d.setColor(Color.WHITE);
                Font originalFont = g2d.getFont();
                g2d.setFont(new Font(originalFont.getName(), originalFont.getStyle(), 8));
                g2d.drawString(name, pos.x - SPORE_SIZE / 2, pos.y - SPORE_SIZE / 2 - 2);
                g2d.setFont(originalFont);
            }
        }
    }

    /**
     * Draws a spore object based on its class and the available images
     * 
     * @param g2d   the Graphics2D object to draw the spore on
     * @param spore the spore object to be drawn
     * @param x     the x position of the spore image
     * @param y     the y position of the spore image
     * @param width the width of the spore image
     * @param height the height of the spore image
     * @param sporeImages the available spore images
     */
    private void drawSporeImage(Graphics2D g2d, Object spore, int x, int y, int width, int height, Image[] sporeImages) {
        if (spore instanceof FastSpore && sporeImages[0] != null) {
            g2d.drawImage(sporeImages[0], x, y, width, height, null);
        } else if (spore instanceof SlowSpore && sporeImages[1] != null) {
            g2d.drawImage(sporeImages[1], x, y, width, height, null);
        } else if (spore instanceof DisableCutSpore && sporeImages[2] != null) {
            g2d.drawImage(sporeImages[2], x, y, width, height, null);
        } else if (spore instanceof MultiplyInsectSpore && sporeImages[3] != null) {
            g2d.drawImage(sporeImages[3], x, y, width, height, null);
        } else if (spore instanceof StunSpore && sporeImages[4] != null) {
            g2d.drawImage(sporeImages[4], x, y, width, height, null);
        } else if (sporeImages[5] != null) { // Default spore image
            g2d.drawImage(sporeImages[5], x, y, width, height, null);
        } else {
            // Fallback: Draw a colored circle if no image is available
            g2d.setColor(Color.WHITE);
            g2d.draw(new Ellipse2D.Double(x, y, width, height));
        }
    }
}
