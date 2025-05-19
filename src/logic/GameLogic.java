package logic;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import utils.*;
import GUI.FungoriumGamePanel;
import GUI.RenderMap;
import commands.CommandProcessor;
import fungus.FungusSpecies;
import fungus.FungusThread;
import insect.Insect;
import insect.InsectSpecies;
import tektonTypes.Tekton;
import GUI.ResultScreen;

public class GameLogic {
    private Map<String, Object> players = new HashMap<>(); // Egyetlen HashMap az összes fajhoz
    private int fungusPlayers = 2; // Minimum fungus játékos
    private int insectPlayers = 2; // Minimum insect játékos
    private int playerCount = 4; // A játékosok száma
    private int gameTime = 10; // A játék időtartama
    private int round = 0; // Az eltelt idő
    private boolean IsLoaded = false; // Ha a játék betöltve van
    private String currentSpecies; // Az aktuális játékos
    private BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();
    // Map
    private Map<String, Tekton> tektons = new HashMap<>();
    
    private Boolean tektonBreak = false;
    private Boolean newRound = false;

    private CommandProcessor commandProcessor;

    private Scanner scanner;
    private FungoriumGamePanel gamePanel;
    private RenderMap.MapSize mapSize = RenderMap.MapSize.MEDIUM;

    public RenderMap.MapSize getMapSize() {
        return mapSize;
    }

    public void setIsLoaded(boolean isLoaded) {
        IsLoaded = isLoaded;
    }
    public void setMapSize(RenderMap.MapSize mapSize) {
        this.mapSize = mapSize;
    }
    
    public GameLogic() {
        // Initialize the command processor
        this.commandProcessor = new CommandProcessor(this);
    }

    public void setGamePanel(FungoriumGamePanel panel) {
        this.gamePanel = panel;
    }

    public void setBreak(Boolean sss){
        tektonBreak = sss;
    }
    public Boolean getBreaking(){
        return tektonBreak;
    }
    public void setNewRound(Boolean sss){
        newRound = sss;
    }
    public Boolean isNewRound(){
        return newRound;
    }
    public FungoriumGamePanel getGamePanel() {
        return gamePanel;
    }

    public int getPlayersCount() {
        return playerCount;
    }

    public void setPlayersCount(int size) {
        this.playerCount = size;
    }

    public String getCurrentSpecies() {
        return currentSpecies;
    }
    /**
     * Sets the command processor for the game logic.
     * 
     * @param commandProcessor The command processor to be set.
     */
    public void setCommandProcessor(CommandProcessor commandProcessor) {
        this.commandProcessor = commandProcessor;
    }

