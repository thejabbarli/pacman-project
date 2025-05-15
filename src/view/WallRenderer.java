package view;

import model.Wall;

import javax.swing.*;
import java.awt.*;

/**
 * Renderer for wall elements
 * Follows Single Responsibility Principle by focusing only on wall rendering
 */
public class WallRenderer extends AbstractRenderer {
    private Wall wall;
    private static ImageIcon wallImage = null;

    // Static initializer to load wall image once
    static {
        try {
            wallImage = new ImageIcon("res/walls/wallBlue.png");
        } catch (Exception e) {
            System.err.println("Error loading wall image: " + e.getMessage());
        }
    }

    public WallRenderer(Wall wall, int cellSize) {
        super(cellSize);
        this.wall = wall;
        updateImage();
        updatePosition();
    }

    @Override
    public void updateImage() {
        if (wallImage != null) {
            // Resize the image to fit the cell
            Image resizedImg = resizeImage(wallImage.getImage());
            renderLabel.setIcon(new ImageIcon(resizedImg));
        }
    }

    @Override
    public void updatePosition() {
        int x = wall.getColumn() * cellSize;
        int y = wall.getRow() * cellSize;
        renderLabel.setBounds(x, y, cellSize, cellSize);
    }

    public Wall getWall() {
        return wall;
    }
}