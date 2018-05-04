package ru.atom.gameservice.tick;

public class Field {

    private final int height;
    private final int width;

    private GameObject[][] gameObjects;

    public Field(int height, int width) {
        this.height = height;
        this.width = width;
        gameObjects = new GameObject[height][];
        for (int i = 0; i < height; i++) {
            gameObjects[i] = new GameObject[width];

        }
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
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
