package ru.atom.gameservice.tick;

public class PowerUp extends GameObject {

    private final PowerUpType type;

    public PowerUp(int x, int y, Field field) {
        super(x, y, field);
        type = PowerUpType.getRandomType();
    }

    public PowerUpType getType() {
        return type;
    }


    @Override
    public String toJson() {
        return null;
    }
}
