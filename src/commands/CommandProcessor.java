package commands;

import java.util.HashMap;
import java.util.Map;

public class CommandProcessor {
    private Map<String, Object> createdObjects = new HashMap<>();

    public void process(String input) {
        String[] parts = input.split(" ");
        if (parts.length < 2) {
            System.out.println("Hibás parancsformátum! Használat: create <objecttype> <name>");
            return;
        }

        String command = parts[0];

        if ("create".equals(command)) {
            processCreateCommand(parts);
        } else {
            System.out.println("Ismeretlen parancs: " + command);
        }
    }

    private void processCreateCommand(String[] parts) {
        if (parts.length < 3) {
            System.out.println("Hibás create parancs! Használat: create <type> <name>");
            return;
        }

        String objectType = parts[1];
        String name = parts[2];

        if (createdObjects.containsKey(name)) {
            System.out.println("Hiba: Már létezik ilyen nevű objektum: " + name);
            return;
        }

        try {
            Class<?> clazz = Class.forName("game." + objectType);
            Object instance = clazz.getDeclaredConstructor().newInstance();
            createdObjects.put(name, instance);
            System.out.println("Létrehozva: " + objectType + " névvel " + name);
        } catch (ClassNotFoundException e) {
            System.out.println("Hiba: Ismeretlen objektumtípus: " + objectType);
        } catch (Exception e) {
            System.out.println("Hiba az objektum létrehozásakor: " + e.getMessage());
        }
    }

    // Delete megvalósítása és set megvalósítások
    // ! itt még kéne type check mert mindegyik máshogy más fv-el törlődik
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

        createdObjects.remove(name);
        System.out.println("Törölve: " + name);
    }

    // ! Load
    // • Lehet egy saját pálya leíró nyelv fájlba be fájlból ki, pontos formátumát
    // specifikálni kell, de ez nem feltétlenül
    // • Ha a create set stb... parancsok is létrehoznak egy pályát ezeket egy
    // fájlba beleírni és akkor ezekkel a parancsokkal lefuttatni, létrehozni
}
