package ru.atom.gameservice.tick;

import ru.atom.gameservice.message.Message;

import java.util.ArrayList;
import java.util.List;

public class Player extends GameObject implements Tickable {

    private final String name;
    private int velX;
    private int velY;
    private int pixelX;
    private int pixelY;
    protected int radius = 1;
    List<PowerUp> powerUps;
    private int bombCount = 2;
    private int plantedBombs = 0;


    public Player(int x, int y, Field field, String name, int pixX, int pixY) {
        super(x, y, field);
        this.name = name;
        pixelX = x;
        pixelY = y;
    }

    public void setVels(int x, int y) {
        velX = x;
        velY = y;
    }


    @Override
    public void tick(long elapsed) {
        //some collision check
        GameObject collideObj = null;

        //Очень плохой способ поиска сталкивающихся объектов
        // по Х
        int xTileVel =0;
        int yTileVel = 0;
        if (velX > 0) {
            int newX = (pixelX + velX) / field.tile;
            if (newX != x){
                collideObj = field.getAt(newX, y);
                xTileVel++;
            }

        }
        if (velX < 0) {
            int newX = (pixelX + velX) / field.tile;
            if (newX != x){
                collideObj = field.getAt(newX, y);
                xTileVel--;
            }
        }
        // по Y
        if (velY > 0) {
            int newY = (pixelY + velY) / field.tile;
            if (newY != x) {
                collideObj = field.getAt(x, newY);
                yTileVel++;
            }
        }
        if (velY < 0) {
            int newY = (pixelY + velY) / field.tile;
            if (newY != x) {
                collideObj = field.getAt(x, newY);
                yTileVel--;
            }
        }

        if ( pixelX + velX < field.pixLEFTborder()
                && pixelY + velY < field.pixBOTTOMborder()
                && pixelX + velX < field.pixRIGHTborder()
                && pixelY + velY < field.pixTOPborder()       )
        {                                                 // not out of field
            if (collideObj == null || collideObj instanceof PowerUp ) {
                x += xTileVel;
                y += yTileVel;
                pixelX += velX;
                pixelY += velY;
                if (collideObj instanceof PowerUp){
                    switch (((PowerUp)collideObj).getType()) {
                        case BOMB_COUNT:
                            bombCount++;
                            break;
                        case VELOCITY:
                            //TODO: make velocity logic
                            break;
                        case BOMB_RADIUS:
                            radius++;
                            break;
                    }

                }
            }


        } else {
            // else: set x, y to the border
            if (velX > 0) {
                x = field.getWidth();
                pixelX = field.pixRIGHTborder();
            }else {
                x = 0;
                pixelX = field.pixLEFTborder();
            }
            if (velY > 0) {
                y = field.getHeight();
                pixelY = field.pixBOTTOMborder();
            }else {
                y = 0;
                pixelY = field.pixTOPborder();
            }
        }
        velX = 0;
        velY = 0;

        //TODO: check good collisions
    }

    public String getName() {
        return name;
    }

    public boolean tryToPlantBomb() {
        if (plantedBombs < bombCount){
            plantedBombs++;
            return true;
        }
        return false;
    }

    public void giveNewBomb() {
        plantedBombs--;
    }
}
