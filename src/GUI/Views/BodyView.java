package GUI.Views;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Map;

import fungus.FungusBody;

public class BodyView {
    private static final int BODY_SIZE = 45;

    /**
     * Draws all FungusBody objects in the game.
     *
     * @param g2d the Graphics2D object to draw on
     * @param objectPositions a mapping of object names to their positions on the board
     * @param createdObjects a mapping of object names to their objects
     * @param fungusBodyImg the image to use for the bodies, or null if none
     */
    public void drawBodies(Graphics2D g2d, Map<String, Point> objectPositions, Map<String, Object> createdObjects,
            Image fungusBodyImg) {
        for (Map.Entry<String, Object> entry : createdObjects.entrySet()) {
            if (entry.getValue() instanceof FungusBody) {
                String name = entry.getKey();
                Point pos = objectPositions.getOrDefault(name, new Point(100, 100));

                // Draw fungus body image
                if (fungusBodyImg != null) {
                    g2d.drawImage(fungusBodyImg, pos.x - BODY_SIZE / 2, pos.y - BODY_SIZE / 2, BODY_SIZE, BODY_SIZE,
                            null);
                } else {
                    g2d.setColor(new Color(100, 50, 0));
                    g2d.fill(new Ellipse2D.Double(pos.x - BODY_SIZE / 2, pos.y - BODY_SIZE / 2, BODY_SIZE, BODY_SIZE));
                    g2d.setColor(Color.WHITE);
                    g2d.draw(new Ellipse2D.Double(pos.x - BODY_SIZE / 2, pos.y - BODY_SIZE / 2, BODY_SIZE, BODY_SIZE));
                }

                // Draw body name
                g2d.drawString(name, pos.x - BODY_SIZE / 2 + 5, pos.y - BODY_SIZE / 2 + 15);
            }
        }
    }
}
