package GUI.Views;

import javax.swing.*;
import java.awt.*;

public class StatusView extends JPanel {
    private JTextArea statusTextArea;
    private final Point rightPosition = new Point(600, 10); // Original position (right-top corner)
    private final Point leftPosition = new Point(10, 10); // Alternate position (left-top corner)

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

    /**
     * Updates the status view based on the given status string.
     * If the status is empty or null, the status block is hidden.
     * Otherwise, the status block is shown and its text is updated to the given status.
     * @param status the status string to be displayed
     */
    public void updateStatus(String status) {
        if (status == null || status.isEmpty()) {
            clearStatus(); // Hide the status block
        } else {
            statusTextArea.setText(status);
            statusTextArea.setVisible(true); // Show the status block
        }
    }

    /**
     * Clears the status view by hiding the status block and resetting the text area.
     * Equivalent to calling {@link #updateStatus(String)} with an empty string as argument.
     */
    public void clearStatus() {
        statusTextArea.setText("");
        statusTextArea.setVisible(false); // Hide the status block
    }

    /**
     * Moves the status view to the alternate position specified by {@link #leftPosition}.
     * The size of the status view remains the same.
     * This method is usually called when the status view needs to be moved out of the way of the
     * game panel's buttons.
     */
    public void moveToLeftPosition() {
        statusTextArea.setBounds(leftPosition.x, leftPosition.y, 180, 100);
        repaint();
    }

    /**
     * Moves the status view to the original position specified by {@link #rightPosition}.
     * The size of the status view remains the same.
     * This method is usually called when the status view needs to be moved back to its original
     * position after being moved out of the way of the game panel's buttons.
     */
    public void moveToRightPosition() {
        statusTextArea.setBounds(rightPosition.x, rightPosition.y, 180, 100);
        repaint();
    }
}
