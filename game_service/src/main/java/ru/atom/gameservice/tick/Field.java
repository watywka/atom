package ru.atom.gameservice.tick;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    private Set<GameObject> replicaObjects;

    private GameObject[][] gameObjects;

    public Field(int height, int width, List<String> players) {
        this.height = height;
        this.width = width;
        replicaObjects = new HashSet<>();
        gameObjects = new GameObject[height][];
        for (int i = 0; i < height; i++) {
            gameObjects[i] = new GameObject[width];
        }

        //Добавлю Стены WALL
        for (int i = 1; i < height; i += 2)
            for (int j = 1; j < width; j+= 2) {
                Wall wall = new Wall(i, j, this);
                gameObjects[i][j] = new Wall(i, j, this);
                replicaObjects.add(wall);
            }

        //Добавлю Игроков
        int[] X = {0, 9, 0, 9};
        int[] Y = {0, 0, 9, 9};

        for (int i = 0; i < 4; i++) {
            Player player = new Player(X[i],Y[i],this, players.get(i));
            gameObjects[X[i]][Y[i]] = player;
            replicaObjects.add(player);
        }
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public GameObject getAt(int x, int y) {
        return (x < 0 || y < 0 || x >= width || y >= height) ? null : gameObjects[x][y];
    }

    public void plantBomb(Player player) {
        if(player.tryToPlantBomb()) {
            gameObjects[player.x][player.y] = new Bomb(this, player);
            replicaObjects.add(gameObjects[player.x][player.y]);
        }
    }

    public void gameLogic(long elapsed) {
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
                if (gameObject instanceof Player || gameObject instanceof Bomb) {
                    replicaObjects.add(gameObject);
                }
                if (gameObject != null && !gameObject.isAlive()){
                    replicaObjects.add(gameObject);
                    gameObjects[i][j] = (gameObject instanceof Box) ? PowerUp.generateNewPowerUp(i, j, this) : null;
                    if (gameObjects[i][j] != null) replicaObjects.add(gameObjects[i][j]);
                }
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

    //взято с фронта
    public static boolean checkCollision(int newX, int newY, GameObject collideObj) {
        if(collideObj == null) return false;
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
        StringBuilder stringBuilder = new StringBuilder("{   \"topic\": \"REPLICA\",  \"data\":[");
        String collect = replicaObjects.stream().map(GameObject::toJson).collect(Collectors.joining(","));
        replicaObjects.clear();
        stringBuilder.append(collect);
        stringBuilder.append("]}");
        return stringBuilder.toString();
    }
}
