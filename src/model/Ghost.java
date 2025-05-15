package model;

import java.util.*;
import javax.swing.ImageIcon;

/**
 * Abstract Ghost class that implements behavior for all ghost types
 * Ghosts will continuously move and chase the player
 */
public abstract class Ghost implements Movable, Collidable {

    protected int row;
    protected int column;
    protected Direction currentDirection;
    protected int speed;
    protected List<ImageIcon> animationFrames;
    protected int currentFrame;

    // Target coordinates (usually Pacman's position)
    protected int targetRow;
    protected int targetColumn;

    // Personality variables to be defined by subclasses
    protected int scatterModeTime; // Time ghost stays in scatter mode
    protected int chaseModeTime;   // Time ghost stays in chase mode

    // Movement state
    protected GhostState state;

    /**
     * Basic states for ghost behavior
     */
    public enum GhostState {
        CHASE,      // Chase pacman
        SCATTER,    // Move to a corner
        FRIGHTENED, // Move away from pacman (when power pellet)
        EATEN       // Return to ghost house
    }

    /**
     * Constructor for a ghost
     * @param initialRow Starting row
     * @param initialColumn Starting column
     */
    public Ghost(int initialRow, int initialColumn) {
        this.row = initialRow;
        this.column = initialColumn;
        this.currentDirection = Direction.NONE;
        this.speed = 1;
        this.currentFrame = 0;
        this.state = GhostState.SCATTER; // Start in scatter mode

        // Initialize animation frames
        loadAnimationFrames();
    }

    /**
     * Load animation images - to be implemented by subclasses
     */
    protected abstract void loadAnimationFrames();

    /**
     * Calculate next direction based on AI logic
     * @param gameMap The game map with walls
     * @param pacmanRow Pacman's row
     * @param pacmanColumn Pacman's column
     * @return The next direction to move
     */
    public Direction calculateNextMove(GameMapWithWalls gameMap, int pacmanRow, int pacmanColumn) {
        // Update target
        updateTarget(pacmanRow, pacmanColumn);

        // Get available directions (excluding walls)
        List<Direction> possibleDirections = getAvailableDirections(gameMap);

        // Don't reverse direction unless it's the only option
        removeSelfReversalDirection(possibleDirections);

        // If no direction available (trapped), allow reversal
        if (possibleDirections.isEmpty()) {
            possibleDirections = getAvailableDirections(gameMap);
        }

        // If still no options, don't move
        if (possibleDirections.isEmpty()) {
            return Direction.NONE;
        }

        // Choose best direction based on state
        switch (state) {
            case CHASE:
                return getBestDirectionTowardsTarget(possibleDirections, gameMap);
            case SCATTER:
                return getScatterModeDirection(possibleDirections, gameMap);
            case FRIGHTENED:
                return getRandomDirection(possibleDirections);
            case EATEN:
                // When eaten, directly target ghost house
                return getBestDirectionTowardsTarget(possibleDirections, gameMap);
            default:
                return getRandomDirection(possibleDirections);
        }
    }

    /**
     * Update target position based on current state and Pacman's position
     * @param pacmanRow Pacman's row
     * @param pacmanColumn Pacman's column
     */
    protected void updateTarget(int pacmanRow, int pacmanColumn) {
        switch (state) {
            case CHASE:
                // In chase mode, typically target Pacman directly
                // Subclasses can override for more complex targeting
                targetRow = pacmanRow;
                targetColumn = pacmanColumn;
                break;
            case SCATTER:
                // In scatter mode, target a specific corner
                // Subclasses should override to specify different corners
                setScatterTarget();
                break;
            case FRIGHTENED:
                // Random target in frightened mode
                // No need to set target, as movement is random
                break;
            case EATEN:
                // Target ghost house when eaten
                targetRow = getGhostHouseRow();
                targetColumn = getGhostHouseColumn();
                break;
        }
    }

    /**
     * Set scatter mode target (usually a map corner)
     * Subclasses should override to target different corners
     */
    protected abstract void setScatterTarget();

    /**
     * Get ghost house row for return when eaten
     */
    protected int getGhostHouseRow() {
        // Default ghost house position - override in subclasses if needed
        return 11; // Example value
    }

    /**
     * Get ghost house column for return when eaten
     */
    protected int getGhostHouseColumn() {
        // Default ghost house position - override in subclasses if needed
        return 13; // Example value
    }

    /**
     * Get all available directions from current position
     * @param gameMap The game map
     * @return List of possible directions
     */
    private List<Direction> getAvailableDirections(GameMapWithWalls gameMap) {
        List<Direction> availableDirections = new ArrayList<>();

        // Check each direction
        if (isValidMove(row - 1, column, gameMap)) {
            availableDirections.add(Direction.UP);
        }
        if (isValidMove(row + 1, column, gameMap)) {
            availableDirections.add(Direction.DOWN);
        }
        if (isValidMove(row, column - 1, gameMap)) {
            availableDirections.add(Direction.LEFT);
        }
        if (isValidMove(row, column + 1, gameMap)) {
            availableDirections.add(Direction.RIGHT);
        }

        return availableDirections;
    }

