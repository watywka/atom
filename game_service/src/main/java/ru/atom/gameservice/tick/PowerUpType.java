package ru.atom.gameservice.tick;

import java.util.List;
import java.util.Random;

public enum PowerUpType {
    BOMB_RADIUS,
    VELOCITY,
    BOMB_COUNT;


    private static final List<PowerUpType> VALUES =
            List.of(values());
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static PowerUpType getRandomType() {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
