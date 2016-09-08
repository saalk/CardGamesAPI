package nl.knikit.cardgames.Service;

import nl.knikit.cardgames.model.AiLevel;
import nl.knikit.cardgames.model.Origin;
import nl.knikit.cardgames.model.Player;

import org.springframework.context.annotation.Scope;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * <H1>PlayerService</H1>
 *
 * @author Klaas van der Meulen
 */

@Service
@Scope("prototype")
public class PlayerService extends ResourceSupport {

    private ArrayList<Player> players = new ArrayList<Player>();

    //TODO get from database
    PlayerService() {
        players.add(new Player.PlayerBuilder()
                .withSequence(-1)
                .withOrigin(Origin.ELF)
                .withAlias("John Doe")
                .withIsHuman(true)
                .withAiLevel(AiLevel.HUMAN)
                .build()
        );
        players.add(new Player.PlayerBuilder()
                .withSequence(-1)
                .withOrigin(Origin.ELF)
                .withAlias("Alien1")
                .withIsHuman(false)
                .withAiLevel(AiLevel.MEDIUM)
                .build()
        );
        players.add(new Player.PlayerBuilder()
                .withSequence(-1)
                .withOrigin(Origin.ELF)
                .withAlias("Alien2")
                .withIsHuman(false)
                .withAiLevel(AiLevel.MEDIUM)
                .build()
        );
    }

    public ArrayList<Player> list() {
        return players; }

    public Player get(String uid) {

        for (Player p : players)
            if (p.getUid().equals(uid)) {
                return p;
            }
        return null;
    }

    public Player getBySequence(int sequence) {

        for (Player p : players)
            if (p.getSequence() == sequence) {
                return p;
            }
        return null;
    }

    public String delete(String uid) {
        boolean found = false;
        for (Player p : players) {
            if (p.getUid() == uid) {
                players.remove(p);
                // break to avoid concurrency problems for next iteration when removing last...
                found = true;
                break;
            }
        }
        int index = 0;
        if (found) {
            // set all the next players with sequence - 1 to align them neatly
            for (Player p : players) {
                p.setSequence(index);
                players.set(index, p);
                index++;
            }
            // set back the startId for next addition
            Player.setSequenceBackWithOne();
        }
        // use ternary operator (ternary = conditional operator -> expression?true:false)
        return (found ? uid : "-1");
    }

    public int deleteBySequence(int sequence) {
        boolean found = false;
        for (Player p : players) {
            if (p.getSequence() == sequence) {
                players.remove(p);
                // break to avoid concurrency problems for next iteration when removing last...
                found = true;
                break;
            }
        }
        int index = 0;
        if (found) {
            // set all the next players with sequence - 1 to align them neatly
            for (Player p : players) {
                p.setSequence(index);
                players.set(index, p);
                index++;
            }
            // set back the startId for next addition
            Player.setSequenceBackWithOne();
        }
        // use ternary operator (ternary = conditional operator -> expression?true:false)
        return (found ? sequence : -1);
    }

    public Player create(Player player) {
        Player playerBuilder = new Player.PlayerBuilder()
                .withSequence(-1)
                .withOrigin(player.getOrigin())
                .withAlias(player.getAlias())
                .withIsHuman(player.isHuman())
                .withAiLevel(player.getAiLevel())
                .withCubits(player.getCubits())
                .withSecuredLoan(player.getSecuredLoan())
                .build();
        players.add(playerBuilder);
        // return the new player by looking in array for playerBuilder new sequence;
        return players.get(playerBuilder.getSequence());
    }

    public void deleteAll() {
        players.clear();
        Player.setSequenceToZero();
    }

    public Player update(String uid, Player player) {
        int index = 0;
        for (Player p : players) {
            if (p.getUid() == uid) {
                // Use ArrayList remove and add to make a new sequence or set to change the current
                Player playerBuilder = new Player.PlayerBuilder()
                        .withSequence(player.getSequence())
                        .withOrigin(player.getOrigin())
                        .withAlias(player.getAlias())
                        .withIsHuman(player.isHuman())
                        .withAiLevel(player.getAiLevel())
                        .withCubits(player.getCubits())
                        .withSecuredLoan(player.getSecuredLoan())
                        .build();
                players.set(index, playerBuilder);
                return players.get(index);
            }
            index++;
        }
        return null;
    }

    public Player updateBySequence(int sequence, Player player) {
        int index = 0;
        for (Player p : players) {
            if (p.getSequence() == sequence) {
                // Use ArrayList remove and add to make a new sequence or set to change the current
                Player playerBuilder = new Player.PlayerBuilder()
                        .withSequence(sequence)
                        .withOrigin(player.getOrigin())
                        .withAlias(player.getAlias())
                        .withIsHuman(player.isHuman())
                        .withAiLevel(player.getAiLevel())
                        .withCubits(player.getCubits())
                        .withSecuredLoan(player.getSecuredLoan())
                        .build();
                players.set(index, playerBuilder);
                // do not use
                // players.remove(p);
                // players.add(playerBuilder);
                return players.get(index);
              }
            index++;
        }
        return null;
    }
}