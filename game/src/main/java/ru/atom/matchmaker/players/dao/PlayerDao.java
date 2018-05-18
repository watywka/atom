package ru.atom.matchmaker.players.dao;

import ru.atom.matchmaker.players.model.Player;
import java.util.List;

public interface PlayerDao {
    //public Player getByLogin(String login);
    public void save (Player player);
    public List<Player> getPlayerList();
}
