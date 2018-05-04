package ru.atom.gameservice.tick;

public class Player extends GameObject implements Tickable{

    private final String name;
    private int velX;
    private int velY;
    protected int radius = 1;


    public Player(int x, int y, Field field, String name) {
        super(x, y, field);
        this.name = name;
    }

    @Override
    public void tick(long elapsed) {
        x += velX;
        y += velY;
        velX = 0;
        velY = 0;
        //TODO: check collisions
    }

    public String getName() {
        return name;
    }
}
