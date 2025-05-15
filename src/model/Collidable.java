package model;

/**
 * Interface for game elements that can collide with each other
 * Follows Interface Segregation Principle by defining a focused interface
 */
public interface Collidable {

    boolean collidesWithMap(int row, int column);

    /**
     * Get the row position of this collidable object
     */
    int getRow();

    /**
     * Get the column position of this collidable object
     */
    int getColumn();

    /**
     * Method called when a collision is detected
     * @param other The other collidable object involved in the collision
     */
    void onCollision(Collidable other);
}