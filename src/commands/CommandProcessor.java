package commands;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Consumer;

// Model
import fungus.*;
import insect.InsectSpecies;
import insect.Insect;
import logic.GameLogic;
import sporeTypes.*;
import tektonTypes.*;

// ! Commands:
// ? System Commands:
//      /helpSys: kiírja a user és sys parancsokat
//      /helpObj: objektum típusok lekérdezése
//      /load <filename>
//      /break <Tekton>
//      /kill <Insect>
//      /set: kezelés osztályonként
//      /create <objecttype> <name>
// TODO /delete <name>   -  befejezni a delete parancsot
//      /status    az összes állapot lekérdezése
//      /status <name> állapot lekérdezés
//  TODO    /save játék állását menti fájlba
// TODO     /log a konzolon lévő kimeneteket menti fáklba
//      /trig események triggerelése nr: Következő kör np: Következő játékos
//      /chance breaktekton && spora milyen fajta
//
// ? User Commands:
//   *All player commands:
//      help: kiírja a user parancsokat
//      exit
//   *FungusSpecies Commands:
//      growBody <FungusThread> <Tekton> 
//      growThread <Tekton> <FungusBody>
//      growThread <Tekton> <FungusThread>
//      sporulate <FungusBody>
//      eatinsect <Insect> <Thread> 
//   *InsectSpecies Commands:
//      move: <Insect> <Thread>
//      cut <FungusThread>
//      eat <Spore> <Insect> 

//* Type names:
//? Default:
//      body: fungus.Fungusbody
//      spore: sporeTypes.Spore
//      tekton: tektonTypes.Tekton
//      insect: insect.Insect
//      thread: fungus.FungusThread
//      fungusspecies: fungus.FungusSpecies
//      insectspecies: insect.InsectSpecies
//? Custom spores:
//      fastspore: sporeTypes.FastSpore
//      multiplyinsectspore: sporeTypes.MultiplyInsectSpore
//      slowspore: sporeTypes.SlowSpore
//      stunspore: sporeTypes.StunSpore
//      disablecutspore: sporeTypes.DisableCutSpore
//? Custom tektons:
//      decomptekton: tektonTypes.DecomposingTekton
//      decreasetekton: tektonTypes.DecreasingTekton
//      feedthreadtekton: tektonTypes.FeedThreadTekton
//      onethreadtekton: tektonTypes.OneThreadTekton
//      onlythreadtekton: tektonTypes.OnlyThreadTekton

public class CommandProcessor {
    private Map<String, Object> createdObjects = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private Map<String, Consumer<String[]>> commands = new HashMap<>();
    private Map<String, String> commandDescriptions = new HashMap<>();
    private Map<String, String> objectTypeMap = new HashMap<>();

    private Set<String> fungusCommands = Set.of("growBody", "growThread", "sporulate", "eatinsect");
    private Set<String> insectCommands = Set.of("move", "cut", "eat");
    private Set<String> commonCommands = Set.of("help", "exit");
    private Set<String> systemCommands = Set.of("/helpsys", "/helpobj", "/load", "/break", "/kill", "/set", "/delete",
            "/status", "/save", "/log", "/trig", "/create");

    private GameLogic gameLogic;

    // ! -------------------------- INIT -----------------------------
    /*
     * Parancsok regisztrálása
     */
    public CommandProcessor() {
        initializeObjectTypeMap();

        commands.put("help", parts -> help());
        commandDescriptions.put("help", "help");

        commands.put("/helpsys", parts -> helpSys());
        commandDescriptions.put("/helpsys", "/helpsys");

        commands.put("/helpobj", parts -> helpObj());
        commandDescriptions.put("/helpobj", "/helpobj");

        commands.put("/load", parts -> processConfigText(parts[1]));
        commandDescriptions.put("/load", "/load <filename>");

        commands.put("/create", this::processCreateCommand);
        commandDescriptions.put("/create", "/create <objecttype> <name>");

        commands.put("/delete", this::processDeleteCommand);
        commandDescriptions.put("/delete", "/delete <name>");

        commands.put("/set", this::processSetCommand);
        commandDescriptions.put("/set", "/set <object> <property> <value>");

        commands.put("/status", this::processStatusCommand);
        commandDescriptions.put("/status", "/status <name>");

        commands.put("/break", this::processBreakCommand);
        commandDescriptions.put("/break", "/break <Tekton>");

        commands.put("/trig", this::processTriggerCommand);
        commandDescriptions.put("/trig", "/trig <event>");

        commands.put("cut", this::processCutCommand);
        commandDescriptions.put("cut", "cut <FungusThread> <Insect>");

        commands.put("sporulate", this::processSporulateCommand);
        commandDescriptions.put("sporulate", "sporulate <FungusBody>");

        commands.put("eat", this::processEatCommand);
        commandDescriptions.put("eat", "eat <Spore> <Insect>");

        commands.put("kill", this::processKillCommand);
        commandDescriptions.put("kill", "kill <Insect>");

        commands.put("move", this::processMoveCommand);
        commandDescriptions.put("move", "move <Insect> <Thread>");

        commands.put("growbody", this::processGrowBodyCommand);
        commandDescriptions.put("growbody", "growbody <FungusThread>");

        commands.put("growthread", this::processGrowThreadCommand);
        commandDescriptions.put("growthread", "growthread <Tekton> <FungusBody>/<FungusThread>");

        commands.put("eatinsect", this::processEatInsectCommand);
        commandDescriptions.put("eatinsect", "eatinsect <Insect> <Thread>");
    }

    public CommandProcessor(GameLogic gameLogic) {
        initializeObjectTypeMap();

        this.gameLogic = gameLogic;

        commands.put("help", parts -> help());
        commandDescriptions.put("help", "help");

        commands.put("/helpsys", parts -> helpSys());
        commandDescriptions.put("/helpsys", "/helpsys");

        commands.put("/helpobj", parts -> helpObj());
        commandDescriptions.put("/helpobj", "/helpobj");

        commands.put("/load", parts -> processConfigText(parts[1]));
        commandDescriptions.put("/load", "/load <filename>");

        commands.put("/create", this::processCreateCommand);
        commandDescriptions.put("/create", "/create <objecttype> <name>");

        commands.put("/delete", this::processDeleteCommand);
        commandDescriptions.put("/delete", "/delete <name>");

        commands.put("/set", this::processSetCommand);
        commandDescriptions.put("/set", "/set <object> <property> <value>");

        commands.put("/status", this::processStatusCommand);
        commandDescriptions.put("/status", "/status <name>");

        commands.put("/break", this::processBreakCommand);
        commandDescriptions.put("/break", "/break <Tekton>");

        commands.put("/trig", this::processTriggerCommand);
        commandDescriptions.put("/trig", "/trig <event>");

        commands.put("cut", this::processCutCommand);
        commandDescriptions.put("cut", "cut <FungusThread> <Insect>");

        commands.put("sporulate", this::processSporulateCommand);
        commandDescriptions.put("sporulate", "sporulate <FungusBody>");

        commands.put("eat", this::processEatCommand);
        commandDescriptions.put("eat", "eat <Spore> <Insect>");

        commands.put("/kill", this::processKillCommand);
        commandDescriptions.put("/kill", "/kill <Insect>");

        commands.put("move", this::processMoveCommand);
        commandDescriptions.put("move", "move <Insect> <Thread>");

        commands.put("growbody", this::processGrowBodyCommand);
        commandDescriptions.put("growbody", "growbody <FungusThread>");

        commands.put("growthread", this::processGrowThreadCommand);
        commandDescriptions.put("growthread", "growthread <Tekton> <FungusBody>/<FungusThread>");

        commands.put("eatinsect", this::processEatInsectCommand);
        commandDescriptions.put("eatinsect", "eatinsect <Insect> <Thread>");
    }

