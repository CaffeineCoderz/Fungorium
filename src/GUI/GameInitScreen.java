package GUI;
import javax.swing.*;

class GameInitScreen extends JFrame {
    public GameInitScreen(int playerCount) {
        setTitle("Fungorium - Game Initialization");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel loadingLabel = new JLabel("Játék inicializálása " + playerCount + " játékos számára...");
        loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(loadingLabel);

        // loader timer
        // itt lesz minden hujeseg ami a jatek inicializalashoz kell
        // pl. fajok, jatekosok, map, stb.
        //erre majd kulon fuggveny lesz de ez majd ki lesz gondolva
        Timer timer = new Timer(3000, e -> openGameMapScreen());
        timer.setRepeats(false);
        timer.start();
    }

    private void openGameMapScreen() {
        // palya betoltes
        new GameMapScreen().setVisible(true);
        dispose();
    }
}