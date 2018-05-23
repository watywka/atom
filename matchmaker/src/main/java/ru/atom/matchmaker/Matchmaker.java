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
    private static final int PLAYERS_PER_GAME = 1;
    private long currentGameId = 1;
    private boolean newGame = true;
    private static final int rating = 1500;
    private static final int ratingChange = 5;
    private static final int ratingStep = 50;

    private BlockingQueue<Connection> queue;

    public BlockingQueue<Connection> getQueue() {
        return queue;
    }

    public Matchmaker () {
        queue = new LinkedBlockingQueue<>();
    }

    @PostConstruct
    private void startMatchmaker() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        logger.info("Matchmaker started");
        List<Connection> players = new ArrayList<>();
        List<Connection> goToGame = new ArrayList<>(PLAYERS_PER_GAME);
        while (!Thread.currentThread().isInterrupted()) {
            if (goToGame.size() == PLAYERS_PER_GAME) {
                startGame(currentGameId);
                goToGame.clear();
                newGame = true;
            } else {

               try {
                    Connection newPlayer = queue.poll(10_000, TimeUnit.SECONDS);
                    players.add(newPlayer);
                    synchronized (newPlayer) {
                        if (newGame) {
                            getNextGameId();
                            newGame = false;
                        }
                        for (Connection player : players) {
                            if (player.getRating() < (rating - ratingStep)) {
                                player.changeRating(ratingChange);
                            } else if (player.getRating() > (rating + ratingStep)) {
                                player.changeRating(-ratingChange);
                            } else {
                                if (goToGame.size() < 4) {
                                    goToGame.add(player);
                                    players.remove(player);
                                    player.setCreatingGame(true);
                                    player.setGameId(currentGameId);
                                }
                            }
                        }
                        newPlayer.notify();
                    }
               } catch (InterruptedException e) {
                    logger.info("No new players");
                }
            }
            try {
                wait(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void getNextGameId() {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().add("count", String.valueOf(PLAYERS_PER_GAME)).build();
        Request request = new Request.Builder().url("http://localhost:8090/game/create")
                .post(requestBody).addHeader("Content-Type", "application/x-www-form-urlencoded").build();
        Response response;
        try {
            response = client.newCall(request).execute();
            currentGameId =  Long.parseLong(response.body().string());
            
        } catch (IOException e) {
            logger.error("Can't create new game");
            logger.error(e.getLocalizedMessage());
            currentGameId = -1;
        }
    }

    private void startGame(long gameId) {
        logger.info("Request to stat game {}",gameId );
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().add("gameId", String.valueOf(gameId)).build();
        Request request = new Request.Builder().url("http://localhost:8090/game/start")
                .post(requestBody).addHeader("Content-Type", "application/x-www-form-urlencoded").build();
        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            logger.error("Can't start game");
            logger.error(e.getLocalizedMessage());
        }
    }
}
