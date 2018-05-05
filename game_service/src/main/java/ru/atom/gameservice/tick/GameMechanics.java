package ru.atom.gameservice.tick;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.atom.gameservice.controller.GameServiceController;
import ru.atom.gameservice.message.Message;
import ru.atom.gameservice.message.Topic;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;



public class GameMechanics implements Tickable {

    private static final Logger logger = LoggerFactory.getLogger(GameServiceController.class);
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
                //add(new Bomb());

            }
        }
    }

    private void writeReplica(Message message) {
       /* Player player = new Player(message.getName(),
                new ArrayList<Message>(){{add(message.getTopic(), message.getData(), message.getName());
                add("BOMB", "abc", "name");
                add("BOMB", "abc", "name");}});
        ObjectMapper mapper = new ObjectMapper();
        try {
            //куда выводить?
            mapper.writeValue(player);
            logger.info(mapper.writeValueAsString(player));
        } catch (IOException ex) {
            logger.error("Can't create JAVA -> JSON");
        }*/
    }


    public void setInputQueue(ConcurrentLinkedQueue<Message> inputQueue) {

    }
}
