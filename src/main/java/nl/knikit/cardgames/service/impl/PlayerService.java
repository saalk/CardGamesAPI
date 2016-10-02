package nl.knikit.cardgames.service.impl;

import nl.knikit.cardgames.dao.IPlayerDao;
import nl.knikit.cardgames.dao.common.IOperations;
import nl.knikit.cardgames.model.Player;
import nl.knikit.cardgames.service.IPlayerService;
import nl.knikit.cardgames.service.common.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerService extends AbstractService<Player> implements IPlayerService {

    @Autowired
    private IPlayerDao dao;

    public PlayerService() {
        super();
    }

    // API

    @Override
    protected IOperations<Player> getDao() {
        return dao;
    }

}
