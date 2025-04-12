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
     * Gets the command processor.
     * 
     * @return The command processor.
     */
    public CommandProcessor getCommandProcessor() {
        return commandProcessor;
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
     * Starts the game, handles player type selection,
     * then takes turns until the game time runs out.
     */
    public void startGame() {
        Scanner scanner = new Scanner(System.in);
        // First, we need to select the players
        selectPlayers(scanner);

        // Second, take turns
        while (gameTime > 0) {
            takeTurn(scanner);
        }
    }

    /**
     * Selects the players and their types (Fungus or Insect).
     * 
     * @param scanner The scanner to read user input.
     */
    private void selectPlayers(Scanner scanner) {
        while (true) {
            if (fungusPlayers == 0 && insectPlayers == 0) {
                System.out.println(
                        "Which type of player would you like to be? Fungus - Insect (F/I) OR type 'exit' to start the game.");
            } else {
                System.out.println("Which type of player would you like to be? Fungus - Insect (F/I)");
            }
            String choice = scanner.nextLine().trim().toUpperCase();

            if (choice.equals("EXIT")) {
                System.out.println("All players are ready. Starting the game...");
                // commandProcessor.start();
                // scanner.close();
                return;
            }

            // Chose any type after minimum players reached
            else if (fungusPlayers == 0 && insectPlayers == 0) {
                System.out.println("Enter the name for your Player:");
                String name = scanner.nextLine().trim();
                switch (choice) {
                    case "F":
                        if (!players.containsKey(name)) {
                            handleSpeciesCreation("Fungus", name);
                            System.out.println("You chose Fungus with name: " + name);
                        } else {
                            System.out
                                    .println("A player with this name already exists. Please choose a different name.");
                        }
                        break;
                    case "I":
                        if (!players.containsKey(name)) {
                            handleSpeciesCreation("Insect", name);
                            System.out.println("You chose Insect with name: " + name);
                        } else {
                            System.out
                                    .println("A player with this name already exists. Please choose a different name.");
                        }
                        break;
                    case "exit":
                        if (fungusPlayers == 0 && insectPlayers == 0) {
                            System.out.println("All players are ready. Starting the game...");
                            scanner.close();
                            commandProcessor.start();
                            return;
                        } else {
                            System.out.println("You cannot exit the game now. Please choose a type.");
                        }
                        break;
                    default:
                        System.out.println("Invalid choice. Please choose F or I.");
                }
            }

            // Minimum players not reached yet
            if (choice.equals("F") && fungusPlayers > 0) {
                System.out.println("Enter the name for your Fungus:");
                String name = scanner.nextLine().trim();
                if (!players.containsKey(name)) {
                    --fungusPlayers;
                    if (fungusPlayers == 0) {
                        System.out.println("Minimum number of fungus players reached.");
                    } else {
                        System.out.println("You chose Fungus. You need at least " + fungusPlayers
                                + " more Fungus player to start the game.");
                    }
                    handleSpeciesCreation("Fungus", name);
                } else {
                    System.out.println("A player with this name already exists. Please choose a different name.");
                }
            } else if (choice.equals("I") && insectPlayers > 0) {
                System.out.println("Enter the name for your Insect:");
                String name = scanner.nextLine().trim();
                if (!players.containsKey(name)) {
                    --insectPlayers;
                    if (insectPlayers == 0) {
                        System.out.println("Minimum number of insect players reached.");
                    } else {
                        System.out.println("You chose Insect. You need at least " + insectPlayers
                                + " more Insect player to start the game.");
                    }
                    handleSpeciesCreation("Insect", name);
                } else {
                    System.out.println("A player with this name already exists. Please choose a different name.");
                }
            }
        }
    }

    /**
     * Handles the creation of species based on the type and name provided.
     * 
     * @param type The type of species to create (Fungus or Insect).
     * @param name The name of the species to create.
     */
    private void handleSpeciesCreation(String type, String name) {
        try {
            if (type.equals("Fungus")) {
                String create = "/create fungusspecies " + name;
                commandProcessor.process(create);
                // Debug purposes for endgame
                // commandProcessor.process("/set " + name + " addscore 10");
                FungusSpecies newFungusSpecies = (FungusSpecies) commandProcessor.getCreatedObjects().get(name);
                if (newFungusSpecies != null) {
                    addSpecies(name, newFungusSpecies);
                } else {
                    System.out.println("Failed to create FungusSpecies with name: " + name);
                }
            } else if (type.equals("Insect")) {
                String create = "/create insectspecies " + name;
                commandProcessor.process(create);
                // Debug purposes for endgame
                // commandProcessor.process("/set " + name + " addscore 12");
                InsectSpecies insect = (InsectSpecies) commandProcessor.getCreatedObjects().get(name);
                if (insect != null) {
                    addSpecies(name, insect);
                } else {
                    System.out.println("Failed to create InsectSpecies with name: " + name);
                }
            } else {
                System.out.println("Invalid species type.");
            }
        } catch (Exception e) {
            System.out.println("Error creating " + type + ": " + e.getMessage());
        }
    }

    /**
     * Takes a turn for each player in the game.
     * 
     * @param scanner The scanner to read user input.
     */
    public void takeTurn(Scanner scanner) {
        while (gameTime > 0) {
            System.out.println("---------> Round: " + (round + 1) + " <---------");

            // Iterate through each player and prompt for commands
            for (String playerName : players.keySet()) {
                System.out.println("It's " + playerName + "'s turn. Enter a command:");
                while (true) {
                    System.out.print("> ");
                    String command = scanner.nextLine().trim().toLowerCase();
                    if (command.equalsIgnoreCase("next")) {
                        break; // Move to the next player
                    }
                    commandProcessor.process(command);
                }
            }

            // Update game state after all players have taken their turns
            for (Object playerObj : players.values()) {
                if (playerObj instanceof FungusSpecies) {
                    ((FungusSpecies) playerObj).timeElapsed();
                } else if (playerObj instanceof InsectSpecies) {
                    ((InsectSpecies) playerObj).timeElapsed();
                }
            }

            round++;
            gameTime--;

            if (gameTime <= 0) {
                GameLogic.endGame(commandProcessor.getCreatedObjects());
                break;
            }
        }

        System.out.println("Game over!");
    }

    /**
     * Ends the game and determines the winners.
     * 
     * @param createdObjects The map of created objects (species).
     */
    public static void endGame(Map<String, Object> createdObjects) {
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

}