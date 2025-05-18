package GUI;

import logic.GameLogic;
import tektonTypes.Tekton;
import java.io.*;
import java.util.Map;

public class SaveLoadHandler {
    private GameLogic gameLogic;

    public SaveLoadHandler(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
    }

    public void saveGame(String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            // 1. CommandProcessor állapotok
            out.writeObject(gameLogic.getCommandProcessor().getCreatedObjects());
            // 2. GameLogic állapotok
            out.writeObject(gameLogic.getPlayers());
            out.writeInt(gameLogic.getGameTime());
            out.writeInt(gameLogic.getRound());
            out.writeObject(gameLogic.getMapSize());
            out.writeInt(gameLogic.getPlayersCount());
            out.writeObject(gameLogic.getCurrentSpecies());
            out.writeObject(gameLogic.getTektons());
            out.writeObject(gameLogic.getBreaking());
            System.out.println("Game saved successfully.");
        } catch (Exception e) {
            System.err.println("Error saving game: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void loadGame(String filename) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            // 1. CommandProcessor állapotok
            gameLogic.getCommandProcessor().getCreatedObjects().clear();
            gameLogic.getCommandProcessor().getCreatedObjects().putAll((Map<String, Object>) in.readObject());
            // 2. GameLogic állapotok
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
            System.out.println("Game loaded successfully.");
        }catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading game: " + e.getMessage());
        }
    }
}