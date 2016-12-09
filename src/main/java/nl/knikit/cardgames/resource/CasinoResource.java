package nl.knikit.cardgames.resource;

import nl.knikit.cardgames.model.Casino;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.service.ICasinoService;
import nl.knikit.cardgames.service.IGameService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;

import lombok.extern.slf4j.Slf4j;

// @RestController = @Controller + @ResponseBody
@CrossOrigin
@RestController
@Component
@ExposesResourceFor(Casino.class)
@Slf4j
@Scope("prototype")
public class CasinoResource {

    // @Resource = javax, @Inject = javax, @Autowire = spring bean factory
    @Autowired
    private ICasinoService casinoService;

    @Autowired
    private IGameService gameService;

    @GetMapping("/casinos")
    public ResponseEntity<ArrayList<Casino>> getCasinos() {

        ArrayList<Casino> casinos;
        casinos = (ArrayList<Casino>) casinoService.findAll("playingOrder", "ASC");

        return new ResponseEntity(casinos, HttpStatus.OK);
    }

    @GetMapping("/casinos/{id}")
    public ResponseEntity getCasino(@PathVariable("id") int id) {

        Casino casino = casinoService.findOne(id);
        if (casino == null) {
    
            return ResponseEntity
                           .status(HttpStatus.BAD_REQUEST)
                           .body("[{}]");
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(casino);
    }

    // @QueryParam is a JAX-RS framework annotation and @RequestParam is from Spring
    //
    // SPRING
    // use @RequestParam(value = "date", required = false, defaultValue = "01-01-1999") Date dateOrNull)
    // you fromLabel the Date dataOrNull for ?date=12-05-2013
    //
    // JAX_RS
    // also use: @DefaultValue("false") @QueryParam("from") boolean human
    // you fromLabel the boolean human with value 'true' for ?human=true

    @GetMapping(value = "/casinos", params = { "game" } )
    public ResponseEntity<ArrayList<Casino>> findAllWhere(
            @RequestParam(value = "game", required = true) String param) {

        try {

            ArrayList<Casino> casinos = (ArrayList) casinoService.findAllWhere("game", param);
            if (casinos == null || casinos.isEmpty()) {
                return ResponseEntity
                               .status(HttpStatus.BAD_REQUEST)
                               .body(new ArrayList<Casino>());
            }

            return new ResponseEntity(casinos, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ArrayList<Casino>() );
        }
    }

    @PostMapping("/casinos")
    public ResponseEntity createCasino(@RequestBody Casino casino) {

        if (casino == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .body("No Casino supplied to create: " + casino);
        }
        casinoService.create(casino);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(casino);
    }
    /*
        Transient entity vs detached entity for save and persist:
        - Transient = make a new() object, set and save the object a get the generated id
        - Detached = get a saved object and in a new session set and save the object with a new id
        - Detached = get a saved object and in a new session set and persist the object with the same id

        Hibernate methods:
        - merge         (copy using same id)
        - persist       (saves the transient instance at flush, returns void: use in extended session context)
        - save          (saves the transient instance directly, returns direct with new generated id)
        - saveOrUpdate  (either save or update depending on unsaved-value check)
        - update        (updates the persistent instance with the id)

        1 - Create parent:      game = new Game("HIGHLOW")
        2 - Persist it:         persist(game)
        3 - Create child class  deck = new Deck/Card(jokers)
        4 - Associate child with it's parent
                                card.setGameId(game.getId())
        5 - Persist child       persist(deck/card)

        Retrieval:
        1 - Get childs          card = game.getDeck/Cards()

        if you do 1, 3, game.getDeck.Cards().add(deck), 5
        the deck is not initialized

        if you do 1,2,3,5 and then game.getDeck/Cards().add(Deck) the
        with cascade=CascadeType.Persist takes care of relations
    */

    @PostMapping(value = "/casinos", params = { "game" } )
    public ResponseEntity createCasino(@RequestBody Casino casino, @RequestParam(value = "game", required = true) String param) {

        Game game = gameService.findOne(Integer.parseInt(param));
        if (game == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .body("No Game supplied for Casino to create: " + casino);
        }

        if (casino == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .body("No Casino supplied to create: " + casino);
        }
        Casino newCasino = new Casino();
        newCasino.setGame(game);
        casinoService.create(newCasino);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(newCasino);
    }

    @PutMapping("/casinos/{id}")
    public ResponseEntity updateCasino(@PathVariable int id, @RequestBody Casino
            casino) {
        Casino newCasino = casinoService.update(casino);
        if (null == newCasino) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No Casino found to change for path /{id): " + id);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(newCasino);
    }

    @DeleteMapping("/casinos/{id}")
    public ResponseEntity deleteCasinos(@PathVariable("id") int id) {

        try {
            Casino classCasino = new Casino();
            classCasino.setCasinoId(id);
            casinoService.deleteOne(classCasino);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No Casino with /{id}: " + id + " found to delete");
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Casino with /{id}: " + id + " deleted");
    }

}
