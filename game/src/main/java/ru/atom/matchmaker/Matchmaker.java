package ru.atom.matchmaker;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Service
public class Matchmaker implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(Matchmaker.class);
    private static final int PLAYERS_PER_GAME = 4;
    private long currentGameId = 1;

    private BlockingQueue<Connection> queue;


    public BlockingQueue<Connection> getQueue() {
        return queue;
    }

    public Matchmaker () {
        queue = new LinkedBlockingQueue<>(PLAYERS_PER_GAME);
    }


    @PostConstruct
    private void startMatchmaker() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        logger.info("Matchmaker started");
        getNextGameId();
        List<Connection> players = new ArrayList<>(PLAYERS_PER_GAME);
        while (!Thread.currentThread().isInterrupted()) {
            if (players.size() == PLAYERS_PER_GAME) {
                getNextGameId();
                players.clear();
            }
            else {
                try {
                    Connection e = queue.poll(10_000, TimeUnit.SECONDS);
                    synchronized (e) {
                        e.setGameId(currentGameId);
                        logger.info("Player {} -> game {}",e.getName(), e.getGameId());
                        players.add(e);
                        e.notify();
                    }
                } catch (InterruptedException e) {
                    logger.info("No new players");
                }
            }
        }
    }

    private void getNextGameId() {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().add("count", String.valueOf(PLAYERS_PER_GAME)).build();
        Request request = new Request.Builder().url("http://localhost:8090/game/create").post(requestBody).addHeader("Content-Type", "application/x-www-form-urlencoded").build();
        Response response;
        try {
            response = client.newCall(request).execute();
            currentGameId =  Long.parseLong(response.body().string());
        } catch (IOException e) {
            logger.error("Can't create new game");
            logger.error(e.getLocalizedMessage());
            Thread.currentThread().interrupt();
        }

    }
}
