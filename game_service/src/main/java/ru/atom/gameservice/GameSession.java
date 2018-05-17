package ru.atom.gameservice;

import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import ru.atom.gameservice.message.Direction;
import ru.atom.gameservice.message.Message;
import ru.atom.gameservice.message.MoveMessage;
import ru.atom.gameservice.network.ConnectionPool;
import ru.atom.gameservice.tick.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class GameSession implements Runnable {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(GameSession.class);

    private ConnectionPool connectionPool;

    private List<String> players;
    private ConcurrentLinkedQueue<Message> inputQueue;

    private Field field;

    public void addInput(Message message) {
        inputQueue.add(message);
    }



    public GameSession(int numberOfPlayers) {
        players = new ArrayList<>(numberOfPlayers);
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
                        //TODO: чекнуть, приходит ли в месседже имя игрока
                        Player p = field.getPlayerByName(message.getName());
                        Direction direction = ((MoveMessage)message).getDirection();
                        p.changeVelocity(direction.getX(),direction.getY());

                }
            }
            // Проходит по всем тикаемым объектам
            // Потом проверяет тех, кто умер и удаляет их с поля
            field.Proyti_po_vsem(FRAME_TIME);

            long elapsed = System.currentTimeMillis() - started;

            if (elapsed < FRAME_TIME) {
                LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(FRAME_TIME - elapsed));
            } else {
                log.warn("tick lag {} ms", elapsed - FRAME_TIME);
            }

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
        }

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
