package pkg.utils;
import java.util.HashMap;
package pkg.utils;
import java.util.HashMap;
import java.util.Scanner;

/**
 * The Logger class provides a simple logging mechanism to track method calls and
 * other messages within an application. It supports indentation based on the call
 * depth, making it easy to visualize the flow of execution.
 * <p>
 * Loggers are stored in a static HashMap, ensuring that only one Logger instance
 * exists per name. This allows consistent logging across different classes.
 * </p>
 */
public class Logger {

    private int depth = 0; // Tracks the current call depth for indentation
    private String name;   // The name of the Logger instance
    private static HashMap<String, Logger> loggerMap = new HashMap<>(); // Stores all Logger instances

    /**
     * Private constructor to prevent direct instantiation. Use {@link #getLogger(String)}
     * to create or retrieve Logger instances.
     *
     * @param name The name of the Logger instance.
     */
    private Logger(String name) {
        this.name = name;
        register();
    }

    /**
     * Registers the Logger instance in the static HashMap.
     */
    private void register() {
        loggerMap.put(this.name, this);
    }

    /**
     * Retrieves a Logger instance with the specified name. If a Logger with the given
     * name already exists, it is returned. Otherwise, a new Logger is created and
     * registered.
     *
     * @param name The name of the Logger instance.
     * @return The Logger instance associated with the specified name.
     */
    public static Logger getLogger(String name) {
        if (loggerMap.containsKey(name)) {
            return loggerMap.get(name);
        } else {
            return new Logger(name);
        }
    }

    /**
     * Logs the entry into a method. Increases the call depth and prints the method
     * name with appropriate indentation.
     *
     * @param functionName The name of the method being entered.
     */
    public void stepIn(String functionName) {
        printIndentation();
        System.out.printf("--> %s%n", functionName);
        depth++;
    }

    /**
     * Logs the exit from a method. Decreases the call depth and prints the method
     * name with appropriate indentation.
     *
     * @param functionName The name of the method being exited.
     */
    public void stepOut(String functionName, Object pReturn) {
        depth--;
        printIndentation();
        System.out.printf("<-- %s%n", functionName);
        if (pReturn != null) {
            System.out.printf("Returned: %s%n (%s)", pReturn, pReturn.getClass().toString());
        }else{
            System.out.printf("No return value");
        }
        
    }

    /**
     * Logs a message or question within a method. If the message is a question, it
     * waits for user input and returns the response.
     *
     * @param message The message or question to log.
     * @param hasQ    If true, the message is treated as a question and user input
     *                is requested.
     * @return The user's response if the message is a question, otherwise an empty
     *         string.
     */
    public String askQ(String message, boolean hasQ) {
        String input = "";
        if (hasQ) {
            printIndentation();
            try (Scanner scanner = new Scanner(System.in)) {
                System.out.printf("%4s ", message);
                input = scanner.nextLine();
            }
        } else {
            printIndentation();
            System.out.printf("%4s%n", message);
        }
        return input;
    }

    /**
     * Helper method to print indentation based on the current call depth.
     */
    private void printIndentation() {
        for (int i = 0; i < depth; ++i) {
            System.out.printf("%10s", " "); // 10 spaces per depth level
        }
    }

    /**
     * Returns the name of this Logger instance.
     *
     * @return The name of the Logger.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the static HashMap containing all registered Logger instances.
     *
     * @return The HashMap of Logger instances.
     */
    public static HashMap<String, Logger> getLoggerMap() {
        return loggerMap;
    }
}