package commands;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
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
//      delete<name>
//      load<filename>
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
// Move: kezelés osztályonként
// grow
// cut
// sporulate <FungusBody>
// eat <Spore>
// kill <Insect>

public class CommandProcessor {
    private Map<String, Object> createdObjects = new HashMap<>();

    /*
     * Parancsok folyamatos kérése
     * 
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
     * A parancsok kiírására szolgáló függvény
     */
    public void help() {
        System.out.println("Parancsok:");
        System.out.println("create <objecttype> <name> \t creates an object with the given name");
        System.out.println("delete <name> \t\t\t deletes the object with the given name");
        System.out.println("load <filename> \t\t loads and processes the commands from the given file");
        System.out.println("exit \t\t\t\t exits the program");
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
        } else if ("load".equals(command)) {
            processConfigText(parts[1]);
        } else if ("create".equals(command)) {
            processCreateCommand(parts);
        } else if ("delete".equals(command)) {
            processDeleteCommand(parts);
        } else {
            System.out.println("Ismeretlen parancs: " + command);
        }
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
