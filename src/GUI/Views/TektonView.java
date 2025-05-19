package GUI.Views;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import tektonTypes.*;

public class TektonView {
    private static final int TEKTON_SIZE = 80;
    private Map<String, Image> images;

    public TektonView(){
        images = new HashMap<>();
        loadImages();
    }
    /**
     * Draws Tekton objects on the graphics context provided.
     *
     * @param g2d The Graphics2D object to draw on.
     * @param objectPositions A map containing the positions of objects by their names.
     * @param createdObjects A map of created objects by their names.
     * @param cellWidth The width of each cell in the grid layout.
     * @param cellHeight The height of each cell in the grid layout.
     * @param size The size multiplier for the Tekton.
     * @param tektonImages An array of images corresponding to different Tekton types.
     */

    public void drawTektons(
            Graphics2D g2d, 
            Map<String, Point> objectPositions, 
            Map<String, Object> createdObjects,
            int cellWidth, 
            int cellHeight, 
            int size
    ) {
        for (Map.Entry<String, Point> entry : objectPositions.entrySet()) {
            String name = entry.getKey();
            Object obj = createdObjects.get(name);
            if (!(obj instanceof Tekton)) {
                continue;
            }

            Point topLeft = entry.getValue();
            int x = topLeft.y * cellWidth;
            int y = topLeft.x * cellHeight;
            int width = cellWidth * size;
            int height = cellHeight * size;

            // Draw shadow
            g2d.setColor(new Color(139, 69, 19, 255));
            g2d.fill(new Ellipse2D.Double(x + 1, y + 5, width, height));

            // Draw Tekton image
            drawTektonImage(g2d, obj, x, y, width, height);

            // Draw Tekton name
            g2d.setColor(Color.WHITE);
            g2d.drawString(name, x, y);
        }
    }

    /**
     * Draws a Tekton object based on its class and the available images
     * 
     * @param g2d   the Graphics2D object to draw the Tekton on
     * @param tekton the Tekton object to be drawn
     * @param x     the x position of the Tekton image
     * @param y     the y position of the Tekton image
     * @param width the width of the Tekton image
     * @param height the height of the Tekton image
     * @param tektonImages the available Tekton images
     */
    private void drawTektonImage(
        Graphics2D g2d, 
        Object tekton, 
        int x, 
        int y, 
        int width, 
        int height
    ) {
        if (tekton instanceof DecomposingTekton && images.get("decomposing_tekton") != null) {
            g2d.drawImage(images.get("decomposing_tekton"), x, y, width, height, null);
        } else if (tekton instanceof DecreasingTekton && images.get("decreasing_tekton") != null) {
            g2d.drawImage(images.get("decreasing_tekton"), x, y, width, height, null);
        } else if (tekton instanceof FeedThreadTekton && images.get("feedthread_tekton") != null) {
            g2d.drawImage(images.get("feedthread_tekton"), x, y, width, height, null);
        } else if (tekton instanceof OneThreadTekton && images.get("onethread_tekton") != null) {
            g2d.drawImage(images.get("onethread_tekton"), x, y, width, height, null);
        } else if (tekton instanceof OnlyThreadTekton && images.get("onlythread_tekton") != null) {
            g2d.drawImage(images.get("onlythread_tekton"), x, y, width, height, null);
        } else if (tekton instanceof Tekton && images.get("tekton") != null) {
            g2d.drawImage(images.get("tekton"), x, y, width, height, null);
        } else {
            // Fallback: Draw a gray circle if no image is available
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.fill(new Ellipse2D.Double(x, y, width, height));
            g2d.setColor(Color.WHITE);
            g2d.draw(new Ellipse2D.Double(x, y, width, height));
        }
    }

    /**
     * Returns the size of the Tekton image in pixels.
     * @return the size of the Tekton image in pixels.
     */
    public static int getTektonSize() {
        return TEKTON_SIZE;
    }
    /**
     * Loads the circular tekton images from the resources folder.
     * If the images are not found, a log message is printed.
     */
    private void loadImages() {
        try {
            // * Load CIRCULAR tekton images
            images.put("tekton",createCircularImage(ImageIO.read(new File("src/resources/tektons/defaultTekton1.jpg"))));
            images.put("decomposing_tekton",createCircularImage(ImageIO.read(new File("src/resources/tektons/decomposingTekton1.jpg"))));
            images.put("decreasing_tekton",createCircularImage(ImageIO.read(new File("src/resources/tektons/decreasingTekton1.jpg"))));
            images.put("feedthread_tekton",createCircularImage(ImageIO.read(new File("src/resources/tektons/feedThreadTekton1.jpg"))));
            images.put("onethread_tekton",createCircularImage(ImageIO.read(new File("src/resources/tektons/oneThreadTekton1.jpg"))));
            images.put("onlythread_tekton",createCircularImage(ImageIO.read(new File("src/resources/tektons/onlyThreadTekton1.jpg"))));

        } catch (Exception e) {
            System.err.println("Error loading resources: " + e.getMessage());
        }
    }
    /**
     * Creates a circular image from a given input image by masking the non-circular parts and
     * applying a semi-transparent black layer to reduce the brightness of the image.
     * 
     * @param input the input image to be modified
     * @return the circular image
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

}
