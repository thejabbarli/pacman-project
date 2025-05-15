package map;

import model.GameMapWithWalls;
import model.Wall;

/**
 * Converts raw map data into game-specific map objects
 * Follows Single Responsibility Principle by focusing only on map conversion
 */
public class MapConverter {

    /**
     * Converts a character map into a GameMapWithWalls object
     * @param mapData The 2D char array containing the map data
     * @return A populated GameMapWithWalls object
     */
    public GameMapWithWalls convertToGameMap(char[][] mapData) {
        if (mapData == null || mapData.length == 0 || mapData[0].length == 0) {
            throw new IllegalArgumentException("Invalid map data: Map cannot be empty");
        }

        int rows = mapData.length;
        int cols = mapData[0].length;

        // Create the game map
        GameMapWithWalls gameMap = new GameMapWithWalls(rows, cols);

        // Process each cell in the map
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                char cell = mapData[row][col];
                processCellType(gameMap, cell, row, col);
            }
        }

        return gameMap;
    }

    /**
     * Processes a single cell based on its type
     * @param gameMap The game map to populate
     * @param cellType The type of the cell (from the map file)
     * @param row The row position
     * @param col The column position
     */
    private void processCellType(GameMapWithWalls gameMap, char cellType, int row, int col) {
        switch (cellType) {
            case '#': // Wall
                gameMap.addWall(row, col);
                break;
            case 'o': // Walkable path with dot
            case 'O': // Walkable path with power pellet
                // Set as walkable (it's walkable by default, but being explicit)
                gameMap.setWalkable(row, col, true);
                // Note: You can add code here to add dots or pellets
                break;
            case ' ': // Empty walkable space
                gameMap.setWalkable(row, col, true);
                break;
            default: // Treat unknown characters as walkable by default
                gameMap.setWalkable(row, col, true);
                break;
        }
    }
}