    // ! InsectSpecies és FungusSpecies parancsok
    private void initializeObjectTypeMap() {
        objectTypeMap.put("fungusspecies", "FungusSpecies");
        objectTypeMap.put("insectspecies", "InsectSpecies");
        objectTypeMap.put("body", "FungusBody");
        objectTypeMap.put("spore", "Spore");
        objectTypeMap.put("tekton", "Tekton");
        objectTypeMap.put("insect", "Insect");
        objectTypeMap.put("thread", "FungusThread");
        objectTypeMap.put("disablecutspore", "DisableCutSpore");
        objectTypeMap.put("fastspore", "FastSpore");
        objectTypeMap.put("multiplyinsectspore", "MultiplyInsectSpore");
        objectTypeMap.put("slowspore", "SlowSpore");
        objectTypeMap.put("stunspore", "StunSpore");
        objectTypeMap.put("decomptekton", "DecomposingTekton");
        objectTypeMap.put("decreasetekton", "DecreasingTekton");
        objectTypeMap.put("feedthreadtekton", "FeedThreadTekton");
        objectTypeMap.put("onethreadtekton", "OneThreadTekton");
        objectTypeMap.put("onlythreadtekton", "OnlyThreadTekton");
    }

    public void clearCreatedObjects() {
        createdObjects.clear();
    }

    // ! -------------------------- COMMNAD HANDLING -----------------------------

