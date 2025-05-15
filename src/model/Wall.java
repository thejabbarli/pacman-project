package model;

/**
 * Represents a wall cell in the game
 * Follows Single Responsibility Principle by focusing only on wall representation
 */
public class Wall implements Collidable {
    private int row;
    private int column;

    public Wall(int row, int column) {
        this.row = row;
        this.column = column;
    }

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getColumn() {
        return column;
    }

    @Override
    public boolean collidesWithMap(int row, int column) {
        // Check if the given row,column matches this wall's position
        return this.row == row && this.column == column;
    }

    @Override
    public void onCollision(Collidable other) {
        // Walls don't do anything when collided with
        // This is intentionally empty
    }
}