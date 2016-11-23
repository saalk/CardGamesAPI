package nl.knikit.cardgames.dao.impl;

import nl.knikit.cardgames.dao.IHandDao;
import nl.knikit.cardgames.dao.common.AbstractHibernateDao;
import nl.knikit.cardgames.model.Hand;

import org.springframework.stereotype.Repository;

@Repository
public class HandDao extends AbstractHibernateDao<Hand> implements IHandDao {

    public HandDao() {
        super();

        setClazz(Hand.class);
    }

}
