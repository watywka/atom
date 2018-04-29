package ru.atom.game.Controller;

import jdk.jfr.consumer.RecordedMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.atom.game.Connection;
import ru.atom.game.Matchmaker;

@Controller
@RequestMapping("matchmaker")
public class MatchmakerController {

    public static final Logger logger = LoggerFactory.getLogger(MatchmakerController.class);
    private Matchmaker matchmaker;

    @Autowired
    public MatchmakerController(Matchmaker matchmaker) {
        this.matchmaker = matchmaker;
    }

    @RequestMapping(path = "join",
            method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Integer> join(@RequestParam("name") String name) {
        logger.info("New connection: name = {}", name);
        Connection playerConnection = new Connection(name);
        matchmaker.getQueue().offer(playerConnection);
        synchronized (playerConnection) {
            try {
                playerConnection.wait();
            } catch (InterruptedException e) {
                logger.error(e.getLocalizedMessage());
            }
        }

        return ResponseEntity.ok(playerConnection.getGameId());
    }

    @RequestMapping(path = "test",
            method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> check() {
        logger.info("WTF");
        return ResponseEntity.ok("ok");
    }

}
