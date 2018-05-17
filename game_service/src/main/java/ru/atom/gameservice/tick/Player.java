package ru.atom.gameservice.tick;

public class Player extends GameObject implements Tickable {

    private final String name;
    private int velX;
    private int velY;
    private int pixelX;
    private int pixelY;
    protected int bombsRadius = 1;
    private int bombsMax = 2;
    private int bombsPlanted = 0;


    public Player(int x, int y, Field field, String name) {
        super(x, y, field);
        this.name = name;
        pixelX = x*Field.tile;
        pixelY = y*Field.tile;
    }

    public void changeVelocity(int dx, int dy) {
        velX += dx;
        velY += dy;
    }


    @Override
    public void tick(long elapsed) {
        if(alive) {
            int newX = (pixelX + velX) / Field.tile;
            int newY = (pixelY + velY) / Field.tile;
            GameObject collideObj = field.getAt(newX, newY);
            if (collideObj == null || !Field.checkCollision(newX, newY, collideObj)) {
                pixelX = velX;
                pixelY = velY;
                x = newX;
                y = newY;
            }
            velX = 0;
            velY = 0;
        }
        //TODO: check good collisions
    }

    public String getName() {
        return name;
    }

    public boolean tryToPlantBomb() {
        if (bombsPlanted < bombsMax){
            bombsPlanted++;
            return true;
        }
        return false;
    }

    public void giveNewBomb() {
        bombsPlanted--;
    }

    @Override
    public String toJson() {
        return String.format("{\"position\":{\"x\":%d,\"y\":%d},\"id\":%d, \"type\":\"Pawn\"}",
                x * Field.tile,
                y * Field.tile,
                id);
    }
}
