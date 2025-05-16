package model;

import javax.swing.ImageIcon;
import java.util.ArrayList;
import java.util.List;

/**
 * Blinky - A simple ghost that moves toward Pacman
 */
public class Blinky implements Movable, Collidable {

    private int row;
    private int column;
    private Direction currentDirection;
    private int speed;           // Movement speed (higher = faster)
    private int moveDelay;       // Delay between moves in milliseconds
    private List<ImageIcon> animationFrames;
    private int currentFrame;

    /**
     * Create a new Blinky ghost
     * @param initialRow Starting row
     * @param initialColumn Starting column
     */
    public Blinky(int initialRow, int initialColumn) {
        this.row = initialRow;
        this.column = initialColumn;
        this.currentDirection = Direction.NONE;
        this.speed = 1;          // Default speed value
        this.moveDelay = 500;    // Default delay in milliseconds
        this.currentFrame = 0;
        loadAnimationFrames();
    }

    /**
     * Load animation images
     */
    private void loadAnimationFrames() {
        animationFrames = new ArrayList<>();

        try {
            // Load single ghost image
            String path = "res/ghosts/enemyRed.png";
            ImageIcon icon = new ImageIcon(path);
            animationFrames.add(icon);

            // If image loading fails, create a fallback
            if (icon.getIconWidth() <= 0) {
                createFallbackImage();
            }
        } catch (Exception e) {
            System.err.println("Error loading Blinky image: " + e.getMessage());
            createFallbackImage();
        }
    }

    /**
     * Create a fallback image if the real image can't be loaded
     */
    private void createFallbackImage() {
        // Create a simple red square as fallback
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(
                20, 20, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g = img.createGraphics();
        g.setColor(java.awt.Color.RED);
        g.fillRect(0, 0, 20, 20);
        g.dispose();

        animationFrames.clear();
        animationFrames.add(new ImageIcon(img));
    }

    /**
     * Calculate next move towards Pacman
     * @param pacmanRow Pacman's row
     * @param pacmanColumn Pacman's column
     * @param gameMap Game map with walls
     * @return Direction to move
     */
    public Direction calculateNextMove(int pacmanRow, int pacmanColumn, GameMapWithWalls gameMap) {
        // Calculate direction to move towards Pacman

        // Get all valid directions
        List<Direction> validDirections = new ArrayList<>();

        // Check each direction if it's valid (not a wall)
        if (row > 0 && gameMap.isWalkable(row - 1, column)) {
            validDirections.add(Direction.UP);
        }
        if (row < gameMap.getRows() - 1 && gameMap.isWalkable(row + 1, column)) {
            validDirections.add(Direction.DOWN);
        }
        if (column > 0 && gameMap.isWalkable(row, column - 1)) {
            validDirections.add(Direction.LEFT);
        }
        if (column < gameMap.getColumns() - 1 && gameMap.isWalkable(row, column + 1)) {
            validDirections.add(Direction.RIGHT);
        }

        // If no valid directions, don't move
        if (validDirections.isEmpty()) {
            return Direction.NONE;
        }

        // Choose best direction to move toward Pacman
        Direction bestDirection = validDirections.get(0);
        int bestDistance = Integer.MAX_VALUE;

        for (Direction dir : validDirections) {
            int newRow = row;
            int newCol = column;

            // Calculate new position if moving in this direction
            switch (dir) {
                case UP:
                    newRow--;
                    break;
                case DOWN:
                    newRow++;
                    break;
                case LEFT:
                    newCol--;
                    break;
                case RIGHT:
                    newCol++;
                    break;
            }

            // Calculate Manhattan distance to Pacman
            int distance = Math.abs(newRow - pacmanRow) + Math.abs(newCol - pacmanColumn);

            // If this is better than the current best, update it
            if (distance < bestDistance) {
                bestDistance = distance;
                bestDirection = dir;
            }
        }

        return bestDirection;
    }

    /**
     * Get the next animation frame
     */
    public void nextFrame() {
        currentFrame = (currentFrame + 1) % animationFrames.size();
    }

    /**
     * Get current animation frame
     */
    public ImageIcon getCurrentFrame() {
        return animationFrames.get(currentFrame);
    }

    /**
     * Get movement delay in milliseconds
     * @return Movement delay
     */
    public int getMoveDelay() {
        return moveDelay;
    }

    /**
     * Set movement delay in milliseconds (lower = faster)
     * @param moveDelay Movement delay
     */
    public void setMoveDelay(int moveDelay) {
        if (moveDelay >= 50) { // Prevent too fast movement
            this.moveDelay = moveDelay;
        }
    }

    // Interface implementations

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getColumn() {
        return column;
    }

    @Override
    public void setPosition(int row, int column) {
        this.row = row;
        this.column = column;
    }

    @Override
    public Direction getCurrentDirection() {
        return currentDirection;
    }

    @Override
    public void setDirection(Direction direction) {
        this.currentDirection = direction;
    }

    @Override
    public int getSpeed() {
        return speed;
    }

    @Override
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    @Override
    public boolean collidesWithMap(int row, int column) {
        return this.row == row && this.column == column;
    }

    @Override
    public void onCollision(Collidable other) {
        // Handle collision with other objects
        // For simple implementation, we don't need to do anything here
    }
}