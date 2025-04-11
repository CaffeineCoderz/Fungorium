import commands.CommandProcessor;
import commands.Tester;
import logic.GameLogic;

public class main {

    public static void main(String[] args) {
        // CommandProcessor processor = new CommandProcessor();
        // processor.processConfigText("config");
        // processor.start();

        Tester tester = new Tester();
        tester.checkTestResults();

        // Create an instance of GameLogic
        GameLogic gameLogic = new GameLogic();

        // Set the game time (optional)
        gameLogic.setGameTime(15);

        // Start the game
        System.out.println("Welcome to the game!");
        gameLogic.startGame();

        // Simulate game rounds
        while (gameLogic.getGameTime() > 0) {
            gameLogic.takeTurn();
        }

        // End of the game
        System.out.println("Game over!");
    }
}