package model;

// Interface for position-related functionality
public interface Positionable {
    int getRow();
    int getColumn();
    void setPosition(int row, int column);
}