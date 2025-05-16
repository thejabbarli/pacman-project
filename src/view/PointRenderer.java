package view;

import model.Point;

import javax.swing.*;
import java.awt.*;

/**
 * Renderer for points/dots in the game
 */
public class PointRenderer {

    private Point point;
    private JLabel renderLabel;
    private int cellSize;
    private static ImageIcon pointImage;

    // Static initializer to load the point image once
    static {
        try {
            pointImage = new ImageIcon("res/edibles/point-l.png");

            // If the image couldn't be loaded, create a fallback
            if (pointImage.getIconWidth() <= 0) {
                createFallbackImage();
            }
        } catch (Exception e) {
            System.err.println("Error loading point image: " + e.getMessage());
            createFallbackImage();
        }
    }

    /**
     * Create a fallback image if the real image can't be loaded
     */
    private static void createFallbackImage() {
        // Create a simple yellow dot as fallback
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(
                10, 10, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g = img.createGraphics();
        g.setColor(java.awt.Color.YELLOW);
        g.fillOval(0, 0, 10, 10);
        g.dispose();

        pointImage = new ImageIcon(img);
    }

    /**
     * Create a new point renderer
     * @param point The point to render
     * @param cellSize Cell size in pixels
     */
    public PointRenderer(Point point, int cellSize) {
        this.point = point;
        this.cellSize = cellSize;
        this.renderLabel = new JLabel();
        this.renderLabel.setVisible(true);

        updateImage();
        updatePosition();
    }

    /**
     * Update the point's visual representation
     */
    public void updateImage() {
        if (pointImage != null && !point.isCollected()) {
            // Resize the image to fit the cell (make it smaller than the cell)
            int pointSize = Math.max(cellSize / 3, 5); // Point size is 1/3 of cell size, min 5px
            Image resizedImg = pointImage.getImage().getScaledInstance(pointSize, pointSize, Image.SCALE_SMOOTH);
            renderLabel.setIcon(new ImageIcon(resizedImg));
        } else {
            // If point is collected or image is null, show nothing
            renderLabel.setIcon(null);
        }
    }

    /**
     * Update the point's position
     */
    public void updatePosition() {
        // Calculate position (center the point in the cell)
        int pointSize = Math.max(cellSize / 3, 5);
        int x = point.getColumn() * cellSize + (cellSize - pointSize) / 2;
        int y = point.getRow() * cellSize + (cellSize - pointSize) / 2;

        renderLabel.setBounds(x, y, pointSize, pointSize);
    }

    /**
     * Update the cell size
     * @param newCellSize New cell size in pixels
     */
    public void updateCellSize(int newCellSize) {
        this.cellSize = newCellSize;
        updateImage();
        updatePosition();
    }

    /**
     * Get the point model
     * @return The point
     */
    public Point getPoint() {
        return point;
    }

    /**
     * Get the JLabel component
     * @return The label
     */
    public JLabel getLabel() {
        return renderLabel;
    }
}