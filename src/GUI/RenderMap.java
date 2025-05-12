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

    /**
     * Beállítja a pálya méretét a paraméterben megadott MapSize alapján.
     * 
     * A pálya méretét úgy kell beállítani, hogy a Tektonok 9/4 db cellát foglaljanak
     * el, tehát a pálya méretét úgy kell beállítani, hogy a Tektonok illeszkedjenek a
     * pályára.
     * 
     * @param size a pálya mérete
     */
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

    /**
     * Initializes the tiles of the map by creating a 2D array of points where
     * each point represents a cell in the map. The size of the map is determined
     * by the rows and cols fields.
     */
    private void initializeTiles() {
        tiles = new ArrayList<>();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                tiles.add(new Point(row, col));
            }
        }
    }


    /**
     * Returns a list of all tiles in the map. Each tile is represented as a
     * Point object where the x-coordinate is the row number and the y-coordinate
     * is the column number. The size of the list is determined by the rows and
     * cols fields.
     * 
     * @return a list of all tiles in the map
     */
    public List<Point> getTiles() {
        return tiles;
    }

    /**
     * Returns the number of rows in the map.
     * 
     * @return the number of rows in the map
     */
    public int getRows() {
        return rows;
    }

    /**
     * Returns the number of columns in the map.
     * 
     * @return the number of columns in the map
     */

    public int getCols() {
        return cols;
    }
}