    /**
     * Check if a move to the given position is valid
     * @param newRow Row to check
     * @param newColumn Column to check
     * @param gameMap Game map with walls
     * @return true if move is valid
     */
    private boolean isValidMove(int newRow, int newColumn, GameMapWithWalls gameMap) {
        // Check if position is within bounds and walkable
        if (newRow < 0 || newRow >= gameMap.getRows() ||
                newColumn < 0 || newColumn >= gameMap.getColumns()) {
            return false;
        }

        return gameMap.isWalkable(newRow, newColumn);
    }

    /**
     * Remove the direction that would cause reversing
     * @param directions List of available directions
     */
    private void removeSelfReversalDirection(List<Direction> directions) {
        Direction oppositeDirection = getOppositeDirection(currentDirection);
        directions.remove(oppositeDirection);
    }

    /**
     * Get the opposite direction
     * @param dir Current direction
     * @return Opposite direction
     */
    private Direction getOppositeDirection(Direction dir) {
        switch (dir) {
            case UP: return Direction.DOWN;
            case DOWN: return Direction.UP;
            case LEFT: return Direction.RIGHT;
            case RIGHT: return Direction.LEFT;
            default: return Direction.NONE;
        }
    }

    /**
     * Get best direction towards target using a simple distance heuristic
     * @param availableDirections Available directions to choose from
     * @param gameMap The game map
     * @return Best direction towards target
     */
    private Direction getBestDirectionTowardsTarget(List<Direction> availableDirections, GameMapWithWalls gameMap) {
        // If no directions available, don't move
        if (availableDirections.isEmpty()) {
            return Direction.NONE;
        }

        // Find direction that minimizes distance to target
        Direction bestDirection = availableDirections.get(0);
        double minDistance = Double.MAX_VALUE;

        for (Direction dir : availableDirections) {
            // Calculate new position if moving in this direction
            int newRow = row;
            int newCol = column;

            switch (dir) {
                case UP: newRow--; break;
                case DOWN: newRow++; break;
                case LEFT: newCol--; break;
                case RIGHT: newCol++; break;
                case NONE: break;
            }

            // Calculate Manhattan distance to target
            double distance = Math.abs(newRow - targetRow) + Math.abs(newCol - targetColumn);

            // Update best direction if this is better
            if (distance < minDistance) {
                minDistance = distance;
                bestDirection = dir;
            }
        }

        return bestDirection;
    }

    /**
     * Get direction for scatter mode - can be overridden by subclasses
     * @param availableDirections Available directions
     * @param gameMap Game map
     * @return Direction to move in scatter mode
     */
    protected Direction getScatterModeDirection(List<Direction> availableDirections, GameMapWithWalls gameMap) {
        // By default, use same algorithm as chase but target corner instead of Pacman
        return getBestDirectionTowardsTarget(availableDirections, gameMap);
    }

    /**
     * Get a random direction from available directions
     * @param availableDirections Available directions
     * @return Random direction
     */
    private Direction getRandomDirection(List<Direction> availableDirections) {
        if (availableDirections.isEmpty()) {
            return Direction.NONE;
        }

        // Choose random direction
        int randomIndex = (int) (Math.random() * availableDirections.size());
        return availableDirections.get(randomIndex);
    }

    /**
     * Change ghost state
     * @param newState New state
     */
    public void setState(GhostState newState) {
        this.state = newState;
    }

    /**
     * Toggle between chase and scatter modes
     */
    public void toggleChaseScatter() {
        if (state == GhostState.CHASE) {
            state = GhostState.SCATTER;
        } else if (state == GhostState.SCATTER) {
            state = GhostState.CHASE;
        }
    }

    /**
     * Set ghost to frightened mode
     */
    public void setFrightened() {
        state = GhostState.FRIGHTENED;
    }

    /**
     * Get next animation frame
     */
    public void nextFrame() {
        currentFrame = (currentFrame + 1) % animationFrames.size();
    }

    // Interface method implementations

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
        if (other instanceof PacmanModel) {
            // Handle collision with Pacman
            // This will be checked by the game controller to determine if Pacman loses a life
            // or if the ghost is eaten when in frightened mode
        }
    }

    /**
     * Get current animation frame
     * @return The current frame image
     */
    public ImageIcon getCurrentFrame() {
        return animationFrames.get(currentFrame);
    }

    /**
     * Get all animation frames
     * @return List of animation frames
     */
    public List<ImageIcon> getAnimationFrames() {
        return animationFrames;
    }

    /**
     * Get current ghost state
     * @return The ghost state
     */
    public GhostState getState() {
        return state;
    }
}