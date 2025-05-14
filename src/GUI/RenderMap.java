package GUI;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class RenderMap extends JPanel {
    public enum MapSize {
        SMALL, MEDIUM, LARGE
    }

    private int rows;
    private int cols;
    private List<Point> tiles;

    public RenderMap(MapSize size) {
        setupGrid(size);
        initializeTiles();
    }

    private void setupGrid(MapSize size) {
        // Egy Tekton 9/4 db cellát foglal el, ehhez igazodva kell be a méretet
        // beállítani
        switch (size) {
            case SMALL:
                rows = 90;
                cols = 90;
                break;
            case MEDIUM:
                rows = 25;
                cols = 25;
                break;
            case LARGE:
                rows = 150;
                cols = 150;
                break;
            default:
                throw new IllegalArgumentException("Invalid map size");
        }
    }

    private void initializeTiles() {
        tiles = new ArrayList<>();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                tiles.add(new Point(row, col));
            }
        }
    }


    public List<Point> getTiles() {
        return tiles;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }
}