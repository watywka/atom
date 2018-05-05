package ru.atom.gameservice.tick;


public class GameObject {

    protected int x;
    protected int y;

    protected Field field;

    private boolean alive = true;

    public GameObject(int x, int y, Field field) {
        this.x = x;
        this.y = y;
        this.field = field;
    }

    public GameObject() {
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
