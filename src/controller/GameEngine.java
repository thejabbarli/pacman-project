package controller;

import model.Direction;
import model.GameMap;
import model.PacmanModel;
import service.MovementService;
import view.CharacterRenderer;
import animation.PacmanAnimator;

// Game engine to orchestrate game logic
public class GameEngine {
    private GameMap gameMap;
    private PacmanModel pacman;
    private CharacterRenderer pacmanRenderer;
    private PacmanAnimator pacmanAnimator;

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
    }

    public boolean movePacman(Direction direction) {
        // Delegate movement to the movement service
        return MovementService.moveEntity(pacman, direction, gameMap.getWalkableCells());
    }

    public void updateRenderers() {
        pacmanRenderer.updateLabelPosition();
    }

    public void updateRendererSize(int cellSize) {
        pacmanRenderer.updateRenderer(cellSize);
    }

    public void stopAnimations() {
        if (pacmanAnimator != null) {
            pacmanAnimator.stop();
        }
    }

    public PacmanModel getPacman() {
        return pacman;
    }

    public CharacterRenderer getPacmanRenderer() {
        return pacmanRenderer;
    }
}