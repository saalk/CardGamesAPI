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

    6 REST Endpoint                 HTTP Method 	Description
    /players 	                    GET             Returns the list of players
    /players/{id}                   GET             Returns player detail for given player {id}
    /players 	                    POST            Creates new player from the post data
    /players/{id}                   PUT             Replace the details for given player {id}
    /players 	                    DELETE          Deletes all players
    /players/{id}                   DELETE          Delete the player for given player {id}
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


import nl.knikit.cardgames.exception.PlayerNotFoundForIdException;
import nl.knikit.cardgames.service.IPlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.hateoas.ExposesResourceFor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import org.springframework.web.bind.annotation.*;

import nl.knikit.cardgames.model.Player;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;

// @RestController = @Controller + @ResponseBody
@CrossOrigin
@RestController
@Component
@ExposesResourceFor(Player.class)
@Slf4j
@Scope("prototype")
public class PlayerController {

    // @Resource = javax, @Inject = javax, @Autowire = spring bean factory
    @Autowired
    private IPlayerService playerService;

    @GetMapping("/players")
    public ResponseEntity<ArrayList<Player>> getPlayers() {

        ArrayList<Player> players;
        players = (ArrayList) playerService.findAll();
        return new ResponseEntity(players, HttpStatus.OK);
    }

    @GetMapping("/players/{id}")
    public ResponseEntity getPlayer(@PathVariable("id") int id) throws PlayerNotFoundForIdException {

        Player player = playerService.findOne(id);
        if (player == null) {

            throw new PlayerNotFoundForIdException(id);
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
        playerService.create(player);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(player);
    }

    @DeleteMapping("/players")
    public ResponseEntity deletePlayers() {

        playerService.deleteAll();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("All Players deleted");

    }

    @DeleteMapping("/players/{id}")
    public ResponseEntity deletePlayer(@PathVariable int id) {

        try {
            playerService.deleteById(id);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No Player with /{id}: " + id + " found to delete");
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Player with /{id}: " + id + " deleted");
    }

    @PutMapping("/players/{id}")
    public ResponseEntity updatePlayer(@PathVariable int id, @RequestBody Player
            player) {
        Player newPlayer = playerService.update(player);
        if (null == newPlayer) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No Player found to change for path /{id): " + id);
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newPlayer);
    }

    // To handle an exception, we need to create an exception method annotated with @ExceptionHandler.
    // This method will return java bean as JSON with error info. Returning ModelAndView with HTTP 200
    @ExceptionHandler(PlayerNotFoundForIdException.class)
    public ModelAndView handlePlayerNotFoundForIdException(HttpServletRequest request, Exception ex) {
        log.error("Requested URL=" + request.getRequestURL());
        log.error("Exception Raised=" + ex);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exception", ex);
        modelAndView.addObject("url", request.getRequestURL());

        modelAndView.setViewName("error");
        return modelAndView;
    }

}
