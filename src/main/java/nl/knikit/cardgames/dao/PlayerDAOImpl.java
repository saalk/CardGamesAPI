package nl.knikit.cardgames.dao;

import lombok.extern.slf4j.Slf4j;
import nl.knikit.cardgames.model.Player;

import org.hibernate.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

// Implementing dao for all the crud in the dao interface
@Slf4j
@Repository
public class PlayerDAOImpl implements PlayerDAO {

    // the basic interfaces between a Java application and Hibernate
    // every method that interact with the database gets a session via the factory
    @Autowired
    private SessionFactory sessionFactory;

    public PlayerDAOImpl() {
    }

    public PlayerDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @Transactional
    public ArrayList<Player> listAll() {

        @SuppressWarnings("unchecked")
        ArrayList<Player> listPlayer = (ArrayList<Player>) sessionFactory.getCurrentSession()
                .createCriteria(Player.class)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();

        return listPlayer;
    }

    @Override
    @Transactional
    public ArrayList<Player> findAllForCriteria(String criteria) {
        @SuppressWarnings("unchecked")
        ArrayList<Player> listPlayer = (ArrayList<Player>) sessionFactory.getCurrentSession()
                .createCriteria(Player.class)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();

        return listPlayer;
    }

    @Override
    @Transactional
    public void createOrUpdate(Player player) {
        String message = String.format("Player in DAO: %s", player.toString());
        log.info(message);

        sessionFactory.getCurrentSession().saveOrUpdate(player);

    }

    @Override
    @Transactional
    public void delete(String playerId) {

        Player userToDelete = get(playerId);
        sessionFactory.getCurrentSession().delete(userToDelete);

    }

    @Override
    @Transactional
    public Player get(String playerId) {
        String hql = "from Player where playerId=" + playerId;
        Query query = sessionFactory.getCurrentSession().createQuery(hql);

        @SuppressWarnings("unchecked")
        ArrayList<Player> listPlayer = (ArrayList<Player>) query.list();

        if (listPlayer != null && !listPlayer.isEmpty()) {
            return listPlayer.get(0);
        }

        return null;
    }
}
