package nl.knikit.cardgames.dao;

import nl.knikit.cardgames.model.Player;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

// Implementing dao for all the crud in the dao interface
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
    public ArrayList<Player> list() {
        @SuppressWarnings("unchecked")
        ArrayList<Player> listPlayer = (ArrayList<Player>) sessionFactory.getCurrentSession()
                .createCriteria(Player.class)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();

        return listPlayer;
    }

    @Override
    @Transactional
    public Player saveOrUpdate(Player player) {
        sessionFactory.getCurrentSession().saveOrUpdate(player);
        return player;
    }

    @Override
    @Transactional
    public void delete(String id) {
        Player playerToDelete = new Player();
        playerToDelete.setId(id);
        sessionFactory.getCurrentSession().delete(playerToDelete);
    }

    @Override
    @Transactional
    public Player get(String id) {
        String hql = "from Player where id=" + id;
        Query query = sessionFactory.getCurrentSession().createQuery(hql);

        @SuppressWarnings("unchecked")
        ArrayList<Player> listPlayer = (ArrayList<Player>) query.list();

        if (listPlayer != null && !listPlayer.isEmpty()) {
            return listPlayer.get(0);
        }

        return null;
    }
}
