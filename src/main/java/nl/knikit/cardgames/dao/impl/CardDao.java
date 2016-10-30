package nl.knikit.cardgames.dao.impl;

import nl.knikit.cardgames.dao.ICardDao;
import nl.knikit.cardgames.dao.common.AbstractHibernateDao;
import nl.knikit.cardgames.model.Card;

import org.springframework.stereotype.Repository;

@Repository
public class CardDao extends AbstractHibernateDao<Card> implements ICardDao {

    public CardDao() {
        super();

        setClazz(Card.class);
    }

}
