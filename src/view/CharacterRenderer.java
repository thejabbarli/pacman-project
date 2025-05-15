package view;

import model.PacmanModel;
import model.Direction;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Responsible for rendering the Pacman character
 * Follows Single Responsibility Principle by focusing only on visual representation
 */
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
            ImageIcon rotatedIcon = rotateIconForDirection(resizedIcon, pacman.getCurrentDirection());

            pacmanLabel.setIcon(rotatedIcon);
        }
    }

    private ImageIcon rotateIconForDirection(ImageIcon icon, Direction direction) {
        if (direction == Direction.NONE || direction == Direction.RIGHT) {
            // Default orientation - no transformation needed
            return icon;
        }

        // Get image from icon
        Image img = icon.getImage();
        BufferedImage bufferedImage = new BufferedImage(
                img.getWidth(null),
                img.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);

        // Draw the image into the buffered image
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();

        // Create a new buffered image for the transformed result
        BufferedImage rotatedImage = null;

        switch (direction) {
            case UP:
                // Rotate 270 degrees (counterclockwise)
                rotatedImage = rotateImage(bufferedImage, 270);
                break;

            case DOWN:
                // Rotate 90 degrees (clockwise)
                rotatedImage = rotateImage(bufferedImage, 90);
                break;

            case LEFT:
                // Flip horizontally
                rotatedImage = flipImageHorizontally(bufferedImage);
                break;

            default:
                // Should never happen since we handle NONE/RIGHT above
                return icon;
        }

        return new ImageIcon(rotatedImage);
    }

    /**
     * Rotates an image by the specified angle in degrees
     */
    private BufferedImage rotateImage(BufferedImage image, int degrees) {
        double rads = Math.toRadians(degrees);
        double sin = Math.abs(Math.sin(rads));
        double cos = Math.abs(Math.cos(rads));

        int width = image.getWidth();
        int height = image.getHeight();

        // Calculate new dimensions after rotation
        int newWidth = (int) Math.floor(width * cos + height * sin);
        int newHeight = (int) Math.floor(height * cos + width * sin);

        // Create a new buffered image for the rotated result
        BufferedImage rotatedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotatedImage.createGraphics();

        // Apply transformations
        AffineTransform transform = new AffineTransform();
        transform.translate((newWidth - width) / 2.0, (newHeight - height) / 2.0);
        transform.rotate(rads, width / 2.0, height / 2.0);

        // Use better quality rendering
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw the original image with transformation
        g2d.drawImage(image, transform, null);
        g2d.dispose();

        return rotatedImage;
    }

    /**
     * Flips an image horizontally
     */
    private BufferedImage flipImageHorizontally(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage flippedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = flippedImage.createGraphics();

        // Flip operation
        g2d.drawImage(image, width, 0, -width, height, null);
        g2d.dispose();

        return flippedImage;
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