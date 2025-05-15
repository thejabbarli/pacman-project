package controller;

import model.Direction;
import model.GameMapWithWalls;
import model.PacmanModel;
import service.MovementManager;
import service.MovementService;
import view.CharacterRenderer;
import view.GameLayeredPane;
import animation.PacmanAnimator;

/**
 * Game engine to orchestrate game logic
 * Updated to work with GameMapWithWalls
 */
public class GameEngine {
    private GameMapWithWalls gameMap;
    private PacmanModel pacman;
    private CharacterRenderer pacmanRenderer;
    private PacmanAnimator pacmanAnimator;
    private MovementManager movementManager;
    private boolean renderersInitialized = false;

    public GameEngine(GameMapWithWalls gameMap) {
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

    public void initializeMovement(GameLayeredPane gamePane) {
        movementManager = new MovementManager(pacman, gameMap, gamePane);
        movementManager.start();
    }

    public void setPacmanDirection(Direction direction) {
        // Just set the direction, the MovementManager will handle actual movement
        pacman.setDirection(direction);
    }

    public boolean movePacmanImmediate(Direction direction) {
        pacman.setDirection(direction);
        return MovementService.moveEntity(pacman, direction, gameMap.getWalkableCells());
    }

    public void updateRenderers() {
        if (renderersInitialized && pacmanRenderer != null) {
            // Use the abstract method name instead of the old method name
            pacmanRenderer.updatePosition();
        }
    }

    public void updateRendererSize(int cellSize) {
        if (renderersInitialized && pacmanRenderer != null) {
            pacmanRenderer.updateCellSize(cellSize);
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