package controller;

import model.*;
import model.Point;
import service.GhostManager;
import service.MovementManager;
import service.MovementService;
import view.CharacterRenderer;
import view.GameLayeredPane;
import view.GhostRenderer;
import view.PointRenderer;
import animation.PacmanAnimator;

import java.util.ArrayList;
import java.util.List;

/**
 * Game engine to orchestrate game logic
 */
public class GameEngine {
    private GameMapWithWalls gameMap;
    private PacmanModel pacman;
    private Blinky ghost;
    private CharacterRenderer pacmanRenderer;
    private GhostRenderer ghostRenderer;
    private PacmanAnimator pacmanAnimator;
    private MovementManager movementManager;
    private GhostManager ghostManager;
    private List<Point> points;
    private List<PointRenderer> pointRenderers;
    private int score = 0;
    private int lives = 3;
    private boolean renderersInitialized = false;

    /**
     * Create a new game engine
     * @param gameMap The game map
     */
    public GameEngine(GameMapWithWalls gameMap) {
        this.gameMap = gameMap;

        // Create Pacman in the center of the map
        int centerRow = gameMap.getRows() / 2;
        int centerColumn = gameMap.getColumns() / 2;
        this.pacman = new PacmanModel(centerRow, centerColumn);

        // Create ghost at the top-right corner
        this.ghost = new Blinky(1, gameMap.getColumns() - 2);

        // Initialize points list
        this.points = new ArrayList<>();
        this.pointRenderers = new ArrayList<>();
    }

    /**
     * Initialize renderers for game characters
     * @param cellSize Cell size in pixels
     */
    public void initializeRenderers(int cellSize) {
        // Create renderers
        pacmanRenderer = new CharacterRenderer(pacman, cellSize);
        ghostRenderer = new GhostRenderer(ghost, cellSize);

        // Create and start animators
        pacmanAnimator = new PacmanAnimator(pacman, pacmanRenderer);
        pacmanAnimator.start();

        // Mark as initialized
        renderersInitialized = true;
    }

    /**
     * Initialize point renderers
     * @param cellSize Cell size in pixels
     */
    public void initializePointRenderers(int cellSize) {
        pointRenderers.clear();
        for (Point point : points) {
            PointRenderer renderer = new PointRenderer(point, cellSize);
            pointRenderers.add(renderer);
        }
    }

    /**
     * Initialize movement systems
     * @param gamePane Game layered pane
     */
    public void initializeMovement(GameLayeredPane gamePane) {
        // Initialize Pacman movement
        movementManager = new MovementManager(pacman, gameMap, gamePane);
        movementManager.start();

        // Initialize ghost movement
        ghostManager = new GhostManager(ghost, gameMap, pacman, gamePane);
        ghostManager.start();
    }

    /**
     * Set Pacman's direction
     * @param direction The direction
     */
    public void setPacmanDirection(Direction direction) {
        // Just set the direction, the MovementManager will handle actual movement
        pacman.setDirection(direction);
    }

    /**
     * Move Pacman immediately in the given direction
     * @param direction The direction
     * @return true if movement was successful
     */
    public boolean movePacmanImmediate(Direction direction) {
        pacman.setDirection(direction);
        return MovementService.moveEntity(pacman, direction, gameMap.getWalkableCells());
    }

    /**
     * Add a point to the game
     * @param row Row position
     * @param column Column position
     */
    public void addPoint(int row, int column) {
        Point point = new Point(row, column);
        points.add(point);
    }

    /**
     * Check if Pacman has collected any points
     */
    public void checkPointCollection() {
        for (Point point : points) {
            if (!point.isCollected() &&
                    point.getRow() == pacman.getRow() &&
                    point.getColumn() == pacman.getColumn()) {
                // Collect the point
                point.collect();
                // Increase score
                score += 10;
                // Update UI
                updatePointRenderers();
            }
        }
    }

    /**
     * Check if Pacman collides with the ghost
     */
    public void checkGhostCollision() {
        if (ghost.getRow() == pacman.getRow() && ghost.getColumn() == pacman.getColumn()) {
            // Lose a life
            lives--;

            // Reset positions
            if (lives > 0) {
                resetPositions();
            } else {
                // Game over
                // This would be handled by a game over screen or similar
                System.out.println("Game Over!");
            }
        }
    }

    /**
     * Reset character positions after losing a life
     */
    private void resetPositions() {
        // Reset Pacman to center
        int centerRow = gameMap.getRows() / 2;
        int centerColumn = gameMap.getColumns() / 2;
        pacman.setPosition(centerRow, centerColumn);

        // Reset ghost to starting position
        ghost.setPosition(1, gameMap.getColumns() - 2);
    }

