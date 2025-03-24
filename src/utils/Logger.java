package utils;

import java.util.HashMap;
import java.util.Scanner;

public class Logger {

    private static int depth = 0; // Tracks the current call depth for indentation
    private String name;   // The name of the Logger instance
    private static HashMap<String, Logger> loggerMap = new HashMap<>(); // Stores all Logger instances
    private static final Scanner scanner = new Scanner(System.in); // Shared Scanner instance

    private Logger(String name) {
        this.name = name;
        register();
    }

    private void register() {
        loggerMap.put(this.name, this);
    }

    public static Logger getLogger(String name) {
        if (loggerMap.containsKey(name)) {
            return loggerMap.get(name);
        } else {
            return new Logger(name);
        }
    }

    public void stepIn(String functionName) {
        printIndentation();
        System.out.printf("--> %s%n", functionName);
        depth++;
    }

    public void stepOut(String functionName, Object pReturn) {
        depth--;
        printIndentation();
        System.out.printf("<-- %s%n", functionName);
        if (pReturn != null) {
            System.out.printf("Returned: %s%n (%s)", pReturn, pReturn.getClass().toString());
        } else {
            System.out.printf("No return value");
        }
    }

    public String askQ(String message, boolean hasQ) {
        String input = "";
        if (hasQ) {
            printIndentation();
            System.out.printf("%4s ", message);
            input = scanner.nextLine(); // Use the shared Scanner instance
        } else {
            printIndentation();
            System.out.printf("%4s%n", message);
        }
        return input;
    }

    private void printIndentation() {
        for (int i = 0; i < depth; ++i) {
            System.out.printf("%10s", " "); // 10 spaces per depth level
        }
    }

    public String getName() {
        return name;
    }

    public static HashMap<String, Logger> getLoggerMap() {
        return loggerMap;
    }
}
