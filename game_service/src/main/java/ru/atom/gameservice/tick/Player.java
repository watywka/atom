package ru.atom.gameservice.tick;

import ru.atom.gameservice.message.Message;

import java.util.ArrayList;
import java.util.List;

public class Player extends GameObject implements Tickable{

    private final String name;
    private int velX;
    private int velY;
    protected int radius = 1;

    public Player(int x, int y, Field field, String name) {
        super(x, y, field);
        this.name = name;
    }


    public void setVels(int x, int y){
        velX = x;
        velY = y;
    }

    @Override
    public void tick(long elapsed) {
        //some collision check
        List<GameObject> colliding = new ArrayList<>();

        //Очень плохой способ поиска сталкивающихся объектов
        // по Х
        if(velX > 0){
            for (int i = x;i< x+velX; i++){
                GameObject fieldAt = field.getAt(i, y);
                if (fieldAt instanceof Box || fieldAt instanceof Bomb || fieldAt instanceof Wall)
                    //FUCK THIS IS REALLY STUPID
            }
        }else{
            GameObject fieldAt = field.getAt(i, y);
            for (int i = x;i> x+velX; i--){

            }
        }
        // по Y
        if(velY > 0){
            for (int i = y;i< y+velY; i++){

            }
        }else{
            for (int i = y;i> y+velY; i--){

            }
        }

        if ((x+velX<field.getHeight())&&(y+velY<field.getWidth())) { // out of field
            if ()
            x += velX;
            y += velY;
        }else {
            // else: set x, y to the border
            if (velX>0)
                x = field.getHeight();
            else
                x = 0;

            if (velY>0)
                y = field.getWidth();
            else
                y = 0;
        }
        velX = 0;
        velY = 0;

        //TODO: check collisions
    }

    public String getName() {
        return name;
    }
}
