package nl.knikit.cardgames.service.impl;

import nl.knikit.cardgames.dao.IDeckDao;
import nl.knikit.cardgames.dao.common.IOperations;
import nl.knikit.cardgames.model.Deck;
import nl.knikit.cardgames.service.IDeckService;
import nl.knikit.cardgames.service.common.AbstractService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeckService extends AbstractService<Deck> implements IDeckService {

    @Autowired
    private IDeckDao dao;

    public DeckService() {
        super();
    }

    // API

    @Override
    protected IOperations<Deck> getDao() {
        return dao;
    }

}
