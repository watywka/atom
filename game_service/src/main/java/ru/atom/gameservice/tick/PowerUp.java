package ru.atom.gameservice.tick;

public class PowerUp extends GameObject {

    private  PowerUpType type = PowerUpType.BOMB_COUNT;//TODO: make final

    public PowerUp(int x, int y, Field field, int number) {
        super(x, y, field);
        switch (number) {
            case 1:
                type = PowerUpType.BOMB_COUNT;
                break;
            case 2:
                type = PowerUpType.BOMB_RADIUS;
                break;
            case 3:
                type = PowerUpType.VELOCITY;
                break;
        }
    }

    public PowerUpType getType() {
        return type;
    }


}
