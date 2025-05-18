package GUI;

import logic.GameLogic;
import tektonTypes.Tekton;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SaveLoadHandler {
private GameLogic gameLogic;
    private FungoriumGamePanel gamePanel;

    public SaveLoadHandler(GameLogic gameLogic, FungoriumGamePanel gamePanel) {
        this.gameLogic = gameLogic;
        this.gamePanel = gamePanel;
    }

public void saveGame(String filename) {
    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
        // Mentés:
        out.writeObject(gameLogic.getCommandProcessor().getCreatedObjects());
        out.writeObject(gameLogic.getPlayers());
        out.writeInt(gameLogic.getGameTime());
        out.writeInt(gameLogic.getRound());
        out.writeObject(gameLogic.getMapSize());
        out.writeInt(gameLogic.getPlayersCount());
        out.writeObject(gameLogic.getCurrentSpecies());
        out.writeObject(gameLogic.getTektons());
        out.writeObject(gameLogic.getBreaking());
        out.writeObject(gamePanel.getObjectPositions()); //ezzel lesz gond
        out.writeObject(gamePanel.getThreadEndpoints()); //ezzel lesz gond
        out.writeObject(gamePanel.getTektonCardinalPoints());
        out.writeObject(gamePanel.getSelectedObjects());
        out.writeObject(gamePanel.getOccupiedCells());
        System.out.println("objectPositions: " + gamePanel.getObjectPositions());
System.out.println("threadEndpoints: " + gamePanel.getThreadEndpoints());
System.out.println("tektonCardinalPoints: " + gamePanel.getTektonCardinalPoints());
System.out.println("selectedObjects: " + gamePanel.getSelectedObjects());
System.out.println("occupiedCells: " + gamePanel.getOccupiedCells());
        System.out.println("Game saved successfully.");
    } catch (Exception e) {
        System.err.println("Error saving game: " + e.getMessage());
    }
}

    @SuppressWarnings("unchecked")
public void loadGame(String filename) {
    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
       // Betöltés:
        gameLogic.getCommandProcessor().getCreatedObjects().clear();
        gameLogic.getCommandProcessor().getCreatedObjects().putAll((Map<String, Object>) in.readObject());
        gameLogic.getPlayers().clear();
        gameLogic.getPlayers().putAll((Map<String, Object>) in.readObject());
        gameLogic.setGameTime(in.readInt());
        gameLogic.setRound(in.readInt());
        gameLogic.setMapSize((RenderMap.MapSize) in.readObject());
        gameLogic.setPlayersCount(in.readInt());
        gameLogic.setCurrentSpecies((String) in.readObject());
        gameLogic.getTektons().clear();
        gameLogic.getTektons().putAll((Map<String, Tekton>) in.readObject());
        gameLogic.setBreak((Boolean) in.readObject());
        gamePanel.setObjectPositions((Map<String, java.awt.Point>) in.readObject());
        gamePanel.setThreadEndpoints((Map<String, java.awt.Point>) in.readObject());
        gamePanel.setTektonCardinalPoints((Map<String, List<java.awt.Point>>) in.readObject());
        Object selObj = in.readObject();
        if (selObj instanceof List) {
            List<?> rawList = (List<?>) selObj;
            List<String> stringList = new ArrayList<>();
            for (Object o : rawList) {
                if (o instanceof String) stringList.add((String) o);
            }
            gamePanel.setSelectedObjects(stringList);
        } else {
            gamePanel.setSelectedObjects(new ArrayList<>());
        }
        gamePanel.setOccupiedCells((Set<java.awt.Point>) in.readObject());                   
        System.out.println("Game loaded successfully.");
    } catch (Exception e) {
        e.printStackTrace();
        System.err.println("Error loading game: " + e.getMessage());
    }
}
}