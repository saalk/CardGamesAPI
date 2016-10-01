package nl.knikit.cardgames.service;

import nl.knikit.cardgames.dao.PlayerDAO;
import nl.knikit.cardgames.model.AiLevel;
import nl.knikit.cardgames.model.Origin;
import nl.knikit.cardgames.model.Player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * <H1>PlayerService</H1>
 *
 * @author Klaas van der Meulen
 */

@Slf4j
@Service
@Scope("prototype")
public class PlayerService extends ResourceSupport {

    // @Resource = javax, @Inject = javax, @Autowire = spring bean factory
    // @Controller (presentation layer), @Service (service layer) and @Repository (dao layer) conver all beans

    @Autowired
    private PlayerDAO playerDAO;

    private ArrayList<Player> players = new ArrayList<Player>();

    //TODO move to PLayerDAO instead of this temporary PlayerService constructor
    PlayerService() {
    }

    public ArrayList<Player> list() {
        if (players.size()==0) {
            players.add(new Player.PlayerBuilder()
                    //.withSequence(-1)
                    //.withPlayerId(null)
                    .withOrigin(Origin.ELF)
                    .withAlias("Java Doe")
                    .withIsHuman(true)
                    .withAiLevel(AiLevel.HUMAN)
                    .withCubits(0)
                    .withSecuredLoan(0)
                    .build()
            );
            players.add(new Player.PlayerBuilder()
                    //.withSequence(-1)
                    //.withPlayerId(null)
                    .withOrigin(Origin.ELF)
                    .withAlias("JavaAlien1")
                    .withIsHuman(false)
                    .withAiLevel(AiLevel.MEDIUM)
                    .withCubits(0)
                    .withSecuredLoan(0)
                    .build()
            );
            players.add(new Player.PlayerBuilder()
                    //.withSequence(-1)
                    //.withPlayerId(null)
                    .withOrigin(Origin.ELF)
                    .withAlias("JavaAlien2")
                    .withIsHuman(false)
                    .withAiLevel(AiLevel.MEDIUM)
                    .withCubits(0)
                    .withSecuredLoan(0)
                    .build()
            );
        }
        return players;
    }

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
        int index = 1;
        if (found) {
            // set all the next player's sequence back with 1 to align them neatly
            for (Player p : players) {
                p.setSequence(index);
                players.set(index, p);
                index++;
            }
            // set back the startId for next addition
            Player.setSequenceBackWithOne();
        }
        // use ternary operator (ternary = conditional operator -> expression?true:false)
        return (found ? sequence : 0);
    }

    public Player create(Player player) {

        // logging
        String message = String.format("Player from api: %s", player.toString());
        log.info(message);

        Player playerBuilder = new Player.PlayerBuilder()
                .withSequence(0)
                .withOrigin(player.getOrigin())
                .withAlias(player.getAlias())
                .withIsHuman(player.isHuman())
                .withAiLevel(player.getAiLevel())
                .withCubits(player.getCubits())
                .withSecuredLoan(player.getSecuredLoan())
                .build();

        // logging
        message = String.format("Player after Builder pattern: %s", playerBuilder.toString());
        log.info(message);

        players.add(playerBuilder);

        // logging
        message = String.format("Players after new player is added: %s\n", players.toString());
        log.info(message);

        try {
            playerDAO.createOrUpdate(playerBuilder);
        } catch (Exception e) {
            log.error("Exception while saving or updating Player: ", e);
        }

        return playerBuilder;
    }

    public void deleteAll() {
        players.clear();
        Player.setSequenceToFirst();
    }

    public Player update(Player player) {
        int index = 0;
        for (Player p : players) {
            if (p.getPlayerId() == player.getPlayerId()) {
                // Use ArrayList remove and add to make a new sequence or set to change the current
                Player playerBuilder = new Player.PlayerBuilder()
                        .withId(player.getId())
                        .withPlayerId(player.getPlayerId())
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

    public Player updateBySequence(Player player) {
        int index = 0;
        for (Player p : players) {
            if (p.getSequence() == player.getSequence()) {
                // Use ArrayList remove and add to make a new sequence or set to change the current
                Player playerBuilder = new Player.PlayerBuilder()
                        .withSequence(player.getSequence())
                        .withPlayerId(player.getPlayerId())
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