package service;

import model.Direction;
import model.Movable;

// Service class responsible for movement logic
public class MovementService {

    // Calculate new position based on current position, direction, and speed
    public static int[] calculateNewPosition(int row, int column, Direction direction, int speed) {
        int[] newPosition = new int[] {row, column};

        switch (direction) {
            case UP:
                newPosition[0] -= speed;
                break;
            case DOWN:
                newPosition[0] += speed;
                break;
            case LEFT:
                newPosition[1] -= speed;
                break;
            case RIGHT:
                newPosition[1] += speed;
                break;
            case NONE:
                // No movement
                break;
        }

        return newPosition;
    }

    // Move entity if the move is valid
    public static boolean moveEntity(Movable entity, Direction direction, boolean[][] walkableCells) {
        int[] newPosition = calculateNewPosition(
                entity.getRow(),
                entity.getColumn(),
                direction,
                entity.getSpeed()
        );

        // Check if the new position is valid
        if (isValidMove(newPosition[0], newPosition[1], walkableCells)) {
            entity.setPosition(newPosition[0], newPosition[1]);
            entity.setDirection(direction);
            return true;
        }
        return false;
    }

    // Check if a move is valid based on walkable cells
    private static boolean isValidMove(int row, int column, boolean[][] walkableCells) {
        // Check bounds
        if (row < 0 || row >= walkableCells.length ||
                column < 0 || column >= walkableCells[0].length) {
            return false;
        }

        // Check if the cell is walkable
        return walkableCells[row][column];
    }
}