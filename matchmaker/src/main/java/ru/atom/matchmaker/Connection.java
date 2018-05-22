package ru.atom.matchmaker;

public class Connection {

    private String name;

    private long gameId;

    private boolean creatingGame = false;

    private int rating;

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

    public void changeRating(int rating) {
        this.rating += rating;
    }

    public boolean isCreatingGame() {
        return creatingGame;
    }

    public void setCreatingGame(boolean creatingGame) {
        this.creatingGame = creatingGame;
    }
}
