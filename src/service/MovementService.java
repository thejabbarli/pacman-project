package service;

import model.Collidable;
import model.Direction;
import model.Movable;

import java.util.List;

/**
 * Service class responsible for movement logic
 * Follows Single Responsibility Principle by focusing only on movement calculations
 */
public class MovementService {

    /**
     * Calculate new position based on current position, direction, and speed
     */
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

    /**
     * Move entity if the move is valid
     * @param entity The entity to move
     * @param direction The direction to move in
     * @param walkableCells Map of walkable cells
     * @return true if movement was successful
     */
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
            return true;
        }
        return false;
    }

    /**
     * Move entity with collision detection
     * @param entity The entity to move
     * @param direction The direction to move in
     * @param walkableCells Map of walkable cells
     * @param obstacles List of obstacles to check for collisions
     * @return true if movement was successful
     */
    public static boolean moveEntityWithCollision(Movable entity, Direction direction,
                                                  boolean[][] walkableCells,
                                                  List<? extends Collidable> obstacles) {
        int[] newPosition = calculateNewPosition(
                entity.getRow(),
                entity.getColumn(),
                direction,
                entity.getSpeed()
        );

        // Check if the new position is valid
        if (isValidMove(newPosition[0], newPosition[1], walkableCells)) {
            // Check for collisions with obstacles
            if (!CollisionService.wouldCollide(newPosition[0], newPosition[1], obstacles)) {
                entity.setPosition(newPosition[0], newPosition[1]);
                return true;
            }
        }
        return false;
    }

    /**
     * Check if a move is valid based on walkable cells
     */
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