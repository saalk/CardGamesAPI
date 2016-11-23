package nl.knikit.cardgames.resource;


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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

        ArrayList<Deck> decks;
        decks = (ArrayList<Deck>) deckService.findAll("gameObj", "ASC");
        return new ResponseEntity(decks, HttpStatus.OK);
    }

    @GetMapping("/decks/{id}")
    public ResponseEntity getDeck(
            @PathVariable("id") int id) {

        Deck deck = deckService.findOne(id);
        if (deck == null) {
                return ResponseEntity
                           .status(HttpStatus.INTERNAL_SERVER_ERROR)
                           .body(new ArrayList<>());
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(deck);
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

    @GetMapping(value = "/decks", params = { "game" } )
    public ResponseEntity<ArrayList<Deck>> findAllWhere(
            @RequestParam(value = "game", required = true) String param) {

         try {
            ArrayList<Deck> decks = (ArrayList) deckService.findAllWhere("game", param);
            if (decks == null || decks.isEmpty()) {
                
                return ResponseEntity
                               .status(HttpStatus.INTERNAL_SERVER_ERROR)
                               .body(new ArrayList<>());
            }

            return new ResponseEntity(decks, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ArrayList<Deck>() );
        }
    }

    @PostMapping(value = "/decks", params = { "jokers" } )
	public ResponseEntity createDeck(
        @RequestBody Deck deck, @RequestParam(value = "jokers", required = true) String param) {

	    // check for gameObj
        if (!(deck.getGameObj().getGameId()>0)) {
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .body("No Game supplied for Deck to create " + deck);
        }
        Game currentGame = gameService.findOne(deck.getGameObj().getGameId());
	    if (currentGame == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .body("No Game supplied to relate Deck to: " + deck);
        }

	    // check if param has int
	    boolean isInteger = isInteger(param);
	    if (!isInteger) {
		    return ResponseEntity
				           .status(HttpStatus.NOT_ACCEPTABLE)
				           .body("Param jokers has no valid value but has: " + param);
	    }
	    
        int order = 1;
	    List<Card> newDeck = Card.newDeck(0);
	    // shuffle the list
	    Collections.shuffle(newDeck);
	    
        for (Card card: newDeck){
            deck.setCardObj(card);
            deck.setCardOrder(order++);
            deck.setDealtTo(null);
            deckService.create(deck);
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(deck);
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
            Deck deleteDeck = new Deck();
            deleteDeck.setDeckId(id);
            deckService.deleteOne(deleteDeck);
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
    // you fromLabel the Date dataOrNull for ?date=12-05-2013
    //
    // JAX_RS
    // also use: @DefaultValue("false") @QueryParam("from") boolean isHuman
    // you fromLabel the boolean isHuman with value 'true' for ?isHuman=true

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
    
    // Helper for params
	public static boolean isInteger(String s) {
		try
		{
			Integer.parseInt(s); // s is a valid integer
			return true;
		}
		catch (NumberFormatException ex)
		{
			return false;
		}
	}

}