    /**
     * Update point renderers
     */
    public void updatePointRenderers() {
        for (PointRenderer renderer : pointRenderers) {
            renderer.updateImage();
        }
    }

    /**
     * Update all renderers
     */
    public void updateRenderers() {
        if (renderersInitialized) {
            if (pacmanRenderer != null) {
                pacmanRenderer.updatePosition();
            }
            if (ghostRenderer != null) {
                ghostRenderer.updatePosition();
                ghostRenderer.updateImage();
            }
        }
    }

    /**
     * Update renderer size
     * @param cellSize New cell size in pixels
     */
    public void updateRendererSize(int cellSize) {
        if (renderersInitialized) {
            if (pacmanRenderer != null) {
                pacmanRenderer.updateCellSize(cellSize);
            }
            if (ghostRenderer != null) {
                ghostRenderer.updateCellSize(cellSize);
            }
            for (PointRenderer renderer : pointRenderers) {
                renderer.updateCellSize(cellSize);
            }
        }
    }

    /**
     * Stop all game threads
     */
    public void stopAll() {
        if (pacmanAnimator != null) {
            pacmanAnimator.stop();
        }
        if (movementManager != null) {
            movementManager.stop();
        }
        if (ghostManager != null) {
            ghostManager.stop();
        }
    }

    /**
     * Set ghost's movement delay (higher = slower)
     * @param delay The movement delay in milliseconds
     */
    public void setGhostMoveDelay(int delay) {
        if (ghost != null) {
            ghost.setMoveDelay(delay);
        }
    }

    /**
     * Get ghost's movement delay
     * @return The movement delay in milliseconds
     */
    public int getGhostMoveDelay() {
        if (ghost != null) {
            return ghost.getMoveDelay();
        }
        return 0;
    }

    /**
     * Set ghost's speed level
     * @param level Speed level (1-5, where 1 is slowest, 5 is fastest)
     */
    public void setGhostSpeedLevel(int level) {
        if (ghost != null) {
            // Map level 1-5 to delay values
            int delay;
            switch (level) {
                case 1: delay = 800; break; // Very slow
                case 2: delay = 600; break; // Slow
                case 3: delay = 400; break; // Medium
                case 4: delay = 250; break; // Fast
                case 5: delay = 150; break; // Very fast
                default: delay = 400; break; // Default medium speed
            }
            ghost.setMoveDelay(delay);
        }
    }

    /**
     * Set Pacman's movement delay (higher = slower)
     * @param delay The movement delay in milliseconds
     */
    public void setPacmanMoveDelay(int delay) {
        if (movementManager != null) {
            movementManager.setMovementDelay(delay);
        }
    }

    /**
     * Get Pacman's movement delay
     * @return The movement delay in milliseconds
     */
    public int getPacmanMoveDelay() {
        if (movementManager != null) {
            return movementManager.getMovementDelay();
        }
        return 0;
    }

    /**
     * Set Pacman's speed level
     * @param level Speed level (1-5, where 1 is slowest, 5 is fastest)
     */
    public void setPacmanSpeedLevel(int level) {
        if (movementManager != null) {
            // Map level 1-5 to delay values
            int delay;
            switch (level) {
                case 1: delay = 300; break; // Very slow
                case 2: delay = 200; break; // Slow
                case 3: delay = 150; break; // Medium
                case 4: delay = 100; break; // Fast
                case 5: delay = 70; break;  // Very fast
                default: delay = 200; break; // Default medium speed
            }
            movementManager.setMovementDelay(delay);
        }
    }

    /**
     * Get the Pacman model
     * @return The Pacman model
     */
    public PacmanModel getPacman() {
        return pacman;
    }

    /**
     * Get the Pacman renderer
     * @return The Pacman renderer
     */
    public CharacterRenderer getPacmanRenderer() {
        return pacmanRenderer;
    }

    /**
     * Get the ghost
     * @return The ghost
     */
    public Blinky getGhost() {
        return ghost;
    }

    /**
     * Get the ghost renderer
     * @return The ghost renderer
     */
    public GhostRenderer getGhostRenderer() {
        return ghostRenderer;
    }

    /**
     * Get point renderers
     * @return List of point renderers
     */
    public List<PointRenderer> getPointRenderers() {
        return pointRenderers;
    }

    /**
     * Get current score
     * @return The score
     */
    public int getScore() {
        return score;
    }

    /**
     * Get remaining lives
     * @return Number of lives
     */
    public int getLives() {
        return lives;
    }

    /**
     * Check if renderers are initialized
     * @return true if renderers are initialized
     */
    public boolean areRenderersInitialized() {
        return renderersInitialized;
    }
}