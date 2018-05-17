package ru.atom.matchmaker.players.dao;

import ru.atom.matchmaker.players.model.Player;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class PlayerDaoImpl implements PlayerDao{

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(Player player) {
        Session session = this.sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.persist(player);
        tx.commit();
        session.close();
    }

    @Override
    public List<Player> getPlayerList() {
        Session session = this.sessionFactory.openSession();
        String hql = "from Player";
        List<Player> playerList = session.createQuery(hql).list();
        session.close();
        return playerList;
    }
}
