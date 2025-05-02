package GUI.Views;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;

import GUI.GameObjectEvent;
import tektonTypes.*;

public class TektonView extends AbstractGameObjectView {
    private static final int TEKTON_SIZE = 80;
    private Color color;
    private Tekton tekton;
    private List<Point> cardinalPoints;
    private Point center;
    private static final int TEKTON_CELLS = 3; // From FungoriumGamePanel


    public TektonView(String objectName, Point position, Tekton tekton, int cellWidth, int cellHeight) {
        super(objectName, position);
        this.tekton = tekton;
        this.color = determineColor(tekton);
        loadAppropriateImage(tekton);
        calculateCardinalPoints(cellWidth, cellHeight);
    }
    private void calculateCardinalPoints(int cellWidth, int cellHeight) {
        int width = cellWidth * TEKTON_CELLS;
        int height = cellHeight * TEKTON_CELLS;
        
        Point ccenter = new Point(position.x + width/2, position.y + height/2);
        this.center = ccenter;
        int offset = Math.min(width, height)/2;
        
        cardinalPoints = new ArrayList<>();
        cardinalPoints.add(new Point(center.x + offset, center.y)); // East
        cardinalPoints.add(new Point(center.x, center.y - offset)); // North
        cardinalPoints.add(new Point(center.x - offset, center.y)); // West
        cardinalPoints.add(new Point(center.x, center.y + offset)); // South
    }

    public List<Point> getCardinalPoints() {
        return cardinalPoints;
    }
    public Point getCardinalPoint(String direction) {
        if (cardinalPoints == null || cardinalPoints.size() < 4) return null;

        switch (direction.toLowerCase()) {
            case "e": return cardinalPoints.get(0); // East
            case "n": return cardinalPoints.get(1); // North
            case "w": return cardinalPoints.get(2); // West
            case "s": return cardinalPoints.get(3); // South
            default: return null;
        }
    }
    private void loadAppropriateImage(Tekton tekton) {
        String basePath = "src/resources/tektons/";
        String imageName = "";
        
        if (tekton instanceof DecomposingTekton) {
            imageName = "decomposingTekton.jpg";
        } else if (tekton instanceof DecreasingTekton) {
            imageName = "decreasingTekton.jpg";
        } else if (tekton instanceof FeedThreadTekton) {
            imageName = "feedThreadTekton.jpg";
        } else if (tekton instanceof OneThreadTekton) {
            imageName = "oneThreadTekton.jpg";
        } else if (tekton instanceof OnlyThreadTekton) {
            imageName = "onlyThreadTekton.jpg";
        } else {
            imageName = "defaultTekton.jpg";
        }
        
        this.image = createCircularImage((BufferedImage)loadImage(basePath + imageName));
    }
    
    private Color determineColor(Tekton tekton) {
        return Color.LIGHT_GRAY; // Default color if image fails to load
    }
    
    @Override
    public void draw(Graphics2D g2d) {
        // Draw shadow
        g2d.setColor(new Color(139, 69, 19, 255));
        g2d.fill(new Ellipse2D.Double(position.x + 1, position.y + 5, TEKTON_SIZE, TEKTON_SIZE));
        
        // Draw tekton
        if (image != null) {
            g2d.drawImage(image, position.x, position.y, TEKTON_SIZE, TEKTON_SIZE, null);
        } else {
            g2d.setColor(color);
            g2d.fill(new Ellipse2D.Double(position.x, position.y, TEKTON_SIZE, TEKTON_SIZE));
            g2d.setColor(Color.WHITE);
            g2d.draw(new Ellipse2D.Double(position.x, position.y, TEKTON_SIZE, TEKTON_SIZE));
        }
        
        // Draw name
        g2d.setColor(Color.WHITE);
        g2d.drawString(objectName, position.x, position.y);
    }
    
    @Override
    public void update(GameObjectEvent event) {
        //! Handle tekton state changes if needed
    }

    public Tekton getTekton() {
        return tekton;
    }
   public Point getCenter(){
    return center;
   }
}
