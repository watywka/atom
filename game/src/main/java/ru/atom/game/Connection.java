package ru.atom.game;

public class Connection {

    private String name;

    private int gameId;

    public Connection(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getGameId() {
        return gameId;
    }

    public Connection setGameId(int gameId) {
        this.gameId = gameId;
        return this;
    }
}
