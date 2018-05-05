package ru.atom.gameservice;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.socket.TextMessage;
import ru.atom.gameservice.message.Message;
import ru.atom.gameservice.message.Topic;
import ru.atom.gameservice.network.ConnectionPool;
import ru.atom.gameservice.tick.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class GameSession implements Runnable {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(GameSession.class);

    private ConnectionPool connectionPool;

    private List<String> players;
    private ConcurrentLinkedQueue<Message> inputQueue;
    private Ticker ticker;
    private int numberOfPlayers;
    private GameMechanics gameMechanics;

    private Field field;

    private long tickNumber = 0;

    public void addInput(Message message) {
        inputQueue.add(message);
    }



    public GameSession(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        players = new ArrayList<>(numberOfPlayers);
        gameMechanics.setInputQueue(inputQueue);
        ticker.registerTickable(gameMechanics);
    }

    @Override
    public void run() {
        final int FPS = 60;
        final long FRAME_TIME = 1000 / FPS;
        while (!Thread.currentThread().isInterrupted()) {
            for(String name: players) {
                ConnectionPool connectionPool = BeanUtil.getBean(ConnectionPool.class);
                try {
                    connectionPool.getSession(name).sendMessage(new TextMessage("{\n" +
                            "   \"topic\": \"REPLICA\",\n" +
                            "   \"data\":\n" +
                            "   {\n" +
                            "       \"objects\":[{\"position\":{\"x\":16.0,\"y\":12.0},\"id\":16,\"type\":\"Wall\"},{\"position\":{\"x\":32.0,\"y\":32.0},\"id\":213,\"velocity\":0.05,\"maxBombs\":1,\"bombPower\":1,\"speedModifier\":1.0,\"type\":\"Pawn\"},{\"position\":{\"x\":32.0,\"y\":352.0},\"id\":214,\"velocity\":0.05,\"maxBombs\":1,\"bombPower\":1,\"speedModifier\":1.0,\"type\":\"Pawn\"}],\n" +
                            "       \"gameOver\":false\n" +
                            "   }\n" +
                            "} "));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            long started = System.currentTimeMillis();

            List<Message> messages = readInputQueue();
            for (Message message : messages ) {
                switch (message.getTopic()) {
                    case PLANT_BOMB:
                        field.plantBomb(field.getPlayerByName(message.getName()));
                        break;
                    case MOVE:
                        //получаем плеера и ставим ему velx vely
                        Player p = field.getPlayerByName(message.getName());
                        int vx,vy;
                        /*
                        TODO: Нужно посмотреть как скорости запакованы в дату
                        vx  = message.getData().toCharArray();
                        p.
                        */
                }
            }


            field.Proyti_po_vsem(FRAME_TIME);
            long elapsed = System.currentTimeMillis() - started;

            if (elapsed < FRAME_TIME) {
                log.info("All tick finish at {} ms", elapsed);
                LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(FRAME_TIME - elapsed));
            } else {
                log.warn("tick lag {} ms", elapsed - FRAME_TIME);
            }
            log.info("{}: tick ", tickNumber);
            tickNumber++;
        }

//        ticker.gameLoop();
    }

    private List<Message> readInputQueue() {
        List<Message> out = new ArrayList<>();
        synchronized (inputQueue){  // Сделал на всякий случай , если здесь не нужна синхронизация , то удали)
            out.addAll(inputQueue);
            inputQueue.clear();
        }

        return out;
    }

    public void addPlayer(String name) {
        players.add(name);
    }
}
