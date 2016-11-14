package nl.knikit.cardgames.dao.impl;

import nl.knikit.cardgames.dao.IHandDao;
import nl.knikit.cardgames.dao.common.AbstractHibernateDao;
import nl.knikit.cardgames.model.hand;

import org.springframework.stereotype.Repository;

@Repository
public class HandDao extends AbstractHibernateDao<hand> implements IHandDao {

    public HandDao() {
        super();

        setClazz(hand.class);
    }

}
