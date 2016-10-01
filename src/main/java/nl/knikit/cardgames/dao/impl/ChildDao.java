package nl.knikit.cardgames.dao.impl;

import nl.knikit.cardgames.dao.IChildDao;
import nl.knikit.cardgames.dao.common.AbstractHibernateDao;
import nl.knikit.cardgames.model.Child;
import org.springframework.stereotype.Repository;

@Repository
public class ChildDao extends AbstractHibernateDao<Child> implements IChildDao {

    public ChildDao() {
        super();

        setClazz(Child.class);
    }

    // API

}
