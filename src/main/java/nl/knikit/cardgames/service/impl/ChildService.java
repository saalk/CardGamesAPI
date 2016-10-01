package nl.knikit.cardgames.service.impl;

import nl.knikit.cardgames.dao.IChildDao;
import nl.knikit.cardgames.dao.common.IOperations;
import nl.knikit.cardgames.model.Child;
import nl.knikit.cardgames.service.IChildService;
import nl.knikit.cardgames.service.common.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChildService extends AbstractService<Child> implements IChildService {

    @Autowired
    private IChildDao dao;

    public ChildService() {
        super();
    }

    // API

    @Override
    protected IOperations<Child> getDao() {
        return dao;
    }

}
