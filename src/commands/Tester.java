package commands;

import java.io.File;
import java.io.IOException;
import java.util.List;

import logic.GameLogic;

public class Tester {

    // private List<String> testCases = List.of("testcase neve", "testcase neve1",
    // "testcase neve2", "testcase neve3");

    public void runTests(GameLogic gameLogic) {
        CommandProcessor cmdproc = gameLogic.getCommandProcessor();
        File testDirectory = new File("data/tests/");

        // Ellenőrizzük, hogy a mappa létezik-e
        if (!testDirectory.exists() || !testDirectory.isDirectory()) {
            System.out.println("Hiba: A tesztfájlokat tartalmazó mappa nem található: data/tests/");
            return;
        }

        // Kilistázzuk az összes fájlt a mappában, amelyek ".txt" kiterjesztésűek
        File[] testFiles = testDirectory.listFiles((dir, name) -> name.endsWith(".txt"));
        if (testFiles == null || testFiles.length == 0) {
            System.out.println("Hiba: Nincsenek tesztfájlok a data/tests/ mappában.");
            return;
        }

        // Minden tesztfájlra meghívjuk a cmdproc.runTest() metódust
        for (File testFile : testFiles) {
            String testName = testFile.getName().replace(".txt", ""); // Kiterjesztés eltávolítása
            //System.out.println("Futtatás alatt: " + testName);
            cmdproc.clearCreatedObjects();
            cmdproc.runTest(testName);
        }
    }


    /*
     * A checkTestResults() metódus végrehajtja a teszteket, összehasonlítva az
     * elvárt és a
     * kimeneti fájlokat.
     * A teszteléshez a Windows "fc" parancsát használja, amely a fájlok
     * összehasonlítására szolgál.
     * A tesztelés eredményét a konzolra írja ki.
     * A teszteléshez szükséges fájlok a "data/expected" és "data/output" mappákban
     * találhatók.
     * Az elvárt fájloknak ".txt" kiterjesztésűeknek kell lenniük, és a kimeneti
     * fájloknak ugyanazzal a névvel kell rendelkezniük.
     */
    public void checkTestResults(GameLogic gameLogic) {
        File outputDir = new File("data/output/");
        File expectedDir = new File("data/expected/");

        // Ellenőrizzük, hogy a kimeneti mappa létezik-e, ha igen, töröljük a fájlokat
        if (outputDir.exists()) {
            for (File file : outputDir.listFiles()) {
                if (file.isFile()) {
                    file.delete();
                }
            }
        } else {
            outputDir.mkdir(); // ha nem létezik, hozzuk létre
        }
        // Futtassuk a teszteket
        runTests(gameLogic);

        // Ellenőrizzük, hogy a kimeneti és elvárt mappák léteznek-e
        // és tartalmaznak-e fájlokat
        if (!expectedDir.exists() || !outputDir.exists()) {
            System.out.println("Hiba: Az elvárt vagy a kimeneti mappa nem található.");
            return;
        }

        File[] expectedFiles = expectedDir.listFiles((dir, name) -> name.endsWith(".txt"));
        if (expectedFiles == null || expectedFiles.length == 0) {
            System.out.println("Hiba: Nincsenek elvárt fájlok a data/expected mappában.");
            return;
        }

        for (File expectedFile : expectedFiles) {
            String testName = expectedFile.getName();
            File outputFile = new File(outputDir, testName.replace(".txt", "_output.txt"));

            if (!outputFile.exists()) {
                System.out.println("-Sikertelen lefutás " + testName + ": A kimeneti fájl nem található.");
                continue;
            }

            try {
                Process process = new ProcessBuilder("cmd.exe", "/c", "fc", expectedFile.getAbsolutePath(),
                        outputFile.getAbsolutePath())
                        .redirectErrorStream(true)
                        .start();

                int exitCode = process.waitFor();
                if (exitCode == 0) {
                    System.out.println(
                            "+Sikeres lefutás " + testName + ": A kapott és az elvárt fájl tartalma megegyezik.");
                } else {
                    System.out.println("-Sikertelen lefutás " + testName
                            + ": A kapott és az elvárt fájl tartalma nem egyezik meg.");
                }
            } catch (IOException | InterruptedException e) {
                System.out
                        .println("-Sikertelen lefutás " + testName + ": Hiba történt a fájlok összehasonlítása során.");
                e.printStackTrace();
            }
        }
    }

