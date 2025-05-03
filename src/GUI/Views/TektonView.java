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
    private static final int TEKTON_CELLS = 3;
    private static final int TEKTON_SIZE = 80;

    public void drawTektons(Graphics2D g2d, Map<String, Point> objectPositions, Map<String, Object> createdObjects,
            int cellWidth, int cellHeight, Image[] tektonImages) {
        for (Map.Entry<String, Point> entry : objectPositions.entrySet()) {
            String name = entry.getKey();
            Object obj = createdObjects.get(name);
            if (!(obj instanceof Tekton)) {
                continue;
            }

            Point topLeft = entry.getValue();
            int x = topLeft.y * cellWidth;
            int y = topLeft.x * cellHeight;
            int width = cellWidth * TEKTON_CELLS;
            int height = cellHeight * TEKTON_CELLS;

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
}
