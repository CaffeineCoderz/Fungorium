package commands;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Tester {
    private final HashMap<String, File> expectedFilesMap;
    private final HashMap<String, File> outputFilesMap;
    private final HashMap<String, File> testFilesMap;
    private int passed = 0;
    private int failed = 0;
    private int skipped = 0;


    public Tester() {
        expectedFilesMap = new HashMap<>();
        outputFilesMap = new HashMap<>();
        testFilesMap = new HashMap<>();
        passed = 0;failed = 0; skipped = 0;
        loadFilesFromDirectory(new File("data/expected"), expectedFilesMap);
        loadFilesFromDirectory(new File("data/output"), outputFilesMap);
        loadFilesFromDirectory(new File("data/tests"), testFilesMap);
    }

    private void loadFilesFromDirectory(File directory, HashMap<String, File> targetMap) {
        if (!directory.exists()) {
            System.out.println("Figyelmeztetés: A " + directory.getName() + " mappa nem található.");
            return;
        }

        File[] files = directory.listFiles((dir, name) -> name.endsWith(".txt"));
        if (files != null) {
            for (File file : files) {
                targetMap.put(file.getName(), file);
            }
        }
    }

    public void checkAllTestResults() {
        if (testFilesMap.isEmpty()) {
            System.out.println("Nincsenek tesztfájlok a data/tests mappában.");
            return;
        }

        System.out.println("\n=== TESZT EREDMÉNYEK ===");
        System.out.println("Összes teszt: " + testFilesMap.size());
        

        for (String testFileName : testFilesMap.keySet()) {
            checkXTestResults(testFileName);
        }

        // Összesített eredmények
        System.out.println("\n=== ÖSSZESÍTETT EREDMÉNY ===");
        System.out.println("Sikeres: " + passed);
        System.out.println("Sikertelen: " + failed);
        System.out.println("Kihagyott: " + skipped);
        System.out.println("Sikerarány: " + (passed * 100 / testFilesMap.size()) + "%");
        passed = 0;failed = 0;skipped = 0;
    }

    private boolean compareFiles(File expectedFile, File outputFile) {
        try {
            Process process = new ProcessBuilder("cmd.exe", "/c", "fc", 
                    expectedFile.getAbsolutePath(), outputFile.getAbsolutePath())
                    .redirectErrorStream(true)
                    .start();

            int exitCode = process.waitFor();
            return exitCode == 0;
            
        } catch (IOException | InterruptedException e) {
            System.out.println("Hiba történt a fájlok összehasonlítása közben:");
            e.printStackTrace();
            return false;
        }
    }

    // Segédmetódus a hiányzó fájlok listázásához
    public void listMissingFiles() {
        System.out.println("\n=== HIÁNYZÓ FÁJLOK ===");
        
        for (String testFile : testFilesMap.keySet()) {
            if (!expectedFilesMap.containsKey(testFile)) {
                System.out.println("Hiányzó elvárt fájl: " + testFile);
            }
            if (!outputFilesMap.containsKey(testFile)) {
                System.out.println("Hiányzó kimeneti fájl: " + testFile);
            }
        }
    }
    public void checkXTestResults(String testFileName){
        if (testFilesMap.isEmpty()) {
            System.out.println("Nincsenek tesztfájlok a data/tests mappában.");
            return;
        }
        System.out.println("\nTeszt: " + testFileName);
            
            File expectedFile = expectedFilesMap.get(testFileName);
            File outputFile = outputFilesMap.get(testFileName);

            // Hiányzó fájlok ellenőrzése
            if (expectedFile == null) {
                System.out.println("[SKIPPED] Nincs elvárt eredmény fájl ehhez a teszthez.");
                skipped++;
                return;
            }

            if (outputFile == null) {
                System.out.println("[FAILED] Nincs kimeneti fájl ehhez a teszthez.");
                failed++;
                return;
            }

            // Fájlok összehasonlítása
            boolean result = compareFiles(expectedFile, outputFile);
            
            if (result) {
                System.out.println("[PASSED] A teszt sikeresen lefutott.");
                passed++;
            } else {
                System.out.println("[FAILED] A teszt eredménye nem egyezik az elvártal.");
                failed++;
            }
    }
}