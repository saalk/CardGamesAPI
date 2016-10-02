package nl.knikit.cardgames.service.impl;

import nl.knikit.cardgames.dao.ICasinoDao;
import nl.knikit.cardgames.dao.common.IOperations;
import nl.knikit.cardgames.model.Casino;
import nl.knikit.cardgames.service.ICasinoService;
import nl.knikit.cardgames.service.common.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CasinoService extends AbstractService<Casino> implements ICasinoService {

    @Autowired
    private ICasinoDao dao;

    public CasinoService() {
        super();
    }

    // API

    @Override
    protected IOperations<Casino> getDao() {
        return dao;
    }

}
