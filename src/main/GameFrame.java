package main;

import view.GameView;
import controller.GameController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class GameFrame extends JFrame {
    private GameView gameView;
    private GameController gameController;

    public GameFrame() {
        setTitle("Pacman Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a grid according to project requirements (10-100 rows/columns)
        int rows = 20;
        int columns = 20;

        // Create the game view
        gameView = new GameView(rows, columns);

        // Create the game controller
        gameController = new GameController(gameView);

        // Set up the frame
        setContentPane(gameView);
        setBackground(Color.BLACK);

        // Set initial size larger
        setSize(800, 600);
        setLocationRelativeTo(null); // Center the frame

        // Allow resizing
        setResizable(true);

        // Register key listener
        addKeyListener(gameController);
        setFocusable(true);

        // Add component listener to handle resize events
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Trigger resize in the view
                gameView.revalidate();
                gameView.repaint();
            }
        });
    }
}