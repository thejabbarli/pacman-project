package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Extension of GameMap to include walls
 * Follows Open/Closed Principle by extending GameMap functionality
 */
public class GameMapWithWalls extends GameMap {
    private List<Wall> walls;

    public GameMapWithWalls(int rows, int columns) {
        super(rows, columns);
        this.walls = new ArrayList<>();
    }

    /**
     * Adds a wall at the specified position
     */
    public void addWall(int row, int column) {
        // Create wall object
        Wall wall = new Wall(row, column);
        walls.add(wall);

        // Mark cell as not walkable
        setWalkable(row, column, false);
    }

    /**
     * Generate walls for a basic maze pattern
     */
    public void generateBasicWalls() {
        int rows = getRows();
        int columns = getColumns();

        // Add border walls
        for (int i = 0; i < rows; i++) {
            addWall(i, 0);                 // Left border
            addWall(i, columns - 1);       // Right border
        }

        for (int j = 0; j < columns; j++) {
            addWall(0, j);                 // Top border
            addWall(rows - 1, j);          // Bottom border
        }

        // Add some internal walls for a simple maze
        // Horizontal walls
        for (int j = 3; j < columns - 3; j++) {
            if (j != columns / 2) {        // Leave a gap in the middle
                addWall(rows / 4, j);      // Upper horizontal wall
                addWall(rows * 3 / 4, j);  // Lower horizontal wall
            }
        }

        // Vertical walls
        for (int i = 3; i < rows - 3; i++) {
            if (i != rows / 2) {           // Leave a gap in the middle
                addWall(i, columns / 4);   // Left vertical wall
                addWall(i, columns * 3 / 4); // Right vertical wall
            }
        }

        // Add some random walls
        int numRandomWalls = (rows + columns) / 4;
        for (int k = 0; k < numRandomWalls; k++) {
            int i = (int) (Math.random() * (rows - 4)) + 2;  // Avoid borders
            int j = (int) (Math.random() * (columns - 4)) + 2;

            // Don't place walls near Pacman's starting position (center)
            if (Math.abs(i - rows / 2) > 2 || Math.abs(j - columns / 2) > 2) {
                addWall(i, j);
            }
        }
    }

    /**
     * Get all walls
     */
    public List<Wall> getWalls() {
        return new ArrayList<>(walls); // Return a copy to prevent modification
    }
}