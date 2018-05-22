package ru.atom.gameservice.tick;

import ru.atom.gameservice.message.Direction;

public class Player extends GameObject implements Tickable {

    private final String name;
    private int velX;
    private int velY;
    private int pixelX; // центр игрока
    private int pixelY; // центр игрока
    private int bombsRadius = 1;
    private int bombsMax = 1;
    private int speed = 1;
    private int bombsPlanted = 0;
    private int bombRadius;
    private Direction direction = Direction.IDLE;

    public Player(int x, int y, Field field, String name) {
        super(x, y, field);
        this.name = name;
        pixelX = x*Field.tile + Field.tile / 2;
        pixelY = y*Field.tile + Field.tile / 2;
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
        if (alive) {
            int x1 = 0, y1 = 0, x2 = 0, y2 = 0;
            switch (direction) {
                case UP:
                    x1 = pixelX - Field.tile/4;
                    x2 = pixelX + Field.tile/4;
                    y1 = y2 = pixelY + Field.tile;
                    break;
                case DOWN:
                    x1 = pixelX - Field.tile/4;
                    x2 = pixelX + Field.tile/4;
                    y1 = y2 = pixelY - Field.tile;
                    break;
                case LEFT:
                    x1 = x2 = pixelX - Field.tile;
                    y1 = pixelY + Field.tile/4;
                    y2 = pixelY - Field.tile/4;
                    break;
                case RIGHT:
                    x1 = x2 = pixelX + Field.tile;
                    y1 = pixelY + Field.tile/4;
                    y2 = pixelY - Field.tile/4;
                    break;

            }
            GameObject col1 = field.getAt(x1/Field.tile, y1/Field.tile);
            GameObject col2 = field.getAt(x2/Field.tile, y2/Field.tile);
            boolean collide;
            collide= Field.checkCollision(pixelX + velX, pixelY + velY, col1)
                    || Field.checkCollision(pixelX + velX, pixelY + velY, col2);
            if (!collide) {
                if (col1 instanceof Fire || col2 instanceof Fire) this.setAlive(false);
                if (col1 instanceof PowerUp) {
                    applyPowerUp((PowerUp) col1);
                }
                if (col2 instanceof PowerUp) {
                    applyPowerUp((PowerUp) col2);
                }
                pixelX += velX;
                pixelY += velY;
            }

            x = (pixelX)/Field.tile;
            y = (pixelY)/Field.tile;
            field.updatePlayerPosition(this, x, y);
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
                pixelX - Field.tile / 2,
                pixelY - Field.tile / 2,
                direction.toString(),
                id);
    }
}
