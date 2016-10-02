package nl.knikit.cardgames.dao.impl;

import nl.knikit.cardgames.dao.ICasinoDao;
import nl.knikit.cardgames.dao.common.AbstractHibernateDao;
import nl.knikit.cardgames.model.Casino;
import org.springframework.stereotype.Repository;

@Repository
public class CasinoDao extends AbstractHibernateDao<Casino> implements ICasinoDao {

    public CasinoDao() {
        super();

        setClazz(Casino.class);
    }

    // API

}
