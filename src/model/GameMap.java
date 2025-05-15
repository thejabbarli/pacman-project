package model;

// Class responsible for the game map state
public class GameMap {
    private boolean[][] walkableCells;
    private int rows;
    private int columns;

    public GameMap(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.walkableCells = new boolean[rows][columns];

        // Initialize all cells as walkable for now
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                walkableCells[i][j] = true;
            }
        }
    }

    public boolean isWalkable(int row, int column) {
        if (row < 0 || row >= rows || column < 0 || column >= columns) {
            return false;
        }
        return walkableCells[row][column];
    }

    public void setWalkable(int row, int column, boolean walkable) {
        if (row >= 0 && row < rows && column >= 0 && column < columns) {
            walkableCells[row][column] = walkable;
        }
    }

    public boolean[][] getWalkableCells() {
        return walkableCells;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }
}