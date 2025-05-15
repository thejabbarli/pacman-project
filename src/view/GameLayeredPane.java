package view;

import controller.GameEngine;
import model.GameMapWithWalls;
import model.Wall;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Main game panel using JLayeredPane to organize game elements in layers
 * Follows Single Responsibility Principle by focusing on UI organization
 */
public class GameLayeredPane extends JLayeredPane {
    // Layer constants
    public static final Integer GRID_LAYER = 1;
    public static final Integer WALL_LAYER = 2;
    public static final Integer EDIBLE_LAYER = 3;
    public static final Integer CHARACTER_LAYER = 4;
    public static final Integer UI_LAYER = 5;

    private GameMapWithWalls gameMap;
    private GameEngine gameEngine;

    private JPanel gridPanel;
    private JPanel wallPanel;
    private JPanel ediblePanel;
    private JPanel characterPanel;
    private JPanel uiPanel;

    private final int CELL_SIZE = 20; // Default cell size
    private GridRenderer gridRenderer;
    private List<WallRenderer> wallRenderers;
    private boolean initialized = false;

    public GameLayeredPane(GameMapWithWalls gameMap, GameEngine gameEngine) {
        this.gameMap = gameMap;
        this.gameEngine = gameEngine;
        this.wallRenderers = new ArrayList<>();

        setLayout(null); // JLayeredPane uses absolute positioning
        setBackground(Color.BLACK);
        setOpaque(true);

        // Initialize the character renderers first
        gameEngine.initializeRenderers(CELL_SIZE);

        // Now initialize the layers
        initializeLayers();

        // Add characters to layers
        addCharactersToLayers();

        // Add walls to layers
        addWallsToLayers();

        // Initialize movement after everything is set up
        gameEngine.initializeMovement(this);

        // Mark as initialized
        initialized = true;

        // Add resize listener
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeComponents();
            }
        });
    }

    private void initializeLayers() {
        // 1. Grid layer
        gridPanel = new JPanel(null);
        gridPanel.setOpaque(false);
        gridRenderer = new GridRenderer(gameMap.getRows(), gameMap.getColumns(), CELL_SIZE);
        gridPanel.add(gridRenderer);

        // 2. Wall layer
        wallPanel = new JPanel(null);
        wallPanel.setOpaque(false);

        // 3. Edible layer
        ediblePanel = new JPanel(null);
        ediblePanel.setOpaque(false);

        // 4. Character layer
        characterPanel = new JPanel(null);
        characterPanel.setOpaque(false);

        // 5. UI layer
        uiPanel = createUIPanel();

        // Add layers to the layered pane
        add(gridPanel, GRID_LAYER);
        add(wallPanel, WALL_LAYER);
        add(ediblePanel, EDIBLE_LAYER);
        add(characterPanel, CHARACTER_LAYER);
        add(uiPanel, UI_LAYER);

        // Set initial bounds
        int width = getWidth();
        int height = getHeight();
        int uiHeight = 40; // Height of UI panel

        // Set UI panel bounds at the top
        uiPanel.setBounds(0, 0, width, uiHeight);

        // Calculate game area dimensions
        int gameWidth = CELL_SIZE * gameMap.getColumns();
        int gameHeight = CELL_SIZE * gameMap.getRows();

        // Center the game area horizontally
        int gameX = (width - gameWidth) / 2;

        gridPanel.setBounds(gameX, uiHeight, gameWidth, gameHeight);
        wallPanel.setBounds(gameX, uiHeight, gameWidth, gameHeight);
        ediblePanel.setBounds(gameX, uiHeight, gameWidth, gameHeight);
        characterPanel.setBounds(gameX, uiHeight, gameWidth, gameHeight);

        // Set grid renderer bounds
        gridRenderer.setBounds(0, 0, gameWidth, gameHeight);
    }

    private JPanel createUIPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBackground(Color.BLACK);

        // Add score, time, and lives labels
        Font statsFont = new Font("Arial", Font.BOLD, 16);

        JLabel scoreLabel = new JLabel("Score: 0");
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(statsFont);

        JLabel timeLabel = new JLabel("Time: 0");
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setFont(statsFont);

        JLabel livesLabel = new JLabel("Lives: 3");
        livesLabel.setForeground(Color.WHITE);
        livesLabel.setFont(statsFont);

        panel.add(scoreLabel);
        panel.add(Box.createHorizontalStrut(20));
        panel.add(timeLabel);
        panel.add(Box.createHorizontalStrut(20));
        panel.add(livesLabel);

        return panel;
    }

    private void addCharactersToLayers() {
        // Add Pacman to the character layer
        CharacterRenderer pacmanRenderer = gameEngine.getPacmanRenderer();
        if (pacmanRenderer != null) {
            characterPanel.add(pacmanRenderer.getPacmanLabel());
        }
    }

    private void addWallsToLayers() {
        // Create wall renderers
        for (Wall wall : gameMap.getWalls()) {
            WallRenderer renderer = new WallRenderer(wall, CELL_SIZE);
            wallRenderers.add(renderer);
            wallPanel.add(renderer.getLabel());
        }
    }

    private void resizeComponents() {
        int width = getWidth();
        int height = getHeight();
        int uiHeight = 40; // Height of UI panel

        // Set UI panel bounds at the top
        uiPanel.setBounds(0, 0, width, uiHeight);

        // Calculate game area size
        int gameAreaHeight = height - uiHeight;

        // Calculate cell size to fit the grid
        int cellSizeByWidth = width / gameMap.getColumns();
        int cellSizeByHeight = gameAreaHeight / gameMap.getRows();
        int newCellSize = Math.max(Math.min(cellSizeByWidth, cellSizeByHeight), 5); // At least 5px

        // Update grid renderer
        gridRenderer.updateCellSize(newCellSize);

        // Calculate new game dimensions
        int gameWidth = newCellSize * gameMap.getColumns();
        int gameHeight = newCellSize * gameMap.getRows();

        // Center the game area horizontally
        int gameX = (width - gameWidth) / 2;

        // Set bounds for all layers
        gridPanel.setBounds(gameX, uiHeight, gameWidth, gameHeight);
        wallPanel.setBounds(gameX, uiHeight, gameWidth, gameHeight);
        ediblePanel.setBounds(gameX, uiHeight, gameWidth, gameHeight);
        characterPanel.setBounds(gameX, uiHeight, gameWidth, gameHeight);

        // Set grid renderer bounds
        gridRenderer.setBounds(0, 0, gameWidth, gameHeight);

        // Update character renderers
        gameEngine.updateRendererSize(newCellSize);

        // Update wall renderers
        for (WallRenderer wallRenderer : wallRenderers) {
            wallRenderer.updateCellSize(newCellSize);
        }

        // Update characters
        gameEngine.updateRenderers();
    }

    @Override
    public Dimension getPreferredSize() {
        // Calculate preferred size based on grid size and cell size
        int width = gameMap.getColumns() * CELL_SIZE;
        int height = gameMap.getRows() * CELL_SIZE + 40; // Add space for UI
        return new Dimension(width, height);
    }

    public void updateGameState() {
        // Update character positions
        gameEngine.updateRenderers();
        repaint();
    }
}