package GUI;

import javax.swing.*;

public class Settings {

    public static void showSettings(JFrame parent) {
        JOptionPane.showMessageDialog(parent, "Itt lehet majd beállításokat módosítani.", "Beállítások", JOptionPane.INFORMATION_MESSAGE);
    }
}