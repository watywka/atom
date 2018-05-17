package ru.atom.gameservice.tick;

import java.util.List;
import java.util.Random;
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
    private int idGenerator;
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
        for (int i = 1; i < height; i += 2)
            for (int j = 1; j < width; j+= 2) gameObjects[i][j] = new Wall(i, j, this);

        //Добавлю Игроков
        int[] X = {0, 9, 0, 9};
        int[] Y = {0, 0, 9, 9};

        for (int i = 0; i < 4; i++) {
            Player player = new Player(X[i],Y[i],this, players.get(i));
            gameObjects[X[i]][Y[i]] = player;
        }
    }

    public int getRightBorder(){
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

    public GameObject getAt(int x, int y) {
        return (x < 0 || y < 0 || x > width || y > height) ? null : gameObjects[x][y];
    }

    public void plantBomb(Player player) {
        if(player.tryToPlantBomb()) {
            gameObjects[player.x][player.y] = new Bomb(this, player);
        }
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
                GameObject gameObject = gameObjects[i][j];
                if (!gameObject.isAlive()){
                    if (gameObject instanceof Box){
                        generatePowerUp(i,j);
                        continue;
                    }
                    gameObjects[i][j] = null;
                }
            }
        }

    }

    private void generatePowerUp(int x, int y) {
        Random rnd = new Random(System.currentTimeMillis());
        int number = rnd.nextInt(100);
        if(number>79){
            gameObjects[x][y] = new PowerUp(x,y,this);
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

    //взято с фронта
    public static boolean checkCollision(int newX, int newY, GameObject collideObj) {
        int down = collideObj.x*tile + 20;
        int up = down + tile - 30;
        int left = collideObj.x*tile + 25;
        int right = left + tile - 30;
        return (down < collideObj.y*tile)
                && (up > collideObj.y*tile + tile)
                && (left < collideObj.x*tile + tile)
                && (right > collideObj.x*tile);
    }

    public int getNextId() {
        return idGenerator++;
    }

    public String getReplica() {
        StringBuilder stringBuilder = new StringBuilder("{   \"topic\": \"REPLICA\",   \"data\": {  \"objects\":[");
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                stringBuilder.append(gameObjects[i][j].toJson());
                stringBuilder.append(",");
            }
        }
        stringBuilder.append("],       \"gameOver\":false   }}");
        return stringBuilder.toString();
    }
}
