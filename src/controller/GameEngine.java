package controller;

import model.Direction;
import model.GameMap;
import model.PacmanModel;
import service.MovementManager;
import service.MovementService;
import view.CharacterRenderer;
import view.GameLayeredPane;
import animation.PacmanAnimator;

/**
 * Game engine to orchestrate game logic
 * Follows Single Responsibility Principle by delegating specific tasks to other classes
 */
public class GameEngine {
    private GameMap gameMap;
    private PacmanModel pacman;
    private CharacterRenderer pacmanRenderer;
    private PacmanAnimator pacmanAnimator;
    private MovementManager movementManager;
    private boolean renderersInitialized = false;

    public GameEngine(GameMap gameMap) {
        this.gameMap = gameMap;

        // Create Pacman in the center of the map
        int centerRow = gameMap.getRows() / 2;
        int centerColumn = gameMap.getColumns() / 2;
        this.pacman = new PacmanModel(centerRow, centerColumn);
    }

    public void initializeRenderers(int cellSize) {
        // Create renderers
        pacmanRenderer = new CharacterRenderer(pacman, cellSize);

        // Create and start animators
        pacmanAnimator = new PacmanAnimator(pacman, pacmanRenderer);
        pacmanAnimator.start();

        // Mark as initialized
        renderersInitialized = true;
    }

    /**
     * Initialize the movement manager after the GameLayeredPane is available
     */
    public void initializeMovement(GameLayeredPane gamePane) {
        movementManager = new MovementManager(pacman, gameMap, gamePane);
        movementManager.start();
    }

    /**
     * Change Pacman's direction
     * Note: This doesn't move Pacman immediately, but changes the direction for continuous movement
     */
    public void setPacmanDirection(Direction direction) {
        // Just set the direction, the MovementManager will handle actual movement
        pacman.setDirection(direction);
    }

    /**
     * For immediate movement (used when testing or for special operations)
     */
    public boolean movePacmanImmediate(Direction direction) {
        pacman.setDirection(direction);
        return MovementService.moveEntity(pacman, direction, gameMap.getWalkableCells());
    }

    public void updateRenderers() {
        if (renderersInitialized && pacmanRenderer != null) {
            pacmanRenderer.updateLabelPosition();
        }
    }

    public void updateRendererSize(int cellSize) {
        if (renderersInitialized && pacmanRenderer != null) {
            pacmanRenderer.updateRenderer(cellSize);
        }
    }

    public void stopAll() {
        if (pacmanAnimator != null) {
            pacmanAnimator.stop();
        }
        if (movementManager != null) {
            movementManager.stop();
        }
    }

    public PacmanModel getPacman() {
        return pacman;
    }

    public CharacterRenderer getPacmanRenderer() {
        return pacmanRenderer;
    }

    public boolean areRenderersInitialized() {
        return renderersInitialized;
    }
}