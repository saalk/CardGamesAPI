package nl.knikit.cardgames.dao;

import nl.knikit.cardgames.model.Player;

import javax.annotation.Resource;
import java.util.ArrayList;

public interface PlayerDAO {

    public ArrayList<Player> listAll();
    public ArrayList<Player> findAllForCriteria(String criteria);
    public Player get(String playerId);
    public void delete(String playerId);
    public void createOrUpdate(Player player);
}
