package GUI.Views;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.List;
import java.util.Map;

import tektonTypes.DecomposingTekton;
import tektonTypes.DecreasingTekton;
import tektonTypes.FeedThreadTekton;
import tektonTypes.OneThreadTekton;
import tektonTypes.OnlyThreadTekton;
import tektonTypes.Tekton;

public class TektonView {
    private static final int TEKTON_SIZE = 80;

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

    public void drawTektons(Graphics2D g2d, Map<String, Point> objectPositions, Map<String, Object> createdObjects,
            int cellWidth, int cellHeight, int size, Image[] tektonImages) {
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
            drawTektonImage(g2d, obj, x, y, width, height, tektonImages);

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
    private void drawTektonImage(Graphics2D g2d, Object tekton, int x, int y, int width, int height, Image[] tektonImages) {
        if (tekton instanceof DecomposingTekton && tektonImages[1] != null) {
            g2d.drawImage(tektonImages[1], x, y, width, height, null);
        } else if (tekton instanceof DecreasingTekton && tektonImages[2] != null) {
            g2d.drawImage(tektonImages[2], x, y, width, height, null);
        } else if (tekton instanceof FeedThreadTekton && tektonImages[3] != null) {
            g2d.drawImage(tektonImages[3], x, y, width, height, null);
        } else if (tekton instanceof OneThreadTekton && tektonImages[4] != null) {
            g2d.drawImage(tektonImages[4], x, y, width, height, null);
        } else if (tekton instanceof OnlyThreadTekton && tektonImages[5] != null) {
            g2d.drawImage(tektonImages[5], x, y, width, height, null);
        } else if (tekton instanceof Tekton && tektonImages[0] != null) {
            g2d.drawImage(tektonImages[0], x, y, width, height, null);
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
}
