package utils;
import java.util.HashMap;
import java.util.Scanner;

public class Logger {
    private static int depth = 0; // Tracks the current call depth for indentation
    private String name;   // The name of the Logger instance
    private static HashMap<String, Logger> loggerMap = new HashMap<>(); // Stores all Logger instances
    private static final Scanner scanner = new Scanner(System.in); // Shared Scanner instance

    /**
     * Private constructor to create a Logger instance and register it.
     *
     * @param name The name of the logger instance.
     */
    private Logger(String name) {
        this.name = name;
        register();
    }

    /**
     * Registers the current logger instance in the logger map.
     */
    private void register() {
        loggerMap.put(this.name, this);
    }

    /**
     * Retrieves an existing Logger instance by name or creates a new one if it does
     * not exist.
     *
     * @param name The name of the logger instance.
     * @return A Logger instance corresponding to the given name.
     */
    public static Logger getLogger(String name) {
        if (loggerMap.containsKey(name)) {
            return loggerMap.get(name);
        } else {
            return new Logger(name);
        }
    }

    /**
     * Logs the entry into a function with indentation.
     *
     * @param functionName The name of the function being entered.
     */
    public void stepIn(String functionName) {
        printIndentation();
        System.out.printf("--> %s%n", functionName);
        depth++;
    }

    /**
     * Logs the exit from a function with indentation and optional return value.
     *
     * @param functionName The name of the function being exited.
     * @param pReturn      The return value of the function, if any.
     */
    public void stepOut(String functionName, Object pReturn) {
        depth--;
        printIndentation();
        System.out.printf("<-- %s%n", functionName);
        if (pReturn != null) {
            printIndentation();
            System.out.printf("Returned: %s (%s)%n", pReturn, pReturn.getClass().toString());
        } else {
            printIndentation();
            System.out.printf("No return value %n");
        }
    }

    /**
     * Asks a question to the user and retrieves input if required.
     *
     * @param message The message to display to the user.
     * @param hasQ    If true, prompts the user for input.
     * @return The user input if requested, otherwise an empty string.
     */
    public String askQ(String message, boolean hasQ) {
        String input = "";
        if (hasQ) {
            printIndentation();
            System.out.printf("%4s ", message);
            input = scanner.nextLine();
        } else {
            printIndentation();
            System.out.printf("%4s%n", message);
        }
        return input;
    }

    /**
     * Prints indentation based on the current depth level.
     */
    private void printIndentation() {
        for (int i = 0; i < depth; ++i) {
            System.out.printf("%10s", " "); // 10 spaces per depth level
        }
    }


    /**
     * Gets the name of the Logger instance.
     *
     * @return The name of the logger.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the map of all Logger instances.
     *
     * @return A HashMap containing all registered loggers.
     */
    public static HashMap<String, Logger> getLoggerMap() {
        return loggerMap;
    }
}
