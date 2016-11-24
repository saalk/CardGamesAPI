package nl.knikit.cardgames.resource;


import nl.knikit.cardgames.model.Card;
import nl.knikit.cardgames.model.Deck;
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

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import lombok.extern.slf4j.Slf4j;

// @RestController = @Controller + @ResponseBody
@CrossOrigin
@RestController
@Component
@ExposesResourceFor(Deck.class)
@Slf4j
@Scope("prototype")
@RequestScoped
public class DeckResource {
	
	// @Resource = javax, @Inject = javax, @Autowire = spring bean factory
	@Autowired
	private IDeckService deckService;
	
	@Autowired
	private IGameService gameService;
	
	@Autowired
	private ICardService cardService;
	
	@GetMapping("/decks")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity<ArrayList<Deck>> getDecks() {
		
		ArrayList<Deck> decks;
		try {
			decks = (ArrayList<Deck>) deckService.findAll("gameObj", "ASC");
			return ResponseEntity
					       .status(HttpStatus.OK)
					       .body(decks);
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(new ArrayList<>());
		}
	}
	
	@GetMapping("/decks/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity getDeck(@PathVariable("id") int id) {
		
		try {
			Deck deck = deckService.findOne(id);
			if (deck == null) {
				return ResponseEntity
						       .status(HttpStatus.NOT_FOUND)
						       .body("[{}]");
			}
			return ResponseEntity
					       .status(HttpStatus.OK)
					       .body(deck);
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(new ArrayList<>());
		}
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
	
	@GetMapping(value = "/decks", params = {"game"})
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity<ArrayList<Deck>> findAllWhere(@RequestParam(value = "game", required = true) String param) {
		
		try {
			ArrayList<Deck> decks = (ArrayList) deckService.findAllWhere("game", param);
			if (decks == null) {
				return ResponseEntity
						       .status(HttpStatus.NOT_FOUND)
						       .body(new ArrayList<>());
			}
			return ResponseEntity
					       .status(HttpStatus.OK)
					       .body(decks);
			
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.NOT_FOUND)
					       .body(new ArrayList<>());
		}
	}
	
	@PostMapping(value = "/decks", params = {"shuffle"})
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity createDeck(
			@RequestBody Deck deck,
			@RequestParam(value = "shuffle", defaultValue = "0", required = false) String shuffle) {
		
		if (deck == null) {
			return ResponseEntity
					       .status(HttpStatus.BAD_REQUEST)
					       .body("[{}]");
		}
		
		int order = 1;
		List<Card> newDeck = Card.newDeck(0);
		
		// check param
		if (shuffle != null && !isBoolean(shuffle)) {
			return ResponseEntity
					       .status(HttpStatus.BAD_REQUEST)
					       .body("Param schuffle if not a boolean but has: " + shuffle);
		}
		if (shuffle != null && Boolean.parseBoolean(shuffle)) {
			Collections.shuffle(newDeck);
		}
		
		for (Card card : newDeck) {
			deck.setCardObj(card);
			deck.setCardOrder(order++);
			deck.setDealtTo(null);
			
			try {
				Deck createdDeck = deckService.create(deck);
				if (createdDeck == null) {
					return ResponseEntity
							       .status(HttpStatus.NOT_FOUND)
							       .body(new ArrayList<>());
				}
			} catch (Exception e) {
				return ResponseEntity
						       .status(HttpStatus.INTERNAL_SERVER_ERROR)
						       .body(e);
			}
		}
		return ResponseEntity
				       .status(HttpStatus.CREATED)
				       .body(deck);
	}
	
	@PutMapping("/decks/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity updateDeck(@PathVariable int id, @RequestBody Deck newDeck) {
		// always use the id in the path instead of id in the body
		
		if (newDeck == null || id == 0) {
			return ResponseEntity
					       .status(HttpStatus.BAD_REQUEST)
					       .body("[{}]");
		}
		Deck deck = deckService.findOne(id);
		if (deck == null) {
			return ResponseEntity
					       .status(HttpStatus.NOT_FOUND)
					       .body("[{Deck not found}]");
		}
		Deck consistentDeck = (newDeck);
		consistentDeck.setDeckId(id);
		try {
			Deck updatedDeck = deckService.update(consistentDeck);
			if (updatedDeck == null) {
				return ResponseEntity
						       .status(HttpStatus.NOT_FOUND)
						       .body(new ArrayList<>());
			}
			return ResponseEntity
					       .status(HttpStatus.OK)
					       .body(updatedDeck);
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(e);
		}
	}
	
	@DeleteMapping("/decks/{id}")
	public ResponseEntity deleteDecks(@PathVariable("id") int id) {
		
		try {
			Deck deleteDeck = deckService.findOne(id);
			if (deleteDeck == null) {
				return ResponseEntity
						       .status(HttpStatus.NOT_FOUND)
						       .body("[{}]");
			}
			deckService.deleteOne(deleteDeck);
			return ResponseEntity
					       .status(HttpStatus.NO_CONTENT)
					       .body("[{}]");
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(e);
		}
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
	@DeleteMapping(value = "/decks", params = {"id"})
	public ResponseEntity deleteDecksById(
			@RequestParam(value = "id", required = false) List<String> ids) {
		
		try {
			for (int i = 0; i < ids.size(); i++) {
				Deck deleteDeck = deckService.findOne(Integer.parseInt(ids.get(i)));
				if (deleteDeck == null) {
					return ResponseEntity
							       .status(HttpStatus.NOT_FOUND)
							       .body(ids.get(i));
				}
			}
			deckService.deleteAllByIds(new Deck(), ids);
			return ResponseEntity
					       .status(HttpStatus.NO_CONTENT)
					       .body("");
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(new ArrayList<Deck>());
		}

	}
	
	// Helper for params
	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s); // s is a valid integer
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}
	
	// Helper for params
	public static boolean isBoolean(String s) {
		try {
			Boolean.parseBoolean(s); // s is a valid integer
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
}