    /*
     * Kitörli a kimeneti fájlokat a "data/output" mappából.
     * Ha a mappa nem létezik, akkor kiírja a konzolra, hogy a mappa nem található.
     */
    public void clearOutputFiles() {
        File outputDir = new File("data/output");
        if (outputDir.exists()) {
            for (File file : outputDir.listFiles()) {
                if (file.isFile()) {
                    file.delete();
                }
            }
        } else {
            System.out.println("A kimeneti mappa nem található: " + outputDir.getAbsolutePath());
        }
    }

    /*
     * A printTestCases() metódus kiírja a tesztelési esetek nevét a konzolra.
     * A tesztelési esetek nevei a "testCases" listában találhatók.
     */
    public void printTestCases() {
        System.out.println("Tesztelési esetek:");
        File testDirectory = new File("data/tests/");

        // Ellenőrizzük, hogy a mappa létezik-e
        if (!testDirectory.exists() || !testDirectory.isDirectory()) {
            System.out.println("Hiba: A tesztfájlokat tartalmazó mappa nem található: data/tests/");
            return;
        }

        // Kilistázzuk az összes fájlt a mappában, amelyek ".txt" kiterjesztésűek
        File[] testFiles = testDirectory.listFiles((dir, name) -> name.endsWith(".txt"));
        if (testFiles == null || testFiles.length == 0) {
            System.out.println("Hiba: Nincsenek tesztfájlok a data/tests/ mappában.");
            return;
        }

        // Számozva kiírjuk a fájlneveket kiterjesztés nélkül
        int index = 1;
        for (File testFile : testFiles) {
            String testName = testFile.getName().replace(".txt", ""); // Kiterjesztés eltávolítása
            System.out.println(index + ". " + testName);
            index++;
        }
    }

    /*
     * A checkTestResults(int testCase) metódus végrehajtja a megadott tesztet,
     * összehasonlítva az elvárt és a kimeneti fájlokat.
     * A teszteléshez a Windows "fc" parancsát használja, amely a fájlok
     * összehasonlítására szolgál.
     * A tesztelés eredményét a konzolra írja ki.
     */
    public void checkTestResults(GameLogic gameLogic, int testCase) {
        this.clearOutputFiles(); // Töröljük a kimeneti fájlokat

        File testDirectory = new File("data/tests/");
        File[] testFiles = testDirectory.listFiles((dir, name) -> name.endsWith(".txt"));

        // Ellenőrizzük, hogy a mappa létezik-e és tartalmaz-e tesztfájlokat
        if (testFiles == null || testFiles.length == 0) {
            System.out.println("Hiba: Nincsenek tesztfájlok a data/tests/ mappában.");
            return;
        }

        // Ellenőrizzük, hogy a testCase index érvényes-e
        if (testCase < 1 || testCase > testFiles.length) {
            System.out.println("Hiba: Érvénytelen teszteset index: " + testCase);
            return;
        }

        // Megkeressük a megfelelő tesztfájlt
        File testFile = testFiles[testCase - 1];
        String testName = testFile.getName().replace(".txt", ""); // Kiterjesztés eltávolítása

        // Lefuttatjuk a tesztet
        //System.out.println("Futtatás alatt: " + testName);
        CommandProcessor cmdproc = gameLogic.getCommandProcessor();
        cmdproc.clearCreatedObjects();
        cmdproc.runTest(testName);

        // Ellenőrizzük az elvárt és a kimeneti fájlokat
        File expectedFile = new File("data/expected/" + testFile.getName());
        File outputFile = new File("data/output/" + testName + "_output.txt"); // Kimeneti fájl neve módosítva

        if (!expectedFile.exists()) {
            System.out.println("Hiba: Az elvárt fájl nem található: " + expectedFile.getName());
            return;
        }

        if (!outputFile.exists()) {
            System.out.println("Hiba: A kimeneti fájl nem található: " + outputFile.getName());
            return;
        }

        try {
            Process process = new ProcessBuilder("cmd.exe", "/c", "fc", expectedFile.getAbsolutePath(),
                    outputFile.getAbsolutePath())
                    .redirectErrorStream(true)
                    .start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out
                        .println("+Sikeres lefutás: " + testName + ": A kapott és az elvárt fájl tartalma megegyezik.");
            } else {
                System.out.println("-Sikertelen lefutás: " + testName
                        + ": A kapott és az elvárt fájl tartalma nem egyezik meg.");
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("-Sikertelen lefutás: " + testName + ": Hiba történt a fájlok összehasonlítása során.");
            e.printStackTrace();
        }
    }
}
