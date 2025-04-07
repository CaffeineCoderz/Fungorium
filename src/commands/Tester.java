package commands;

import java.io.File;
import java.io.IOException;

public class Tester {
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
}
