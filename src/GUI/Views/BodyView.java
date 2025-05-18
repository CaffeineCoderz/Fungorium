package GUI.Views;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import GUI.FungoriumGUIBuilder;
import GUI.RenderMap.MapSize;
import commands.CommandProcessor;
import fungus.*;

public class BodyView {
    private static final int BODY_SIZE = 45;
    private Map<String, Image> images;
    private MapSize mapSize;
    private FungoriumGUIBuilder guiBuilder;
    private CommandProcessor cmdproc;

    public BodyView(){
        images = new HashMap<>();
        loadImages();
    }

    public void setGuiBuilder(FungoriumGUIBuilder builder) {
        this.guiBuilder = builder;
    }
    public void setCommandProcessor(CommandProcessor cmdproc) {
        this.cmdproc = cmdproc;
    }
    
    /**
     * Draws all FungusBody objects in the game.
     *
     * @param g2d the Graphics2D object to draw on
     * @param objectPositions a mapping of object names to their positions on the board
     * @param createdObjects a mapping of object names to their objects
     * @param fungusBodyImg the image to use for the bodies, or null if none
     */
    public void drawBodies(Graphics2D g2d, Map<String, Point> objectPositions, Map<String, Object> createdObjects) {
        for (Map.Entry<String, Object> entry : createdObjects.entrySet()) {
            if (entry.getValue() instanceof FungusBody) {
                String name = entry.getKey();
                Point pos = objectPositions.getOrDefault(name, new Point(100, 100));
                FungusBody body = (FungusBody) entry.getValue();

                String speciesColor = guiBuilder.getSpeciesStringColor(cmdproc.findByObject(body.getSpecies()));

                if(mapSize == MapSize.SMALL) {
                    g2d.drawImage(images.get(speciesColor), pos.x - BODY_SIZE / 2, pos.y - BODY_SIZE / 2, BODY_SIZE,
                            BODY_SIZE, null);
                } else if (mapSize == MapSize.MEDIUM) {
                    int NEW_BODY_SIZE = BODY_SIZE * 3 / 5;
                    g2d.drawImage(images.get(speciesColor), pos.x - NEW_BODY_SIZE / 2, pos.y - NEW_BODY_SIZE / 2, NEW_BODY_SIZE, NEW_BODY_SIZE, null);
                } else  if (mapSize == MapSize.LARGE) {
                    int NEW_BODY_SIZE = BODY_SIZE * 4 / 7;
                    g2d.drawImage(images.get(speciesColor), pos.x - NEW_BODY_SIZE / 2, pos.y - NEW_BODY_SIZE / 2,
                            NEW_BODY_SIZE, NEW_BODY_SIZE, null);
                } 
                

                // Draw body name
                g2d.drawString(name, pos.x - BODY_SIZE / 2 + 5, pos.y - BODY_SIZE / 2 + 15);
            }
        }
    }

    public void setMapSize(MapSize mapSize) {
        this.mapSize = mapSize;
    }

    private void loadImages() {
        try {
            // Load fungus body image
            //images.put("Body1", ImageIO.read(new File("src/resources/fungusBody.png")));
            
            images.put("LightBlue", ImageIO.read(new File("src/resources/bodies/LightBlue.png")));
            images.put("LightGreen", ImageIO.read(new File("src/resources/bodies/LightGreen.png")));
            images.put("LightPink", ImageIO.read(new File("src/resources/bodies/LightPink.png")));
            images.put("LightPurple", ImageIO.read(new File("src/resources/bodies/LightPurple.png")));
        } catch (Exception e) {
            System.err.println("Error loading resources: " + e.getMessage());
        }
    }

}
