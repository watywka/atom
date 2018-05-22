package ru.atom.matchmaker.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.atom.matchmaker.Connection;
import ru.atom.matchmaker.Matchmaker;

@Controller
@RequestMapping("matchmaker")
public class MatchmakerController {

    private static final Logger logger = LoggerFactory.getLogger(MatchmakerController.class);
    private Matchmaker matchmaker;

    @Autowired
    public MatchmakerController(Matchmaker matchmaker) {
        this.matchmaker = matchmaker;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(path = "join",
            method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Long> join(@RequestParam("name") String name) {
        logger.info("New connection: name = {}", name);
        Connection playerConnection = new Connection(name);
        matchmaker.getQueue().offer(playerConnection);
        synchronized (playerConnection) {
            try {
                playerConnection.wait(10_000);
            } catch (InterruptedException e) {
                logger.error(e.getLocalizedMessage());
                return ResponseEntity.badRequest().body((long) 0);
            }
        }
        if ( playerConnection.isAvailable()){
            return ResponseEntity.ok(playerConnection.getGameId());
        }

        return ResponseEntity.badRequest().body((long) 0);
    }

}
