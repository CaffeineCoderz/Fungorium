package GUI;
import javax.swing.SwingUtilities;

public class GUITest {

    public static void main(String[] args) {
        // Indítsuk el a GUI-t a SwingUtilities.invokeLater segítségével
        SwingUtilities.invokeLater(() -> {
            MainMenu mainMenu = new MainMenu();
            mainMenu.setVisible(true);
        });
    }
}