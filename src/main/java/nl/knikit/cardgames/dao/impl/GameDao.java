package nl.knikit.cardgames.dao.impl;

import nl.knikit.cardgames.dao.IGameDao;
import nl.knikit.cardgames.dao.common.AbstractHibernateDao;
import nl.knikit.cardgames.model.Game;
import org.springframework.stereotype.Repository;

@Repository
public class GameDao extends AbstractHibernateDao<Game> implements IGameDao {

    public GameDao() {
        super();

        setClazz(Game.class);
    }

    // API

}
