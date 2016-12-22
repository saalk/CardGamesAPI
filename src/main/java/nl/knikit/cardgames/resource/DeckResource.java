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

import java.text.ParseException;
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
// @ExposesResourceFor(Deck.class) //hateoas
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
			decks = (ArrayList<Deck>) deckService.findAll("game", "ASC");
			ArrayList<DeckDto> deckDtos = new ArrayList<>();
			for (Deck deck : decks) {
				deckDtos.add(mapUtil.convertToDto(deck));
			}
			return ResponseEntity
					       .status(HttpStatus.OK)
					       .body(deckDtos);
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(e);
		}
	}

	@GetMapping(value = "/decks", params = {"game"})
	@Produces({MediaType.APPLICATION_JSON})
	public ResponseEntity getDecksWhere(@RequestParam(value = "game", required = true) String param) {
		
		try {
			List<Deck> decks = deckService.findAllWhere("game", param);
			if (decks == null) {
				// getDeckDtos empty is not an error
				return ResponseEntity
						       .status(HttpStatus.OK)
						       .body("{}");
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
	
//	@GetMapping(value = "/decks", params = {"game", "dealtTo"})
//	@Produces({MediaType.APPLICATION_JSON})
//	public ResponseEntity findAllForGameAndPlayer(
//			@RequestParam(value = "game", required = true) String game,
//			@RequestParam(value = "dealtTo", required = true) int dealtTo) throws Exception {
//
//		//TODO should dealtToDto param be int or String?
//		if (dealtTo != 0) {
//			Player player = playerService.findOne((dealtTo));
//			if (player == null) {
//				return ResponseEntity
//						       .status(HttpStatus.NOT_FOUND)
//						       .body("detaltTo Player not found");
//			}
//		}
//
//		List<Deck> decks;
//		try {
//			// TODO make a findAllWhereAnd
//			decks = deckService.findAllWhere("game", game);
//			if (decks == null) {
//				return ResponseEntity
//						       .status(HttpStatus.NOT_FOUND)
//						       .body("Deck not found");
//			}
//		} catch (Exception e) {
//			return ResponseEntity
//					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
//					       .body(e);
//		}
//		ArrayList<DeckDto> responseDecksDto = new ArrayList<>();
//		for (Deck deck : decks) {
//			if (dealtTo != 0 && deck.getDealtTo().getPlayerId()==dealtTo) {
//				responseDecksDto.add(mapUtil.convertToDto(deck));
//			}
//		}
//		return ResponseEntity
//				       .status(HttpStatus.OK)
//				       .body(responseDecksDto);
//	}
	
	@PostMapping(value = "/decks", params = {"shuffle"})
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity createDeck(
			                                @RequestBody DeckDto deckDto,
			                                @RequestParam(value = "shuffle", defaultValue = "true", required = false) String shuffle) throws Exception {
		// check the gameDto in the DeckDto
		if (deckDto == null || deckDto.getGameDto() == null) {
			return ResponseEntity
					       .status(HttpStatus.BAD_REQUEST)
					       .body("Body or GameId not present");
		}
		Game game = gameService.findOne((deckDto.getGameDto().getGameId()));
		if (game == null) {
			return ResponseEntity
					       .status(HttpStatus.NOT_FOUND)
					       .body("Game not found");
		}
		
		// check the dealtToDto in the DeckDto
		if (deckDto.getDealtToDto() != null) {
			return ResponseEntity
					       .status(HttpStatus.BAD_REQUEST)
					       .body("Player dealtToDto for new deck not null");
		}
		
		// check param shuffle
		if (shuffle != null && !isBoolean(shuffle)) {
			return ResponseEntity
					       .status(HttpStatus.BAD_REQUEST)
					       .body("Param shuffle not a boolean: " + shuffle);
		}
		
		// Get all cards and shuffle (since java 1.8)
		List<Card> cards = Card.newDeck(0);
		if (shuffle != null && Boolean.parseBoolean(shuffle)) {
			Collections.shuffle(cards);
		}
		
		int order = 1;
		List<DeckDto> newDeckDtos = new ArrayList<>();
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
				newDeckDtos.add(mapUtil.convertToDto(createdDeck));
				
			} catch (Exception e) {
				return ResponseEntity
						       .status(HttpStatus.INTERNAL_SERVER_ERROR)
						       .body(e);
			}
		}
		return ResponseEntity
				       .status(HttpStatus.CREATED)
				       .body(newDeckDtos);
	}
	
	// a body is always needed but can be {}
	@PutMapping(value = "/decks/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity updateDeck(
			                                          @PathVariable("id") Integer pathId,
			                                          @RequestBody DeckDto deckDtoToUpdate,
			                                          @RequestParam(value = "dealtTo", required = false) Integer dealtTo) throws ParseException {
		// init
		DeckDto updatedDeckDto;
		Deck deckToUpdate;
		Deck updatedDeck;
		Player player;
		
		// check path var game/{id}
		int id = pathId;
		if (id == 0) {
			return ResponseEntity
					       .status(HttpStatus.BAD_REQUEST)
					       .body("Id in /decks{id}?delatTo={dealtTo} null or zero");
		} else {
			try {
				deckToUpdate = deckService.findOne(id);
				if (deckToUpdate == null) {
					return ResponseEntity
							       .status(HttpStatus.NOT_FOUND)
							       .body("Deck for /decks{id}?dealtTo={dealtTo} not found");
				}
			} catch (Exception e) {
				return ResponseEntity
						       .status(HttpStatus.INTERNAL_SERVER_ERROR)
						       .body(e);
			}
		}
		
		// check request param ?dealtTo={dealtTo} if present
		if (!(dealtTo == null)) {
			
			// use the deckDto found for path var in previous section
			if (dealtTo.toString().isEmpty()) {
				deckToUpdate.setDealtTo(null);
			} else {
				try {
					player = playerService.findOne(dealtTo);
					if (player == null || player.getPlayerId() == 0) {
						return ResponseEntity
								       .status(HttpStatus.NOT_FOUND)
								       .body("DealtTo in /decks{id}?dealtTo={dealtTo} not found");
					}
					deckToUpdate.setDealtTo(player);
				} catch (Exception e) {
					return ResponseEntity
							       .status(HttpStatus.INTERNAL_SERVER_ERROR)
							       .body(e);
				}
			}
		} else if (deckDtoToUpdate != null && deckDtoToUpdate.getDeckId() != 0) {
			
			// use the deckDtoToUpdate from the request body
			deckToUpdate = mapUtil.convertToEntity(deckDtoToUpdate);
		} else {
			return ResponseEntity
					       .status(HttpStatus.BAD_REQUEST)
					       .body("Body or dealtTo in /decks{id}?dealtTo={dealtTo} should be present");
		}
		
		// do the update
		try {
			updatedDeck = deckService.update(deckToUpdate);
			if (updatedDeck == null) {
				return ResponseEntity
						       .status(HttpStatus.NOT_FOUND)
						       .body("Deck in /decks{id}?dealtTo={dealtTo} could not be updated");
			}
			updatedDeckDto = mapUtil.convertToDto(updatedDeck);
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
	
	// /decks?id=1,2,3,4
	@DeleteMapping(value = "/decks", params = {"id"})
	public ResponseEntity deleteDecksById(
			                                     @RequestParam(value = "id", required = false) List<String> ids) {
		
		try {
			for (int i = 0; i < ids.size(); i++) {
				Deck deleteDeck = deckService.findOne(Integer.parseInt(ids.get(i)));
				if (deleteDeck == null) {
					return ResponseEntity
							       .status(HttpStatus.NOT_FOUND)
							       .body("Deck not found: " + ids.get(i));
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
