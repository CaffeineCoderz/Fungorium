package commands;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

// Model
import fungus.*;
import insect.InsectSpecies;
import insect.Insect;
import insect.InsectEffects;
import logic.GameLogic;
import sporeTypes.*;
import tektonTypes.*;
import utils.*;

// ! Commands:
// ? System Commands:
//      /helpSys: kiírja a user és sys parancsokat
//      /helpObj: objektum típusok lekérdezése
//      ! helpSet: objektum tulajdonságok lekérdezése
//      /load <filename>
//      /break <Tekton>
//      /kill <Insect>
//      /set: kezelés osztályonként
//      /create <objecttype> <name>
//      /delete <name>
//      /status    az összes állapot lekérdezése
//      /status <name> állapot lekérdezés
//      /save játék állását menti fájlba
//      /log a konzolon lévő kimeneteket menti fáklba
//      /trig események triggerelése nr: Következő kör np: Következő játékos
//      /chance breaktekton && spora milyen fajta
//
// ? User Commands:
//   *All player commands:
//      help: kiírja a user parancsokat
//      exit
//   *Mycologist Commands:
//    ! growBody <FungusThread> <Tekton> sima grow átalakult
//    ! growThread <Tekton> <FungusThread> 
//    ! growThread <Tekton> <FungusBody> 
//      sporulate <FungusBody>
//   *Entomologist Commands:
//      move: <Insect> <Thread>
//      cut <FungusThread>
//    ! eat <Spore> <Insect> 

//* Typne names:
//? Default:
//      body: fungus.Fungusbody
//      spore: sporeTypes.Spore
//      tekton: tektonTypes.Tekton
//      insect: insect.Insect
//      thread: fungus.FungusThread
//      species: fungus.FungusSpecies
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

// TODO: 
// Delete parancsok véglegesítése

// TODO parancsok
// Move: <FungusThread> <Tekton>
// STATUS ALL -> kiírja az összes objektumot egy fájlba
// ALL Objects 

