package ru.atom.gameservice.tick;

import ru.atom.gameservice.message.Direction;

public class Player extends GameObject implements Tickable {

    private final String name;
    private int velX;
    private int velY;
    private int pixelX;
    private int pixelY;
    private int bombsRadius = 1;
    private int bombsMax = 2;
    private int speed = 1;
    private int bombsPlanted = 0;
    private int bombRadius;
    private Direction direction = Direction.IDLE;

    public Player(int x, int y, Field field, String name) {
        super(x, y, field);
        this.name = name;
        pixelX = x*Field.tile;
        pixelY = y*Field.tile;
    }

    public Player setDirection(Direction direction) {
        this.direction = direction;
        return this;
    }


    public void changeVelocity(int dx, int dy) {
        velX += dx*speed;
        velY += dy*speed;
    }


    @Override
    public void tick(long elapsed) {
        if(alive) {
            int newX = (pixelX + Field.tile/2 + velX) / Field.tile;
            int newY = (pixelY + Field.tile/2 + velY) / Field.tile;
            GameObject collideObj = field.getAt(newX, newY);

            boolean collide = Field.checkCollision(newX, newY, collideObj);
            if (!collide) {
                pixelX += velX;
                pixelY += velY;
                x = newX;
                y = newY;
            }
            if (collide) {
                if (collideObj instanceof PowerUp) {
                    applyPowerUp((PowerUp) collideObj);
                    pixelX += velX;
                    pixelY += velY;
                    x = newX;
                    y = newY;
                }
                if (collideObj instanceof Fire) this.setAlive(false);
            }
            velX = 0;
            velY = 0;
        }
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

    public int getBombRadius() {
        return bombRadius;
    }

    public void giveNewBomb() {
        bombsPlanted--;
    }

    private void applyPowerUp(PowerUp powerUp) {
        if(powerUp.isAlive()) {
            powerUp.setAlive(false);
            switch (powerUp.getType()) {
                case SPEED:
                    speed++;
                    break;
                case BOMB:
                    bombsMax++;
                    break;
                case RANGE:
                    bombsRadius++;
            }
        }
    }

    @Override
    public String toJson() {
        return String.format("{\"position\":{\"x\":%d,\"y\":%d}, \"direction\": \"%s\", \"id\":%d, \"type\":\"Pawn\"}",
                pixelX,
                pixelY,
                direction.toString(),
                id);
    }
}
