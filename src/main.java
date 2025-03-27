import commands.CommandProcessor;

public class main {
    
    public static void main(String[] args) {
        CommandProcessor processor = new CommandProcessor();
        processor.processConfigText("initialize");
        processor.start();
    }
}