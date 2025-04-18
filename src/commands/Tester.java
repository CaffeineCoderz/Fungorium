package commands;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Tester {

    private List<String> testCases = List.of("testcase neve", "testcase neve1", "testcase neve2", "testcase neve3");

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
    public void checkTestResults() {
        File expectedDir = new File("data/expected");
        File outputDir = new File("data/output");

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
            File outputFile = new File(outputDir, testName);

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
     * A printTestCases() metódus kiírja a tesztelési esetek nevét a konzolra.
     * A tesztelési esetek nevei a "testCases" listában találhatók.
     */
    public void printTestCases() {
        System.out.println("Tesztelési esetek:");
        for (int i = 0; i < testCases.size(); i++) {
            System.out.println((i + 1) + ". " + testCases.toArray()[i]);
        }
    }

    /*
     * A checkTestResults(int testCase) metódus végrehajtja a megadott tesztet,
     * összehasonlítva az elvárt és a kimeneti fájlokat.
     * A teszteléshez a Windows "fc" parancsát használja, amely a fájlok
     * összehasonlítására szolgál.
     * A tesztelés eredményét a konzolra írja ki.
     */
    public void checkTestResults(int testCase) {
        String testCaseName = testCases.get(testCase - 1);
        File expectedFile = new File("data/expected/Test" + testCase + ".txt");
        File outputFile = new File("data/output/Test" + testCase + ".txt");

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
                System.out.println(
                        "+Sikeres lefutás Test: " + testCaseName + ": A kapott és az elvárt fájl tartalma megegyezik.");
            } else {
                System.out.println("-Sikertelen lefutás! " + testCaseName
                        + ": A kapott és az elvárt fájl tartalma nem egyezik meg.");
            }
        } catch (IOException | InterruptedException e) {
            System.out.println(
                    "-Sikertelen lefutás! " + testCaseName + ": Hiba történt a fájlok összehasonlítása során.");
            e.printStackTrace();
        }
    }
}
