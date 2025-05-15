package main;

import view.GameView;
import view.GameDimensions;
import controller.GameController;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    private GameView gameView;
    private GameController gameController;

    public GameFrame() {
        initializeFrame();
        setupGameComponents();
        setupEventHandling();
        pack(); // Pack components to preferred size
    }

    private void initializeFrame() {
        setTitle("Pacman Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);

        // Ensure all panels have black background
        setBackground(Color.BLACK);

        // Set minimum size
        setMinimumSize(GameDimensions.getMinimumWindowSize());

        // Remove all default margins
        JPanel contentPane = new JPanel(new BorderLayout(0, 0));
        contentPane.setBackground(Color.BLACK);
        contentPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        setContentPane(contentPane);

        // Remove any insets or borders
        getRootPane().setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        ((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    }

    private void setupGameComponents() {
        // Create a grid
        int rows = 25;
        int columns = 25;

        // Create the game view
        gameView = new GameView(rows, columns);

        // Add view to content pane with no gaps
        getContentPane().add(gameView, BorderLayout.CENTER);
    }

    private void setupEventHandling() {
        // Create the game controller
        gameController = new GameController(gameView);

        // Register key listener
        addKeyListener(gameController);
        setFocusable(true);
    }
}