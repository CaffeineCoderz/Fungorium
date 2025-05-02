package GUI.Views;

import java.awt.*;
import java.awt.geom.Ellipse2D;

import GUI.GameObjectEvent;
import fungus.FungusBody;

public class BodyView extends AbstractGameObjectView {
    private static final int BODY_SIZE = 30;
    private FungusBody body;

    public BodyView(String objectName, Point position, FungusBody body) {
        super(objectName, position);
        this.image = loadImage("src/resources/fungusBody.png");
        this.body = body;
    }
    
    @Override
    public void draw(Graphics2D g2d) {
        if (image != null) {
            g2d.drawImage(image, position.x - BODY_SIZE, position.y - BODY_SIZE/2, 
                BODY_SIZE, BODY_SIZE, null);
        } else {
            g2d.setColor(new Color(100, 50, 0));
            g2d.fill(new Ellipse2D.Double(position.x - BODY_SIZE/2, position.y - BODY_SIZE/2, 
                BODY_SIZE, BODY_SIZE));
            g2d.setColor(Color.WHITE);
            g2d.draw(new Ellipse2D.Double(position.x - BODY_SIZE/2, position.y - BODY_SIZE/2, 
                BODY_SIZE, BODY_SIZE));
        }
        
        // Draw body name
        g2d.drawString(objectName, position.x - BODY_SIZE/2 + 5, position.y - BODY_SIZE/2 + 15);
    }
    
    @Override
    public void update(GameObjectEvent event) {
        //! Handle body state changes if needed
    }

    public FungusBody getBody() {
        return body;
    }
}
