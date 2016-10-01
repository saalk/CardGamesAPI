package nl.knikit.cardgames.dao.impl;

import nl.knikit.cardgames.dao.IFooDao;
import nl.knikit.cardgames.dao.common.AbstractHibernateDao;
import nl.knikit.cardgames.model.Foo;
import org.springframework.stereotype.Repository;

@Repository
public class FooDao extends AbstractHibernateDao<Foo> implements IFooDao {

    public FooDao() {
        super();

        setClazz(Foo.class);
    }

    // API

}
