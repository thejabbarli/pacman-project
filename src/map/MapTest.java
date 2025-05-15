package map;

/**
 * Simple test class to verify map loading
 */
public class MapTest {

    public static void main(String[] args) {
        // Load the map
        MapLoader loader = new MapLoader();
        char[][] map = loader.loadMap("res/maps/map1.txt");

        // Print the map to console for verification
        System.out.println("Map size: " + map.length + " rows x " + map[0].length + " columns");

        // Print original map (with spaces)
        System.out.println("\nOriginal map (first few lines):");
        try {
            java.io.BufferedReader reader = new java.io.BufferedReader(
                    new java.io.FileReader("res/maps/map1.txt"));
            for (int i = 0; i < 5; i++) { // Print first 5 lines
                String line = reader.readLine();
                if (line == null) break;
                System.out.println(line);
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Error reading original map: " + e.getMessage());
        }

        // Print parsed map
        System.out.println("\nParsed map (first few lines, spaces removed):");
        for (int i = 0; i < Math.min(5, map.length); i++) {
            for (int j = 0; j < map[i].length; j++) {
                System.out.print(map[i][j]);
            }
            System.out.println();
        }

        // Print map with explicit spaces to make it clear
        System.out.println("\nParsed map with explicit spaces (first few lines):");
        for (int i = 0; i < Math.min(5, map.length); i++) {
            for (int j = 0; j < map[i].length; j++) {
                System.out.print("[" + map[i][j] + "]");
            }
            System.out.println();
        }

        // Count walls
        int wallCount = 0;
        int openCount = 0;
        int startCount = 0;

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == '#') {
                    wallCount++;
                } else if (map[i][j] == 'o' || map[i][j] == 'O') {
                    startCount++;
                } else {
                    openCount++;
                }
            }
        }

        System.out.println("\nMap statistics:");
        System.out.println("Wall cells (#): " + wallCount);
        System.out.println("Start positions (o/O): " + startCount);
        System.out.println("Open cells: " + openCount);
    }
}