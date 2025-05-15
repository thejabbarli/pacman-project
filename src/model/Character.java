package model;

// Interface for game characters (Pacman, ghosts)
public interface Character {
    int getRow();
    int getColumn();
    void setPosition(int row, int column);
    void move(Direction direction);
    Direction getCurrentDirection();
}