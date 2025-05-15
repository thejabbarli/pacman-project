package controller;

import model.Direction;
import model.GameMapWithWalls;
import model.PacmanModel;
import model.Ghost;
import model.Blinky;
import service.MovementManager;
import service.MovementService;
import service.GhostManager;
import view.CharacterRenderer;
import view.GhostRenderer;
import view.GameLayeredPane;
import animation.PacmanAnimator;

import java.util.ArrayList;
import java.util.List;

/**
 * Game engine to orchestrate game logic
 * Updated to include ghost management
 */
public class GameEngine {
    private GameMapWithWalls gameMap;
    private PacmanModel pacman;
    private CharacterRenderer pacmanRenderer;
    private PacmanAnimator pacmanAnimator;
    private MovementManager movementManager;
    private GhostManager ghostManager;
    private List<Ghost> ghosts;
    private List<GhostRenderer> ghostRenderers;
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

        // Initialize ghosts
        this.ghosts = new ArrayList<>();
        this.ghostRenderers = new ArrayList<>();

        // Create ghosts at default positions
        createGhosts();
    }

    /**
     * Create ghost characters
     */
    private void createGhosts() {
        // Create Blinky (top-right area)
        Blinky blinky = new Blinky(1, gameMap.getColumns() - 2);
        ghosts.add(blinky);

        // You can add more ghost types here later
    }

    /**
     * Initialize renderers for all game characters
     * @param cellSize Cell size in pixels
     */
    public void initializeRenderers(int cellSize) {
        // Create Pacman renderer
        pacmanRenderer = new CharacterRenderer(pacman, cellSize);

        // Create ghost renderers
        ghostRenderers.clear();
        for (Ghost ghost : ghosts) {
            GhostRenderer renderer = new GhostRenderer(ghost, cellSize);
            ghostRenderers.add(renderer);
        }

        // Create and start animators
        pacmanAnimator = new PacmanAnimator(pacman, pacmanRenderer);
        pacmanAnimator.start();

        // Mark as initialized
        renderersInitialized = true;
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
        ghostManager = new GhostManager(gameMap, pacman, gamePane);
        for (Ghost ghost : ghosts) {
            ghostManager.addGhost(ghost);
        }
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
     * Update all renderers
     */
    public void updateRenderers() {
        if (renderersInitialized) {
            // Update Pacman renderer
            if (pacmanRenderer != null) {
                pacmanRenderer.updatePosition();
            }

            // Update ghost renderers
            for (GhostRenderer renderer : ghostRenderers) {
                renderer.updatePosition();
                renderer.updateImage();
            }
        }
    }

    /**
     * Update renderer size
     * @param cellSize New cell size in pixels
     */
    public void updateRendererSize(int cellSize) {
        if (renderersInitialized) {
            // Update Pacman renderer
            if (pacmanRenderer != null) {
                pacmanRenderer.updateCellSize(cellSize);
            }

            // Update ghost renderers
            for (GhostRenderer renderer : ghostRenderers) {
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
     * Get all ghost renderers
     * @return List of ghost renderers
     */
    public List<GhostRenderer> getGhostRenderers() {
        return ghostRenderers;
    }

    /**
     * Get all ghosts
     * @return List of ghosts
     */
    public List<Ghost> getGhosts() {
        return ghosts;
    }

    /**
     * Check if renderers are initialized
     * @return true if renderers are initialized
     */
    public boolean areRenderersInitialized() {
        return renderersInitialized;
    }
}