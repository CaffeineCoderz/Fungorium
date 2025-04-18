import java.util.Scanner;
import commands.CommandProcessor;
import commands.Tester;
import logic.GameLogic;

public class main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice = 0;

        while (choice != 4) {
            System.out.println("Choose an option:");
            System.out.println("1. Process configuration and start CommandProcessor");
            System.out.println("2. Run Tester to check test results");
            System.out.println("3. Start the game");
            System.out.println("4. Exit");

            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    CommandProcessor processor = new CommandProcessor();
                    processor.processConfigText("config");
                    processor.start();
                    break;
                case 2:
                    Tester tester = new Tester();
                    tester.checkTestResults();
                    break;
                case 3:
                    GameLogic gameLogic = new GameLogic();
                    gameLogic.setGameTime(15);
                    System.out.println("Welcome to the game!");
                    gameLogic.startGame();
                    break;
                case 4:
                    System.out.println("Exiting the program. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }
}