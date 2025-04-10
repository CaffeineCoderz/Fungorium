package logic;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import commands.CommandProcessor;
import fungus.FungusSpecies;
import insect.InsectSpecies;
import tektonTypes.Tekton;

public class GameLogic {
    private Map<String, Object> players = new HashMap<>(); // Egyetlen HashMap az összes fajhoz
    private int fungusPlayers = 2; // Minimum fungus játékos
    private int insectPlayers = 2; // Minimum insect játékos
    private int gameTime = 10; // A játék időtartama
    private int round = 0; // Az eltelt idő
    // Map
    private Map<String, Tekton> tektons = new HashMap<>();

    private CommandProcessor commandProcessor;

    public GameLogic() {
        // Initialize the command processor
        this.commandProcessor = new CommandProcessor();
    }

    /**
     * Sets the command processor for the game logic.
     * 
     * @param commandProcessor The command processor to be set.
     */
    public void setCommandProcessor(CommandProcessor commandProcessor) {
        this.commandProcessor = commandProcessor;
    }

    /**
     * Gets the game time.
     * 
     * @return The current game time.
     */
    public int getGameTime() {
        return gameTime;
    }

    /**
     * Sets the game time.
     * 
     * @param gameTime The game time to be set.
     */
    public void setGameTime(int gameTime) {
        this.gameTime = gameTime;
    }

    /**
     * Adds a species (Fungus or Insect) to the map.
     * 
     * @param id      The ID (name) of the species.
     * @param species The species object to be added.
     */
    public void addSpecies(String id, Object species) {
        if (species instanceof FungusSpecies || species instanceof InsectSpecies) {
            players.put(id, species);
        } else {
            throw new IllegalArgumentException("Invalid species type.");
        }
    }

    /**
     * Removes a species from the map.
     * 
     * @param id The ID (name) of the species to be removed.
     */
    public void removeSpecies(String id) {
        players.remove(id);
    }

    /**
     * Gets a species by its ID.
     * 
     * @param id The ID (name) of the species.
     * @return The species object, or null if not found.
     */
    public Object getSpecies(String id) {
        return players.get(id);
    }

    /**
     * Starts the game and handles player type selection.
     */
    public void startGame() {
        Scanner scanner = new Scanner(System.in);

        while (fungusPlayers > 0 || insectPlayers > 0) {
            System.out.println("Which type of player would you like to be? Fungus - Insect (F/I)");
            String choice = scanner.nextLine().trim().toUpperCase();

            if (choice.equals("F") && fungusPlayers > 0) {
                fungusPlayers--;
                System.out.println("You chose Fungus. Remaining Fungus slots: " + fungusPlayers);
            } else if (choice.equals("I") && insectPlayers > 0) {
                insectPlayers--;
                System.out.println("You chose Insect. Remaining Insect slots: " + insectPlayers);
            } else if (fungusPlayers == 0 && insectPlayers == 0) {
                System.out.println("All player slots are filled. You can choose any type.");
            } else {
                System.out.println("Invalid choice or no slots available for the selected type.");
            }
        }

        System.out.println("All players are ready. Starting the game...");
        commandProcessor.start();
        scanner.close();
    }

    /**
     * Ends the game and determines the winners.
     * 
     * @param createdObjects The map of created objects (species).
     */
    public static void endgame(Map<String, Object> createdObjects) {
        int maxFungusScore = -1;
        int maxInsectScore = -1;
        String fungusWinner = "N/A";
        String insectWinner = "N/A";

        // Győztesek meghatározása
        for (Map.Entry<String, Object> entry : createdObjects.entrySet()) {
            Object obj = entry.getValue();
            if (obj instanceof FungusSpecies) {
                FungusSpecies species = (FungusSpecies) obj;
                if (species.getScore() > maxFungusScore) {
                    maxFungusScore = species.getScore();
                    fungusWinner = entry.getKey();
                }
            } else if (obj instanceof InsectSpecies) {
                InsectSpecies species = (InsectSpecies) obj;
                if (species.getScore() > maxInsectScore) {
                    maxInsectScore = species.getScore();
                    insectWinner = entry.getKey();
                }
            }
        }

        // Eredmények kiírása
        System.out.println("Játék vége!");
        System.out.println("Fungus győztes: " + fungusWinner + " pontszám: " + maxFungusScore);
        System.out.println("Insect győztes: " + insectWinner + " pontszám: " + maxInsectScore);
    }

    /*
     * This method is called every end of a round to update the game state.
     */
    public void TimeElapsed() {
        for (Object playerObj : players.values()) {
            if (playerObj instanceof FungusSpecies) {
                FungusSpecies player = (FungusSpecies) playerObj;
                // ! player.takeTurn(); // Check issue #159
            } else if (playerObj instanceof InsectSpecies) {
                InsectSpecies player = (InsectSpecies) playerObj;
                // ! player.takeTurn(); // Check issue #159
            } else {
                System.out.println("Invalid player type.");
            }
        }

        // The Map should be rendered here
        for (Tekton tekton : tektons.values()) {
            // tekton.updateState();
        }

        round++;
        System.out.println("Round: " + round);

        gameTime--;
        if (gameTime <= 0) {
            this.endgame(commandProcessor.getCreatedObjects());
        }
    }
}

     * Choose player type
     */
    public void playerchoosing() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the game!");
        Integer input = 0;
        while (input < 4) {        
            System.out.println("How many players are there? (Atleast 4 player needed):");
            input = scanner.nextInt();
        }
        
        int MycoCount = 0;
        int EntoCount =0;
        for(Integer i=0; i < input; i++){
            System.out.println("Player " + (i + 1) + "\nChoose between Entomologists[1] and Mycologist[2]: (1/2)");
            Integer key = scanner.nextInt();
            if (key == 1) {
                if (EntoCount > Math.round(input/2)) {
                    System.out.println("Too many entomologist. You have to choose Mycologist");
                    i--;
                }
            }else if(key == 2){
                if (EntoCount > Math.round(input/2)) {
                    System.out.println("Too many Mycologist. You have to choose Entomologist");
                    i--;
                }
            }
        }
    }
    
}

