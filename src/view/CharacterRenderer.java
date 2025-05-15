package view;

import model.PacmanModel;
import model.Direction;

import javax.swing.*;
import java.awt.*;

// Class responsible only for rendering characters
public class CharacterRenderer {
    private PacmanModel pacman;
    private int cellSize;
    private JLabel pacmanLabel;

    public CharacterRenderer(PacmanModel pacman, int cellSize) {
        this.pacman = pacman;
        this.cellSize = cellSize;
        initializePacmanLabel();
    }

    private void initializePacmanLabel() {
        pacmanLabel = new JLabel();
        updateLabelImage();
        updateLabelPosition();
    }

    public void updateLabelImage() {
        ImageIcon originalIcon = pacman.getCurrentFrame();
        if (originalIcon != null) {
            // Resize the image to fit the cell
            Image img = originalIcon.getImage();
            Image resizedImg = img.getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(resizedImg);

            // Apply rotation based on direction
            resizedIcon = rotateIconForDirection(resizedIcon, pacman.getCurrentDirection());

            pacmanLabel.setIcon(resizedIcon);
        }
    }

    private ImageIcon rotateIconForDirection(ImageIcon icon, Direction direction) {
        // In a full implementation, this would rotate the image based on direction
        return icon;
    }

    public void updateLabelPosition() {
        int x = pacman.getColumn() * cellSize;
        int y = pacman.getRow() * cellSize;
        pacmanLabel.setBounds(x, y, cellSize, cellSize);
    }

    public void updateRenderer(int cellSize) {
        this.cellSize = cellSize;
        updateLabelImage();
        updateLabelPosition();
    }

    public JLabel getPacmanLabel() {
        return pacmanLabel;
    }
}