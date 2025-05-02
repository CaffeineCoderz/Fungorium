package GUI.Views;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;

import GUI.GameObjectEvent;
import insect.*;

public class InsectView extends AbstractGameObjectView {
    private static final int INSECT_SIZE = 15;
    private Insect insect;
    
    public InsectView(String objectName, Point position, Insect insect) {
        super(objectName, position);
        this.insect = insect;
    }
    
    @Override
    public void draw(Graphics2D g2d) {
        // Base insect color
        g2d.setColor(new Color(150, 100, 50));
        g2d.fill(new Ellipse2D.Double(position.x - INSECT_SIZE/2, position.y - INSECT_SIZE/2, 
            INSECT_SIZE, INSECT_SIZE));
        g2d.setColor(Color.WHITE);
        g2d.draw(new Ellipse2D.Double(position.x - INSECT_SIZE/2, position.y - INSECT_SIZE/2, 
            INSECT_SIZE, INSECT_SIZE));

        //? Draw effect indicator
        /*InsectEffects effect = insect.gEffect();
        if (effect != null) {
            switch (effect) {
                case STUN: g2d.setColor(Color.YELLOW); break;
                case SLOW: g2d.setColor(Color.BLUE); break;
                case FAST: g2d.setColor(Color.RED); break;
                case NO_CUT: g2d.setColor(Color.MAGENTA); break;
                default: g2d.setColor(Color.WHITE);
            }
            g2d.fillOval(position.x - INSECT_SIZE/4, position.y - INSECT_SIZE/4, 
                INSECT_SIZE/2, INSECT_SIZE/2);
        }*/

        // Draw insect name
        Font originalFont = g2d.getFont();
        g2d.setFont(new Font(originalFont.getName(), originalFont.getStyle(), 8));
        g2d.setColor(Color.WHITE);
        g2d.drawString(objectName, position.x - INSECT_SIZE/2, position.y - INSECT_SIZE/2 - 2);
        g2d.setFont(originalFont);
    }
    
    @Override
    public void update(GameObjectEvent event) {
        //! Handle insect state changes if needed
    }
}