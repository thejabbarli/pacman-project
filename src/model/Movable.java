package model;

// Interface for movement-related functionality
public interface Movable extends Positionable {
    Direction getCurrentDirection();
    void setDirection(Direction direction);
    int getSpeed();
    void setSpeed(int speed);
}