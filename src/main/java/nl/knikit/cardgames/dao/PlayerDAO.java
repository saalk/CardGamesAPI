package nl.knikit.cardgames.dao;

import nl.knikit.cardgames.model.Player;
import java.util.ArrayList;

// PlayerDAO also called PlayerService
public interface PlayerDAO {
    public ArrayList<Player> list();
    public Player get(String id);
    public void delete(String id);
    public Player saveOrUpdate(Player player);
}
