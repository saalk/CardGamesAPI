package nl.knikit.cardgames.service.impl;

import nl.knikit.cardgames.dao.IParentDao;
import nl.knikit.cardgames.dao.common.IOperations;
import nl.knikit.cardgames.model.Parent;
import nl.knikit.cardgames.service.IParentService;
import nl.knikit.cardgames.service.common.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParentService extends AbstractService<Parent> implements IParentService {

    @Autowired
    private IParentDao dao;

    public ParentService() {
        super();
    }

    // API

    @Override
    protected IOperations<Parent> getDao() {
        return dao;
    }

}
