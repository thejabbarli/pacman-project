package view;

import model.Direction;
import model.Ghost;

import javax.swing.*;
import java.awt.*;

/**
 * Renderer for ghost characters
 */
public class GhostRenderer extends AbstractRenderer {

    private Ghost ghost;

    /**
     * Create a new ghost renderer
     * @param ghost The ghost to render
     * @param cellSize Cell size in pixels
     */
    public GhostRenderer(Ghost ghost, int cellSize) {
        super(cellSize);
        this.ghost = ghost;
        updateImage();
        updatePosition();
    }

    @Override
    public void updateImage() {
        ImageIcon originalIcon = ghost.getCurrentFrame();
        if (originalIcon != null) {
            // Resize the image to fit the cell
            Image resizedImg = resizeImage(originalIcon.getImage());
            renderLabel.setIcon(new ImageIcon(resizedImg));
        }
    }

    @Override
    public void updatePosition() {
        int x = ghost.getColumn() * cellSize;
        int y = ghost.getRow() * cellSize;
        renderLabel.setBounds(x, y, cellSize, cellSize);
    }

    /**
     * Get the ghost model
     * @return The ghost
     */
    public Ghost getGhost() {
        return ghost;
    }

    /**
     * Get the label component
     * @return The JLabel
     */
    public JLabel getGhostLabel() {
        return renderLabel;
    }
}