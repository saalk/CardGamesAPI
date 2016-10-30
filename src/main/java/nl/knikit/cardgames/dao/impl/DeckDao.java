package nl.knikit.cardgames.dao.impl;

import nl.knikit.cardgames.dao.IDeckDao;
import nl.knikit.cardgames.dao.common.AbstractHibernateDao;
import nl.knikit.cardgames.model.Deck;

import org.springframework.stereotype.Repository;

@Repository
public class DeckDao extends AbstractHibernateDao<Deck> implements IDeckDao {

    public DeckDao() {
        super();

        setClazz(Deck.class);
    }

}
