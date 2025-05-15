package service;

import model.Direction;
import model.GameMapWithWalls;
import model.Ghost;
import model.PacmanModel;
import view.GameLayeredPane;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Service for managing ghost AI and movement
 */
public class GhostManager implements Runnable {

    private final List<Ghost> ghosts;
    private final GameMapWithWalls gameMap;
    private final PacmanModel pacman;
    private final GameLayeredPane gamePane;

    private volatile boolean running;
    private Thread movementThread;

    // Timing constants
    private static final int MOVEMENT_DELAY_MS = 250; // Ghost movement speed (ms)
    private static final int MODE_SWITCH_INTERVAL = 15000; // Time between mode switches (ms)

    private long lastModeSwitchTime;

    /**
     * Create a GhostManager
     * @param gameMap The game map
     * @param pacman Pacman model
     * @param gamePane Game UI pane
     */
    public GhostManager(GameMapWithWalls gameMap, PacmanModel pacman, GameLayeredPane gamePane) {
        this.ghosts = new CopyOnWriteArrayList<>(); // Thread-safe list
        this.gameMap = gameMap;
        this.pacman = pacman;
        this.gamePane = gamePane;
        this.running = false;
        this.lastModeSwitchTime = System.currentTimeMillis();
    }

    /**
     * Add a ghost to be managed
     * @param ghost The ghost to add
     */
    public void addGhost(Ghost ghost) {
        ghosts.add(ghost);
    }

    /**
     * Start the ghost management thread
     */
    public void start() {
        if (!running) {
            running = true;
            movementThread = new Thread(this);
            movementThread.start();
        }
    }

    /**
     * Stop the ghost management thread
     */
    public void stop() {
        running = false;
        if (movementThread != null) {
            movementThread.interrupt();
        }
    }

    @Override
    public void run() {
        while (running) {
            try {
                // Check if it's time to switch modes
                checkModeSwitching();

                // Move each ghost
                moveGhosts();

                // Check collisions
                checkCollisions();

                // Update the UI
                gamePane.updateGameState();

                // Sleep before next movement
                Thread.sleep(MOVEMENT_DELAY_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    /**
     * Check if it's time to switch ghost modes between chase and scatter
     */
    private void checkModeSwitching() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastModeSwitchTime > MODE_SWITCH_INTERVAL) {
            // Toggle ghost modes
            for (Ghost ghost : ghosts) {
                // Only toggle if not frightened or eaten
                if (ghost.getState() != Ghost.GhostState.FRIGHTENED &&
                        ghost.getState() != Ghost.GhostState.EATEN) {
                    ghost.toggleChaseScatter();
                }
            }
            lastModeSwitchTime = currentTime;
        }
    }

    /**
     * Move all ghosts according to their AI
     */
    private void moveGhosts() {
        for (Ghost ghost : ghosts) {
            // Calculate next direction based on ghost AI
            Direction nextDirection = ghost.calculateNextMove(
                    gameMap,
                    pacman.getRow(),
                    pacman.getColumn()
            );

            // Apply the direction
            ghost.setDirection(nextDirection);

            // Move the ghost
            if (nextDirection != Direction.NONE) {
                boolean moved = MovementService.moveEntity(
                        ghost,
                        nextDirection,
                        gameMap.getWalkableCells()
                );

                // If couldn't move, try another direction
                if (!moved) {
                    // Get a random valid direction if stuck
                    Direction randomDirection = getRandomValidDirection(ghost);
                    ghost.setDirection(randomDirection);
                    MovementService.moveEntity(
                            ghost,
                            randomDirection,
                            gameMap.getWalkableCells()
                    );
                }

                // Update animation
                ghost.nextFrame();
            }
        }
    }

    /**
     * Get a random valid direction for a ghost that is stuck
     * @param ghost The ghost
     * @return A random valid direction
     */
    private Direction getRandomValidDirection(Ghost ghost) {
        // Try each direction randomly until a valid one is found
        Direction[] directions = {Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};

        // Shuffle the directions
        for (int i = 0; i < directions.length; i++) {
            int randomIndex = (int) (Math.random() * directions.length);
            Direction temp = directions[i];
            directions[i] = directions[randomIndex];
            directions[randomIndex] = temp;
        }

        // Try each direction
        for (Direction dir : directions) {
            int newRow = ghost.getRow();
            int newCol = ghost.getColumn();

            switch (dir) {
                case UP: newRow--; break;
                case DOWN: newRow++; break;
                case LEFT: newCol--; break;
                case RIGHT: newCol++; break;
                case NONE: break;
            }

            // Check if valid
            if (newRow >= 0 && newRow < gameMap.getRows() &&
                    newCol >= 0 && newCol < gameMap.getColumns() &&
                    gameMap.isWalkable(newRow, newCol)) {
                return dir;
            }
        }

        // If all else fails
        return Direction.NONE;
    }

    /**
     * Check collisions between ghosts and Pacman
     */
    private void checkCollisions() {
        for (Ghost ghost : ghosts) {
            if (ghost.getRow() == pacman.getRow() && ghost.getColumn() == pacman.getColumn()) {
                // Collision occurred
                handleGhostPacmanCollision(ghost);
            }
        }
    }

    /**
     * Handle collision between a ghost and Pacman
     * @param ghost The ghost involved in the collision
     */
    private void handleGhostPacmanCollision(Ghost ghost) {
        switch (ghost.getState()) {
            case FRIGHTENED:
                // Ghost is eaten
                ghost.setState(Ghost.GhostState.EATEN);
                // Trigger score increase
                break;

            case EATEN:
                // Ghost is returning to house, no effect
                break;

            default: // CHASE or SCATTER
                // Pacman loses a life
                // This would be handled by the Game Controller
                break;
        }
    }

    /**
     * Get all managed ghosts
     * @return List of ghosts
     */
    public List<Ghost> getGhosts() {
        return new ArrayList<>(ghosts);
    }

    /**
     * Make all ghosts frightened (when power pellet is eaten)
     * @param duration Duration of frightened state in milliseconds
     */
    public void frightenGhosts(int duration) {
        for (Ghost ghost : ghosts) {
            ghost.setFrightened();
        }

        // Schedule a task to end frightened mode after duration
        new Thread(() -> {
            try {
                Thread.sleep(duration);
                endFrightenedMode();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    /**
     * End frightened mode for all ghosts
     */
    private void endFrightenedMode() {
        for (Ghost ghost : ghosts) {
            if (ghost.getState() == Ghost.GhostState.FRIGHTENED) {
                ghost.setState(Ghost.GhostState.CHASE);
            }
        }
    }
}