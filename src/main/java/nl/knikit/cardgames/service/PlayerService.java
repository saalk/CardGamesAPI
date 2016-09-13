package nl.knikit.cardgames.service;

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

    //TODO move to PLayerDAO instead of this temporary PlayerService
    PlayerService() {
        players.add(new Player.PlayerBuilder()
                .withSequence(-1)
                .withCreated(null)
                .withOrigin(Origin.ELF)
                .withAlias("John Doe")
                .withIsHuman(true)
                .withAiLevel(AiLevel.HUMAN)
                .withCubits(0)
                .withSecuredLoan(0)
                .build()
        );
        players.add(new Player.PlayerBuilder()
                .withSequence(-1)
                .withCreated(null)
                .withOrigin(Origin.ELF)
                .withAlias("Alien1")
                .withIsHuman(false)
                .withAiLevel(AiLevel.MEDIUM)
                .withCubits(0)
                .withSecuredLoan(0)
                .build()
        );
        players.add(new Player.PlayerBuilder()
                .withSequence(-1)
                .withCreated(null)
                .withOrigin(Origin.ELF)
                .withAlias("Alien2")
                .withIsHuman(false)
                .withAiLevel(AiLevel.MEDIUM)
                .withCubits(0)
                .withSecuredLoan(0)
                .build()
        );
    }

    public ArrayList<Player> list() {
        return players; }

    public Player get(Long id) {

        for (Player p : players)
            if (p.getId() == id) {
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

    public Long delete(Long id) {
        boolean found = false;
        for (Player p : players) {
            if (p.getId() == id) {
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
        return (found ? id : -1);
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
                .withId(player.getId())
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

    public Player update(Long id, Player player) {
        int index = 0;
        for (Player p : players) {
            if (p.getId() == id) {
                // Use ArrayList remove and add to make a new sequence or set to change the current
                Player playerBuilder = new Player.PlayerBuilder()
                        .withId(player.getId())
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
                        .withId(player.getId())
                        .withSequence(sequence)
                        .withCreated(player.getCreated())
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