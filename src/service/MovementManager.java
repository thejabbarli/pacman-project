package service;

import model.Direction;
import model.GameMapWithWalls;
import model.Movable;
import view.GameLayeredPane;

/**
 * MovementManager - Responsible for continuous movement logic
 * Follows Single Responsibility Principle by focusing only on movement execution
 */
public class MovementManager implements Runnable {
    private final Movable entity;
    private final GameMapWithWalls gameMap;
    private final GameLayeredPane gamePane;
    private volatile boolean running;
    private Thread movementThread;
    private final int MOVEMENT_DELAY_MS = 200; // Movement speed (lower = faster)

    public MovementManager(Movable entity, GameMapWithWalls gameMap, GameLayeredPane gamePane) {
        this.entity = entity;
        this.gameMap = gameMap;
        this.gamePane = gamePane;
        this.running = false;
    }

    public void start() {
        if (!running) {
            running = true;
            movementThread = new Thread(this);
            movementThread.start();
        }
    }

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
                // Move entity in current direction
                Direction currentDirection = entity.getCurrentDirection();
                if (currentDirection != Direction.NONE) {
                    boolean moved = MovementService.moveEntity(
                            entity,
                            currentDirection,
                            gameMap.getWalkableCells()
                    );

                    // Update the UI
                    if (moved) {
                        gamePane.updateGameState();
                    }
                }

                // Sleep before next movement
                Thread.sleep(MOVEMENT_DELAY_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}