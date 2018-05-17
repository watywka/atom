package ru.atom.gameservice.message;

public enum Direction {
    UP(0,-1),
    DOWN(0,1),
    LEFT(-1, 0),
    RIGTH(1, 0);

    private int x;
    private int y;

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
