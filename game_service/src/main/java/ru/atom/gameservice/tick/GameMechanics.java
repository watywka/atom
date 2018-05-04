package ru.atom.gameservice.tick;

import ru.atom.gameservice.message.Message;
import ru.atom.gameservice.message.Topic;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GameMechanics implements Tickable {

    private Queue<Message> allMoves;
    private List<Message> moves;

    @Override
    public void tick(long elapsed) {
        readInputQueue();
    }

    private void readInputQueue() {

    }

    private void doMechanics() {
        for(Message m: moves) {
            if (m.getTopic() == Topic.BOMB) {
                add(new Bomb());

            }
        }
    }

    private void writeReplica() {

    }

    public void setInputQueue(ConcurrentLinkedQueue<Message> inputQueue) {
        this.inputQueue = inputQueue;
    }
}
