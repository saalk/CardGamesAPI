package nl.knikit.cardgames.service.impl;

import nl.knikit.cardgames.dao.IHandDao;
import nl.knikit.cardgames.dao.common.IOperations;
import nl.knikit.cardgames.model.hand;
import nl.knikit.cardgames.service.IHandService;
import nl.knikit.cardgames.service.common.AbstractService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HandService extends AbstractService<hand> implements IHandService {

    @Autowired
    private IHandDao dao;

    public HandService() {
        super();
    }

    // API

    @Override
    protected IOperations<hand> getDao() {
        return dao;
    }

}
