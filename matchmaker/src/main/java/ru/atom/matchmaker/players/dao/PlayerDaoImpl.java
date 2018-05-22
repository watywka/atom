package ru.atom.matchmaker.players.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.atom.matchmaker.controller.MatchmakerController;
import ru.atom.matchmaker.players.model.Player;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class PlayerDaoImpl implements PlayerDao{

    private static final Logger logger = LoggerFactory.getLogger(MatchmakerController.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(Player player) {
        entityManager.persist(player);
    }

    public void delete(Player player) {
        entityManager.remove(player);
    }

    @Override
    public void update(Player player) {
        entityManager.merge(player);
    }

    @Override
    public List<Player> getPlayerList() {
        return entityManager.createQuery("from Player", Player.class).getResultList();
    }

    @Override
    public Player getByLogin(String login) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Player> criteria = builder.createQuery(Player.class);
        Root<Player> from = criteria.from(Player.class);
        criteria.select(from);
        criteria.where(builder.equal(from.get("login"), login));
        TypedQuery<Player> typed = entityManager.createQuery(criteria);
        Player player;
        try {
            player = typed.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
        return player;
    }
}
