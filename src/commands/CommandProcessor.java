package commands;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.io.FileReader;
import java.io.IOException;

// Model
import fungus.*;
import insect.Insect;
import insect.InsectEffects;
import java.util.Scanner;
import logic.GameLogic;
import sporeTypes.*;
import tektonTypes.*;
import utils.*;

// ! Commands:
//      create <objecttype> <name>
//      delete <name>
//      load <filename>
//      grow <FungusThread name>
//      exit
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
// Delete parancsok implementálása az osztályokban!!

// TODO parancsok
// Set: kezelés osztályonként
// Move: <FungusThread> <Tekton(hová)>?????
// Move: <Insect> <Tekton/Thread(hová)>?????
// kill <Insect>
// ? Needs Review
// helpObj: objektum típusok lekérdezése
// load <filename>: fájl beolvasása
// Status: állapot lekérdezés
// cut <FungusThread>
// sporulate <FungusBody>
// eat <Spore> <Insect>
// !IN PROGRESS
// grow <FungusThread name> 

public class CommandProcessor {
    private Map<String, Object> createdObjects = new HashMap<>();

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
     * 
     * @param input: a beolvasott parancs és részei
     */
    public void process(String input) {
        String[] parts = input.split(" ");

        String command = parts[0];
        if ("help".equals(command)) {
            help();
        } else if ("helpObj".equals(command)) {
            helpObj();
        } else if ("load".equals(command)) {
            processConfigText(parts[1]);
        } else if ("create".equals(command)) {
            processCreateCommand(parts);
        } else if ("delete".equals(command)) {
            processDeleteCommand(parts);
        } else if ("set".equals(command)) {
            // processSetCommand(parts);
        } else if ("status".equals(command)) {
            processStatusCommand(parts);
        } else if ("cut".equals(command)) {
            // processCutCommand(parts);
        } else if ("sporulate".equals(command)) {
            processSporulateCommand(parts);
        } else if ("eat".equals(command)) {
            processEatCommand(parts);
        } else if ("kill".equals(command)) {
            processKillCommand(parts);
        } else if ("move".equals(command)) {
            // processMoveCommand(parts);
        } else if ("grow".equals(command)) {
            processGrowCommand(parts);
        } else {
            System.out.println("Ismeretlen parancs: " + command);
        }
    }

    /*
     * A parancsok kiírására szolgáló függvény
     */
    public void help() {
        System.out.println("/-----------------------------------------------------------\\");
        System.out.println("Commands:");
        System.out.println("help \t\t\t\t prints the commands");
        System.out.println("helpObj \t\t\t prints all avaliable objectTypes");
        System.out.println("status <name> \t\t\t prints an object's status");
        System.out.println("create <objectType> <name> \t creates an object with the given name");
        System.out.println("delete <name> \t\t\t deletes the object with the given name");
        System.out.println("load <filename> \t\t loads the commands from the given file");
        System.out.println("cut <FungusThread> <Insect> \t cuts a thread with the selected insect");
        System.out.println("eat <Spore> <Insect> \t\t eats a spore with the selected insect");
        System.out.println("grow <FungusThread> \t\t grows a thread");
        System.out.println("sporulate <FungusBody> \t\t sporulates with the selected body");
        System.out.println("exit \t\t\t\t exits the program");
        System.out.println("\\-----------------------------------------------------------/\n");
    }

    /*
     * Az objektum típusok kiírására szolgáló függvény
     */
    public void helpObj() {
        System.out.println("/-----------------------------------------------------------\\");
        System.out.println("All avaliable objectTypes:");
        System.out.println("species \t\t\t creates a FungusSpecies");
        System.out.println("thread \t\t\t\t creates a FungusThread");
        System.out.println("body \t\t\t\t creates a FungusBody");
        System.out.println("insect \t\t\t\t creates an Insect");
        System.out.println("spore \t\t\t\t creates a Spore");
        System.out.println("disablecutspore \t\t creates a DisableCutSpore");
        System.out.println("fastspore \t\t\t creates a FastSpore");
        System.out.println("multiplyinsectspore \t\t creates a MultiplyInsectSpore");
        System.out.println("slowspore \t\t\t creates a SlowSpore");
        System.out.println("stunspore \t\t\t creates a StunSpore");
        System.out.println("tekton \t\t\t\t creates a Tekton");
        System.out.println("decomptekton \t\t\t creates a DecomposingTekton");
        System.out.println("decreasetekton \t\t\t creates a DecreasingTekton");
        System.out.println("feedthreadtekton \t\t creates a FeedThreadTekton");
        System.out.println("onethreadtekton \t\t creates a OneThreadTekton");
        System.out.println("onlythreadtekton \t\t creates a OnlyThreadTekton");
        System.out.println("\\-----------------------------------------------------------/\n");
    }

