package ru.atom.gameservice.tick;


public abstract class GameObject {

    protected int id;

    protected int x;
    protected int y;

    protected boolean alive = true;
    protected Field field;

    public GameObject(int x, int y, Field field) {
        this.id = field.getNextId();
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

    public abstract String toJson();
}
