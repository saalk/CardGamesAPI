package nl.knikit.cardgames.resource;


import nl.knikit.cardgames.exception.GameNotFoundForIdException;
import nl.knikit.cardgames.model.Player;
import nl.knikit.cardgames.service.IGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.hateoas.ExposesResourceFor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import nl.knikit.cardgames.model.Game;
import java.util.ArrayList;
import java.util.List;


import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;

// @RestController = @Controller + @ResponseBody
@CrossOrigin
@RestController
@Component
@ExposesResourceFor(Game.class)
@Slf4j
@Scope("prototype")
public class GameResource {

    // @Resource = javax, @Inject = javax, @Autowire = spring bean factory
    @Autowired
    private IGameService gameService;

    @GetMapping("/games")
    public ResponseEntity<ArrayList<Game>> getGames() {

        ArrayList<Game> games;
        games = (ArrayList) gameService.findAll("cardGameType", "ASC");
        return new ResponseEntity(games, HttpStatus.OK);
    }

    @GetMapping("/games/{id}")
    public ResponseEntity getGame(
            @PathVariable("id") int id) throws GameNotFoundForIdException {

        Game game = gameService.findOne(id);
        if (game == null) {

            throw new GameNotFoundForIdException(id);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(game);
    }

    // @QueryParam is a JAX-RS framework annotation and @RequestParam is from Spring
    //
    // SPRING
    // use @RequestParam(value = "date", required = false, defaultValue = "01-01-1999") Date dateOrNull)
    // you get the Date dataOrNull for ?date=12-05-2013
    //
    // JAX_RS
    // also use: @DefaultValue("false") @QueryParam("from") boolean isHuman
    // you get the boolean isHuman with value 'true' for ?isHuman=true

    @GetMapping(value = "/games", params = { "cardGameType" } )
    public ResponseEntity<ArrayList<Game>> findAllWhere(
            @RequestParam(value = "cardGameType", required = true) String param) {

        Game classGame = new Game(0);
        // ternary operator = shorthand if for conditional assignment -> The ? : operator in Java
        // boolean isHumanBoolean = ( param=="true" )?true:false;
        // classGame.setHuman( isHumanBoolean );

        try {

            ArrayList<Game> games = (ArrayList) gameService.findAllWhere(classGame, "cardGameType", param);
            if (games == null || games.isEmpty()) {
                throw new GameNotFoundForIdException(999);
            }

            return new ResponseEntity(games, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ArrayList<Game>() );
        }
    }

    @PostMapping(name = "/games")
    public ResponseEntity createGame(
            @RequestBody Game game) {

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

    @PostMapping(name = "/games", params = { "jokers" } )
    public ResponseEntity createGame(
            @RequestParam(value = "jokers", required = false, defaultValue = "0") Integer jokers,
            @RequestBody Game game) {

        // TODO jokers param
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
    public ResponseEntity updateGame(
            @PathVariable int id, @RequestBody Game game) {

        Game newGame = gameService.update(game);
        if (null == newGame) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No Game found to change for path /{id): " + id);
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newGame);
    }

    @DeleteMapping("/games/{id}")
    public ResponseEntity deleteGames(
            @PathVariable("id") int id) {

        try {
            Game classGame = new Game(0);
            classGame.setId(id);
            gameService.deleteOne(classGame);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No Games with /{id}: " + id + " found to delete");
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Game with /{id}: " + id + " deleted");
    }


    // @MultipartConfig is a JAX-RS framework annotation and @RequestParam is from Spring
    //
    // SPRING
    // use @RequestParam(value = "date", required = false, defaultValue = "01-01-1999") Date dateOrNull)
    // you get the Date dataOrNull for ?date=12-05-2013
    //
    // JAX_RS
    // also use: @DefaultValue("false") @QueryParam("from") boolean isHuman
    // you get the boolean isHuman with value 'true' for ?isHuman=true

    // /games?id=1,2,3,4
    @DeleteMapping(value = "/games", params = { "id" } )
    public ResponseEntity deleteGamesById(
            @RequestParam(value = "id", required = false) List<String> ids) {

        Game classGame = new Game(0);

        try {
            gameService.deleteAllByIds(classGame, ids);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No Games with /{id} found to delete, ids: " + ids);
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Game with ?id= : " + ids + " deleted");
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