    /*
     * Create parancs formája: create <objecttype> <name>
     * Példa: create Insect i1
     * 
     * @param parts: parancs részei
     */
    private void processCreateCommand(String[] parts) {
        if (parts.length < 3) {
            System.out.println("Hibás create parancs! Használat: create <type> <name>");
            return;
        }

        String objectType = parts[1];
        String name = parts[2];

        switch (objectType.toLowerCase()) {
            case "species":
                objectType = "FungusSpecies";
                break;
            case "body":
                objectType = "FungusBody";
                break;
            case "spore":
                objectType = "Spore";
                break;
            case "tekton":
                objectType = "Tekton";
                break;
            case "insect":
                objectType = "Insect";
                break;
            case "thread":
                objectType = "FungusThread";
                break;
            case "disablecutspore":
                objectType = "DisableCutSpore";
                break;
            case "fastspore":
                objectType = "FastSpore";
                break;
            case "multiplyinsectspore":
                objectType = "MultiplyInsectSpore";
                break;
            case "slowspore":
                objectType = "SlowSpore";
                break;
            case "stunspore":
                objectType = "StunSpore";
                break;
            case "decomptekton":
                objectType = "DecomposingTekton";
                break;
            case "decreasetekton":
                objectType = "DecreasingTekton";
                break;
            case "feedthreadtekton":
                objectType = "FeedThreadTekton";
                break;
            case "onethreadtekton":
                objectType = "OneThreadTekton";
                break;
            case "onlythreadtekton":
                objectType = "OnlyThreadTekton";
                break;
            default:
                System.out.println("Hiba: Ismeretlen objektumtípus: " + objectType);
                return;
        }

        if (createdObjects.containsKey(name)) {
            System.out.println("Hiba: Már létezik ilyen nevű objektum: " + name);
            return;
        }

        String[] packages = {
                "tektonTypes",
                "fungus",
                "sporeTypes",
                "insect"
        };

        boolean created = false;

        for (String pkg : packages) {
            try {
                Class<?> clazz = Class.forName(pkg + "." + objectType);
                Object instance = clazz.getDeclaredConstructor().newInstance();
                createdObjects.put(name, instance);
                System.out.println("+ Created: " + objectType + " | Name: " + name);
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
            System.out.println("Hiba: Ismeretlen objektumtípus: " + objectType);
        }
    }

    /*
     * Create parancs formája: delete <name>
     * Példa: delete i1
     * 
     * @param parts: parancs részei
     */
    private void processDeleteCommand(String[] parts) {
        if (parts.length < 2) {
            System.out.println("Hibás delete parancs! Használat: delete <name>");
            return;
        }

        String name = parts[1];

        if (!createdObjects.containsKey(name)) {
            System.out.println("Hiba: Nem létezik ilyen nevű objektum: " + name);
            return;
        }

        Object obj = createdObjects.get(name);
        // * Debug: System.out.println("Objektum típusa: " + obj);

        // ! Objektum típus ellenőrzés, és megfelelő függvény meghívása
        // TODO: Delete függvények implementálása az osztályokban
        if (obj instanceof Tekton) {
            Tekton tekton = (Tekton) obj;
            tekton.deleteTekton();
            System.out.println("- Deleted Tekton | Name: " + name);
        } else if (obj instanceof Insect) {
            Insect insect = (Insect) obj;
            System.out.println("- Deleted Insect | Name: " + name);
        } else if (obj instanceof Spore) {
            Spore spore = (Spore) obj;
            System.out.println("- Deleted Spore | Name: " + name);
        } else if (obj instanceof FungusThread) {
            FungusThread thread = (FungusThread) obj;
            System.out.println("- Deleted FungusThread | Name: " + name);
        } else if (obj instanceof FungusBody) {
            FungusBody body = (FungusBody) obj;
            System.out.println("- Deleted FungusBody | Name: " + name);
        } else if (obj instanceof FungusSpecies) {
            FungusSpecies species = (FungusSpecies) obj;
            System.out.println("- Deleted FungusSpecies | Name: " + name);
        } else {
            System.out.println("Hiba: Ismeretlen típusú objektum: " + name);
        }

        // Töröljük az objektumot a HashMap-ből
        createdObjects.remove(name);
    }

    /*
     * Create parancs formája: status <name>
     * Példa: status i1
     * 
     * @param parts: parancs részei
     */
    public void processStatusCommand(String[] parts) {
        if (parts.length < 2) {
            System.out.println("Hibás status parancs! Használat: status <name>");
            return;
        }

        String name = parts[1];

        if (!createdObjects.containsKey(name)) {
            System.out.println("Hiba: Nem létezik ilyen nevű objektum: " + name);
            return;
        }

        Object obj = createdObjects.get(name);
        if (obj instanceof FungusThread) {
            FungusThread thread = (FungusThread) obj;
            System.out.println("FungusThread: " + "\n\tSpecies " + thread.getSpecies() + "\n\tBody: " + thread.getBody()
                    + "\n\tTekton: " + thread.getTekton() + "\n\tIsBridge " + thread.isBridge() + "\n\t Lifespan: "
                    + thread.getLifeSpan() + "\n\tIsDying: " + thread.getIsDying() + "\n\tNext: " + thread.getNext()
                    + "\n\tPrev: " + thread.getPrev());
        } else if (obj instanceof FungusBody) {
            FungusBody body = (FungusBody) obj;
            System.out.println("FungusBody: " + "\n\tSpecies: " + body.getSpecies() + "\n\t Tekton: " + body.getTekton()
                    + "\n\t Threads: " + body.getThreads() + "\n\t Sporecount: " + body.getSporeCount());
        } else if (obj instanceof FungusSpecies) {
            FungusSpecies species = (FungusSpecies) obj;
            System.out.println(
                    "FungusSpecies: " + "\n\tScore: " + species.getScore() + "\n\tBodies: " + species.getBodies());
        } else if (obj instanceof Insect) {
            Insect insect = (Insect) obj;
            System.out.println("Insect: " + "\n\tSpecies: " + insect.getMySpecies() + "\n\tTekton: "
                    + insect.getRecent() + "\n\tEffect: " + insect.gEffect());
        } else if (obj instanceof Spore || obj instanceof FastSpore || obj instanceof MultiplyInsectSpore
                || obj instanceof SlowSpore || obj instanceof StunSpore || obj instanceof DisableCutSpore) {
            Spore spore = (Spore) obj;
            System.out.println(
                    "Spore: " + "\n\tNutrition value: " + spore.getNutValue() + "\n\tTekton: " + spore.getTekton());
        } else if (obj instanceof Tekton || obj instanceof DecomposingTekton || obj instanceof DecreasingTekton
                || obj instanceof FeedThreadTekton || obj instanceof OneThreadTekton
                || obj instanceof OnlyThreadTekton) {
            Tekton tekton = (Tekton) obj;
            System.out.println("Tekton: " + "\n\tCanGrowThread: " + tekton.canGrowThread() + "\n\tCanGrowBody: "
                    + tekton.canGrowBody() + "\n\tGetBody: " + tekton.getBody() + "\n\tInsects: " + tekton.getInsects()
                    + "\n\tNeighbours: " + tekton.getNeighbours() + "\n\tSpores: " + tekton.getSpores()
                    + "\n\tThreads: " + tekton.getThreads());
        }
    }

    /*
     * Grow parancs formája: grow <FungusThread>
     * Példa: grow th1
     * 
     * @param parts: parancs részei
     */
    public void processGrowCommand(String[] parts) {
        if (parts.length < 2) {
            System.out.println("Hibás grow parancs! Használat: grow <Tekton>");
            return;
        }

        String name = parts[1];

        if (!createdObjects.containsKey(name)) {
            System.out.println("Hiba: Nem létezik ilyen nevű objektum: " + name);
            return;
        }

        Object obj = createdObjects.get(name);
        if (obj instanceof FungusThread) {
            FungusThread thread = (FungusThread) obj;
            // ! még nincs kipróbálva de sztem így működik
            thread.getSpecies().growBody(thread);
            System.out.println("A tekton nőtt!");
        } else {
            System.out.println("Hiba: Nem lehet növeszteni ezt az objektumot: " + name);
        }
    }

    /*
     * Eat parancs formája: eat <Spore> <Insect>
     * Példa: eat s1 i1
     * 
     * @param parts: parancs részei
     */
    public void processEatCommand(String[] parts) {
        if (parts.length < 2) {
            System.out.println("Hibás eat parancs! Használat: eat <Spore> <Insect>");
            return;
        }

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
            System.out.println("Az Insect megette a Spore-t!");
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
        if (parts.length < 2) {
            System.out.println("Hibás sporulate parancs! Használat: sporulate <FungusBody>");
            return;
        }

        String name = parts[1];

        if (!createdObjects.containsKey(name)) {
            System.out.println("Hiba: Nem létezik ilyen nevű objektum: " + name);
            return;
        }

        Object obj = createdObjects.get(name);
        if (obj instanceof FungusBody) {
            FungusBody body = (FungusBody) obj;
            body.sporulate();
            System.out.println("A FungusBody sporulált!");
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
        if (parts.length < 2) {
            System.out.println("Hibás cut parancs! Használat: cut  <FungusThread> <Insect>");
            return;
        }

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
            System.out.println("A FungusThread el lett vágva!");
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
        if (parts.length < 2) {
            System.out.println("Hibás kill parancs! Használat: kill <Insect>");
            return;
        }

        String name = parts[1];

        if (!createdObjects.containsKey(name)) {
            System.out.println("Hiba: Nem létezik ilyen nevű objektum: " + name);
            return;
        }

        Object obj = createdObjects.get(name);
        if (obj instanceof Insect) {
            Insect insect = (Insect) obj;
            insect.deadInsect();
            System.out.println("Az Insect meghalt!");
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
}
