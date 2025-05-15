package map;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple class for parsing map files
 */
public class MapLoader {

    /**
     * Loads a map from a text file.
     * '#' represents walls, other characters are treated as open space.
     * Handles spaces between characters.
     * @param filePath Path to the map file
     * @return 2D char array containing the map data
     */
    public char[][] loadMap(String filePath) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new char[0][0];
        }

        if (lines.isEmpty()) {
            return new char[0][0];
        }

        // Determine map dimensions, skipping spaces between chars
        int height = lines.size();
        int maxWidth = 0;

        for (String line : lines) {
            // Count every other character (skipping spaces)
            int effectiveWidth = (line.length() + 1) / 2;
            maxWidth = Math.max(maxWidth, effectiveWidth);
        }

        // Create the map
        char[][] map = new char[height][maxWidth];

        // Fill map with spaces by default
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < maxWidth; j++) {
                map[i][j] = ' ';
            }
        }

        // Fill the map, skipping spaces
        for (int i = 0; i < height; i++) {
            String line = lines.get(i);
            for (int j = 0; j < line.length(); j += 2) { // Skip every other character (space)
                if (j / 2 < maxWidth) {
                    map[i][j / 2] = line.charAt(j);
                }
            }
        }

        return map;
    }

    /**
     * Creates a GameMapWithWalls from a 2D char array
     * @param charMap The character map
     * @return A GameMapWithWalls object
     */
    public model.GameMapWithWalls createGameMap(char[][] charMap) {
        int height = charMap.length;
        int width = charMap[0].length;

        model.GameMapWithWalls gameMap = new model.GameMapWithWalls(height, width);

        // Process each character
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (charMap[row][col] == '#') {
                    // Add wall
                    gameMap.addWall(row, col);
                } else {
                    // Set cell as walkable
                    gameMap.setWalkable(row, col, true);
                }
            }
        }

        return gameMap;
    }
}