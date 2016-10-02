package nl.knikit.cardgames.dao.impl;

import nl.knikit.cardgames.dao.IPlayerDao;
import nl.knikit.cardgames.dao.common.AbstractHibernateDao;
import nl.knikit.cardgames.model.Player;
import org.springframework.stereotype.Repository;

@Repository
public class PlayerDao extends AbstractHibernateDao<Player> implements IPlayerDao {

    public PlayerDao() {
        super();

        setClazz(Player.class);
    }

    // API

}
