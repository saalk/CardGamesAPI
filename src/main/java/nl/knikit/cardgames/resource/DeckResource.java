package nl.knikit.cardgames.resource;


import nl.knikit.cardgames.DTO.DeckDto;
import nl.knikit.cardgames.mapper.ModelMapperUtil;
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
import javax.ws.rs.Consumes;
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
	
	@Autowired
	private ModelMapperUtil mapUtil;
	
	
	@GetMapping("/decks/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public ResponseEntity getDeck(@PathVariable("id") int id) {
		
		try {
			Deck deck = deckService.findOne(id);
			if (deck == null) {
				return ResponseEntity
						       .status(HttpStatus.NOT_FOUND)
						       .body("Deck not found");
			}
			DeckDto deckDto = mapUtil.convertToDto(deck);
			return ResponseEntity
					       .status(HttpStatus.OK)
					       .body(deckDto);
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(e);
		}
	}
	
	@GetMapping("/decks")
	@Produces({MediaType.APPLICATION_JSON})
	public ResponseEntity getDecks() {
		
		ArrayList<Deck> decks;
		try {
			decks = (ArrayList<Deck>) deckService.findAll("gameDto", "ASC");
			ArrayList<DeckDto> decksDto = new ArrayList<>();
			for (Deck deck : decks) {
				decksDto.add(mapUtil.convertToDto(deck));
			}
			return ResponseEntity
					       .status(HttpStatus.OK)
					       .body(decksDto);
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(e);
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
	
	@GetMapping(value = "/decks", params = {"gameDto"})
	@Produces({MediaType.APPLICATION_JSON})
	public ResponseEntity findAllForGame(@RequestParam(value = "gameDto", required = true) String param) {
		
		try {
			List<Deck> decks = deckService.findAllWhere("gameDto", param);
			if (decks == null) {
				return ResponseEntity
						       .status(HttpStatus.NOT_FOUND)
						       .body("[{}]");
			}
			ArrayList<DeckDto> decksDto = new ArrayList<>();
			for (Deck deck : decks) {
				decksDto.add(mapUtil.convertToDto(deck));
			}
			return ResponseEntity
					       .status(HttpStatus.OK)
					       .body(decksDto);
			
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(e);
		}
	}
	
	@GetMapping(value = "/decks", params = {"gameDto", "dealtToDto"})
	@Produces({MediaType.APPLICATION_JSON})
	public ResponseEntity findAllForGameAndPlayer(
			@RequestParam(value = "gameDto", required = true) String game,
			@RequestParam(value = "dealtToDto", required = true) int dealtTo) throws Exception {
		
		//TODO should dealtToDto param be int or String?
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
			decks = deckService.findAllWhere("gameDto", game);
			if (decks == null) {
				return ResponseEntity
						       .status(HttpStatus.NOT_FOUND)
						       .body("Deck not found");
			}
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(e);
		}
		ArrayList<DeckDto> responseDecksDto = new ArrayList<>();
		for (Deck deck : decks) {
			if (dealtTo != 0 && deck.getDealtTo().getPlayerId()==dealtTo) {
				responseDecksDto.add(mapUtil.convertToDto(deck));
			}
		}
		return ResponseEntity
				       .status(HttpStatus.OK)
				       .body(responseDecksDto);
	}
	// TODO /gameDto/{id}/decks?shuffle
	@PostMapping(value = "/decks", params = {"shuffle"})
	@Consumes(MediaType.APPLICATION_JSON)
	public ResponseEntity createDeck(
			                                @RequestBody DeckDto deckDto,
			                                @RequestParam(value = "shuffle", defaultValue = "0", required = false) String shuffle) throws Exception {
		// check gameDto
		if (deckDto == null || deckDto.getGameDto() == null) {
			return ResponseEntity
					       .status(HttpStatus.BAD_REQUEST)
					       .body("Body or Game not present");
		}
		Game game = gameService.findOne((deckDto.getGameDto().getGameId()));
		if (game == null) {
			return ResponseEntity
					       .status(HttpStatus.NOT_FOUND)
					       .body("Game not found");
		}
		if (deckDto.getDealtToDto() != null) {
			return ResponseEntity
					       .status(HttpStatus.BAD_REQUEST)
					       .body("Player dealtToDto found");
		}
		
		// Get all cards
		List<Card> cards = Card.newDeck(0);
		
		// check param
		if (shuffle != null && !isBoolean(shuffle)) {
			return ResponseEntity
					       .status(HttpStatus.BAD_REQUEST)
					       .body("Param shuffle not a boolean: " + shuffle);
		}
		if (shuffle != null && Boolean.parseBoolean(shuffle)) {
			Collections.shuffle(cards);
		}
		
		int order = 1;
		for (Card card : cards) {
			Deck newDeck= new Deck();
			newDeck.setCard(card);
			newDeck.setCardOrder(order++);
			newDeck.setDealtTo(null);
			newDeck.setGame(game);
			try {
				Deck createdDeck = deckService.create(newDeck);
				if (createdDeck == null) {
					return ResponseEntity
							       .status(HttpStatus.NOT_FOUND)
							       .body("Deck not created");
				}
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
	@Consumes(MediaType.APPLICATION_JSON)
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
					       .body("Player dealtToDto not integer");
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
			
			DeckDto updatedDeckDto = mapUtil.convertToDto(updatedDeck);
			return ResponseEntity
					       .status(HttpStatus.OK)
					       .body(updatedDeckDto);
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
						       .body("Deck not found");
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
					       .body(e);
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
			return Boolean.parseBoolean(s); // s is a valid boolean
		} catch (Exception ex) {
			return false;
		}
	}
}