    /*
     * Parancsok folyamatos kérése
     */
    public void start() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();
            if ("exit".equals(input)) {
                break;
            }
            process(input);
        }
        scanner.close();
    }

    /*
     * A parancsok feldolgozása
     */
    public void process(String input) {
        String[] parts = input.split(" ");
        String command = parts[0];

        Consumer<String[]> action = commands.get(command);
        if (action != null) {
            try {
                action.accept(parts);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Hibás parancs! Túl kevés paraméter.");
                System.out.println("Helyes használat: " + commandDescriptions.get(command));
            }
        } else {
            System.out.println("Ismeretlen parancs: " + command);
        }
    }

    public void process(String input, Object player) {
        String[] parts = input.split(" ");
        String command = parts[0];

        if (player instanceof FungusSpecies && !fungusCommands.contains(command) && !commonCommands.contains(command)
                && !systemCommands.contains(command)) {
            System.out.println("Hiba: FungusSpecies nem használhatja ezt a parancsot: " + command);
            return;
        }

        if (player instanceof InsectSpecies && !insectCommands.contains(command) && !commonCommands.contains(command)
                && !systemCommands.contains(command)) {
            System.out.println("Hiba: InsectSpecies nem használhatja ezt a parancsot: " + command);
            return;
        }

        // Kommentezz ki a következő sort, ha nem akarod, hogy a játékosok
        // használhassák a rendszerparancsokat
        /*
         * if (systemCommands.contains(command)) {
         * System.out.
         * println("Hiba: A rendszerparancsok nem használhatók játékosok által: "
         * +command);
         * return;
         * }
         */

        // Ha a parancs érvényes, hajtsd végre
        Consumer<String[]> action = commands.get(command);
        if (action != null) {
            try {
                action.accept(parts);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Hibás parancs! Túl kevés paraméter.");
                System.out.println("Helyes használat: " + commandDescriptions.get(command));
            }
        } else {
            System.out.println("Ismeretlen parancs: " + command);
        }
    }

    // ! ------------------------------ HELP -----------------------------
    /*
     * A user parancsok kiírására szolgáló függvény
     */
    public void help() {
        System.out.println("User Commands:");
        System.out.println(" ");
        System.out.println("help \t\t\t\t\t\t prints the commands");
        System.out.println("cut <FungusThread> <Insect> \t\t\t cuts a thread with the selected insect");
        System.out.println("eat <Spore> <Insect> \t\t\t\t eats a spore with the selected insect");
        System.out.println("move <Insect> <Thread> \t\t\t\t moves an insect to the selected thread");
        System.out.println("growBody <FungusThread> <Tekton> \t\t grows a body");
        System.out.println("growThread <Tekton> <FungusBody> \t\t grows a thread from the selected body");
        System.out.println("growThread <Tekton> <Thread> \t\t\t grows a thread from an existing thread");
        System.out.println("sporulate <FungusBody> \t\t\t\t sporulates with the selected body");
        System.out.println("exit \t\t\t\t\t\t exits the program");
    }

    /*
     * A Sys és user parancsok kiírására szolgáló függvény
     */
    public void helpSys() {
        System.out.println(
                "/-------------------------------------------------------------------------------------------\\");
        System.out.println("System Commands:");
        System.out.println(" ");
        System.out.println("/helpsys \t\t\t\t\t prints all the system the commands");
        System.out.println("/helpobj \t\t\t\t\t prints all available objectTypes");
        System.out.println("/create <objectType> <name> \t\t\t creates an object with the given name");
        System.out.println("/delete <name> \t\t\t\t\t deletes the object with the given name");
        System.out.println("/status <name> \t\t\t\t\t prints an object's status");
        System.out.println("/load <filename> \t\t\t\t loads the commands from the given file");
        System.out.println("/break <Tekton> \t\t\t\t breaks a tekton");
        System.out.println("/kill <Insect> \t\t\t\t\t kills an insect");
        System.out.println("/set <object> <property> <value> \t\t sets the property of the object to the given value");
        // System.out.println("/log <filename> \t\t\t\t saves the console output to a file");
        System.out.println("/trig <event> \t\t\t\t\t triggers an event (next round, next player)");
        System.out.println("/endgame \t\t\t\t\t ends the game and prints the winners");
        System.out.println(" ");
        this.help();
        System.out.println(
                "\\-------------------------------------------------------------------------------------------/\n");
    }

    /*
     * Az objektum típusok kiírására szolgáló függvény
     */
    public void helpObj() {
        System.out.println("/-----------------------------------------------------------\\");
        System.out.println("All available object types:");
        objectTypeMap.forEach((key, value) -> {
            System.out.printf("%-20s creates a %s%n", key, value);
        });
        System.out.println("\\-----------------------------------------------------------/\n");
    }

    // ! ------------------------------ UTILS -----------------------------
    /*
     * Vissaadja a létrehozott objektumokat
     */
    public TreeMap<String, Object> getCreatedObjects() {
        return (TreeMap<String, Object>) createdObjects;
    }

    /**
     * Finds the key associated with the given object in the createdObjects map.
     *
     * @param target The object to search for.
     * @return The key associated with the object, or null if not found.
     */
    public <T> String findByObject(T target) {
        for (Map.Entry<String, Object> entry : createdObjects.entrySet()) {
            if (entry.getValue().equals(target)) {
                return entry.getKey();
            }
        }
        return null; // Return null if the object is not found
    }

    /**
     * Counts the number of objects of a specific type in the createdObjects map.
     *
     * @param <T>  The type of objects to count.
     * @param type The class of the type to count.
     * @return The number of objects of the specified type.
     */
    public <T> int countObjectsOfType(Class<T> type) {
        return (int) createdObjects.values().stream()
                .filter(type::isInstance)
                .count();
    }

    /**
     * Generates a unique name for a FungusBody by recursively checking if the name
     * exists in the createdObjects map.
     *
     * @param baseName The base name to start with (e.g., "b").
     * @param count    The current count to append to the base name.
     * @return A unique name that does not exist in the createdObjects map.
     */
    private String generateUniqueName(String baseName, int count) {
        String name = baseName + count;
        if (createdObjects.containsKey(name)) {
            return generateUniqueName(baseName, count + 1); // Rekurzív hívás, ha a név már létezik
        }
        return name; // Visszatér a nem létező névvel
    }

    // ! ------------------------------ COMMANDS -----------------------------

    /*
     * Create parancs formája: /create <objecttype> <name>
     * Példa: /create Insect i1
     * 
     * @param parts: parancs részei
     */
    public void processCreateCommand(String[] parts) {
        String objectType = parts[1].toLowerCase();
        String name = parts[2];

        if (!objectTypeMap.containsKey(objectType)) {
            System.out.println("Hiba: Ismeretlen objektumtípus: " + objectType);
            return;
        }

        if (createdObjects.containsKey(name)) {
            System.out.println("Hiba: Már létezik ilyen nevű objektum: " + name);
            return;
        }

        String className = objectTypeMap.get(objectType);
        String[] packages = { "tektonTypes", "fungus", "sporeTypes", "insect" };

        boolean created = false;

        for (String pkg : packages) {
            try {
                Class<?> clazz = Class.forName(pkg + "." + className);
                Object instance = clazz.getDeclaredConstructor().newInstance();
                createdObjects.put(name, instance);
                created = true;
                break;
            } catch (ClassNotFoundException e) {
                // Ha az osztály nem található az adott csomagban, próbáljuk a következőt
            } catch (Exception e) {
                System.out.println("Hiba az objektum létrehozásakor: " + e.getMessage());
                return;
            }
        }

        if (!created) {
            System.out.println("Hiba: Nem sikerült létrehozni az objektumot: " + className);
        }
    }

    /*
     * delete parancs formája: /delete <name>
     * Példa: /delete i1
     * 
     * @param parts: parancs részei
     */
    public void processDeleteCommand(String[] parts) {
        String name = parts[1];

        if (!createdObjects.containsKey(name)) {
            System.out.println("Hiba: Nem létezik ilyen nevű objektum: " + name);
            return;
        }

        Object obj = createdObjects.get(name);

        if (obj instanceof Tekton || obj instanceof DecomposingTekton || obj instanceof DecreasingTekton
                || obj instanceof FeedThreadTekton || obj instanceof OneThreadTekton
                || obj instanceof OnlyThreadTekton) {
            Tekton tekton = (Tekton) obj;
            tekton.deleteTekton();
        } else if (obj instanceof Insect) {
            Insect insect = (Insect) obj;
            if (insect.getMyOwner() == null || insect.getRecent() == null) {
                System.out.println(
                        "Hiba: Nem lehet törölni az Insect-et, mert nincs hozzárendelve Entomologist vagy Tekton!");
                return;
            }
            insect.deadInsect(this);
        } else if (obj instanceof Spore || obj instanceof FastSpore || obj instanceof MultiplyInsectSpore
                || obj instanceof SlowSpore || obj instanceof StunSpore || obj instanceof DisableCutSpore) {
            Spore spore = (Spore) obj;
            if (spore.getTekton() == null) {
                System.out.println("Hiba: Nem lehet törölni a Spore-t, mert nincs hozzárendelve Tekton!");
                return;
            }
            spore.absorbed();
            System.out.println("- Deleted Spore | Name: " + name);
        } else if (obj instanceof FungusThread) {
            FungusThread thread = (FungusThread) obj;
            thread.setLifeSpan(0);
            thread.destroy();
        } else if (obj instanceof FungusBody) {
            FungusBody body = (FungusBody) obj;
            if (body.getSpecies() == null) {
                System.out.println("Hiba: Nem lehet törölni a FungusBody-t, mert nincs hozzárendelve FungusSpecies!");
                return;
            }
            body.getSpecies().deleteBody(body);
        } else if (obj instanceof InsectSpecies) {
            InsectSpecies player = (InsectSpecies) obj;
            for (Insect insect : player.getInsects()) {
                insect.deadInsect(this);
                player.removeInsect(insect);
            }
        } else if (obj instanceof FungusSpecies) {
            FungusSpecies player = (FungusSpecies) obj;
            for (FungusBody body : player.getBodies()) {
                body.getSpecies().deleteBody(body);
            }

            for (FungusThread thread : player.getThreads()) {
                thread.destroy();
            }
        } else {
            System.out.println("Hiba: Ismeretlen típusú objektum: " + name);
        }

        createdObjects.remove(name);
    }

    /*
     * A státusz kiírására szolgáló függvény
     * 
     * @param name: objektum neve
     * 
     * @param obj: objektum
     */
    public void printObjectStatus(String name, Object obj) {
        System.out.println("Név: " + name);

        if (obj instanceof FungusThread) {
            FungusThread thread = (FungusThread) obj;
            String PrevBodyName = createdObjects.entrySet().stream()
                    .filter(entry -> entry.getValue() == thread.getPrevBody())
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse("N/A");
            String NextBodyName = createdObjects.entrySet().stream()
                    .filter(entry -> entry.getValue() == thread.getNextBody())
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse("N/A");
            String MyBodyName = createdObjects.entrySet().stream()
                    .filter(entry -> entry.getValue() == thread.getMyBody())
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse("N/A");
            String speciesName = createdObjects.entrySet().stream()
                    .filter(entry -> entry.getValue() == thread.getSpecies())
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse("N/A");
            String tektonNames = thread.getTektons().stream()
                    .map(tekton -> createdObjects.entrySet().stream()
                            .filter(entry -> entry.getValue() == tekton)
                            .map(Map.Entry::getKey)
                            .findFirst()
                            .orElse("N/A"))
                    .toList()
                    .toString();
            String prevName = createdObjects.entrySet().stream()
                    .filter(entry -> entry.getValue() == thread.getPrev())
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse("N/A");
            String nextName = createdObjects.entrySet().stream()
                    .filter(entry -> entry.getValue() == thread.getNext())
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse("N/A");
            System.out.println("FungusThread: "
                    + "\n\tSpecies: " + speciesName
                    + "\n\tPrevBody: " + PrevBodyName
                    + "\n\tNextBody: " + NextBodyName
                    + "\n\tMyBody: " + MyBodyName
                    + "\n\tTektons: " + tektonNames
                    + "\n\tIsBridge: " + thread.isBridge()
                    + "\n\tLifespan: " + thread.getLifeSpan()
                    + "\n\tIsDying: " + thread.getIsDying()
                    + "\n\tNext: " + nextName
                    + "\n\tPrev: " + prevName);
        } else if (obj instanceof FungusBody) {
            FungusBody body = (FungusBody) obj;
            String tektonName = createdObjects.entrySet().stream()
                    .filter(entry -> entry.getValue() == body.getTekton())
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse("N/A");
            String speciesName = createdObjects.entrySet().stream()
                    .filter(entry -> entry.getValue() == body.getSpecies())
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse("N/A");
            String threadNames = body.getThreads().stream()
                    .map(thread -> createdObjects.entrySet().stream()
                            .filter(entry -> entry.getValue() == thread)
                            .map(Map.Entry::getKey)
                            .findFirst()
                            .orElse("N/A"))
                    .toList()
                    .toString();
            System.out.println("FungusBody: "
                    + "\n\tSpecies: " + speciesName
                    + "\n\tTekton: " + tektonName
                    + "\n\tThreads: " + threadNames
                    + "\n\tSporecount: " + body.getSporeCount()
                    + "\n\tSporulateLeft: " + body.getSporulateLeft());
        } else if (obj instanceof FungusSpecies) {
            FungusSpecies species = (FungusSpecies) obj;
            String bodyNames = species.getBodies().stream()
                    .map(body -> createdObjects.entrySet().stream()
                            .filter(entry -> entry.getValue() == body)
                            .map(Map.Entry::getKey)
                            .findFirst()
                            .orElse("N/A"))
                    .toList()
                    .toString();
            System.out.println("FungusSpecies: "
                    + "\n\tScore: " + species.getScore()
                    + "\n\tBodies: " + bodyNames);
        } else if (obj instanceof InsectSpecies) {
            InsectSpecies species = (InsectSpecies) obj;
            String insectNames = species.getInsects().stream()
                    .map(body -> createdObjects.entrySet().stream()
                            .filter(entry -> entry.getValue() == body)
                            .map(Map.Entry::getKey)
                            .findFirst()
                            .orElse("N/A"))
                    .toList()
                    .toString();
            System.out.println("InsectSpecies: "
                    + "\n\tScore: " + species.getScore()
                    + "\n\tInsects: " + insectNames);
        } else if (obj instanceof Insect) {
            Insect insect = (Insect) obj;
            String threadName = createdObjects.entrySet().stream()
                    .filter(entry -> entry.getValue() == insect.getThread())
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse("N/A");
            String ownerName = createdObjects.entrySet().stream()
                    .filter(entry -> entry.getValue() == insect.getMyOwner())
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse("N/A");
            System.out.println("Insect: "
                    + "\n\tOwner: " + ownerName
                    + "\n\tThread: " + threadName
                    + "\n\tEffect: " + insect.gEffect());
        } else if (obj instanceof Spore) {
            Spore spore = (Spore) obj;
            String sporeType = "Spore";

            if (obj instanceof FastSpore) {
                sporeType = "FastSpore";
            } else if (obj instanceof MultiplyInsectSpore) {
                sporeType = "MultiplyInsectSpore";
            } else if (obj instanceof SlowSpore) {
                sporeType = "SlowSpore";
            } else if (obj instanceof StunSpore) {
                sporeType = "StunSpore";
            } else if (obj instanceof DisableCutSpore) {
                sporeType = "DisableCutSpore";
            }

            String tektonName = createdObjects.entrySet().stream()
                    .filter(entry -> entry.getValue() == spore.getTekton())
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse("N/A");
            System.out.println(sporeType + ": "
                    + "\n\tNutrition value: " + spore.getNutValue()
                    + "\n\tTekton: " + tektonName);
        } else if (obj instanceof Tekton) {
            Tekton tekton = (Tekton) obj;
            String tektonType = "Tekton";

            if (obj instanceof DecomposingTekton) {
                tektonType = "DecomposingTekton";
            } else if (obj instanceof DecreasingTekton) {
                tektonType = "DecreasingTekton";
            } else if (obj instanceof FeedThreadTekton) {
                tektonType = "FeedThreadTekton";
            } else if (obj instanceof OneThreadTekton) {
                tektonType = "OneThreadTekton";
            } else if (obj instanceof OnlyThreadTekton) {
                tektonType = "OnlyThreadTekton";
            }

            String bodyName = createdObjects.entrySet().stream()
                    .filter(entry -> entry.getValue() == tekton.getBody())
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse("N/A");
            String insectNames = tekton.getInsects().stream()
                    .map(insect -> createdObjects.entrySet().stream()
                            .filter(entry -> entry.getValue() == insect)
                            .map(Map.Entry::getKey)
                            .findFirst()
                            .orElse("N/A"))
                    .toList()
                    .toString();
            String tektonNames = tekton.getNeighbours().stream()
                    .map(tektonObj -> createdObjects.entrySet().stream()
                            .filter(entry -> entry.getValue() == tektonObj)
                            .map(Map.Entry::getKey)
                            .findFirst()
                            .orElse("N/A"))
                    .toList()
                    .toString();
            String sporeNames = tekton.getSpores().stream()
                    .map(spore -> createdObjects.entrySet().stream()
                            .filter(entry -> entry.getValue() == spore)
                            .map(Map.Entry::getKey)
                            .findFirst()
                            .orElse("N/A"))
                    .toList()
                    .toString();
            String threadNames = tekton.getThreads().stream()
                    .map(thread -> createdObjects.entrySet().stream()
                            .filter(entry -> entry.getValue() == thread)
                            .map(Map.Entry::getKey)
                            .findFirst()
                            .orElse("N/A"))
                    .toList()
                    .toString();
            System.out.println(tektonType + ": "
                    + "\n\tCanGrowThread: " + tekton.canGrowThread()
                    + "\n\tCanGrowBody: " + tekton.canGrowBody()
                    + "\n\tBodies: " + bodyName
                    + "\n\tInsects: " + insectNames
                    + "\n\tNeighbours: " + tektonNames
                    + "\n\tSpores: " + sporeNames
                    + "\n\tThreads: " + threadNames);
        } else {
            System.out.println("Ismeretlen objektumtípus: " + obj.getClass().getName());
        }
        System.out.println();
    }

    /*
     * Status parancs formája: /status <name>
     * Példa: /status i1 vagy /status
     * 
     * @param parts: parancs részei
     */
    public void processStatusCommand(String[] parts) {
        if (parts.length == 1) {
            // Ha nincs paraméter, írja ki az összes objektum státuszát
            // Forma: /status
            createdObjects.forEach(this::printObjectStatus);
        } else {
            // Ha van paraméter, az adott objektum státuszát írja ki
            // Forma: /status <name>
            String name = parts[1];

            if (!createdObjects.containsKey(name)) {
                System.out.println("Hiba: Nem létezik ilyen nevű objektum: " + name);
                return;
            }

            Object obj = createdObjects.get(name);
            printObjectStatus(name, obj);
        }
    }

    // ! Javítani doksiban
    /*
     * growbody parancs formája: growbody <FungusThread>
     * Példa: growbody th1
     * 
     * @param parts: parancs részei
     */
    public void processGrowBodyCommand(String[] parts) {
        String threadName = parts[1];
        if (!createdObjects.containsKey(threadName)) {
            System.out.println("Hiba: Nem létezik ilyen nevű objektum: " + threadName);
            return;
        }

        Object obj = createdObjects.get(threadName);
        if (obj instanceof FungusThread) {
            FungusThread thread = (FungusThread) obj;
            // actual growbody
            String bodyName = "bo" + (countObjectsOfType(FungusBody.class) + 1);
            FungusBody nBody = thread.getSpecies().growBody(thread);
            if (nBody == null) {
                System.out.println("Hiba: Nem lehetett gombatestet növeszteni a megadott paraméterekkel.");
                return;
            }
            createdObjects.put(bodyName, nBody);
        } else {
            System.out.println("Hiba: Nem lehet növeszteni ezt az objektumot: " + threadName);
        }
    }

    /*
     * growthread parancs formája:
     * growthread <Tekton> <FungusBody>
     * Példa: growthread t1 b1
     * 
     * growthread parancs formája:
     * growthread <Tekton> <existingFungusThread>
     * Példa: growthread t1 th1
     * 
     * @param parts: parancs részei
     */
    public void processGrowThreadCommand(String[] parts) {
        String tektonName = parts[1];
        String secondParam = parts[2];
        String newThreadName = "th" + (countObjectsOfType(FungusThread.class) + 1);

        // Második param type check
        Object obj = createdObjects.get(secondParam);

        if (!createdObjects.containsKey(tektonName)) {
            System.out.println("Hiba: Nem létezik ilyen nevű objektum: " + tektonName);
            return;
        }

        if (!createdObjects.containsKey(secondParam)) {
            System.out.println("Hiba: Nem létezik ilyen nevű objektum: " + secondParam);
            return;
        }

        // Második param fungusBody Típusú
        if (obj instanceof FungusBody) {
            if (createdObjects.get(tektonName) instanceof Tekton
                    && createdObjects.get(secondParam) instanceof FungusBody) {
                Tekton tekton = (Tekton) createdObjects.get(tektonName);
                FungusBody body = (FungusBody) createdObjects.get(secondParam);
                // Actual growthread
                FungusThread nThread = body.getSpecies().growThread(tekton, body);
                if (nThread == null) {
                    System.out.println("Hiba: Nem lehetett fonalat növeszteni a megadott paraméterekkel");
                    return;
                }
                createdObjects.put(newThreadName, nThread);
            } else {
                System.out.println("Hiba: Valamelyik paraméter nem megfelelő típusú.");
            }
        } else if (obj instanceof FungusThread) {
            if (createdObjects.get(tektonName) instanceof Tekton
                    && createdObjects.get(secondParam) instanceof FungusThread) {
                Tekton tekton = (Tekton) createdObjects.get(tektonName);
                FungusThread thread = (FungusThread) createdObjects.get(secondParam);
                // Actual growthread
                FungusThread nThread = thread.getSpecies().growThread(tekton, thread);
                if (nThread == null) {
                    System.out.println("Hiba: Nem lehetett fonalat növeszteni a megadott paraméterekkel");
                    return;
                }
                createdObjects.put(newThreadName, nThread);
            } else {
                System.out.println("Hiba: Valamelyik paraméter nem megfelelő típusú.");
            }
        }
    }

    /*
     * Eat parancs formája: eat <Spore> <Insect>
     * Példa: eat s1 i1
     * 
     * @param parts: parancs részei
     */
    public void processEatCommand(String[] parts) {
        String SporeName = parts[1];
        String InsectName = parts[2];
        if (!createdObjects.containsKey(SporeName)) {
            System.out.println("Hiba: Nem létezik ilyen nevű objektum: " + SporeName);
            return;
        }

        if (!createdObjects.containsKey(InsectName)) {
            System.out.println("Hiba: Nem létezik ilyen nevű objektum: " + InsectName);
            return;
        }

        Object objSpore = createdObjects.get(SporeName);
        Object objInsect = createdObjects.get(InsectName);
        if (objInsect instanceof Insect) {
            Insect insect = (Insect) objInsect;
            Spore spore = (Spore) objSpore;
            if (spore instanceof MultiplyInsectSpore) {
                Insect newInsect = insect.consumeMultiplySpore(spore);
                if (newInsect != null) {
                    String newInsectName = generateUniqueName("i", countObjectsOfType(Insect.class));
                    createdObjects.put(newInsectName, newInsect);
                    // System.out.println("Új rovar jött létre: " + newInsectName);
                }
            } else {
                insect.consumeSpore(spore);
            }
            // Debug purposes
            // System.out.println("Az Insect megette a Spore-t!");
        } else {
            System.out.println("Hiba: Ez az objektum nem ehető: " + objSpore);
        }
    }

    /*
     * Grow parancs formája: sporulate <FungusBody>
     * Példa: sporulate b1
     * 
     * @param parts: parancs részei
     */
    public void processSporulateCommand(String[] parts) {
        String name = parts[1];

        if (!createdObjects.containsKey(name)) {
            System.out.println("Hiba: Nem létezik ilyen nevű objektum: " + name);
            return;
        }

        Object obj = createdObjects.get(name);
        if (obj instanceof FungusBody) {
            FungusBody body = (FungusBody) obj;
            List<Spore> createdSpores = body.sporulate();
            if (createdSpores == null) {
                System.out.println("Hiba: Nem sikerült spórát szórni");
                return;
            }
            for (Spore element : createdSpores) {
                String created_name = "spore" + countObjectsOfType(Spore.class);
                createdObjects.put(created_name, element);
            }
            // Debug purposes
            // System.out.println("A FungusBody sporulált!");
        } else {
            System.out.println("Hiba: Nem lehet sporulálni ezt az objektumot: " + name);
        }
    }

    /*
     * Grow parancs formája: cut <FungusThread> <Insect>
     * Példa: cut th1 i1
     * 
     * @param parts: parancs részei
     */
    public void processCutCommand(String[] parts) {
        String ThreadName = parts[1];
        String InsectName = parts[2];

        if (!createdObjects.containsKey(ThreadName)) {
            System.out.println("Hiba: Nem létezik ilyen nevű objektum: " + ThreadName);
            return;
        }

        if (!createdObjects.containsKey(InsectName)) {
            System.out.println("Hiba: Nem létezik ilyen nevű objektum: " + InsectName);
            return;
        }

        Object objThread = createdObjects.get(ThreadName);
        Object objInsect = createdObjects.get(InsectName);
        if (objThread instanceof FungusThread && objInsect instanceof Insect) {
            FungusThread thread = (FungusThread) objThread;
            Insect insect = (Insect) objInsect;
            insect.cut(thread);
            // Debug purposes
            // System.out.println("A FungusThread el lett vágva!");
        } else {
            System.out.println("Hiba: Nem lehet vágni ezt az objektumot: " + ThreadName);
        }
    }

    /*
     * Grow parancs formája: kill <Insect>
     * Példa: /kill i1
     * 
     * @param parts: parancs részei
     */
    public void processKillCommand(String[] parts) {
        String name = parts[1];

        if (!createdObjects.containsKey(name)) {
            System.out.println("Hiba: Nem létezik ilyen nevű objektum: " + name);
            return;
        }

        Object obj = createdObjects.get(name);
        if (obj instanceof Insect) {
            Insect insect = (Insect) obj;
            insect.deadInsect(this);
            // Debug purposes
            // System.out.println("Az Insect meghalt!");
        } else {
            System.out.println("Hiba: Nem lehet megölni ezt az objektumot: " + name);
        }
    }

    /*
     * Load parancs formája: load <fileName>
     * Példa: /load config
     * 
     * @param fileName A beolvasandó fájl neve (kiterjesztés nélkül).
     * A fájlnak a "data" mappában kell lennie.
     */
    public void processConfigText(String fileName) {
        String filePath = "data/" + fileName + ".txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    process(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Hiba a fájl beolvasásakor: " + e.getMessage());
        }
    }

    /*
     * Teszt fájlok futtatására szolgáló függvény
     * 
     * @param fileName A teszt fájl neve (kiterjesztés nélkül).
     * A fájlnak a "data/tests" mappában kell lennie.
     */
    public void runTest(String fileName) {
        String testFilePath = "data/tests/" + fileName + ".txt";
        String outputFilePath = "data/output/" + fileName + "_output.txt";

        // Redirect System.out to the output file
        try (BufferedReader reader = new BufferedReader(new FileReader(testFilePath));
                PrintStream fileOut = new PrintStream(new FileOutputStream(outputFilePath))) {

            // Redirect System.out to the file
            PrintStream originalOut = System.out;
            System.setOut(fileOut);

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    process(line); // Process each command
                }
            }

            // Restore the original System.out
            System.setOut(originalOut);

        } catch (IOException e) {
            System.err.println("Hiba a teszt futtatása közben: " + e.getMessage());
        }
    }

    /*
     * Set parancs formája: set <object> <property> <value>
     * Példa: /set th1 lifespan 10
     */
    public void processSetCommand(String[] parts) {
        String objectName = parts[1];
        String property = parts[2];
        String value = parts[3];

        if (!createdObjects.containsKey(objectName)) {
            System.out.println("Hiba: Nem létezik ilyen nevű objektum: " + objectName);
            return;
        }

        Object obj = createdObjects.get(objectName);
        // debug
        // System.out.println("Objektum: " + obj.getClass().getName());
        // ! FungusThread
        if (obj instanceof FungusThread) {
            FungusThread thread = (FungusThread) obj;
            switch (property) {
                case "addtekton":
                    if (createdObjects.get(value) instanceof Tekton)
                        thread.addTekton((Tekton) createdObjects.get(value));
                    else
                        System.out.println("Hiba: Nem létezik ilyen nevű objektum: " + value);
                    break;
                case "lifespan":
                    thread.setLifeSpan(Integer.parseInt(value));
                    break;
                case "bridge":
                    thread.setBridge(Boolean.parseBoolean(value));
                    break;
                case "isdying":
                    thread.setIsDying(Boolean.parseBoolean(value));
                    break;
                case "prevbody":
                    thread.setPrevBody((FungusBody) createdObjects.get(value));
                    break;
                case "nextbody":
                    thread.setNextBody((FungusBody) createdObjects.get(value));
                    break;
                case "mybody":
                    thread.setMyBody((FungusBody) createdObjects.get(value));
                    break;
                case "species":
                    thread.setSpecies((FungusSpecies) createdObjects.get(value));
                    break;
                case "prev":
                    thread.setPrevThread((FungusThread) createdObjects.get(value));
                    break;
                case "next":
                    thread.setNextThread((FungusThread) createdObjects.get(value));
                    break;
                default:
                    System.out.println("Hiba: Nem létezik ilyen tulajdonság: " + property);
                    return;
            }
            // Debug purposes
            // System.out.println("Sikeresen frissítve: " + objectName + " | " + property +
            // ": " + value);
            return;
        }

        // ! FungusBody
        else if (obj instanceof FungusBody) {
            FungusBody body = (FungusBody) obj;
            switch (property) {
                case "species":
                    body.setSpecies((FungusSpecies) createdObjects.get(value));
                    break;
                case "sporecount":
                    body.setSporeC(Integer.parseInt(value));
                    break;
                case "tekton":
                    if (createdObjects.get(value) instanceof Tekton)
                        body.setTekton((Tekton) createdObjects.get(value));
                    else
                        System.out.println("Hiba: Nem létezik ilyen nevű objektum: " + value);
                    break;
                case "addthread":
                    if (createdObjects.get(value) instanceof FungusThread)
                        body.addThread((FungusThread) createdObjects.get(value));
                    else
                        System.out.println("Hiba: Nem létezik ilyen nevű objektum: " + value);
                    break;
                case "deletethread":
                    if (createdObjects.get(value) instanceof FungusThread)
                        body.removeThread((FungusThread) createdObjects.get(value));
                    else
                        System.out.println("Hiba: Nem létezik ilyen nevű objektum: " + value);
                    break;
                case "sporulateleft":
                    body.setSporulateLeft(Integer.parseInt(value));
                    break;
                default:
                    System.out.println("Hiba: Nem létezik ilyen tulajdonság: " + property);
                    return;
            }
            // Debug purposes
            // System.out.println("Sikeresen frissítve: " + objectName + " | " + property +
            // ": " + value);
            return;
        }

        // ! FungusSpecies
        else if (obj instanceof FungusSpecies) {
            FungusSpecies species = (FungusSpecies) obj;
            switch (property) {
                case "addbody":
                    if (createdObjects.get(value) instanceof FungusBody)
                        species.addBody((FungusBody) createdObjects.get(value));
                    else
                        System.out.println("Hiba: Nem létezik ilyen nevű objektum: " + value);
                    break;
                case "deletebody":
                    if (createdObjects.get(value) instanceof FungusBody)
                        species.deleteBody((FungusBody) createdObjects.get(value));
                    else
                        System.out.println("Hiba: Nem létezik ilyen nevű objektum: " + value);
                    break;
                case "addthread":
                    if (createdObjects.get(value) instanceof FungusThread)
                        species.addThread((FungusThread) createdObjects.get(value));
                    else
                        System.out.println("Hiba: Nem létezik ilyen nevű objektum: " + value);
                    break;
                case "deleteThread":
                    if (createdObjects.get(value) instanceof FungusThread)
                        species.deleteThread((FungusThread) createdObjects.get(value));
                    else
                        System.out.println("Hiba: Nem létezik ilyen nevű objektum: " + value);
                    break;
                case "addscore":
                    species.addScore(Integer.parseInt(value));
                    break;
                case "descreasescore":
                    species.decreaseScore(Integer.parseInt(value));
                    break;
                default:
                    System.out.println("Hiba: Nem létezik ilyen tulajdonság: " + property);
                    return;
            }
            // Debug purposes
            // System.out.println("Sikeresen frissítve: " + objectName + " | " + property +
            // ": " + value);
            return;
        }

        // ! InsectSpecies
        else if (obj instanceof InsectSpecies) {
            InsectSpecies species = (InsectSpecies) obj;
            switch (property) {
                case "addinsect":
                    if (createdObjects.get(value) instanceof Insect)
                        species.addInsect((Insect) createdObjects.get(value));
                    else
                        System.out.println("Hiba: Nem létezik ilyen nevű objektum: " + value);
                    break;
                case "deleteinsect":
                    if (createdObjects.get(value) instanceof Insect)
                        species.removeInsect((Insect) createdObjects.get(value));
                    else
                        System.out.println("Hiba: Nem létezik ilyen nevű objektum: " + value);
                    break;
                case "addscore":
                    species.addScore(Integer.parseInt(value));
                    break;
                case "decreasescore":
                    species.decreaseScore(Integer.parseInt(value));
                    break;
                default:
                    System.out.println("Hiba: Nem létezik ilyen tulajdonság: " + property);
                    return;
            }
            return;
        }

        // ! Insect
        else if (obj instanceof Insect) {
            Insect insect = (Insect) obj;
            switch (property) {
                case "species":
                    if (createdObjects.get(value) instanceof InsectSpecies)
                        insect.setMyOwner((InsectSpecies) createdObjects.get(value));
                    else
                        System.out.println(
                                "Hiba: Nem megfelelő objektum típus vagy nem létezik ilyen nevű objektum: " + value);
                    break;
                case "thread":
                    if (createdObjects.get(value) instanceof FungusThread)
                        insect.setThread((FungusThread) createdObjects.get(value));
                    else
                        System.out.println(
                                "Hiba: Nem megfelelő objektum típus vagy nem létezik ilyen nevű objektum:" + value);
                    break;
                case "tekton":
                    if (createdObjects.get(value) instanceof Tekton)
                        insect.setRecentTekton((Tekton) createdObjects.get(value));
                    else
                        System.out.println(
                                "Hiba: Nem megfelelő objektum típus vagy nem létezik ilyen nevű objektum:" + value);
                    break;
                case "decrease":
                    insect.setDecrease(Boolean.parseBoolean(value));
                    break;
                case "effect":
                    if (value.equals("stun"))
                        insect.stun();
                    else if (value.equals("slow"))
                        insect.slow();
                    else if (value.equals("fast"))
                        insect.fast();
                    else if (value.equals("disablecut"))
                        insect.disableCut();
                    else
                        System.out.println("Hiba: Nem létezik ilyen hatás: " + value);
                    break;
                case "movingtimer":
                    insect.setMovingEffectTimer(Integer.parseInt(value));
                    break;
                case "abilitytimer":
                    insect.setAbilityEffectTimer(Integer.parseInt(value));
                    break;
                default:
                    System.out.println("Hiba: Nem létezik ilyen tulajdonság: " + property);
                    return;
            }
            // Debug purposes
            // System.out.println("Sikeresen frissítve: " + objectName + " | " + property +
            // ": " + value);
            return;
        }

        // ! Spore
        else if (obj instanceof Spore || obj instanceof FastSpore || obj instanceof MultiplyInsectSpore
                || obj instanceof SlowSpore || obj instanceof StunSpore || obj instanceof DisableCutSpore) {
            Spore spore = (Spore) obj;
            switch (property) {
                case "nutrition":
                    spore.setNutValue(Integer.parseInt(value));
                    break;
                case "tekton":
                    if (createdObjects.get(value) instanceof Tekton)
                        spore.setTekton((Tekton) createdObjects.get(value));
                    else
                        System.out.println("Hiba: Nem létezik ilyen nevű objektum: " + value);
                    break;
                default:
                    System.out.println("Hiba: Nem létezik ilyen tulajdonság: " + property);
                    return;
            }
            // Debug purposes
            // System.out.println("Sikeresen frissítve: " + objectName + " | " + property +
            // ": " + value);
            return;
        }

        // ! Tekton
        else if (obj instanceof Tekton || obj instanceof DecomposingTekton || obj instanceof DecreasingTekton
                || obj instanceof FeedThreadTekton || obj instanceof OneThreadTekton
                || obj instanceof OnlyThreadTekton) {
            Tekton tekton = (Tekton) obj;
            switch (property) {
                case "cangrowthread":
                    tekton.setGrowThread(Boolean.parseBoolean(value));
                    break;
                case "cangrowbody":
                    tekton.setGrowBody(Boolean.parseBoolean(value));
                    break;
                case "body":
                    if (createdObjects.get(value) instanceof FungusBody)
                        tekton.setBody((FungusBody) createdObjects.get(value));
                    else
                        System.out.println("Hiba: Nem létezik ilyen nevű objektum: " + value);
                    break;
                case "addneighbour":
                    if (createdObjects.get(value) instanceof Tekton) {
                        Tekton neighbour = (Tekton) createdObjects.get(value);
                        tekton.addNeighbour(neighbour);
                        neighbour.addNeighbour(tekton);
                    }
                    break;
                case "removeneighbour":
                    if (createdObjects.get(value) instanceof Tekton) {
                        Tekton neighbour = (Tekton) createdObjects.get(value);
                        tekton.removeNeighbour(neighbour);
                        neighbour.removeNeighbour(tekton);
                    }
                    break;
                case "addinsect":
                    if (createdObjects.get(value) instanceof Insect)
                        tekton.addInsect((Insect) createdObjects.get(value));
                    else
                        System.out.println("Hiba: Nem létezik ilyen nevű objektum: " + value);
                    break;
                case "deleteinsect":
                    if (createdObjects.get(value) instanceof Insect)
                        tekton.removeInsect((Insect) createdObjects.get(value));
                    else
                        System.out.println("Hiba: Nem létezik ilyen nevű objektum: " + value);
                    break;
                case "addspore":
                    if (createdObjects.get(value) instanceof Spore)
                        tekton.addSpore((Spore) createdObjects.get(value));
                    else
                        System.out.println("Hiba: Nem létezik ilyen nevű objektum: " + value);
                    break;
                case "deletespore":
                    if (createdObjects.get(value) instanceof Spore)
                        tekton.removeSpore((Spore) createdObjects.get(value));
                    else
                        System.out.println("Hiba: Nem létezik ilyen nevű objektum: " + value);
                    break;
                case "addthread":
                    if (createdObjects.get(value) instanceof FungusThread)
                        tekton.addThread((FungusThread) createdObjects.get(value));
                    else
                        System.out.println("Hiba: Nem létezik ilyen nevű objektum: " + value);
                    break;
                case "deletethread":
                    if (createdObjects.get(value) instanceof FungusThread)
                        tekton.removeThread((FungusThread) createdObjects.get(value));
                    else
                        System.out.println("Hiba: Nem létezik ilyen nevű objektum: " + value);
                    break;
                default:
                    System.out.println("Hiba: Nem létezik ilyen tulajdonság: " + property);
                    return;
            }
            // Debug purposes
            // System.out.println("Sikeresen frissítve: " + objectName + " | " + property +
            // ": " + value);
            return;
        }

        // ! Entomologist
        else if (obj instanceof InsectSpecies) {
            InsectSpecies player = (InsectSpecies) obj;
            switch (property) {
                case "addinsect":
                    if (createdObjects.get(value) instanceof Insect)
                        player.addInsect((Insect) createdObjects.get(value));
                    else
                        System.out.println("Hiba: Nem létezik ilyen nevű objektum: " + value);
                    break;
                case "removeinsect":
                    if (createdObjects.get(value) instanceof Insect)
                        player.removeInsect((Insect) createdObjects.get(value));
                    else
                        System.out.println("Hiba: Nem létezik ilyen nevű objektum: " + value);
                    break;
                default:
                    System.out.println("Hiba: Nem létezik ilyen tulajdonság: " + property);
                    return;
            }
            // Debug purposes
            // System.out.println("Sikeresen frissítve: " + objectName + " | " + property +
            // ": " + value);
            return;
        }

        else {
            System.out.println("Hiba: Nem lehet beállítani ezt az objektumot: " + objectName);
        }
    }

    /*
     * Move parancs formája: Move:<Insect> <Thread>
     * Példa: move i1 th1
     * 
     * @param parts: parancs részei
     */
    public void processMoveCommand(String[] parts) {
        String name = parts[1];
        String thread = parts[2];

        if (!createdObjects.containsKey(name)) {
            System.out.println("Hiba: Nem létezik ilyen nevű objektum: " + name);
            return;
        }

        Object obj = createdObjects.get(name);
        Object threadObject = createdObjects.get(thread);
        if (obj instanceof Insect) {
            Insect insect = (Insect) obj;
            FungusThread th = (FungusThread) threadObject;
            if (th == null) {
                System.out.println("Hiba: Thread null értékű: " + thread);
                return;
            }
            gameLogic.MoveInsect(insect, th);
            // Debug purposes
            // System.out.println("Az Insect mozgott!");
        } else {
            System.out.println("Hiba: Nem lehet mozgatni ezt az objektumot: " + name);
        }
    }

    /*
     * Break parancs formája: break <Tekton>
     * Példa: /break t1
     * 
     * @param parts: parancs részei
     */
    public void processBreakCommand(String[] parts) {
        String name = parts[1];

        if (!createdObjects.containsKey(name)) {
            System.out.println("Hiba: Nem létezik ilyen nevű objektum: " + name);
            return;
        }

        Object obj = createdObjects.get(name);
        if (obj instanceof Tekton || obj instanceof DecomposingTekton || obj instanceof DecreasingTekton
                || obj instanceof FeedThreadTekton || obj instanceof OneThreadTekton
                || obj instanceof OnlyThreadTekton) {
            Tekton tekton = (Tekton) obj;
            String newtektonName = name + "-1";
            String newtektonName2 = name + "-2";
            List<Tekton> tektons = tekton.breakTekton(this);
            createdObjects.put(newtektonName, tektons.get(0));
            createdObjects.put(newtektonName2, tektons.get(1));
            // Debug purposes
            // System.out.println("Az Tekton eltört!");
        } else {
            System.out.println("Hiba: Nem lehet eltörni ezt az objektumot: " + name);
        }
    }

    /*
     * EatInsect parancs formája: eatinsect <Insect> <Thread>
     * Példa: eatinsect i1 th1
     */
    public void processEatInsectCommand(String[] parts) {
        String insectName = parts[1];
        String threadName = parts[2];

        if (!createdObjects.containsKey(insectName)) {
            System.out.println("Hiba: Nem létezik ilyen nevű objektum: " + insectName);
            return;
        }

        if (!createdObjects.containsKey(threadName)) {
            System.out.println("Hiba: Nem létezik ilyen nevű objektum: " + threadName);
            return;
        }

        Object objInsect = createdObjects.get(insectName);
        Object objThread = createdObjects.get(threadName);

        if (objInsect instanceof Insect && objThread instanceof FungusThread) {
            Insect insect = (Insect) objInsect;
            FungusThread thread = (FungusThread) objThread;
            insect.deadInsect(this);
            if (thread.getTekton().canGrowBody() && thread.getTekton() != null) {
                FungusBody b = new FungusBody();
                thread.getSpecies().addBody(b);
                thread.getTekton().setBody(b);
                String baseName = "b";
                int fungusBodyCount = countObjectsOfType(FungusBody.class);
                String bodyname = generateUniqueName(baseName, fungusBodyCount);
                getCreatedObjects().put(bodyname, b);
                thread.getSpecies().addBody(null);
            } else {
                System.out.println("Hiba: Nem lehet ide body-t növeszteni.");
                return;
            }
        } else {
            System.out.println(
                    "Hiba: Valamlyik objektum típusa nem helyes a parancshoz: " + insectName + " " + threadName);
        }
    }

    public void processTriggerCommand(String[] parts) {
        String name = parts[1];

        Object obj = createdObjects.get(name);

        if (name.equals("timeelapsed")) {
            for (Object object : createdObjects.values()) {
                if (object instanceof InsectSpecies) {
                    InsectSpecies insectSpecies = (InsectSpecies) object;
                    insectSpecies.timeElapsed();
                } else if (object instanceof FungusSpecies) {
                    FungusSpecies fungusSpecies = (FungusSpecies) object;
                    fungusSpecies.timeElapsed(this);
                }
            }
        } else if (!createdObjects.containsKey(name)) {
            System.out.println("Hiba: Nem létezik ilyen nevű objektum: " + name);
            return;
        } else if(obj instanceof FungusSpecies) {
            FungusSpecies fungusSpecies = (FungusSpecies) obj;
            fungusSpecies.timeElapsed(this);
        } else if (obj instanceof InsectSpecies) {
            InsectSpecies insectSpecies = (InsectSpecies) obj;
            insectSpecies.timeElapsed();
        } else if (obj instanceof FungusThread) {
            FungusThread thread = (FungusThread) obj;
            thread.decreaseLife();
        } else if (obj instanceof Insect) {
            Insect insect = (Insect) obj;
            insect.timeElapsed();
        }
        else {
            System.out.println("Hiba: Nem megfelelő objektum típus: " + name);
        }
    }
}

