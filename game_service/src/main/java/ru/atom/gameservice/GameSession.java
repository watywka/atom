package ru.atom.gameservice;

import org.slf4j.LoggerFactory;
import ru.atom.gameservice.message.Message;
import ru.atom.gameservice.message.Topic;
import ru.atom.gameservice.tick.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class GameSession implements Runnable {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(GameSession.class);

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
        field = new Field(10,10, players);
        final int FPS = 60;
        final long FRAME_TIME = 1000 / FPS;
        while (!Thread.currentThread().isInterrupted()) {
            long started = System.currentTimeMillis();

            List<Message> messages = readInputQueue();
            for (Message message : messages) {
                switch (message.getTopic()) {
                    case PLANT_BOMB:
                        field.plantBomb(field.getPlayerByName(message.getName()));
                        break;
                    case MOVE:
                        //получаем плеера и ставим ему velx vely
                        Player p = field.getPlayerByName(message.getName());//TODO: чекнуть, приходит ли в месседже имя игрока
                        int vX = 0, vY = 0;
                        String direction = message.getData();
                        if (direction.endsWith("UP\"}")){
                            vY = -1;
                        }
                        if (direction.endsWith("DOWN\"}")){
                            vY = 1;
                        }
                        if (direction.endsWith("RIGHT\"}")){
                            vX= 1;
                        }
                        if (direction.endsWith("UP\"}")){
                            vX = -1;
                        }
                        p.setVels(vX,vY);

                }
            }
            // Проходит по всем тикаемым объектам
            // Потом проверяет тех, кто умер и удаляет их с поля
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

        ticker.gameLoop();
    }

    private List<Message> readInputQueue() {
        List<Message> out = new ArrayList<>();
        synchronized (inputQueue) {  // Сделал на всякий случай , если здесь не нужна синхронизация , то удали)
            out.addAll(inputQueue);
            inputQueue.clear();
        }

        return out;
    }

    public void addPlayer(String name) {
        players.add(name);
    }
}
