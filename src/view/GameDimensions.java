package view;

import java.awt.Dimension;

// This class handles game dimensions, following Single Responsibility Principle
public class GameDimensions {
    private static final int DEFAULT_SIZE = 600; // Square window
    private static final int STATS_HEIGHT = 40;
    private static final int MINIMUM_CELL_SIZE = 5; // Minimum allowed cell size
    private static final int MINIMUM_WINDOW_SIZE = 200; // Minimum window dimension

    // Returns the initial window dimension (square)
    public static Dimension getInitialWindowSize() {
        return new Dimension(DEFAULT_SIZE, DEFAULT_SIZE);
    }

    // Returns the minimum window size
    public static Dimension getMinimumWindowSize() {
        return new Dimension(MINIMUM_WINDOW_SIZE, MINIMUM_WINDOW_SIZE);
    }

    // Calculates cell size based on window dimensions and grid size
    public static int calculateCellSize(int width, int height, int rows, int columns) {
        int availableHeight = height - STATS_HEIGHT;
        int cellSizeByWidth = Math.max(width / columns, MINIMUM_CELL_SIZE);
        int cellSizeByHeight = Math.max(availableHeight / rows, MINIMUM_CELL_SIZE);

        return Math.min(cellSizeByWidth, cellSizeByHeight); // Use smaller value to fit grid
    }

    // Returns the stats panel height
    public static int getStatsHeight() {
        return STATS_HEIGHT;
    }
}