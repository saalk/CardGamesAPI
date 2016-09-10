package nl.knikit.cardgames.DAO;

import nl.knikit.cardgames.model.Player;
import java.util.ArrayList;

public interface PlayerDAOInterface {
    public ArrayList<Player> list();
    public Player get(String id);
    public void delete(String id);
    public Player saveOrUpdate(Player player);
}
