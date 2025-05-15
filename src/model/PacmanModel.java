package model;

import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;

// Pacman model only responsible for storing Pacman's state
public class PacmanModel implements Movable {
    private int row;
    private int column;
    private Direction currentDirection;
    private int speed;
    private List<ImageIcon> animationFrames;
    private int currentFrame;

    public PacmanModel(int initialRow, int initialColumn) {
        this.row = initialRow;
        this.column = initialColumn;
        this.currentDirection = Direction.NONE;
        this.speed = 1;
        this.currentFrame = 0;
        loadAnimationFrames();
    }

    private void loadAnimationFrames() {
        animationFrames = new ArrayList<>();

        // Load the three animation frames
        try {
            for (int i = 1; i <= 3; i++) {
                String path = "res/pacman/pacman" + i + ".png";
                ImageIcon icon = new ImageIcon(path);
                animationFrames.add(icon);
            }
        } catch (Exception e) {
            System.err.println("Error loading Pacman images: " + e.getMessage());
        }
    }

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getColumn() {
        return column;
    }

    @Override
    public void setPosition(int row, int column) {
        this.row = row;
        this.column = column;
    }

    @Override
    public Direction getCurrentDirection() {
        return currentDirection;
    }

    @Override
    public void setDirection(Direction direction) {
        this.currentDirection = direction;
    }

    @Override
    public int getSpeed() {
        return speed;
    }

    @Override
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public ImageIcon getCurrentFrame() {
        return animationFrames.get(currentFrame);
    }

    public void nextFrame() {
        currentFrame = (currentFrame + 1) % animationFrames.size();
    }

    public List<ImageIcon> getAnimationFrames() {
        return animationFrames;
    }
}