package ru.atom.gameservice;

import ru.atom.gameservice.message.Message;
import ru.atom.gameservice.tick.Ticker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GameSession implements Runnable {

    private List<String> players;
    private ConcurrentLinkedQueue<Message> inputQueue;
    private Ticker ticker;
    private int numberOfPlayers;

    public void addInput(Message message) {
        inputQueue.add(message);
    }

    public GameSession(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        players = new ArrayList<>(numberOfPlayers);
    }

    @Override
    public void run() {
        ticker.gameLoop();
    }

    public void addPlayer(String name) {
        players.add(name);
    }
}
