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

/**
 * Main game frame
 */
public class GameFrame extends JFrame {
    private GameLayeredPane gamePane;
    private GameEngine gameEngine;
    private GameMapWithWalls gameMap;
    private char[][] originalMap; // To keep track of the original character map

    /**
     * Create a new game frame
     */
    public GameFrame() {
        initializeFrame();
        initializeGameComponents();
        centerOnScreen();
    }

    /**
     * Initialize the frame properties
     */
    private void initializeFrame() {
        setTitle("Pacman Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);

        // Set background color
        setBackground(Color.BLACK);

        // Remove any default insets
        getRootPane().setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    }

    /**
     * Initialize game components
     */
    private void initializeGameComponents() {
        // Try to load map from file first
        File mapFile = new File("res/maps/map1.txt");

        if (mapFile.exists()) {
            // Load map from file
            MapLoader mapLoader = new MapLoader();
            originalMap = mapLoader.loadMap(mapFile.getPath());
            gameMap = mapLoader.createGameMap(originalMap);

            // Create the game engine
            gameEngine = new GameEngine(gameMap);

            // Add points from map
            mapLoader.addPointsFromMap(gameEngine, originalMap);

            System.out.println("Map loaded from file: " + mapFile.getPath());
        } else {
            // Fall back to generated map
            gameMap = new GameMapWithWalls(25, 25);
            gameMap.generateBasicWalls();
            originalMap = null;
            gameEngine = new GameEngine(gameMap);
            System.out.println("Using generated map (map file not found)");
        }

        // If we have a map file, try to find Pacman's starting position
        if (originalMap != null) {
            setPacmanStartPosition();
        }

        // Create and add the layered pane
        gamePane = new GameLayeredPane(gameMap, gameEngine);
        setContentPane(gamePane);

        // Set character speeds (after movement systems are initialized)
        setCharacterSpeeds();

        // Set size
        setPreferredSize(new Dimension(600, 640)); // Extra room for UI elements
        setMinimumSize(new Dimension(400, 440));
        pack();

        // Add keyboard listener
        addKeyListener(new GameKeyListener(gameEngine, gamePane));
        setFocusable(true);
        requestFocus();

        // Add a timer for game updates (for things not handled by the movement threads)
        createGameTimer();
    }

    /**
     * Set speeds for Pacman and ghosts
     */
    private void setCharacterSpeeds() {
        // Wait a bit to ensure movement systems are initialized
        SwingUtilities.invokeLater(() -> {
            // Set ghost speed to slow (level 2)
            gameEngine.setGhostSpeedLevel(2);

            // Set Pacman speed to medium (level 3)
            gameEngine.setPacmanSpeedLevel(3);

            System.out.println("Pacman speed: " + gameEngine.getPacmanMoveDelay() + "ms");
            System.out.println("Ghost speed: " + gameEngine.getGhostMoveDelay() + "ms");
        });
    }

    /**
     * Create a timer for periodic game updates
     */
    private void createGameTimer() {
        // Update game state 30 times per second
        Timer gameTimer = new Timer(33, e -> {
            // Check for collisions
            gameEngine.checkGhostCollision();

            // Update UI with any game state changes
            gamePane.updateGameState();
        });

        gameTimer.start();
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

    /**
     * Center the frame on the screen
     */
    private void centerOnScreen() {
        // Center the frame on the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        setLocation(x, y);
    }

    /**
     * Cleanup resources when the frame is closed
     */
    @Override
    public void dispose() {
        // Stop all game threads
        if (gameEngine != null) {
            gameEngine.stopAll();
        }
        super.dispose();
    }

    /**
     * Main method to start the game
     */
    public static void main(String[] args) {
        // Use the Event Dispatch Thread for Swing applications
        SwingUtilities.invokeLater(() -> {
            GameFrame game = new GameFrame();
            game.setVisible(true);
        });
    }
}