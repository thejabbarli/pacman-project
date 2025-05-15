package controller;

import view.GameLayeredPane;
import model.Direction;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Handles keyboard input for game control
 * Follows Single Responsibility Principle by focusing only on keyboard handling
 */
public class GameKeyListener extends KeyAdapter {
    private GameEngine gameEngine;
    private GameLayeredPane gamePane;

    public GameKeyListener(GameEngine gameEngine, GameLayeredPane gamePane) {
        this.gameEngine = gameEngine;
        this.gamePane = gamePane;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        Direction direction = Direction.NONE;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                direction = Direction.UP;
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                direction = Direction.DOWN;
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                direction = Direction.LEFT;
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                direction = Direction.RIGHT;
                break;
        }

        if (direction != Direction.NONE) {
            // Just change direction, actual movement is handled by MovementManager
            gameEngine.setPacmanDirection(direction);
        }

        // Check for Ctrl+Shift+Q shortcut (as required in project spec)
        if (e.isControlDown() && e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_Q) {
            // Will implement return to main menu later
            System.out.println("Ctrl+Shift+Q pressed: Return to main menu");
        }
    }
}