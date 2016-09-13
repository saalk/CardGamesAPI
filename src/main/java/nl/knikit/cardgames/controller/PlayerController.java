package nl.knikit.cardgames.controller;

/*
    http://viralpatel.net/blogs/spring-4-mvc-rest-example-json/

    This class is annotated with @RestController annotation. Also note that we are using
    new annotations @GetMapping, @PostMapping, @PutMapping and @DeleteMapping instead of
    standard @RequestMapping.

    These annotations are available since Spring MVC 4.3 and are standard way of defining REST
    endpoints. They act as wrapper to @RequestMapping.
    For example @GetMapping is a composed annotation that acts as a shortcut for
    @RequestMapping(method = RequestMethod.GET).

    9 REST Endpoint                 HTTP Method 	Description
    /players 	                    GET     Returns the list of players
    /players/{id}                   GET     Returns player detail for given player {id}
    /players/?sequence={sequence}   GET     Returns player detail for given player {sequence}
    /players 	                    POST    Creates new player from the post data
    /players/{id}                   PUT     Replace the details for given player {id}
    /players/?sequence={sequence}   PUT     Replace the details for given player {sequence}
    /players 	                    DELETE  Deletes all players
    /players/{id}                   DELETE  Delete the player for given player {id}
    /players/?sequence={sequence}   DELETE  Delete the player for given player {sequence}
*/


/*  The log levels in Java Logging API are different to other standard logging libraries
    Log4j/Logback	Java Logging
    fatal	        SEVERE
    error	        SEVERE
    warn	        WARNING
    info	        INFO
                    CONFIG
    debug	        FINE
                    FINER
    trace	        FINEST
*/


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nl.knikit.cardgames.service.PlayerService;
import nl.knikit.cardgames.model.Player;

import java.util.ArrayList;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

// @RestController = @Controller + @ResponseBody
@RestController
@Component
@ExposesResourceFor(Player.class)
@Slf4j
public class PlayerController {

    // @Resource = javax, @Inject = javax, @Autowire = spring bean factory
    @Autowired
    private PlayerService playerService;

    @GetMapping("/players")
    public ResponseEntity<ArrayList<Player>> getPlayers() {

        ArrayList<Player> players;
        players = playerService.list();
        return new ResponseEntity(players, HttpStatus.OK);
    }

    @GetMapping("/players/{id}")
    public ResponseEntity getPlayer(@PathVariable("id") Long id) {

        Player player = playerService.get(id);
        if (player == null) {

            // %s, %d print string an dinteger as is use with new line \n
            String message = String.format("No Player found for path id: %d\n", id);
            // @Slf4j makes log available

            log.warn(message);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(message);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(player);
    }

    @GetMapping(value = "/players", params = "sequence")
    public ResponseEntity getPlayerBySequence(@RequestParam("sequence") int sequence) {

        Player player = playerService.getBySequence(sequence);
        if (player == null) {

            String message = String.format("No Player found for param sequence: %d\n", sequence);
            log.warn(message);

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(message);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(player);
    }

    @PostMapping("/players")
    public ResponseEntity createPlayer(@RequestBody Player player) {

        if (player == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .body("No Player supplied to create: " + player);
        }
        Player newPlayer = playerService.create(player);
        if (newPlayer == null) {
            return ResponseEntity
                    .status(HttpStatus.PRECONDITION_FAILED)
                    .body("Incorrect Player data supplied: " + player);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(newPlayer);
    }

    @DeleteMapping("/players/{id}")
    public ResponseEntity deletePlayer(@PathVariable Long id) {

        Long playerId = playerService.delete(id);
        if (playerId == -1) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No Player found to delete for path id: " + id);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Player with id: " + id + " deleted");
    }

    @DeleteMapping(value = "/players", params = "sequence")
    public ResponseEntity deletePlayerBySequence(@RequestParam("sequence") int sequence) {

        int playerSequence = playerService.deleteBySequence(sequence);
        if (-1 == playerSequence) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No Player found to delete for param sequence supplied: " + sequence);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Player with sequence: " + playerSequence + " deleted");
    }

    @DeleteMapping("/players")
    public ResponseEntity deleteAllPlayers() {

        playerService.deleteAll();
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("All Players deleted");
    }

    @PutMapping("/players/{id}")
    public ResponseEntity updatePlayer(@PathVariable Long id, @RequestBody Player
            player) {
        Player newPlayer = playerService.update(id, player);
        if (null == newPlayer) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No Player found to change for path id: " + id);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(newPlayer);
    }

    @PutMapping(value = "/players", params = "sequence")
    public ResponseEntity updatePlayerBySequence(
            @RequestParam("sequence") int sequence, @RequestBody Player
            player) {
        Player newPlayer = playerService.updateBySequence(sequence, player);
        if (null == newPlayer) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No Player found to change for param sequence: " +
                            sequence);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(newPlayer);
    }
}
