package nl.knikit.cardgames.dao;

import nl.knikit.cardgames.model.Player;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Repository
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public interface PlayerDAO {

    public ArrayList<Player> listAll();
    public ArrayList<Player> findAllForCriteria(String criteria);
    public Player get(String playerId);
    public void delete(String playerId);
    public void createOrUpdate(Player player);
}
