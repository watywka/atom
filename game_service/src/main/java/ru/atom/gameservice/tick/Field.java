package ru.atom.gameservice.tick;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

public class Field {

    /*

    |-----------------> X
    |       width
    |
    | he
    | ig
    | ht
    |
    V Y

     */

    private final int height; // Y
    private final int width;  // X
    public static final int tile = 48;   // количество пикселей
    //перенес этот сет в field из gamesession мне кажетя логичнее...
    private Set<Tickable> tickables = new ConcurrentSkipListSet<>();

    private GameObject[][] gameObjects;

    public Field(int height, int width, List<String> players) {
        this.height = height;
        this.width = width;
        gameObjects = new GameObject[height][];
        for (int i = 0; i < height; i++) {
            gameObjects[i] = new GameObject[width];

        }

        //Добавлю Стены WALL
        for (int i =0;i<height; i +=2)
            for (int j = 0; j < width; j+=2) gameObjects[i][j] = new Wall(i,j,this);


        //Добавлю Игроков
        int[] X={0,9,0,9};
        int[] Y={0,0,9,9};

        int i=0;
        for(String p : players){
            Player player = new Player(X[i],Y[i],this, p, tile/2+tile*X[i],tile/2+tile*Y[i]);
            setAt(X[i],Y[i],player);
            i++;
        }
    }

    public int pixRIGHTborder(){
        return width*tile - 8;
    }
    public int pixLEFTborder(){
        return 8;
    }
    public int pixTOPborder(){
        return 8;
    }
    public int pixBOTTOMborder(){
        return height*tile - 8;
    }

    public static int PixelsToTiles(int x){
        return x/tile;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public  void setAt(int x, int y, GameObject obj){
        gameObjects[x][y] = obj;
    }

    public GameObject getAt(int x, int y) {
        return gameObjects[x][y];
    }

    public void plantBomb(Player player) {
        plantBomb(player.x, player.y, player.radius);
    }

    public void plantBomb(int x, int y, int radius) {
        gameObjects[x][y] = new Bomb(x, y, this, radius);
    }

    public void Proyti_po_vsem(long elapsed) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                GameObject gameObject = gameObjects[i][j];
                if(gameObject instanceof Tickable) {
                   ((Tickable) gameObject).tick(elapsed);
                }
            }
        }
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (!gameObjects[i][j].isAlive()) gameObjects[i][j] = null;
            }
        }

    }

    public Player getPlayerByName(String name) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                GameObject gameObject = gameObjects[i][j];
                if (gameObject instanceof Player) {
                    if (((Player) gameObject).getName().equals(name)) return (Player) gameObject;
                }
            }
        }
        return null;
    }
}
