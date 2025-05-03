package GUI.Views;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Map;

import sporeTypes.*;

public class SporeView {
    private static final int SPORE_SIZE = 10;

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
            g2d.setColor(determineSporeColor(spore));
            g2d.fill(new Ellipse2D.Double(x, y, width, height));
            g2d.setColor(Color.WHITE);
            g2d.draw(new Ellipse2D.Double(x, y, width, height));
        }
    }

    private Color determineSporeColor(Object spore) {
        if (spore instanceof FastSpore) {
            return new Color(255, 200, 200);
        } else if (spore instanceof MultiplyInsectSpore) {
            return new Color(200, 255, 200);
        } else if (spore instanceof SlowSpore) {
            return new Color(200, 200, 255);
        } else if (spore instanceof StunSpore) {
            return new Color(255, 255, 200);
        } else if (spore instanceof DisableCutSpore) {
            return new Color(255, 200, 255);
        }
        return Color.WHITE;
    }
}
