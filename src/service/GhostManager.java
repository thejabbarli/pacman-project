package service;

import model.Blinky;
import model.Direction;
import model.GameMapWithWalls;
import model.PacmanModel;
import view.GameLayeredPane;

/**
 * Simple service for managing ghost movement
 */
public class GhostManager implements Runnable {

    private final Blinky ghost;
    private final GameMapWithWalls gameMap;
    private final PacmanModel pacman;
    private final GameLayeredPane gamePane;

    private volatile boolean running;
    private Thread movementThread;

    /**
     * Create a new ghost manager
     * @param ghost The ghost to manage
     * @param gameMap The game map
     * @param pacman The pacman model
     * @param gamePane The game pane
     */
    public GhostManager(Blinky ghost, GameMapWithWalls gameMap, PacmanModel pacman, GameLayeredPane gamePane) {
        this.ghost = ghost;
        this.gameMap = gameMap;
        this.pacman = pacman;
        this.gamePane = gamePane;
        this.running = false;
    }

    /**
     * Start the ghost movement thread
     */
    public void start() {
        if (!running) {
            running = true;
            movementThread = new Thread(this);
            movementThread.start();
        }
    }

    /**
     * Stop the ghost movement thread
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
                // Calculate direction toward Pacman
                Direction nextDirection = ghost.calculateNextMove(
                        pacman.getRow(),
                        pacman.getColumn(),
                        gameMap
                );

                // Set ghost direction
                ghost.setDirection(nextDirection);

                // Move ghost
                if (nextDirection != Direction.NONE) {
                    MovementService.moveEntity(
                            ghost,
                            nextDirection,
                            gameMap.getWalkableCells()
                    );

                    // Update animation
                    ghost.nextFrame();
                }

                // Check for collision with Pacman
                if (ghost.getRow() == pacman.getRow() && ghost.getColumn() == pacman.getColumn()) {
                    // In a real game, you would handle this collision (e.g., lose a life)
                    System.out.println("Ghost caught Pacman!");
                }

                // Update the UI
                gamePane.updateGameState();

                // Wait before next movement based on ghost's speed
                Thread.sleep(ghost.getMoveDelay());

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}