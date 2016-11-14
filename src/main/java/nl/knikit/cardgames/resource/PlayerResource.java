package nl.knikit.cardgames.resource;

/*
    http://viralpatel.net/blogs/spring-4-mvc-rest-example-json/

    | Annotation | Meaning in SPRING                                   |
    +------------+-----------------------------------------------------+
    | @Component | generic stereotype for any Spring-managed component |
    | @Repository| stereotype for persistence layer                    |
    | @Service   | stereotype for service layer                        |
    | @Controller| stereotype for presentation layer (spring-mvc)      |

    @RestController = @Controller + @ResponseBody
    You can only use @RequestMapping on @Controller annotated classes.

    new Spring MVC 4.3 REST annotations @GetMapping, @PostMapping, @PutMapping and @DeleteMapping
    instead of standard @RequestMapping. They act as wrapper to @RequestMapping.



    @GetMapping = @RequestMapping(method = RequestMethod.GET).

    JAX-RS = Java API for RESTful Web Services since JAVA EE 6:
    - @Path is from Java EE and identifies the URI path template to which the resource responds
    - @*Param and @GET, @PUT, @POST, @DELETE and @HEAD + @PATH and @Context

    Jersey = a reference implementation of JAX-RS from sun
    - an implementation together with test suites to server as Gold Standard
    - natively supported only by Glassfish (oracle owned)
    - Tomcat (aoache foundation) can run with the JRE and GlassFish requires the full JDK
    - Tomcat supports only servlet and JSP standards

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
import nl.knikit.cardgames.model.Player;
import nl.knikit.cardgames.service.IPlayerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin
@RestController
@Component
@ExposesResourceFor(Player.class)
@Slf4j
@Scope("prototype")
@RequestScoped
public class PlayerResource {

    // @Resource = javax, @Inject = javax, @Autowire = spring bean factory
    @Autowired
    private IPlayerService playerService;

    @GetMapping("/players")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<ArrayList<Player>> getPlayers() {

        ArrayList<Player> players;
        players = (ArrayList<Player>) playerService.findAll("isHuman", "DESC");
        return new ResponseEntity(players, HttpStatus.OK);
    }

    @GetMapping("/players/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity getPlayer(
            @PathVariable("id") int id) throws PlayerNotFoundForIdException {

        Player player = playerService.findOne(id);
        if (player == null) {

            throw new PlayerNotFoundForIdException(id);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(player);
    }

    // @QueryParam is a JAX-RS framework annotation and @RequestParam is from Spring
    //
    // SPRING
    // use @RequestParam(value = "date", required = false, defaultValue = "01-01-1999") Date dateOrNull)
    // you fromLabel the Date dataOrNull for ?date=12-05-2013
    //
    // JAX_RS
    // also use: @DefaultValue("false") @QueryParam("from") boolean isHuman
    // you fromLabel the boolean isHuman with value 'true' for ?isHuman=true

    @GetMapping(value = "/players", params = { "isHuman" } )
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<ArrayList<Player>> findAllWhere(
            @RequestParam(value = "isHuman", required = true) String param) {

        try {
            ArrayList<Player> players = (ArrayList) playerService.findAllWhere("isHuman", param);
            if (players == null || players.isEmpty()) {
                throw new PlayerNotFoundForIdException(999);
            }

            return new ResponseEntity(players, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ArrayList<Player>() );
        }
    }

    @PostMapping("/players")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity createPlayer(
            @RequestBody Player player) {

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

    @PutMapping("/players/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity updatePlayer(
            @PathVariable int id, @RequestBody Player player) {

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

    @DeleteMapping("/players/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity deletePlayers(
            @PathVariable("id") int id) {

        try {
            Player classPlayer = new Player();
            classPlayer.setPlayerId(id);
            playerService.deleteOne(classPlayer);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No Players with /{id}: " + id + " found to delete");
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Player with /{id}: " + id + " deleted");
    }


    // @MultipartConfig is a JAX-RS framework annotation and @RequestParam is from Spring
    //
    // SPRING
    // use @RequestParam(value = "date", required = false, defaultValue = "01-01-1999") Date dateOrNull)
    // you fromLabel the Date dataOrNull for ?date=12-05-2013
    //
    // JAX_RS
    // also use: @DefaultValue("false") @QueryParam("from") boolean isHuman
    // you fromLabel the boolean isHuman with value 'true' for ?isHuman=true

    // /players?id=1,2,3,4
    @DeleteMapping(value = "/players", params = { "id" } )
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity deletePlayersById(
            @RequestParam(value = "id", required = false) List<String> ids) {

        Player classPlayer = new Player();

        try {
            playerService.deleteAllByIds(classPlayer, ids);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No Players with /{id} found to delete, ids: " + ids);
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Player with ?id= : " + ids + " deleted");
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
