package model;

/**
 * Represents a point/dot on the game map
 */
public class Point {

    private int row;
    private int column;
    private boolean collected;

    /**
     * Create a new point
     * @param row Row position
     * @param column Column position
     */
    public Point(int row, int column) {
        this.row = row;
        this.column = column;
        this.collected = false;
    }

    /**
     * Get row position
     * @return Row position
     */
    public int getRow() {
        return row;
    }

    /**
     * Get column position
     * @return Column position
     */
    public int getColumn() {
        return column;
    }

    /**
     * Check if point has been collected
     * @return True if collected
     */
    public boolean isCollected() {
        return collected;
    }

    /**
     * Mark point as collected
     */
    public void collect() {
        collected = true;
    }

    /**
     * Reset point to uncollected state
     */
    public void reset() {
        collected = false;
    }
}