public class CommandProcessor {
    private Map<String, Object> createdObjects = new HashMap<>();
    private Map<String, Consumer<String[]>> commands = new HashMap<>();
    private Map<String, String> commandDescriptions = new HashMap<>();
    private Map<String, String> objectTypeMap = new HashMap<>();

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
     * Parancsok regisztrálása
     */
    public CommandProcessor() {
        initializeObjectTypeMap();

        commands.put("help", parts -> help());
        commandDescriptions.put("help", "help");

        commands.put("/helpsys", parts -> helpSys());
        commandDescriptions.put("/helpsys", "/helpsys");

        commands.put("/helpObj", parts -> helpObj());
        commandDescriptions.put("/helpObj", "/helpObj");

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

        commands.put("grow", this::processGrowCommand);
        commandDescriptions.put("grow", "grow <FungusThread> <Tekton>");
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

    // ! Entomologist és Mycologist parancsok
    private void initializeObjectTypeMap() {
        objectTypeMap.put("species", "FungusSpecies");
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
        objectTypeMap.put("mycologist", "Mycologist");
        objectTypeMap.put("entomologist", "Entomologist");
    }

    /*
     * A user parancsok kiírására szolgáló függvény
     */
    public void help() {
        System.out.println("/-----------------------------------------------------------\\");
        System.out.println("User Commands:");
        System.out.println(" ");
        System.out.println("help \t\t\t\t\t prints the commands");
        System.out.println("cut <FungusThread> <Insect> \t\t cuts a thread with the selected insect");
        System.out.println("eat <Spore> <Insect> \t\t\t eats a spore with the selected insect");
        System.out.println("move <Insect> <Thread> \t\t\t moves an insect to the selected thread");
        System.out.println("growBody <FungusThread> <Tekton> \t grows a body");
        System.out.println("growThread <?> \t\t\t\t grows a thread");
        System.out.println("sporulate <FungusBody> \t\t\t sporulates with the selected body");
        System.out.println("exit \t\t\t\t\t exits the program");
        System.out.println("\\-----------------------------------------------------------/\n");
    }

    /*
     * A Sys és user parancsok kiírására szolgáló függvény
     */
    public void helpSys() {
        System.out.println("/-----------------------------------------------------------\\");
        System.out.println("System Commands:");
        System.out.println(" ");
        System.out.println("/helpsys \t\t\t\t prints all the system the commands");
        System.out.println("/helpObj \t\t\t\t prints all avaliable objectTypes");
        System.out.println("/status <name> \t\t\t\t prints an object's status");
        System.out.println("/create <objectType> <name> \t\t creates an object with the given name");
        System.out.println("/delete <name> \t\t\t\t deletes the object with the given name");
        System.out.println("/load <filename> \t\t\t loads the commands from the given file");
        System.out.println("/break <Tekton> \t\t\t breaks a tekton");
        System.out.println("/kill <Insect> \t\t\t\t kills an insect");
        System.out.println("/set <object> <property> <value> \t sets the property of the object to the given value");
        System.out.println("/log <filename> \t\t\t saves the console output to a file");
        System.out.println("/trig <event> \t\t\t\t triggers an event (next round, next player)");
        System.out.println(" ");
        System.out.println("User Commands:");
        System.out.println(" ");
        System.out.println("help \t\t\t\t\t prints the commands");
        System.out.println("cut <FungusThread> <Insect> \t\t cuts a thread with the selected insect");
        System.out.println("eat <Spore> <Insect> \t\t\t eats a spore with the selected insect");
        System.out.println("move <Insect> <Thread> \t\t\t moves an insect to the selected thread");
        System.out.println("growBody <FungusThread> <Tekton> \t grows a body");
        System.out.println("growThread <?> \t\t\t\t grows a thread");
        System.out.println("sporulate <FungusBody> \t\t\t sporulates with the selected body");
        System.out.println("exit \t\t\t\t\t exits the program");
        System.out.println("\\-----------------------------------------------------------/\n");
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

    /*
     * Create parancs formája: create <objecttype> <name>
     * Példa: create Insect i1
     * 
     * @param parts: parancs részei
     */

    private void processCreateCommand(String[] parts) {
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
                // Debug:
                // System.out.println("+ Created: " + className + " | Name: " + name);
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
     * delete parancs formája: delete <name>
     * Példa: delete i1
     * 
     * @param parts: parancs részei
     */
    private void processDeleteCommand(String[] parts) {
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
            // Debug purposes
            // System.out.println("- Deleted Tekton | Name: " + name);
        } else if (obj instanceof Insect) {
            Insect insect = (Insect) obj;
            if (insect.getMyOwner() == null || insect.getRecent() == null) {
                System.out.println(
                        "Hiba: Nem lehet törölni az Insect-et, mert nincs hozzárendelve Entomologist vagy Tekton!");
                return;
            }
            insect.deadInsect();
            // Debug purposes
            // System.out.println("- Deleted Insect | Name: " + name);
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
            // Debug purposes
            // System.out.println("- Deleted FungusThread | Name: " + name);
        } else if (obj instanceof FungusBody) {
            FungusBody body = (FungusBody) obj;
            if (body.getSpecies() == null) {
                System.out.println("Hiba: Nem lehet törölni a FungusBody-t, mert nincs hozzárendelve FungusSpecies!");
                return;
            }
            body.getSpecies().deleteBody(body);
            // Debug purposes
            // System.out.println("- Deleted FungusBody | Name: " + name);
        } else if (obj instanceof FungusSpecies) {
            FungusSpecies species = (FungusSpecies) obj;
            // Debug purposes
            // System.out.println("- Deleted FungusSpecies | Name: " + name);
        } else if (obj instanceof InsectSpecies) {
            InsectSpecies player = (InsectSpecies) obj;
            for (Insect insect : player.getInsects()) {
                insect.deadInsect();
                player.removeInsect(insect);
            }
            // Debug purposes
            // System.out.println("- Deleted Entomologist | Name: " + name);
        } else if (obj instanceof FungusSpecies) {
            FungusSpecies player = (FungusSpecies) obj;
            for (FungusBody body : player.getBodies()) {
                body.getSpecies().deleteBody(body);
            }

            for (FungusThread thread : player.getThreads()) {
                thread.destroy();
            }
            // Debug purposes
            // System.out.println("- Deleted Mycologist | Name: " + name);
        } else {
            System.out.println("Hiba: Ismeretlen típusú objektum: " + name);
        }

        createdObjects.remove(name);
    }

    /*
     * Create parancs formája: status <name>
     * Példa: status i1
     * 
     * @param parts: parancs részei
     */
    public void processStatusCommand(String[] parts) {
        if (parts.length == 1) {
            // Ha nincs paraméter, írja ki az összes objektum státuszát
            createdObjects.forEach((name, obj) -> {
                System.out.println("Név: " + name);
                if (obj instanceof FungusThread) {
                    FungusThread thread = (FungusThread) obj;
                    String bodyName = createdObjects.entrySet().stream()
                            .filter(entry -> entry.getValue() == thread.getBody())
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
                    System.out.println("FungusThread: "
                            + "\n\tSpecies: " + speciesName
                            + "\n\tBody: " + bodyName
                            + "\n\tTektons: " + tektonNames
                            + "\n\tIsBridge: " + thread.isBridge()
                            + "\n\tLifespan: " + thread.getLifeSpan()
                            + "\n\tIsDying: " + thread.getIsDying()
                            + "\n\tNext: " + thread.getNext()
                            + "\n\tPrev: " + thread.getPrev());
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
                            + "\n\tSporecount: " + body.getSporeCount());
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
                } else if (obj instanceof Insect) {
                    Insect insect = (Insect) obj;
                    String tektonName = createdObjects.entrySet().stream()
                            .filter(entry -> entry.getValue() == insect.getRecent())
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
                            + "\n\tTekton: " + tektonName
                            + "\n\tEffect: " + insect.gEffect());
                } else if (obj instanceof Spore) {
                    Spore spore = (Spore) obj;
                    String sporeType = "Spore";

                    // Típus ellenőrzés explicit módon
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
                    String tektonType = "Tekton"; // Alapértelmezett típus

                    // Típus ellenőrzés explicit módon
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
                            + "\n\tNeighbours: " + tekton.getNeighbours()
                            + "\n\tSpores: " + sporeNames
                            + "\n\tThreads: " + threadNames);
                } else if (obj instanceof InsectSpecies) {
                    InsectSpecies entomologist = (InsectSpecies) obj;
                    String insectNames = entomologist.getInsects().stream()
                            .map(insect -> createdObjects.entrySet().stream()
                                    .filter(entry -> entry.getValue() == insect)
                                    .map(Map.Entry::getKey)
                                    .findFirst()
                                    .orElse("N/A"))
                            .toList()
                            .toString();
                    System.out.println("Entomologist: "
                            + "\n\tInsects: " + insectNames);
                } else {
                    System.out.println("Ismeretlen objektumtípus: " + obj.getClass().getName());
                }
                System.out.println();
            });
        } else {
            // Ha van paraméter, az adott objektum státuszát írja ki
            String name = parts[1];

            if (!createdObjects.containsKey(name)) {
                System.out.println("Hiba: Nem létezik ilyen nevű objektum: " + name);
                return;
            }

            Object obj = createdObjects.get(name);
            // Az egyedi objektumok státuszának kiírása ugyanúgy történik, mint fent.
        }
    }

    /*
     * Grow parancs formája: grow <FungusThread>
     * Példa: grow th1 t1
     * 
     * @param parts: parancs részei
     */
    public void processGrowCommand(String[] parts) {
        String threadName = parts[1];
        String tekton = parts[2];

        if (!createdObjects.containsKey(threadName)) {
            System.out.println("Hiba: Nem létezik ilyen nevű objektum: " + threadName);
            return;
        }

        if (!createdObjects.containsKey(tekton)) {
            System.out.println("Hiba: Nem létezik ilyen nevű objektum: " + tekton);
            return;
        }

        Object obj = createdObjects.get(threadName);
        if (obj instanceof FungusThread) {
            FungusThread thread = (FungusThread) obj;
            Tekton tektonObj = null;
            if (createdObjects.get(tekton) instanceof Tekton) {
                tektonObj = (Tekton) createdObjects.get(tekton);
            } else if (createdObjects.get(tekton) instanceof DecomposingTekton) {
                tektonObj = (DecomposingTekton) createdObjects.get(tekton);
            } else if (createdObjects.get(tekton) instanceof DecreasingTekton) {
                tektonObj = (DecreasingTekton) createdObjects.get(tekton);
            } else if (createdObjects.get(tekton) instanceof FeedThreadTekton) {
                tektonObj = (FeedThreadTekton) createdObjects.get(tekton);
            } else if (createdObjects.get(tekton) instanceof OneThreadTekton) {
                tektonObj = (OneThreadTekton) createdObjects.get(tekton);
            } else if (createdObjects.get(tekton) instanceof OnlyThreadTekton) {
                tektonObj = (OnlyThreadTekton) createdObjects.get(tekton);
            } else {
                System.out.println("Hiba: Nem létezik ilyen nevű objektum: " + tekton);
                return;
            }
            if (tektonObj.canGrowBody()) {
                thread.getSpecies().growBody(thread);
            } else {
                System.out.println("Hiba: Nem lehet ide body-t növeszteni: " + tekton);
                return;
            }

            // Debug purposes
            // System.out.println("Új body nőtt!");
        } else {
            System.out.println("Hiba: Nem lehet növeszteni ezt az objektumot: " + threadName);
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
            insect.consumeSpore(spore);
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
            body.sporulate();
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
     * Példa: kill i1
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
            insect.deadInsect();
            // Debug purposes
            // System.out.println("Az Insect meghalt!");
        } else {
            System.out.println("Hiba: Nem lehet megölni ezt az objektumot: " + name);
        }
    }

    /*
     * Load parancs formája: load <fileName>
     * Példa: load config
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
     * Set parancs formája: set <object> <property> <value>
     * Példa: set th1 lifespan 10
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
                case "body":
                    thread.setBody((FungusBody) createdObjects.get(value));
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

        // ! Insect
        // !!! Timerek settere - movingtimer abilitytimer
        else if (obj instanceof Insect) {
            Insect insect = (Insect) obj;
            switch (property) {
                case "species":
                    // ! insect.setMyOwner((InsectSpecies) createdObjects.get(value));
                    break;
                case "thread":
                    insect.setThread((FungusThread) createdObjects.get(value));
                    break;
                case "tekton":
                    if (createdObjects.get(value) instanceof Tekton)
                        insect.setRecentTekton((Tekton) createdObjects.get(value));
                    else
                        System.out.println("Hiba: Nem létezik ilyen nevű objektum: " + value);
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
                    if (createdObjects.get(value) instanceof Tekton)
                        tekton.addNeighbour((Tekton) createdObjects.get(value));
                    break;
                case "removeneighbour":
                    if (createdObjects.get(value) instanceof Tekton)
                        tekton.removeNeighbour((Tekton) createdObjects.get(value));
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
            insect.move(th);
            // Debug purposes
            // System.out.println("Az Insect mozgott!");
        } else {
            System.out.println("Hiba: Nem lehet mozgatni ezt az objektumot: " + name);
        }
    }

    /*
     * Break parancs formája: break <Tekton>
     * Példa: break t1
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
            tekton.breakTekton();
            // Debug purposes
            // System.out.println("Az Tekton eltört!");
        } else {
            System.out.println("Hiba: Nem lehet eltörni ezt az objektumot: " + name);
        }
    }

    /*
     * Parancsok kiírása egy fájlba
     */
    public void writeCommandsToFile(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String command : commands.keySet()) {
                writer.write(command);
                writer.newLine();
            }
            // Debug purposes
            // System.out.println("A parancsok sikeresen kiírva a fájlba: " + fileName);
        } catch (IOException e) {
            System.out.println("Hiba a fájl írásakor: " + e.getMessage());
        }
    }

    /*
     * redirectOutputToFile metódus: átirányítja a System.out kimenetet egy fájlba.
     * 
     * PrintStream originalOut = System.out; // Mentsd el az eredeti kimenetet
     * redirectOutputToFile("output.log");
     * // ... program futása ...
     * System.setOut(originalOut); // Állítsd vissza az eredeti kimenetet
     */
    public void redirectOutputToFile(String fileName) {
        try {
            File file = new File(fileName);
            FileOutputStream fos = new FileOutputStream(file);
            PrintStream ps = new PrintStream(fos);
            System.setOut(ps); // A System.out kimenet átirányítása a fájlba
            // Debug purposes
            // System.out.println("A konzol kimenet mostantól ide íródik: " + fileName);
        } catch (IOException e) {
            System.err.println("Hiba a kimenet fájlba irányításakor: " + e.getMessage());
        }
    }
}