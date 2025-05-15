package model;

import javax.swing.ImageIcon;
import java.util.ArrayList;

/**
 * Blinky - The red ghost that directly chases Pacman
 */
public class Blinky extends Ghost {

    /**
     * Create a Blinky ghost at the specified position
     * @param initialRow Starting row
     * @param initialColumn Starting column
     */
    public Blinky(int initialRow, int initialColumn) {
        super(initialRow, initialColumn);

        // Blinky is aggressive - short scatter time, long chase time
        this.scatterModeTime = 7000; // 7 seconds in scatter mode
        this.chaseModeTime = 20000;  // 20 seconds in chase mode
    }

    @Override
    protected void loadAnimationFrames() {
        animationFrames = new ArrayList<>();

        try {
            // Load Blinky's animation frames
            // Typically 2-4 frames for animation
            for (int i = 1; i <= 2; i++) {
                String path = "res/ghosts/blinky" + i + ".png";
                ImageIcon icon = new ImageIcon(path);
                animationFrames.add(icon);
            }

            // If no images were loaded, create a placeholder
            if (animationFrames.isEmpty()) {
                // Create a simple red square as placeholder
                java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(
                        20, 20, java.awt.image.BufferedImage.TYPE_INT_ARGB);
                java.awt.Graphics2D g = img.createGraphics();
                g.setColor(java.awt.Color.RED);
                g.fillRect(0, 0, 20, 20);
                g.dispose();

                animationFrames.add(new ImageIcon(img));
            }
        } catch (Exception e) {
            System.err.println("Error loading Blinky images: " + e.getMessage());

            // Create a simple red square as fallback
            java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(
                    20, 20, java.awt.image.BufferedImage.TYPE_INT_ARGB);
            java.awt.Graphics2D g = img.createGraphics();
            g.setColor(java.awt.Color.RED);
            g.fillRect(0, 0, 20, 20);
            g.dispose();

            animationFrames.add(new ImageIcon(img));
        }
    }

    @Override
    protected void setScatterTarget() {
        // Blinky typically targets the top-right corner in scatter mode
        targetRow = 0;
        targetColumn = 25; // Adjust based on your map size
    }

    @Override
    protected void updateTarget(int pacmanRow, int pacmanColumn) {
        if (state == GhostState.CHASE) {
            // Blinky directly targets Pacman's current position
            targetRow = pacmanRow;
            targetColumn = pacmanColumn;
        } else {
            super.updateTarget(pacmanRow, pacmanColumn);
        }
    }

    @Override
    protected int getGhostHouseRow() {
        return 11; // Adjust based on your map
    }

    @Override
    protected int getGhostHouseColumn() {
        return 13; // Adjust based on your map
    }
}