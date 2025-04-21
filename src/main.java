import java.util.Scanner;
import commands.CommandProcessor;
import commands.Tester;
import logic.GameLogic;

public class main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice = 0;

        Tester tester = new Tester();
        GameLogic gameLogic = new GameLogic();

        while (choice != 5) {
            System.out.println("<=========================================>");
            System.out.println("Choose an option:");
            System.out.println("1. Process configuration and start CommandProcessor");
            System.out.println("2. Check all test results");
            System.out.println("3. Check specific test case result");
            System.out.println("4. Start the game");
            System.out.println("5. Exit");
            System.out.println("<=========================================>");
            System.out.println("Please enter your choice (1-5): ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    CommandProcessor processor = new CommandProcessor();
                    processor.processConfigText("config");
                    processor.start();
                    break;
                case 2:
                    tester.checkTestResults();
                    break;
                case 3:
                    tester.printTestCases();
                    System.out.println("Which test case would you like to run? ");
                    int caseChoice = scanner.nextInt();
                    tester.checkTestResults(caseChoice);
                    break;
                case 4:
                    System.out.println("How many rounds should the game have?");
                    int rounds = scanner.nextInt();
                    if(rounds < 1) {
                        System.out.println("Invalid number of rounds. Please enter a positive integer.");
                        break;
                    }
                    gameLogic.setGameTime(rounds);
                    System.out.println("Welcome to the game!");
                    gameLogic.startGame();
                    break;
                case 5:
                    System.out.println("Exiting the program. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }
}