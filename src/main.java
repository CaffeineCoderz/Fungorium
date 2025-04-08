import commands.CommandProcessor;
import commands.Tester;

public class main {

    public static void main(String[] args) {
        // CommandProcessor processor = new CommandProcessor();
        // processor.processConfigText("config");
        // processor.start();

        Tester tester = new Tester();
        tester.checkTestResults();
    }
}