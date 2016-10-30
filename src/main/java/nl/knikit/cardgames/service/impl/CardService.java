package nl.knikit.cardgames.service.impl;

import nl.knikit.cardgames.dao.ICardDao;
import nl.knikit.cardgames.dao.common.IOperations;
import nl.knikit.cardgames.model.Card;
import nl.knikit.cardgames.service.ICardService;
import nl.knikit.cardgames.service.common.AbstractService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardService extends AbstractService<Card> implements ICardService {

    @Autowired
    private ICardDao dao;

    public CardService() {
        super();
    }

    // API

    @Override
    protected IOperations<Card> getDao() {
        return dao;
    }

}
