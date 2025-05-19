package GUI.Views;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import GUI.RenderMap.MapSize;
import sporeTypes.*;

public class SporeView {
    private static final int SPORE_SIZE = 10;
    private MapSize mapSize;

    private Map<String, Image> images;

    public SporeView(){
        images = new HashMap<>();
        loadImages();
    }
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
    public void drawSpores(Graphics2D g2d, Map<String, Point> objectPositions, Map<String, Object> createdObjects) {
        for (Map.Entry<String, Object> entry : createdObjects.entrySet()) {
            if (entry.getValue() instanceof Spore) {
                String name = entry.getKey();
                Point pos = objectPositions.getOrDefault(name, new Point(150, 150));
                Object spore = entry.getValue();

                // Draw spore image
                if(mapSize == MapSize.SMALL) {
                    g2d.drawImage(images.get("defaultSpore"), pos.x - SPORE_SIZE / 2, pos.y - SPORE_SIZE / 2, SPORE_SIZE,
                            SPORE_SIZE, null);
                } else if (mapSize == MapSize.MEDIUM) {
                    int NEW_SPORE_SIZE = SPORE_SIZE *   4 / 5;
                    drawSporeImage(g2d, spore, pos.x - NEW_SPORE_SIZE / 2, pos.y - NEW_SPORE_SIZE / 2, NEW_SPORE_SIZE,
                            NEW_SPORE_SIZE);
                } else if (mapSize == MapSize.LARGE) {
                    int NEW_SPORE_SIZE = SPORE_SIZE * 5 / 7;
                    drawSporeImage(g2d, spore, pos.x - NEW_SPORE_SIZE / 2, pos.y - NEW_SPORE_SIZE / 2, NEW_SPORE_SIZE,
                            NEW_SPORE_SIZE);
                }
                //drawSporeImage(g2d, spore, pos.x - SPORE_SIZE / 2, pos.y - SPORE_SIZE / 2, SPORE_SIZE, SPORE_SIZE);

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
    private void drawSporeImage(Graphics2D g2d, Object spore, int x, int y, int width, int height) {
        if (spore instanceof FastSpore && images.get("fastSpore") != null) {
            g2d.drawImage(images.get("fastSpore"), x, y, width, height, null);
        } else if (spore instanceof SlowSpore && images.get("slowSpore") != null) {
            g2d.drawImage(images.get("slowSpore"), x, y, width, height, null);
        } else if (spore instanceof DisableCutSpore && images.get("disableCutSpore") != null) {
            g2d.drawImage(images.get("disableCutSpore"), x, y, width, height, null);
        } else if (spore instanceof MultiplyInsectSpore && images.get("multiplyInsectSpore") != null) {
            g2d.drawImage(images.get("multiplyInsectSpore"), x, y, width, height, null);
        } else if (spore instanceof StunSpore && images.get("stunSpore") != null) {
            g2d.drawImage(images.get("stunSpore"), x, y, width, height, null);
        } else if (images.get("defaultSpore") != null) { // Default spore image
            g2d.drawImage(images.get("defaultSpore"), x, y, width, height, null);
        } else {
            // Fallback: Draw a colored circle if no image is available
            g2d.setColor(Color.WHITE);
            g2d.draw(new Ellipse2D.Double(x, y, width, height));
        }
    }
    /**
     * Loads the spore images from the resources folder.
     *
     * <p>
     * Tries to load the images for the different spore types. If an image cannot
     * be loaded, an error message is printed to the standard error stream.
     * </p>
     */
    private void loadImages() {
        try {
            // Load spore images
            images.put("defaultSpore" , ImageIO.read(new File("src/resources/spores/spore.png")));
            images.put("fastSpore" , ImageIO.read(new File("src/resources/spores/fastSpore.png")));
            images.put("slowSpore", ImageIO.read(new File("src/resources/spores/slowSpore.png")));
            images.put("stunSpore", ImageIO.read(new File("src/resources/spores/stunSpore.png")));
            images.put("disableCutSpore", ImageIO.read(new File("src/resources/spores/disableCutSpore.png")));
            images.put("multiplyInsectSpore", ImageIO.read(new File("src/resources/spores/multiplyInsectSpore.png")));

            
        } catch (Exception e) {
            System.err.println("Error loading resources: " + e.getMessage());
        }
    }

    /**
     * Sets the current map size for this view.
     *
     * This method is used to notify the view that the map size has changed.
     * The view will then adjust the size of the spore images and their positions
     * accordingly.
     *
     * @param mapSize the new map size
     */
    public void setMapSize(MapSize mapSize) {
        this.mapSize = mapSize;
    }

    /**
     * Returns the default size of a spore image in pixels.
     *
     * @return the default size of a spore image in pixels
     */
    public int getSporeSize() {
        return SPORE_SIZE;
    }
}