    public BlockingQueue<String> getInputQueue() {
        return inputQueue;
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
     * Gets the scanner to read user input.
     * 
     * @return The scanner to read user input.
     */
    public Scanner getScanner() {
        return scanner;
    }

/**
 * Initializes the players for the game, alternating between 
 * Fungus and Insect types. Each player is assigned a unique 
 * identifier based on their type and position in the sequence.
 */

    public void initializePlayers() {
        for (int i = 0; i < playerCount; i++) {
            if (i % 2 == 0) {
                handleSpeciesCreation("Fungus", "Fungus" + (i + 1));
                System.out.println("Created Fungus player: Fungus" + (i + 1));
            } else {
                handleSpeciesCreation("Insect", "Insect" + (i + 1));
                System.out.println("Created Insect player: Insect" + (i + 1));
            }
        }
    }

    /**
     * Starts the game, handles player type selection,
     * then takes turns until the game time runs out.
     */
    public void startGame() {
        this.scanner = new Scanner(System.in);
        // First, we need to select the players
        //selectPlayers(scanner);

        while (gameTime > 0) {
            takeTurn(scanner, IsLoaded);
        }
        new ResultScreen(commandProcessor).setVisible(true);
    }

    /**
     * Prints a guide to the game to the console, with the game title centered and
     * surrounded by arrows. The guide includes the goal of the game, how to win, and
     * how to control the game. The guide is formatted to fit within a fixed width.
     */
    public void printGuide() {
        String[] guideLines = {
                "The goal of the game is to achieve the highest score possible.",
                "At the end of the game, there will be two winners: one FungusSpecies and one InsectSpecies.",
                "\033[0;32m",
                "\tThe FungusSpecies with the most FungusBodies wins.(Highest score)",
                "\tThe InsectSpecies with the most Insects wins.(Highest score)",
                "\033[0m",
                "During the game, players can issue commands to control the Fungus and Insect species.",
                "These user commands can be viewed using the 'help' command.",
                "All system and user commands can be viewed with the '/helpsys' command.",
                "At the end of their turn, players can move to the next player using the 'next' command."
        };

        String guideTitle = "Fungorium Game Guide";

        // Find the longest line
        int maxLength = guideTitle.length();
        for (String line : guideLines) {
            if (line.length() > maxLength) {
                maxLength = line.length();
            }
        }

        // Generate the arrows dynamically
        String arrows = "<" + "-".repeat(maxLength + 4) + ">";

        // Center the title
        int totalWidth = maxLength + 4; // Total width of the arrow line
        int padding = (totalWidth - guideTitle.length()) / 2; // Calculate padding for centering
        String centeredTitle = " ".repeat(padding) + guideTitle;

        // Print the guide
        System.out.println(arrows);
        System.out.print("\033[1;35m");
        System.out.println(centeredTitle);
        System.out.print("\033[0m");// Used for resetting the changes.
        System.out.println(arrows);
        for (String line : guideLines) {
            System.out.println(line);
        }
        System.out.println(arrows);
    }

    /**
     * Selects the players and their types (Fungus or Insect).
     * 
     * @param scanner The scanner to read user input.
     */
    private void selectPlayers(Scanner scanner) {
        System.out.println("The inputs can't process Hungarian characters properly, please use english characters.");
        System.out.println("Dummy players can be added by typing 'dummy'.");
        while (true) {
            if (fungusPlayers == 0 && insectPlayers == 0) {
                System.out.println(
                        "Which type of player would you like to be? Fungus - Insect (F/I) OR type 'start' to start the game.");
            } else {
                System.out.println("Which type of player would you like to be? Fungus - Insect (F/I)");
            }
            String choice = scanner.nextLine().trim().toUpperCase();

            if (choice.equals("START")) {
                printGuide();
                System.out.println("All players are ready. Starting the game...");

                // commandProcessor.start();
                // scanner.close();
                return;
            }

            if (choice.equals("DUMMY")) {
                System.out.println("Generating dummy players...");
                for (int i = 1; i <= 2; i++) {
                    String fungusName = "Fungus" + i;
                    handleSpeciesCreation("Fungus", fungusName);
                    System.out.println("Created Fungus player: " + fungusName);
                    fungusPlayers--;
                }
                for (int i = 1; i <= 2; i++) {
                    String insectName = "Insect" + i;
                    handleSpeciesCreation("Insect", insectName);
                    System.out.println("Created Insect player: " + insectName);
                    insectPlayers--;
                }
                printGuide();
                System.out.println("All dummy players created. Starting the game...");
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
                    case "START":
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
    public void handleSpeciesCreation(String type, String name) {
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
     * Scans for user input and processes commands.
     * Skips rounds if necessary.
     * 
     * @param scanner The scanner to read user input.
     */
    public void takeTurn(Scanner scanner, boolean IsLoaded) {
        while (gameTime > 0) {
            System.out.println("---------> Round: " + (round + 1) + " <---------");
            boolean skipRound = false;
            // Iterate through each player and prompt for commands
            for (String playerName : players.keySet()) {
                if(IsLoaded){
                    if(!playerName.equals(currentSpecies)){
                        System.out.println("It's " + playerName + " and " + currentSpecies );  
                        //itt kene eliteralni a kövi jatekosig
                        continue;
                    }else{
                        IsLoaded = false;
                        // Color playerColor = gamePanel.getGuiBuilder().getPlayerColor(playerName);
                        // gamePanel.setPlayerBorderColor(playerColor);
                        // gamePanel.setCurrentPlayerName(playerName);
                        skipRound = false;
                    }
                }else{
                    currentSpecies = playerName;
                }
                if (skipRound) {
                    break; // Ha a kört át kell ugrani, kilépünk a játékosok ciklusából
                }
                if (gamePanel != null && gamePanel.getGuiBuilder() != null) {
                    Color playerColor = gamePanel.getGuiBuilder().getPlayerColor(playerName);
                    gamePanel.setPlayerBorderColor(playerColor);
                    gamePanel.setCurrentPlayerName(playerName);
                    System.out.println("It's ----->" + playerName + "'s turn.");
                }
                Object player = players.get(playerName);
                System.out.println("It's " + playerName + "'s turn. Enter a command:");
                while (true) {
                    System.out.print("> ");
                    String command;
                    try {
                        command = inputQueue.take(); // Ez blokkol, amíg nincs új parancs
                    } catch (InterruptedException e) {
                        System.out.println("A játék megszakadt.");
                        Thread.currentThread().interrupt();
                        return;
                    }

                    // Check for skip commands
                    if (command.equals("/trig skipround")) {
                        System.out.println("Skipping the current round...");
                        skipRound = true; // Beállítjuk, hogy az egész kört át kell ugrani
                        break; // Kilépünk az aktuális játékos köréből
                    }
                    if (command.equals("/trig skipallrounds")) {
                        System.out.println("Skipping all remaining rounds...");
                        gameTime = 0; // End the game immediately
                        GameLogic.endGame(commandProcessor.getCreatedObjects());
                        return;
                    }

                    if (command.equalsIgnoreCase("next")) {
                        break; // Move to the next player
                    }
                    commandProcessor.process(command, player);
                }
            }

            // Update game state after all players have taken their turns
            for (Object playerObj : players.values()) {
                if (playerObj instanceof FungusSpecies) {
                    ((FungusSpecies) playerObj).timeElapsed(commandProcessor);
                } else if (playerObj instanceof InsectSpecies) {
                    ((InsectSpecies) playerObj).timeElapsed();
                }
            }
            
            
            int chanceofBreaking = RandomGenerator.generateRandomNumber(0, 100);
            if (chanceofBreaking < 5) {
                tektonBreak = true;
                System.out.println("There will be a tekton break!");
            }

            round++;
            newRound = true;
            gameTime--;

            if (gameTime <= 0) {
                GameLogic.endGame(commandProcessor.getCreatedObjects());
                break;
            }
        }
    }

    public int getRound() {
        return round;
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
        System.out.println("<-------------------Játék vége!------------------->");
        System.out.println("Fungus győztes: " + fungusWinner + " pontszám: " + maxFungusScore);
        System.out.println("Insect győztes: " + insectWinner + " pontszám: " + maxInsectScore);
        System.out.println("<------------------------------------------------->");
    }

    /**
     * Moves the specified Insect to the specified FungusThread.
     * Checks if the Insect can reach the specified FungusThread.
     * If the Insect can reach the specified FungusThread, the Insect is moved to the specified FungusThread.
     * If the Insect cannot reach the specified FungusThread, the method prints an error message.
     * 
     * @param insect the Insect to move.
     * @param toThread the FungusThread to move the Insect to.
     */
    public void MoveInsect(Insect insect, FungusThread toThread){
        if(canReachThread(insect, toThread)){
            insect.move(toThread);
        }else{
            System.out.println("Sikertelen elmozgás");
        }
    }

    // ! t1 -> newt <-t2 "Fák gyökerei sem nőnek össze. No para"
    private Boolean canReachThread(Insect insect, FungusThread toThread) {
        Integer distance = 2;
        switch (insect.gEffect()) {
            case SLOW:
                distance = 1;
                break;
            case FAST:
                distance = 3;
                break;
            default:
                distance = 2;
                break;
        }
        FungusThread temp = insect.getThread();
        for (Integer i = 0; i < distance; i++) {
            if (temp == toThread) {
                return true;
            }
            // ? a Body-hoz értünk meg kell nézni, hogy ér-e el másik threadet a bodyból
            else if (temp.getPrev() == null|| temp.getMyBody()!=null) {
                Integer remainingDistance = distance - i - 1; // Mivel az hogy rálép a Body-ra az is egy lépés,
                // szóval Body-ból kijövő fonalak közti váltás az nem 1 hanem 2 lépés
                // 0: nem csinál semmit,
                // 1: body-ból kinövő threadeket nézi,
                // 2: 1-es és a threadek szomszédai
                
                //Olyan thread-et vizsgálunk, amelynek megszakadt a kapcsolata a body-jával, 
                //tehát a getBody az null. Ez akkor történhet mikor egy fonal véghez értünk.
                //Normál esetben ez azt jelenti, hogy Body-hoz értünk, de ha a fonal FeedThreadTektonon van
                //az életben tartja, úgy hogy nincs kapcsolata semmilyen testel. 
                //!A Body csak is kizárólag ebben az esetben lehet null. Máskor sosem. 
                //Olyan is lehet hogy valahol egy összefüggő fonál sor közepén nőtt a test, ezért azt is vizsgáljuk.
                if (temp.getPrevBody() != null || temp.getMyBody() != null) {
                    if (canReachFromBody(toThread, temp, remainingDistance))
                    return true;
                }//Nem csinálunk semmit, ha a body null. Akadályba ütköztünk, ezen úton nem érjük el a keresett fonalat
                break;
            } 
            temp = temp.getPrev();
        }

        temp = insect.getThread().getNext();
        for (Integer i = 0; i < distance; i++) {
            if (temp == toThread) {
                return true;
            } else if (temp == null) {
                break;
            }
            //Ha a getNext() null, akkor fixen egy olyan fonalon vagyunk jelenleg, amiből még gombatest nőtt
            if (temp.getNext() == null|| temp.getMyBody() != null) {
                //Van az az eset, ha szegény rovarunk olyan fonalakon mozog, amelyeknek megszünt a kapcsolata a gombatestjével
                //Nos ilyenkor a cél fonal Body-ja null. Ilyenkor nem tudjuk, megnézni, hogy a cél fonal body-jaból elérhető-e a 
                //keresett fonal, hiszen a test az null.
                //Ha tempnek és a toThreadnek megegyezik a Body-ja(és ez nem null), csak akkor nézhetjük meg, hogy elérhető-e
                //adott body-ból a keresett fonalunk
                //Ha gombatesthez érünk, akkor az a thread amiből kinőtt a gombatest, akkor az annak a mybodyjában van eltárolva
                //Míg a toThreadnek akkor az adott body fixen PrevBody-ban van.
                if (toThread.getPrevBody()!=null && temp.getMyBody() != null && toThread.getPrevBody() == temp.getMyBody()) {
                    if (canReachFromBody(toThread, temp, distance - i - 1))
                        return true;
                }
                break;
            }
            temp = temp.getNext();
        }
        return false;
    }

       /**
     * Gets the map of players.
     * 
     * @return The map of players.
     */
    public Map<String, Object> getPlayers() {
        return players;
    }

    /**
     * Checks if the specified FungusThread can be reached from the body of the specified FungusThread.
     * The method checks if the specified FungusThread is reachable from the body of the specified FungusThread.
     * If the specified FungusThread is reachable, the method returns true, otherwise it returns false.
     * 
     * @param toThread the FungusThread to check if it can be reached from the body of the specified FungusThread.
     * @param temp the FungusThread from which the body is checked.
     * @param distance the maximum distance from the body of the specified FungusThread to check for reachability.
     * @return true if the specified FungusThread can be reached from the body of the specified FungusThread, false otherwise.
     */
    private Boolean canReachFromBody(FungusThread toThread, FungusThread temp, Integer distance) {
        if(temp.getMyBody()!= null){
            for (FungusThread bodyThreads : temp.getMyBody().getThreads()) {
                if (bodyThreads == toThread) {
                    return true;
                } else {
                    Boolean dirChange = false;
                    FungusThread bodyThreadtemp;
                    // ? t3-ből növesztettük a Body-t és a body-ból t1-et és t2-t
                    // ? t1<- FBody ->t2
                    // ? ^
                    // ? |
                    // ? t3
                    if (bodyThreads.getNext() == null && bodyThreads.getPrev() != null) {
                        bodyThreadtemp = bodyThreads.getPrev();
                        dirChange = true;
                    } else {
                        bodyThreadtemp = bodyThreads.getNext();
                        dirChange = false;
                    }

                    for (int j = 0; j < distance; j++) {
                        if (bodyThreadtemp == toThread) {
                            return true;
                        }
                        if (dirChange) {
                            bodyThreadtemp = bodyThreadtemp.getPrev();
                        } else
                            bodyThreadtemp = bodyThreadtemp.getNext();
                    }
                }
            }
        }else{
            for (FungusThread bodyThreads : temp.getPrevBody().getThreads()) {
            if (bodyThreads == toThread) {
                return true;
            } else {
                Boolean dirChange = false;
                FungusThread bodyThreadtemp;
                // ? t3-ből növesztettük a Body-t és a body-ból t1-et és t2-t
                // ? t1<- FBody ->t2
                // ? ^
                // ? |
                // ? t3
                if (bodyThreads.getNext() == null && bodyThreads.getPrev() != null) {
                    bodyThreadtemp = bodyThreads.getPrev();
                    dirChange = true;
                } else {
                    bodyThreadtemp = bodyThreads.getNext();
                    dirChange = false;
                }

                for (int j = 0; j < distance; j++) {
                    if (bodyThreadtemp == toThread) {
                        return true;
                    }
                    if (dirChange) {
                        bodyThreadtemp = bodyThreadtemp.getPrev();
                    } else
                        bodyThreadtemp = bodyThreadtemp.getNext();
                }
            }
        }}
        return false;
    }
    public Map<String, Tekton> getTektons() {
        return tektons;
    }
    public void setTektons(Map<String, Tekton> tektons) {
        this.tektons = tektons;
    }
    public void setRound(int round) {
        this.round = round;
    }
    public void setCurrentSpecies(String species) {
        this.currentSpecies = species;
    }

}
