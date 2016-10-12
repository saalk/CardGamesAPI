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
    /games 	                    GET             Returns the list of games
    /games/{id}                   GET             Returns game detail for given game {id}
    /games 	                    POST            Creates new game from the post data
    /games/{id}                   PUT             Replace the details for given game {id}
    /games 	                    DELETE          Deletes all games
    /games/{id}                   DELETE          Delete the game for given game {id}
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


import lombok.extern.slf4j.Slf4j;
import nl.knikit.cardgames.exception.GameNotFoundForIdException;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.service.IGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

// @RestController = @Controller + @ResponseBody
@RestController
@Component
@ExposesResourceFor(Game.class)
@Slf4j
@Scope("prototype")
public class GameController {

    // @Resource = javax, @Inject = javax, @Autowire = spring bean factory
    @Autowired
    private IGameService gameService;

    @GetMapping("/games")
    public ResponseEntity<ArrayList<Game>> getGames() {

        ArrayList<Game> games;
        games = (ArrayList) gameService.findAll();
        return new ResponseEntity(games, HttpStatus.OK);
    }

    @GetMapping("/games/{id}")
    public ResponseEntity getGame(@PathVariable("id") int id) throws GameNotFoundForIdException {

        Game game = gameService.findOne(id);
        if (game == null) {

            throw new GameNotFoundForIdException(id);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(game);
    }

    @PostMapping("/games")
    public ResponseEntity createGame(@RequestBody Game game) {

        if (game == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .body("No Game supplied to create: " + game);
        }
        gameService.create(game);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(game);
    }

    @PutMapping("/games/{id}")
    public ResponseEntity updateGame(@PathVariable int id, @RequestBody Game
            game) {
        Game newGame = gameService.update(game);
        if (null == newGame) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No Game found to change for path /{id): " + id);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(newGame);
    }

    @DeleteMapping("/games/{id}")
    public ResponseEntity deleteGames(@PathVariable("id") int id) {

        try {
            Game classGame = new Game();
            classGame.setId(id);
            gameService.delete(classGame);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No Casino with /{id}: " + id + " found to delete");
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Casino with /{id}: " + id + " deleted");
    }

    // To handle an exception, we need to create an exception method annotated with @ExceptionHandler.
    // This method will return java bean as JSON with error info. Returning ModelAndView with HTTP 200
    @ExceptionHandler(GameNotFoundForIdException.class)
    public ModelAndView handleGameNotFoundForIdException(HttpServletRequest request, Exception ex) {
        log.error("Requested URL=" + request.getRequestURL());
        log.error("Exception Raised=" + ex);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exception", ex);
        modelAndView.addObject("url", request.getRequestURL());

        modelAndView.setViewName("error");
        return modelAndView;
    }

}
