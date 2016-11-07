package nl.knikit.cardgames.resource;


import nl.knikit.cardgames.exception.DeckNotFoundForIdException;
import nl.knikit.cardgames.model.Card;
import nl.knikit.cardgames.model.Deck;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.service.ICardService;
import nl.knikit.cardgames.service.IDeckService;
import nl.knikit.cardgames.service.IGameService;

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

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

// @RestController = @Controller + @ResponseBody
@CrossOrigin
@RestController
@Component
@ExposesResourceFor(Deck.class)
@Slf4j
@Scope("prototype")
public class DeckResource {

    // @Resource = javax, @Inject = javax, @Autowire = spring bean factory
    @Autowired
    private IDeckService deckService;

    @Autowired
    private IGameService gameService;

    @Autowired
    private ICardService cardService;

    @GetMapping("/decks")
    public ResponseEntity<ArrayList<Deck>> getDecks() {

        ArrayList<Deck> Decks;
        Decks = (ArrayList) deckService.findAll("game", "ASC");
        return new ResponseEntity(Decks, HttpStatus.OK);
    }

    @GetMapping("/decks/{id}")
    public ResponseEntity getDeck(
            @PathVariable("id") int id) throws DeckNotFoundForIdException {

        Deck Deck = deckService.findOne(id);
        if (Deck == null) {

            throw new DeckNotFoundForIdException(id);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Deck);
    }

    // @QueryParam is a JAX-RS framework annotation and @RequestParam is from Spring
    //
    // SPRING
    // use @RequestParam(value = "date", required = false, defaultValue = "01-01-1999") Date dateOrNull)
    // you fromRankName the Date dataOrNull for ?date=12-05-2013
    //
    // JAX_RS
    // also use: @DefaultValue("false") @QueryParam("from") boolean isHuman
    // you fromRankName the boolean isHuman with value 'true' for ?isHuman=true

    @GetMapping(value = "/decks", params = { "game" } )
    public ResponseEntity<ArrayList<Deck>> findAllWhere(
            @RequestParam(value = "game", required = true) String param) {

        Deck classDeck = new Deck();
        // ternary operator = shorthand if for conditional assignment -> The ? : operator in Java
        // boolean isHumanBoolean = ( param=="true")?true:false;
        // classDeck.setHuman( isHumanBoolean );

        try {

            ArrayList<Deck> Decks = (ArrayList) deckService.findAllWhere("game", param);
            if (Decks == null || Decks.isEmpty()) {
                throw new DeckNotFoundForIdException(999);
            }

            return new ResponseEntity(Decks, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ArrayList<Deck>() );
        }
    }

    @PostMapping(value = "/decks", params = { "game" } )
    public ResponseEntity createDeckForGame(
        @RequestBody Deck deck, @RequestParam(value = "game", required = true) String game) {

        if (game == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .body("No Game supplied for Deck to create " + deck);
        }
        Game currentGame = gameService.findOne(Integer.parseInt(game));

        if (deck == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .body("No Deck supplied to create: " + deck);
        }

        ArrayList<Card> cards;
        cards = (ArrayList) cardService.findAll("value", "ASC");

        for (Card card: cards){

            Deck newDeck = new Deck();
            newDeck.setGame(currentGame);
            newDeck.setCard(card);
            deckService.create(newDeck);
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new StringBuilder(game.toString() + cards.toString()));
    }

    @PutMapping("/decks/{id}")
    public ResponseEntity updateDeck(
            @PathVariable int id, @RequestBody Deck deck) {

        Deck newDeck = deckService.update(deck);
        if (null == newDeck) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No Deck found to change for path /{id): " + id);
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newDeck);
    }

    @DeleteMapping("/decks/{id}")
    public ResponseEntity deleteDecks(
            @PathVariable("id") int id) {

        try {
            Deck classDeck = new Deck();
            classDeck.setId(id);
            deckService.deleteOne(classDeck);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No Decks with /{id}: " + id + " found to delete");
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Deck with /{id}: " + id + " deleted");
    }


    // @MultipartConfig is a JAX-RS framework annotation and @RequestParam is from Spring
    //
    // SPRING
    // use @RequestParam(value = "date", required = false, defaultValue = "01-01-1999") Date dateOrNull)
    // you fromRankName the Date dataOrNull for ?date=12-05-2013
    //
    // JAX_RS
    // also use: @DefaultValue("false") @QueryParam("from") boolean isHuman
    // you fromRankName the boolean isHuman with value 'true' for ?isHuman=true

    // /Decks?id=1,2,3,4
    @DeleteMapping(value = "/decks", params = { "id" } )
    public ResponseEntity deleteDecksById(
            @RequestParam(value = "id", required = false) List<String> ids) {

        Deck classDeck = new Deck();

        try {
            deckService.deleteAllByIds(classDeck, ids);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No Decks with /{id} found to delete, ids: " + ids);
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Deck with ?id= : " + ids + " deleted");
    }


    // To handle an exception, we need to create an exception method annotated with @ExceptionHandler.
    // This method will return java bean as JSON with error info. Returning ModelAndView with HTTP 200
    @ExceptionHandler(DeckNotFoundForIdException.class)
    public ModelAndView handleDeckNotFoundForIdException(HttpServletRequest request, Exception ex) {
        log.error("Requested URL=" + request.getRequestURL());
        log.error("Exception Raised=" + ex);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exception", ex);
        modelAndView.addObject("url", request.getRequestURL());

        modelAndView.setViewName("error");
        return modelAndView;
    }

}
