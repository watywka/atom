package ru.atom.gameservice.tick;

import java.lang.Math;

public class Bomb extends GameObject implements Tickable{
    private long timeToLive = 3000;
    private int radius = 1;
    private Player owner;

    public Bomb(Field field, Player p) {
        super(p.x, p.y, field);
        this.radius = p.bombsRadius;
        this.owner = p;
    }

    @Override
    public void tick(long elapsed) {
        if (timeToLive > 0) {
            timeToLive-= elapsed;
        } else {
            for (int i = Math.max(0,x-radius); i < Math.min(field.getWidth(), x + radius); i++) {
                GameObject fieldAt = field.getAt(i, y);
                if (fieldAt instanceof Box || fieldAt instanceof Player) {
                    fieldAt.setAlive(false);
                }
            }
            for (int j = Math.max(0,y-radius); j < Math.min(field.getHeight(), y + radius); j++) {
                GameObject fieldAt = field.getAt(x, j);
                if (fieldAt instanceof Box || fieldAt instanceof Player) {
                    fieldAt.setAlive(false);
                }
            }
            // Мы  убили бомбу после последнего тика и дали плееру новую
            this.setAlive(false);
            owner.giveNewBomb();

        }
    }

    @Override
    public String toJson() {
        return String.format("{\"position\":{\"x\":%d,\"y\":%d},\"id\":%d,\"type\":\"Bomb\"}",
                x * Field.tile,
                y * Field.tile,
                id);
    }

}
