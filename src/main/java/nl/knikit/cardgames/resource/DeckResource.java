package nl.knikit.cardgames.resource;


import nl.knikit.cardgames.model.Card;
import nl.knikit.cardgames.model.Deck;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.model.Player;
import nl.knikit.cardgames.service.ICardService;
import nl.knikit.cardgames.service.IDeckService;
import nl.knikit.cardgames.service.IGameService;
import nl.knikit.cardgames.service.IPlayerService;

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
	
	@Autowired
	private IPlayerService playerService;
	
	@GetMapping("/decks/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity getDeck(@PathVariable("id") int id) {
		
		try {
			Deck deck = deckService.findOne(id);
			if (deck == null) {
				return ResponseEntity
						       .status(HttpStatus.NOT_FOUND)
						       .body("{}");
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
	
	@GetMapping("/decks")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity<ArrayList<Deck>> getDecks() {
		
		ArrayList<Deck> decks;
		try {
			decks = (ArrayList<Deck>) deckService.findAll("game", "ASC");
			return ResponseEntity
					       .status(HttpStatus.OK)
					       .body(decks);
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(new ArrayList<Deck>());
		}
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
	
	@GetMapping(value = "/decks", params = {"game"})
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity<ArrayList<Deck>> findAllForGame(@RequestParam(value = "game", required = true) String param) {
		
		try {
			ArrayList<Deck> decks = (ArrayList) deckService.findAllWhere("game", param);
			if (decks == null) {
				return ResponseEntity
						       .status(HttpStatus.NOT_FOUND)
						       .body(new ArrayList<Deck>());
			}
			return ResponseEntity
					       .status(HttpStatus.OK)
					       .body(decks);
			
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(new ArrayList<Deck>());
		}
	}
	
	@GetMapping(value = "/decks", params = {"game", "dealtTo"})
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity<ArrayList<Deck>> findAllForGameAndPlayer(
			@RequestParam(value = "game", required = true) String game,
			@RequestParam(value = "dealtTo", required = true) int dealtTo) {
		
		//TODO should dealtTo param be int or String?
		if (dealtTo != 0) {
			Player player = playerService.findOne((dealtTo));
			if (player == null) {
				return ResponseEntity
						       .status(HttpStatus.NOT_FOUND)
						       .body(new ArrayList<Deck>());
			}
		}
		
		List<Deck> decks;
		try {
			decks = deckService.findAllWhere("game", game);
			if (decks == null) {
				return ResponseEntity
						       .status(HttpStatus.NOT_FOUND)
						       .body(new ArrayList<Deck>());
			}
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(new ArrayList<Deck>());
		}
		List<Deck> responseDecks = new ArrayList<>();
		for (Deck deck : decks) {
			if (dealtTo != 0 && deck.getDealtTo().getPlayerId()==dealtTo) {
				responseDecks.add(deck);
			}
			if (dealtTo == 0 && deck.getDealtTo()==null) {
				responseDecks.add(deck);
			}
		}
		return ResponseEntity
				       .status(HttpStatus.OK)
				       .body((ArrayList<Deck>) responseDecks);
	}
	
	@PostMapping(value = "/decks", params = {"shuffle"})
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity createDeck(
			                                @RequestBody Deck deck,
			                                @RequestParam(value = "shuffle", defaultValue = "0", required = false) String shuffle) {
		
		if (deck == null || deck.getGame() == null) {
			return ResponseEntity
					       .status(HttpStatus.BAD_REQUEST)
					       .body("");
		}
		
		Game game = gameService.findOne((deck.getGame().getGameId()));
		if (game == null) {
			return ResponseEntity
					       .status(HttpStatus.NOT_FOUND)
					       .body("Game not found");
		}
		
		if (deck.getDealtTo() != null) {
			
			Player player = playerService.findOne((deck.getDealtTo().getPlayerId()));
			if (player == null) {
				return ResponseEntity
						       .status(HttpStatus.NOT_FOUND)
						       .body("Player dealtTo not found");
			}
		}
		
		int order = 1;
		List<Card> newDeck = Card.newDeck(0);
		List<Deck> newDeckForGame = new ArrayList<>();
		
		// check param
		if (shuffle != null && !isBoolean(shuffle)) {
			return ResponseEntity
					       .status(HttpStatus.BAD_REQUEST)
					       .body("Param shuffle not a boolean: " + shuffle);
		}
		if (shuffle != null && Boolean.parseBoolean(shuffle)) {
			Collections.shuffle(newDeck);
		}
		
		for (Card card : newDeck) {
			
			deck.setCard(card);
			deck.setCardOrder(order++);
			deck.setDealtTo(null);
			
			try {
				Deck createdDeck = deckService.create(deck);
				if (createdDeck == null) {
					return ResponseEntity
							       .status(HttpStatus.NOT_FOUND)
							       .body(new ArrayList<>());
				}
				newDeckForGame.add(createdDeck);
			} catch (Exception e) {
				return ResponseEntity
						       .status(HttpStatus.INTERNAL_SERVER_ERROR)
						       .body(e);
			}
		}
		return ResponseEntity
				       .status(HttpStatus.CREATED)
				       .body("");
	}
	
	@PutMapping(value = "/decks/{id}", params = {"dealTo"})
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity updateDeckForDealtTo(
			                                          @PathVariable String id,
			                                          @RequestParam(value = "dealTo", required = true) String param) {
		Deck deck = deckService.findOne(Integer.parseInt(id));
		if (deck == null) {
			return ResponseEntity
					       .status(HttpStatus.NOT_FOUND)
					       .body("Deck not found");
		}
		
		// check param if not null and no int -> param can be null so use &&
		if (param != null && !isInteger(param)) {
			return ResponseEntity
					       .status(HttpStatus.BAD_REQUEST)
					       .body("");
		}
		if (param != null) {
			
			Player player = playerService.findOne(Integer.parseInt(param));
			if (player == null) {
				return ResponseEntity
						       .status(HttpStatus.NOT_FOUND)
						       .body("[{dealTo player not found}]");
			}
			deck.setDealtTo(player);
		} else {
			deck.setDealtTo(null);
		}
		
		try {
			Deck updatedDeck = deckService.update(deck);
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
	public ResponseEntity deleteDeck(@PathVariable("id") int id) {
		
		try {
			Deck deleteDeck = deckService.findOne(id);
			if (deleteDeck == null) {
				return ResponseEntity
						       .status(HttpStatus.NOT_FOUND)
						       .body("{}");
			}
			deckService.deleteOne(deleteDeck);
			return ResponseEntity
					       .status(HttpStatus.NO_CONTENT)
					       .body("");
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
	// also use: @DefaultValue("false") @QueryParam("from") boolean human
	// you fromLabel the boolean human with value 'true' for ?human=true
	
	// /Decks?id=1,2,3,4
	@DeleteMapping(value = "/decks", params = {"id"})
	public ResponseEntity deleteDecksById(
			                                     @RequestParam(value = "id", required = true) List<String> ids) {
		
		for (int i = 0; i < ids.size(); i++) {
			if (!isInteger(ids.get(i))) {
				return ResponseEntity
						       .status(HttpStatus.BAD_REQUEST)
						       .body(ids.get(i));
			}
		}
		
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
			Boolean.parseBoolean(s); // s is a valid boolean
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
}
