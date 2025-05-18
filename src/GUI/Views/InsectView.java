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
import insect.*;

public class InsectView {
    private static final int INSECT_SIZE = 34;
    private MapSize mapSize;
    private Map<String, Image> images;
    private FungoriumGUIBuilder guiBuilder;
    private CommandProcessor cmdproc;
    
    public InsectView(){
        images = new HashMap<>();
        loadImages();
    }
    /**
     * Draws all Insect objects in the game.
     *
     * @param g2d the Graphics2D object to draw on
     * @param objectPositions a mapping of object names to their positions on the board
     * @param createdObjects a mapping of object names to their objects
     * @param insectImg the image to use for the insects, or null if none
     */
    public void drawInsects(Graphics2D g2d, Map<String, Point> objectPositions, Map<String, Object> createdObjects) {
        for (Map.Entry<String, Object> entry : createdObjects.entrySet()) {
            if (entry.getValue() instanceof Insect) {
                String name = entry.getKey();
                Insect insect = (Insect) entry.getValue();
                Point pos = objectPositions.getOrDefault(name, new Point(200, 200));
                String myOwner = cmdproc.findByObject(insect.getMyOwner());
                int size = INSECT_SIZE;
                if (mapSize == MapSize.MEDIUM) {
                    size = INSECT_SIZE * 4 / 5;
                } else if (mapSize == MapSize.LARGE) {
                    size = INSECT_SIZE * 5 / 7;
                }
                drawInsectBasedOnEffect(g2d, insect, pos, size, myOwner);

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

    public void drawInsectBasedOnEffect(Graphics2D g2d, Insect insect, Point pos, int SIZE, String speciesName) {
        // Draw insect based on its effect and Species Color
        String speciesColor = guiBuilder.getSpeciesStringColor(speciesName);

        if (insect.gEffect() != null) {
            switch (insect.gEffect()) {
                case STUN:
                    if(images.get(speciesColor + "Stun") != null){
                        g2d.drawImage(images.get(speciesColor + "Stun"), pos.x - SIZE / 2, pos.y - SIZE / 2, SIZE,
                            SIZE, null);
                    } else{
                        System.out.println("Image not found for " + speciesColor + "Stun");
                    }
                    break;
                case SLOW:
                    if(images.get(speciesColor + "Slow") != null){
                        g2d.drawImage(images.get(speciesColor + "Slow"), pos.x - SIZE / 2, pos.y - SIZE / 2, SIZE,
                            SIZE, null);
                    } else{
                        System.out.println("Image not found for " + speciesColor + "Slow");
                    }
                    break;
                case FAST:
                    if(images.get(speciesColor + "Fast") != null){
                        g2d.drawImage(images.get(speciesColor + "Fast"), pos.x - SIZE / 2, pos.y - SIZE / 2, SIZE,
                            SIZE, null);
                    } else{
                        System.out.println("Image not found for " + speciesColor + "Fast");
                    }
                    break;
                case NO_CUT:
                    if(images.get(speciesColor + "NoCut") != null){
                        g2d.drawImage(images.get(speciesColor + "NoCut"), pos.x - SIZE / 2, pos.y - SIZE / 2, SIZE,
                            SIZE, null);
                    } else{
                        System.out.println("Image not found for " + speciesColor + "NoCut");
                    }
                    break;
                default:
                    if(images.get(speciesColor) != null){
                        g2d.drawImage(images.get(speciesColor), pos.x - SIZE / 2, pos.y - SIZE / 2, SIZE,
                                SIZE, null);
                    } else{
                        System.out.println("Image not found for " + speciesColor);
                    }
            }
        } else {
            g2d.drawImage(images.get(
                    speciesColor), pos.x - SIZE / 2, pos.y - SIZE / 2, SIZE,
                    SIZE, null);
        }
    }

    private void loadImages() {
        try {
            // Load insect image
            //images.put("Insect1",ImageIO.read(new File("src/resources/insect.png")));

            // LightBeige images
            images.put("LightBeige", ImageIO.read(new File("src/resources/insects/LightBeige.png")));
            images.put("LightBeigeFast", ImageIO.read(new File("src/resources/insects/LightBeigeFast.png")));
            images.put("LightBeigeStun", ImageIO.read(new File("src/resources/insects/LightBeigeStun.png")));
            images.put("LightBeigeSlow", ImageIO.read(new File("src/resources/insects/LightBeigeSlow.png")));
            images.put("LightBeigeNoCut", ImageIO.read(new File("src/resources/insects/LightBeigeNoCut.png")));

            // LightCyan images
            images.put("LightCyan", ImageIO.read(new File("src/resources/insects/LightCyan.png")));
            images.put("LightCyanFast", ImageIO.read(new File("src/resources/insects/LightCyanFast.png")));
            images.put("LightCyanStun", ImageIO.read(new File("src/resources/insects/LightCyanStun.png")));
            images.put("LightCyanSlow", ImageIO.read(new File("src/resources/insects/LightCyanSlow.png")));
            images.put("LightCyanNoCut", ImageIO.read(new File("src/resources/insects/LightCyanNoCut.png")));

            // LightOrange images
            images.put("LightOrange", ImageIO.read(new File("src/resources/insects/LightOrange.png")));
            images.put("LightOrangeFast", ImageIO.read(new File("src/resources/insects/LightOrangeFast.png")));
            images.put("LightOrangeStun", ImageIO.read(new File("src/resources/insects/LightOrangeStun.png")));
            images.put("LightOrangeSlow", ImageIO.read(new File("src/resources/insects/LightOrangeSlow.png")));
            images.put("LightOrangeNoCut", ImageIO.read(new File("src/resources/insects/LightOrangeNoCut.png")));

            // LightRed images
            images.put("LightRed", ImageIO.read(new File("src/resources/insects/LightRed.png")));
            images.put("LightRedFast", ImageIO.read(new File("src/resources/insects/LightRedFast.png")));
            images.put("LightRedStun", ImageIO.read(new File("src/resources/insects/LightRedStun.png")));
            images.put("LightRedSlow", ImageIO.read(new File("src/resources/insects/LightRedSlow.png")));
            images.put("LightRedNoCut", ImageIO.read(new File("src/resources/insects/LightRedNoCut.png")));

        } catch (Exception e) {
            System.err.println("Error loading resources: " + e.getMessage());
        }
    }

    public void setMapSize(MapSize mapSize) {
        this.mapSize = mapSize;
    }

    public void setGuiBuilder(FungoriumGUIBuilder guiBuilder) {
        this.guiBuilder = guiBuilder;
    }

    public void setCommandProcessor(CommandProcessor cmdproc) {
        this.cmdproc = cmdproc; 
    }
}
