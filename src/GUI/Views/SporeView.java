package GUI.Views;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import GUI.GameObjectEvent;
import sporeTypes.*;

public class SporeView extends AbstractGameObjectView {
    private static final int SPORE_SIZE = 10;
    private Color color;
    
    public SporeView(String objectName, Point position, Spore spore) {
        super(objectName, position);
        this.color = determineColor(spore);
        loadAppropriateImage(spore);
    }
    
    private Color determineColor(Spore spore) {
        if (spore instanceof FastSpore) return new Color(255, 200, 200);
        if (spore instanceof MultiplyInsectSpore) return new Color(200, 255, 200);
        if (spore instanceof SlowSpore) return new Color(200, 200, 255);
        if (spore instanceof StunSpore) return new Color(255, 255, 200);
        if (spore instanceof DisableCutSpore) return new Color(255, 200, 255);
        return Color.WHITE;
    }
    private void loadAppropriateImage(Spore sp) {
        String basePath = "src/resources/spores/";
        String imageName = "";
        
        if (sp instanceof FastSpore) {
            imageName = "fastSpore.png";
        } else if (sp instanceof SlowSpore) {
            imageName = "slowSpore.png";
        } else if (sp instanceof DisableCutSpore) {
            imageName = "disableCutSpore.png";
        } else if (sp instanceof MultiplyInsectSpore) {
            imageName = "multiplyInsectSpore.png";
        } else if (sp instanceof StunSpore) {
            imageName = "stunSpore.png";
        } else {
            imageName = "spore.png";
        }
        
        this.image = createCircularImage((BufferedImage)loadImage(basePath + imageName));
    }
    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.fill(new Ellipse2D.Double(position.x - SPORE_SIZE/2, position.y - SPORE_SIZE/2, 
            SPORE_SIZE, SPORE_SIZE));
        if (image != null) {
            g2d.drawImage(image, position.x - SPORE_SIZE/2, position.y - SPORE_SIZE/2, SPORE_SIZE, SPORE_SIZE, null);
        }else{
            g2d.setColor(Color.WHITE);
            g2d.draw(new Ellipse2D.Double(position.x - SPORE_SIZE/2, position.y - SPORE_SIZE/2, 
                SPORE_SIZE, SPORE_SIZE));
        }
        

        // Draw name
        Font originalFont = g2d.getFont();
        g2d.setFont(new Font(originalFont.getName(), originalFont.getStyle(), 8));
        g2d.drawString(objectName, position.x - SPORE_SIZE/2, position.y - SPORE_SIZE/2 - 2);
        g2d.setFont(originalFont);
    }
    
    @Override
    public void update(GameObjectEvent event) {
        //! Handle spore state changes if needed
    }
}