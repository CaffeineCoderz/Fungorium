package pkg.utils;
import java.util.HashMap;
import java.util.Scanner;


public class Logger{


    private static int depth = 0;
    private String name;
    private static HashMap<String, Logger> loggerMap = new HashMap<>();

    /**
     * Privát konstruktor, hogy kívülről ne lehessen példányosítani
     * @param name Az új log neve
     */
    private Logger(String name) {
        this.name = name;
        register();
    }
    /**
     * Regisztráció a HashMap-be
     */
    private void register() {
        loggerMap.put(this.name, this);
    }

    /**
     * A Logger példányosításához kell
     * @param name A log neve
     * @return
     */
    public static Logger getLogger(String name) {
        if (loggerMap.containsKey(name)) {
            return loggerMap.get(name);
        } else {
            return new Logger(name);
        }
    }
    /**
     * Fv.-be valo belepeskor hivando
     * @param s0 A függvény fejlece
     */
    public void stepIn(String s0){
        for (int i = 0; i < depth; ++i){
            System.out.printf("%10s", " ");
        }
        System.out.printf("--> %s%n", s0);
        depth++;
    }

    /**
     * Függvényből való visszatéréskor hivandó
     * @param s0 A függvény [helye].[neve]: [visszatérítésiértéke]
     */
    public void stepOut(String s0){
        depth--;
        for (int i = 0; i < depth; ++i){
            System.out.printf("%10s", " ");
        }
        System.out.printf("<-- %s%n", s0);
    }

    /**
     * Egy függvényben felmerulo plusz kiirasok
     * @param s0 A kiiras
     * @param hasQ Ha a kiiras kerdes
     * @return A felhasznalo valasza
     */
    public String askQ(String s0, boolean hasQ){
        String input = "";
        if (hasQ){
            for (int i = 1; i < depth; ++i){
                System.out.printf("%10s", " ");
            }

            try (Scanner scanner = new Scanner(System.in)) {
                System.out.printf("%4s ", s0);
                input = scanner.nextLine();
            }
        }
        else{
            for (int i = 0; i < depth; ++i){
                System.out.printf("%10s", " ");
            }
            System.out.printf("%4s%n", s0);
        }
        return input;
    }
    /**
     * Getter a névhez
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Getter a loggerMap-hez
     * @return
     */
    public static HashMap<String, Logger> getLoggerMap() {
        return loggerMap;
    }
}