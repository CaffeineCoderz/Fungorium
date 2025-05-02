package GUI.Views;
import GUI.GameObjectEvent;
import java.awt.*;

public interface GameObjectView {
    void draw(Graphics2D g2d);
    void update(GameObjectEvent event);
    boolean contains(Point p);
    String getObjectName();
    Point getPosition();
}
