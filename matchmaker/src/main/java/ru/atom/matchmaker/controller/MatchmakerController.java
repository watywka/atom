package ru.atom.matchmaker.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.atom.matchmaker.Connection;
import ru.atom.matchmaker.Matchmaker;
import ru.atom.matchmaker.players.dao.PlayerDao;
import ru.atom.matchmaker.players.model.Player;

@Controller
@RequestMapping("matchmaker")
public class MatchmakerController {

    private static final Logger logger = LoggerFactory.getLogger(MatchmakerController.class);

    private Matchmaker matchmaker;
    private final PlayerDao playerDao;

    @Autowired
    public MatchmakerController(Matchmaker matchmaker, PlayerDao playerDao) {
        this.matchmaker = matchmaker;
        this.playerDao = playerDao;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(path = "login",
            method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> login(@RequestParam("Login") String login, @RequestParam("password") String password) {
        logger.info("New connection: Login = {} beda", login);

        Player player = playerDao.getByLogin(login);
        if (player == null){
            return ResponseEntity.badRequest().body("Cant find this name");
        }

        if (!player.getPassword().equals(password)) {
            return ResponseEntity.badRequest().body("Wrong password");
        }

        try {
            logger.info("login player = {}", login);
            player.setOnline(true);
            playerDao.update(player);
        } catch (Exception ex) {
            logger.error(ex.getLocalizedMessage());
            return ResponseEntity.badRequest().body("smt is wrong in login");
        }

        return ResponseEntity.badRequest().body("0");
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(path = "logout",
            method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> logout(@RequestParam("Login") String login) {
        logger.info("New connection: Login = {}", login);

        Player player = playerDao.getByLogin(login);

        if (player == null){
            return ResponseEntity.badRequest().body("Cant find this name");
        }

        try {
            logger.info("logout player = {}", login);
            player.setOnline(false);
            playerDao.update(player);
        } catch (Exception ex) {
            logger.error(ex.getLocalizedMessage());
            return ResponseEntity.badRequest().body("smt is wrong in logout");
        }

        return ResponseEntity.ok("logout");
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(path = "registration",
            method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> registration(@RequestParam("Login") String login, @RequestParam("password") String password) {
        logger.info("New player: Login = {}", login);

        if (login.length() < 4) {
            return ResponseEntity.badRequest().body("Too short name, sorry :(");
        }

        if (login.length() > 20) {
            return ResponseEntity.badRequest().body("Too long name, sorry :(");
        }

        Player player = playerDao.getByLogin(login);
        if (player != null){
            return ResponseEntity.badRequest().body("Login is created before");
        }

        try {
            logger.info("create player = {}", login);
            Player newPlayer = new Player(login, password);
            playerDao.save(newPlayer);
        } catch (Exception ex) {
            logger.error(ex.getLocalizedMessage());
            return ResponseEntity.badRequest().body("smt is wrong");
        }
        return ResponseEntity.ok("Create new player");
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(path = "findGame",
            method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> Find(@RequestParam("Login") String login) {

        logger.info("new finder: Login = {}", login);

        Player player = playerDao.getByLogin(login);

        if (player == null){
            return ResponseEntity.badRequest().body("Cant find this name");
        }

        if (!player.isOnline()) {
            return ResponseEntity.badRequest().body("fuckUp");
        }

        try {
            logger.info("player in findPool = {}", login);
            player.setInSearch(true);
            playerDao.update(player);
        } catch (Exception ex) {
            logger.error(ex.getLocalizedMessage());
            return ResponseEntity.badRequest().body("smt is wrong in logout");
        }


        Connection playerConnection = new Connection(player.getLogin(), player.getRating());

        matchmaker.getQueue().offer(playerConnection);

        synchronized (playerConnection) {
            try {
                playerConnection.wait(10_000);
            } catch (InterruptedException e) {
                logger.error(e.getLocalizedMessage());
                return ResponseEntity.badRequest().body("0");
            }
        }
        if ( playerConnection.isCreatingGame()){
            try {
                player.setIdSession(playerConnection.getGameId());
                playerDao.update(player);
            } catch (Exception ex) {
                logger.error(ex.getLocalizedMessage());
                return ResponseEntity.badRequest().body("smt is wrong in SetIdSession");
            }
            return ResponseEntity.ok(String.valueOf(playerConnection.getGameId()));
        }

        return ResponseEntity.badRequest().body("0");
    }

}