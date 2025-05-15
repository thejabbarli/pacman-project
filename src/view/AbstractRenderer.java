package view;

import model.Collidable;

import javax.swing.*;
import java.awt.*;

/**
 * Abstract base class for all game element renderers
 * Follows the Template Method pattern and DRY principle
 */
public abstract class AbstractRenderer {
    protected JLabel renderLabel;
    protected int cellSize;

    public AbstractRenderer(int cellSize) {
        this.cellSize = cellSize;
        this.renderLabel = new JLabel();
        this.renderLabel.setVisible(true);
    }

    /**
     * Update the visual representation
     */
    public abstract void updateImage();

    /**
     * Update the position of the rendered element
     */
    public abstract void updatePosition();

    /**
     * Update the cell size and refresh the rendering
     */
    public void updateCellSize(int newCellSize) {
        this.cellSize = newCellSize;
        updateImage();
        updatePosition();
    }

    /**
     * Resize an image to fit the current cell size
     */
    protected Image resizeImage(Image originalImage) {
        return originalImage.getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH);
    }

    /**
     * Get the JLabel component for this renderer
     */
    public JLabel getLabel() {
        return renderLabel;
    }
}