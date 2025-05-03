package GUI.Views;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Map;

import insect.Insect;
import insect.InsectEffects;

public class InsectView {
    private static final int INSECT_SIZE = 50;

    public void drawInsects(Graphics2D g2d, Map<String, Point> objectPositions, Map<String, Object> createdObjects, Image insectImg) {
        for (Map.Entry<String, Object> entry : createdObjects.entrySet()) {
            if (entry.getValue() instanceof Insect) {
                String name = entry.getKey();
                Insect insect = (Insect) entry.getValue();
                Point pos = objectPositions.getOrDefault(name, new Point(200, 200));

                // Draw insect base
                if (insectImg != null) {
                    g2d.drawImage(insectImg, pos.x - INSECT_SIZE / 2, pos.y - INSECT_SIZE / 2, INSECT_SIZE, INSECT_SIZE, null);
                } else {
                    g2d.setColor(new Color(150, 100, 50));
                    g2d.fill(new Ellipse2D.Double(pos.x - INSECT_SIZE / 2, pos.y - INSECT_SIZE / 2, INSECT_SIZE, INSECT_SIZE));
                    g2d.setColor(Color.WHITE);
                    g2d.draw(new Ellipse2D.Double(pos.x - INSECT_SIZE / 2, pos.y - INSECT_SIZE / 2, INSECT_SIZE, INSECT_SIZE));
                }

                // Draw effect indicator
                InsectEffects effect = insect.gEffect();
                if (effect != null) {
                    switch (effect) {
                        case STUN:
                            g2d.setColor(Color.YELLOW);
                            break;
                        case SLOW:
                            g2d.setColor(Color.BLUE);
                            break;
                        case FAST:
                            g2d.setColor(Color.RED);
                            break;
                        case NO_CUT:
                            g2d.setColor(Color.MAGENTA);
                            break;
                        default:
                            g2d.setColor(Color.WHITE);
                    }
                    //g2d.fillOval(pos.x - INSECT_SIZE / 4, pos.y - INSECT_SIZE / 4, INSECT_SIZE / 2, INSECT_SIZE / 2);
                }

                // Draw insect name
                Font originalFont = g2d.getFont();
                g2d.setFont(new Font(originalFont.getName(), originalFont.getStyle(), 8));
                g2d.setColor(Color.WHITE);
                g2d.drawString(name, pos.x - INSECT_SIZE / 2, pos.y - INSECT_SIZE / 2 - 2);
                g2d.setFont(originalFont);
            }
        }
    }
}
