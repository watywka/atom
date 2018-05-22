package ru.atom.matchmaker;

public class Connection {

    private String name;

    private long gameId;

    private boolean available = true;

    private int rating;


    public boolean isAvailable() {
        return available;
    }


    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Connection(String name, int rating) {
        this.name = name;
        this.rating = rating;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
