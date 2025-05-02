package GUI.Views;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public abstract class AbstractGameObjectView implements GameObjectView {
    protected Point position;
    protected String objectName;
    protected Image image;
    
    public AbstractGameObjectView(String objectName, Point position) {
        this.objectName = objectName;
        this.position = position;
    }
    
    @Override
    public boolean contains(Point p) {
        return position.distance(p) <= 20;
    }
    
    @Override
    public String getObjectName() {
        return objectName;
    }
    
    @Override
    public Point getPosition() {
        return position;
    }
    
    protected Image loadImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (Exception e) {
            System.err.println("Error loading image: " + path);
            return null;
        }
    }
    
    protected Image createCircularImage(BufferedImage input) {
        int size = Math.min(input.getWidth(), input.getHeight());
        BufferedImage circleBuffer = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = circleBuffer.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Ellipse2D.Double circle = new Ellipse2D.Double(0, 0, size, size);
        g2.setClip(circle);
        g2.drawImage(input, 0, 0, size, size, null);
        g2.dispose();
        
        return circleBuffer;
    }
}