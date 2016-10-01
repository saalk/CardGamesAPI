package nl.knikit.cardgames.service.impl;

import nl.knikit.cardgames.dao.IFooDao;
import nl.knikit.cardgames.dao.common.IOperations;
import nl.knikit.cardgames.model.Foo;
import nl.knikit.cardgames.service.IFooService;
import nl.knikit.cardgames.service.common.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FooService extends AbstractService<Foo> implements IFooService {

    @Autowired
    private IFooDao dao;

    public FooService() {
        super();
    }

    // API

    @Override
    protected IOperations<Foo> getDao() {
        return dao;
    }

}
