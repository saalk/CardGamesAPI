package nl.knikit.cardgames.dao;

import lombok.extern.slf4j.Slf4j;
import nl.knikit.cardgames.model.Player;


import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

// Implementing dao for all the crud in the dao interface
@Slf4j
@Repository
@Scope("prototype")
public class PlayerDAOImpl implements PlayerDAO {

    // the basic interfaces between a Java application and Hibernate
    // every method that interact with the database gets a session via the factory
    @Autowired
    private SessionFactory sessionFactory;
    protected Session getCurrentSession(){
        return sessionFactory.getCurrentSession();
    }

    public PlayerDAOImpl() {
    }

    public PlayerDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public ArrayList<Player> listAll() {

        @SuppressWarnings("unchecked")
        ArrayList<Player> listPlayer = (ArrayList<Player>) getCurrentSession()
                .createCriteria(Player.class)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();

        return listPlayer;
    }

    @Override
    public ArrayList<Player> findAllForCriteria(String criteria) {
        @SuppressWarnings("unchecked")
        ArrayList<Player> listPlayer = (ArrayList<Player>) getCurrentSession()
                .createCriteria(Player.class)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();

        return listPlayer;
    }

    @Override
    @Transactional
    public void createOrUpdate(Player player) {
        String message = String.format("Player in DAO: %s", player.toString());
        log.info(message);
        getCurrentSession().saveOrUpdate(player);

    }

    @Override
    public void delete(String playerId) {

        Player userToDelete = get(playerId);
        getCurrentSession().delete(userToDelete);

    }

    @Override
     public Player get(String playerId) {
        String hql = "from Player where playerId=" + playerId;
        Query query = getCurrentSession().createQuery(hql);

        @SuppressWarnings("unchecked")
        ArrayList<Player> listPlayer = (ArrayList<Player>) query.list();

        if (listPlayer != null && !listPlayer.isEmpty()) {
            return listPlayer.get(0);
        }

        return null;
    }
}
