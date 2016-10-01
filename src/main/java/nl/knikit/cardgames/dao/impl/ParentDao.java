package nl.knikit.cardgames.dao.impl;

import nl.knikit.cardgames.dao.IParentDao;
import nl.knikit.cardgames.dao.common.AbstractHibernateDao;
import nl.knikit.cardgames.model.Parent;
import org.springframework.stereotype.Repository;

@Repository
public class ParentDao extends AbstractHibernateDao<Parent> implements IParentDao {

    public ParentDao() {
        super();

        setClazz(Parent.class);
    }

    // API

}
