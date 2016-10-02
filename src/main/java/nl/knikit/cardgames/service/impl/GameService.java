package nl.knikit.cardgames.service.impl;

import nl.knikit.cardgames.dao.IGameDao;
import nl.knikit.cardgames.dao.common.IOperations;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.service.IGameService;
import nl.knikit.cardgames.service.common.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService extends AbstractService<Game> implements IGameService {

    @Autowired
    private IGameDao dao;

    public GameService() {
        super();
    }

    // API

    @Override
    protected IOperations<Game> getDao() {
        return dao;
    }

}
