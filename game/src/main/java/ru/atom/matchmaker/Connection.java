package ru.atom.matchmaker;

public class Connection {

    private String name;

    private long gameId;

    public Connection(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public long getGameId() {
        return gameId;
    }

    public Connection setGameId(long gameId) {
        this.gameId = gameId;
        return this;
    }
}
