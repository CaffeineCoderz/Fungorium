package GUI.Views;

import javax.swing.*;
import java.awt.*;

public class StatusView extends JPanel {
    private JTextArea statusTextArea;

    public StatusView() {
        setLayout(null); // Absolute positioning
        setOpaque(false); // Transparent background

        statusTextArea = new JTextArea(5, 20);
        statusTextArea.setEditable(false);
        statusTextArea.setLineWrap(true);
        statusTextArea.setWrapStyleWord(true);
        statusTextArea.setBackground(new Color(240, 240, 240, 100));
        statusTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        statusTextArea.setFont(new Font("Arial", Font.PLAIN, 12));
        statusTextArea.setVisible(false); // Initially hidden

        add(statusTextArea);
        statusTextArea.setBounds(600, 10, 180, 100); // Position at the top-right corner
    }

    public void updateStatus(String status) {
        if (status == null || status.isEmpty()) {
            clearStatus(); // Hide the status block
        } else {
            statusTextArea.setText(status);
            statusTextArea.setVisible(true); // Show the status block
        }
    }

    public void clearStatus() {
        statusTextArea.setText("");
        statusTextArea.setVisible(false); // Hide the status block
    }
}
