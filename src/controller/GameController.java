package controller;

import view.GameView;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameController extends KeyAdapter {
    private GameView gameView;

    public GameController(GameView gameView) {
        this.gameView = gameView;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // We'll implement game controls later

        // Check for Ctrl+Shift+Q shortcut as required by project
        if (e.isControlDown() && e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_Q) {
            // Will implement return to main menu functionality later
            System.out.println("Ctrl+Shift+Q pressed: Return to main menu");
        }
    }
}