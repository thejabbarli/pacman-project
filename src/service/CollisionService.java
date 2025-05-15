package service;

import model.Collidable;

import java.util.List;

/**
 * Service for handling collisions between game objects
 * Follows Single Responsibility Principle by focusing only on collision detection and handling
 */
public class CollisionService {

    /**
     * Check for collisions between a single object and a list of objects
     * @param object The object to check for collisions
     * @param objects List of potential objects to collide with
     * @return true if a collision was detected and handled
     */
    public static boolean checkCollisions(Collidable object, List<? extends Collidable> objects) {
        for (Collidable other : objects) {
            // Skip self-collision
            if (object == other) {
                continue;
            }

            // Check for collision
            if (object.collidesWithMap(other.getRow(), other.getColumn())) {
                // Handle collision
                object.onCollision(other);
                other.onCollision(object);
                return true;
            }
        }
        return false;
    }

    /**
     * Predict if moving to a position would cause a collision
     * @param row Target row position
     * @param column Target column position
     * @param objects List of objects to check against
     * @return true if collision would occur
     */
    public static boolean wouldCollide(int row, int column, List<? extends Collidable> objects) {
        for (Collidable object : objects) {
            if (object.collidesWithMap(row, column)) {
                return true;
            }
        }
        return false;
    }
}