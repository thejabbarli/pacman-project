package map;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Responsible for loading map data from text files
 * Follows Single Responsibility Principle by focusing only on map loading
 */
public class MapParser {

    /**
     * Loads a map from a text file.
     * '#' represents walls, 'o' or 'O' represents walkable paths.
     * @param filePath Path to the map file
     * @return 2D char array containing the map data
     */
    public char[][] parseMapFile(String filePath) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Add the line to our list
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error reading map file: " + filePath);
            return createDefaultMap(); // Return a default map on error
        }

        if (lines.isEmpty()) {
            System.err.println("Map file was empty: " + filePath);
            return createDefaultMap(); // Return a default map if file is empty
        }

        // Create map array from the loaded lines
        return convertLinesToMap(lines);
    }

    /**
     * Converts the read lines into a 2D map array
     */
    private char[][] convertLinesToMap(List<String> lines) {
        // Determine dimensions
        int height = lines.size();
        int width = 0;

        // Find the longest line to determine width
        for (String line : lines) {
            width = Math.max(width, line.length());
        }

        // Create the map array
        char[][] mapData = new char[height][width];

        // Fill with default value (space)
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                mapData[y][x] = ' ';
            }
        }

        // Fill in the actual map data
        for (int y = 0; y < lines.size(); y++) {
            String line = lines.get(y);
            for (int x = 0; x < line.length(); x++) {
                mapData[y][x] = line.charAt(x);
            }
        }

        return mapData;
    }

    /**
     * Creates a small default map in case of errors
     */
    private char[][] createDefaultMap() {
        return new char[][] {
                {'#', '#', '#', '#', '#'},
                {'#', 'o', ' ', ' ', '#'},
                {'#', ' ', '#', ' ', '#'},
                {'#', ' ', ' ', ' ', '#'},
                {'#', '#', '#', '#', '#'}
        };
    }
}