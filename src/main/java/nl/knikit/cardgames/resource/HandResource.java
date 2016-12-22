package nl.knikit.cardgames.resource;

import nl.knikit.cardgames.DTO.CardDto;
import nl.knikit.cardgames.DTO.HandDto;
import nl.knikit.cardgames.mapper.ModelMapperUtil;
import nl.knikit.cardgames.model.Card;
import nl.knikit.cardgames.model.Casino;
import nl.knikit.cardgames.model.Hand;
import nl.knikit.cardgames.model.Player;
import nl.knikit.cardgames.service.ICardService;
import nl.knikit.cardgames.service.ICasinoService;
import nl.knikit.cardgames.service.IHandService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import lombok.extern.slf4j.Slf4j;

// @RestController = @Controller + @ResponseBody
@CrossOrigin
@RestController
@Component
//@ExposesResourceFor(Hand.class)
@Slf4j
@Scope("prototype")
public class HandResource {
	
	// @Resource = javax, @Inject = javax, @Autowire = spring bean factory
	@Autowired
	private IHandService handService;
	
	@Autowired
	private IPlayerService playerService;
	
	@Autowired
	private ICasinoService casinoService;
	
	@Autowired
	private ICardService cardService;
	
	@Autowired
	private ModelMapperUtil mapUtil;
	
	@GetMapping("/hands/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public ResponseEntity getHand(@PathVariable("id") int id) {
		
		try {
			Hand hand = handService.findOne(id);
			if (hand == null) {
				return ResponseEntity
						       .status(HttpStatus.NOT_FOUND)
						       .body("Hand not found");
			}
			HandDto handDto = mapUtil.convertToDto(hand);
			return ResponseEntity
					       .status(HttpStatus.OK)
					       .body(handDto);
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(e);
		}
	}
	
	@GetMapping("/hands")
	@Produces({MediaType.APPLICATION_JSON})
	public ResponseEntity getHands() {
		
		ArrayList<Hand> hands;
		try {
			hands = (ArrayList<Hand>) handService.findAll("casino", "ASC");
			ArrayList<HandDto> handDtos = new ArrayList<>();
			for (Hand hand : hands) {
				handDtos.add(mapUtil.convertToDto(hand));
			}
			return ResponseEntity
					       .status(HttpStatus.OK)
					       .body(handDtos);
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(e);
		}
	}
	
	@GetMapping(value = "/hands", params = {"casino"})
	@Produces({MediaType.APPLICATION_JSON})
	public ResponseEntity getHandsWhere(
			                                   @RequestParam(value = "casino", required = true) String casino) {
		
		try {
			List<Hand> hands = handService.findAllWhere("casino", casino);
			if (hands == null) {
				// TODO getHands empty is not an error
				return ResponseEntity
						       .status(HttpStatus.OK)
						       .body("{}");
			}
			ArrayList<HandDto> handsDto = new ArrayList<>();
			for (Hand hand : hands) {
				handsDto.add(mapUtil.convertToDto(hand));
			}
			return ResponseEntity
					       .status(HttpStatus.OK)
					       .body(handsDto);
			
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(e);
		}
	}
	
	@PostMapping(value = "/hands", params = {"card"})
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity createHand(
			                                @RequestBody HandDto handDto,
			                                @RequestParam(value = "card", required = true) List<String> cards) throws Exception {
		// check the casinoDto in the HandDto
		if (handDto == null || handDto.getCasinoDto() == null) {
			return ResponseEntity
					       .status(HttpStatus.BAD_REQUEST)
					       .body("Body or CasinoId not present");
		}
		Casino casino = casinoService.findOne((handDto.getCasinoDto().getCasinoId()));
		if (casino == null) {
			return ResponseEntity
					       .status(HttpStatus.NOT_FOUND)
					       .body("Casino for new hand not found");
		}
		
		// check the playerDto in the HandDto
		if (handDto.getPlayerDto() == null) {
			return ResponseEntity
					       .status(HttpStatus.BAD_REQUEST)
					       .body("Player for new hand not present");
		}
		Player player = playerService.findOne((handDto.getPlayerDto().getPlayerId()));
		if (player == null) {
			return ResponseEntity
					       .status(HttpStatus.NOT_FOUND)
					       .body("Player for new hand not found");
		}
		
		if (casino.getPlayer().getPlayerId() != handDto.getPlayerDto().getPlayerId()) {
			return ResponseEntity
					       .status(HttpStatus.BAD_REQUEST)
					       .body("Casino supplied has different player than in this hand");
		}
		
		// check param cards, combine this loop with the
		List<Card> newCards = new ArrayList<>();
		try {
			for (int i = 0; i < cards.size(); i++) {
				Card card = new Card();
				card.setCardId(cards.get(i)); // this sets rank and suit as well..
				if (card == null) {
					return ResponseEntity
							       .status(HttpStatus.NOT_FOUND)
							       .body("Card not found: " + cards.get(i));
				}
				newCards.add(card);
			}
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(e);
		}
		
		// Count the cards already in the hand
		String whereCasino = String.valueOf(casino.getCasinoId());
		List<Hand> hands = new ArrayList<>();
		hands = handService.findAllWhere("casino", whereCasino);
		int order;
		if (hands == null || hands.isEmpty()) { // can be null?
			order = 1;
		} else {
			order = hands.size() + 1;
		}
		
		// check for cards in the hand equal to the cards in the param
		for (Hand hand : hands) {
			for (String card : cards) {
				if (hand.getCard().getCardId() == card) {
					return ResponseEntity
							       .status(HttpStatus.NOT_FOUND)
							       .body("Card already in this players Hand: " + card);
				}
			}
		}
		
		// Add all cards after the existing cardOrder
		List<HandDto> newHandDtos = new ArrayList<>();
		for (Card card : newCards) {
			Hand newHand = new Hand();
			newHand.setCard(card);
			newHand.setCardOrder(order++);
			newHand.setPlayer(player);
			newHand.setCasino(casino);
			try {
				Hand createdHand = handService.create(newHand);
				if (createdHand == null) {
					return ResponseEntity
							       .status(HttpStatus.NOT_FOUND)
							       .body("Hand not created");
				}
				newHandDtos.add(mapUtil.convertToDto(createdHand));
				
			} catch (Exception e) {
				return ResponseEntity
						       .status(HttpStatus.INTERNAL_SERVER_ERROR)
						       .body(e);
			}
		}
		return ResponseEntity
				       .status(HttpStatus.CREATED)
				       .body(newHandDtos);
	}
	
	@DeleteMapping("/hands/{id}")
	public ResponseEntity deleteHands(
			                                 @PathVariable("id") int id) {
		
		try {
			Hand deleteHand = handService.findOne(id);
			if (deleteHand == null) {
				return ResponseEntity
						       .status(HttpStatus.NOT_FOUND)
						       .body("Hand not found");
			}
			handService.deleteOne(deleteHand);
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
	
	// /Hands?id=1,2,3,4
	@DeleteMapping(value = "/hands", params = {"id"})
	public ResponseEntity deleteHandsById(
			                                     @RequestParam(value = "id", required = false) List<String> ids) {
		
		try {
			for (int i = 0; i < ids.size(); i++) {
				Hand deleteHand = handService.findOne(Integer.parseInt(ids.get(i)));
				if (deleteHand == null) {
					return ResponseEntity
							       .status(HttpStatus.NOT_FOUND)
							       .body("Hand not found: " + ids.get(i));
				}
			}
			handService.deleteAllByIds(new Hand(), ids);
			return ResponseEntity
					       .status(HttpStatus.NO_CONTENT)
					       .body("");
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(e);
		}
	}
}
