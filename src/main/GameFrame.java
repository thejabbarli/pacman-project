package main;

import controller.GameEngine;
import controller.GameKeyListener;
import model.GameMapWithWalls;
import model.PacmanModel;
import view.GameLayeredPane;
import map.MapLoader;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class GameFrame extends JFrame {
    private GameLayeredPane gamePane;
    private GameEngine gameEngine;
    private GameMapWithWalls gameMap;
    private char[][] originalMap; // To keep track of the original character map

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
        // Try to load map from file first
        File mapFile = new File("res/maps/map1.txt");

        if (mapFile.exists()) {
            // Load map from file
            MapLoader mapLoader = new MapLoader();
            originalMap = mapLoader.loadMap(mapFile.getPath());
            gameMap = mapLoader.createGameMap(originalMap);
            System.out.println("Map loaded from file: " + mapFile.getPath());
        } else {
            // Fall back to generated map
            gameMap = new GameMapWithWalls(25, 25);
            gameMap.generateBasicWalls();
            originalMap = null;
            System.out.println("Using generated map (map file not found)");
        }

        // Initialize game engine
        gameEngine = new GameEngine(gameMap);

        // If we have a map file, try to find Pacman's starting position
        if (originalMap != null) {
            setPacmanStartPosition();
        }

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

    /**
     * Sets Pacman's starting position based on the map
     */
    private void setPacmanStartPosition() {
        // Look for 'o' or 'O' in the map as starting position
        for (int row = 0; row < originalMap.length; row++) {
            for (int col = 0; col < originalMap[row].length; col++) {
                if (originalMap[row][col] == 'o' || originalMap[row][col] == 'O') {
                    // Update Pacman's position
                    PacmanModel pacman = gameEngine.getPacman();
                    pacman.setPosition(row, col);
                    return;
                }
            }
        }

        // If no 'o' or 'O' found, find first walkable position
        for (int row = 0; row < gameMap.getRows(); row++) {
            for (int col = 0; col < gameMap.getColumns(); col++) {
                if (gameMap.isWalkable(row, col)) {
                    PacmanModel pacman = gameEngine.getPacman();
                    pacman.setPosition(row, col);
                    return;
                }
            }
        }
    }

    private void centerOnScreen() {
        // Center the frame on the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        setLocation(x, y);
    }
}