package main;

import controller.GameEngine;
import controller.GameKeyListener;
import model.GameMapWithWalls;
import view.GameLayeredPane;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    private GameLayeredPane gamePane;
    private GameEngine gameEngine;
    private GameMapWithWalls gameMap;

    public GameFrame() {
        initializeFrame();
        initializeGameComponents();
        centerOnScreen();
    }

    private void initializeFrame() {
        setTitle("Pacman Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);

        // Set background color
        setBackground(Color.BLACK);

        // Remove any default insets
        getRootPane().setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    }

    private void initializeGameComponents() {
        // Initialize game map (25x25 grid)
        gameMap = new GameMapWithWalls(25, 25);

        // Generate walls
        gameMap.generateBasicWalls();

        // Initialize game engine
        gameEngine = new GameEngine(gameMap);

        // Create and add the layered pane
        gamePane = new GameLayeredPane(gameMap, gameEngine);
        setContentPane(gamePane);

        // Set size
        setPreferredSize(new Dimension(600, 640)); // Extra room for UI elements
        setMinimumSize(new Dimension(400, 440));
        pack();

        // Add keyboard listener
        addKeyListener(new GameKeyListener(gameEngine, gamePane));
        setFocusable(true);
        requestFocus();
    }

    private void centerOnScreen() {
        // Center the frame on the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        setLocation(x, y);
    }
}