package nl.knikit.cardgames.DAO;

import nl.knikit.cardgames.model.Player;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

// DAO for all the crud
@Repository
public class PlayerDAO implements PlayerDAOInterface {

    // the basic interfaces between a Java application and Hibernate
    // every method that interact with the database gets a session via the factory
    @Autowired
    private SessionFactory sessionFactory;

    public PlayerDAO() {

    }

    public PlayerDAO(SessionFactory sessionFactory) {
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
    public void delete(String uid) {
        Player playerToDelete = new Player();
        playerToDelete.setUid(uid);
        sessionFactory.getCurrentSession().delete(playerToDelete);
    }

    @Override
    @Transactional
    public Player get(String uid) {
        String hql = "from Player where id=" + uid;
        Query query = sessionFactory.getCurrentSession().createQuery(hql);

        @SuppressWarnings("unchecked")
        ArrayList<Player> listPlayer = (ArrayList<Player>) query.list();

        if (listPlayer != null && !listPlayer.isEmpty()) {
            return listPlayer.get(0);
        }

        return null;
    }
}
