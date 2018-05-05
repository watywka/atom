package ru.atom.gameservice.tick;

public class Wall extends GameObject{
    /*
     просто стена ничего не делает
     попробовал перегрузить поле alive как final и сделал true
     не уверен, что это сработает
     */
    private final boolean alive = true;

    public Wall(int x, int y, Field field) {
        super(x, y, field);
    }

    @Override
    public boolean isAlive() {
        return true;
    }

    @Override
    public void setAlive(boolean alive) {
        return;
    }
}
