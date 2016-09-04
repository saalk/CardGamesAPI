package nl.knikit.cardgames.Service;

import nl.knikit.cardgames.model.AiLevel;
import nl.knikit.cardgames.model.Origin;
import nl.knikit.cardgames.model.Player2;

import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * <H1>PlayerDAO</H1>
 *
 * @author Klaas van der Meulen
 * @version 1.0
 * @since v1 - console game
 */

@Service
public class PlayerService {

    private ArrayList<Player2> players = new ArrayList<Player2>();

    //hard coded but usually get from database
    PlayerService() {
        players.add(new Player2(0, Origin.ELF, "Alias1", true, AiLevel.HUMAN, 0, 0));
        players.add(new Player2(1, Origin.ELF, "Alias2", false, AiLevel.NONE, 0, 0));
        players.add(new Player2(2, Origin.ELF, "Alias3", false, AiLevel.NONE, 0, 0));
    }
     /**
     * Returns list of players from dummy database.
     *
     * @return list of players
     */
    public ArrayList<Player2> list() {
        return players;
    };

    /**
     * Return Player object for given id from dummy database. If Player is
     * not found for id, returns null.
     *
     * @param id Player id
     * @return Player object for given id
     */
    public Player2 get(int id) {

        for (Player2 p : players)
            if (p.getId() == id) {
                return p;
            }
        return null;
    }

    /**
     * Create new Player in dummy database. Updates the id and insert new
     * Player in list.
     *
     * @param player
     *            Player object
     * @return Player object with updated id
     */
    public Player2 create(Player2 player) {

        players.add(player);
        return player;
    }

    /**
     * Delete the customer object from dummy database. If customer not found for
     * given id, returns null.
     *
     * @param id
     *            the Player id
     * @return id of deleted customer object
     */
    public int delete(int id) {

        for (Player2 p : players) {
            if (p.getId() == id) {
                players.remove(p);
                return id;
            }
        }

        return -1;
    }

    /**
     * Update the Player object for given id in dummy database. If Player
     * not exists, returns null
     *
     * @param player
     * @return Player object with id
     */
    public Player2 update(int id, Player2 player) {

        for (Player2 p : players) {
            if (p.getId() == id) {
                players.remove(p);
                players.add(player);
                return player;
            }
        }
        return null;
    }

}