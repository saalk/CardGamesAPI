package nl.knikit.cardgames.resource;

import nl.knikit.cardgames.VO.CardGame;
import nl.knikit.cardgames.commons.resource.AbstractResource;
import nl.knikit.cardgames.controller.CardGameController;
import nl.knikit.cardgames.mapper.ModelMapperUtil;
import nl.knikit.cardgames.model.state.CardGameStateMachine;
import nl.knikit.cardgames.response.CardGameResponse;

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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import lombok.extern.slf4j.Slf4j;

// @RestController = @Controller + @ResponseBody
@CrossOrigin
@RestController
@Component
//@ExposesResourceFor(cardgame.class)
@Slf4j
@Scope("prototype")
@RequestScoped
public class CardGameResource extends AbstractResource {
	
	// @Resource = javax, @Inject = javax, @Autowire = spring bean factory
	@Autowired
	CardGameController controller;
	
	@Autowired
	private ModelMapperUtil mapUtil;
	
	// TODO investigate import org.springframework.http.ResponseEntity vs import javax.ws.rs.core.Response;
	
	@GetMapping(
			{"/cardgames/{id}",
					"/cardgames/{id}/{resource}",
					"/cardgames/{id}/{resource}/{resourceId}",
					"/cardgames/{id}/{resource}/{resourceId}/{extraResource}"})
	// get (player(s), cardsInHand or cardsInDeck for) cardGame
	@Produces({MediaType.APPLICATION_JSON})
	public ResponseEntity getCardGames(
			                                  @PathVariable("id") Integer id,
			                                  @PathVariable("resource") Optional<String> resource,
			                                  @PathVariable("resourceId") Optional<Integer> resourceId,
			                                  @PathVariable("extraResource") Optional<String> extraResource) {
		
		//GET    api/cardgames/1
		//GET    api/cardgames/1/player                    // gives active casino (resource=player)
		//GET    api/cardgames/1/players                   // gives all casinos (resource=players)
		//GET    api/cardgames/1/cards                     // gives all decks (resources=cards)
		//GET    api/cardgames/1/players/2                 // gives a specific casino (resource=players, resourceId=int)
		//GET    api/cardgames/1/players/2/cards           // gives a all hands for a player (resource=players, resourceId=int, extraResource=cards)
		
		Map<String, String> pathAndRequestParams = new HashMap<>();
		pathAndRequestParams.put("gameId", String.valueOf(id));
		
		// functional style
		resource.ifPresent(s -> pathAndRequestParams.put("resource", s));
		resourceId.ifPresent(integer -> pathAndRequestParams.put("resourceId", String.valueOf(integer)));
		extraResource.ifPresent(s -> pathAndRequestParams.put("extraResource", s));
		
		CardGameResponse response;
		try {
			response = controller.play(CardGameStateMachine.Trigger.GET, pathAndRequestParams);
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(e);
		}
		
		if (response.getReason().equals(CardGameResponse.Reason.SUCCESS)) {
			return ResponseEntity
					       .status(HttpStatus.CREATED)
					       .body(response);
		} else {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(response);
		}
	}
	
	// a body is always needed but can be {}
	@PostMapping(value = "/cardgames/init", params = {"gameType", "ante"})
	//new cardGame with existing human if present
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity initCardGame(
			                                  @RequestBody CardGame cardGameToUpdate,
			                                  @RequestParam(value = "gameType", required = false) String gameType,
			                                  @RequestParam(value = "ante", required = false) Integer ante) {
		
		//POST   api/cardgames/init           ?gameType/ante
		
		Map<String, String> pathAndRequestParams = new HashMap<>();
		pathAndRequestParams.put("gameType", gameType);
		pathAndRequestParams.put("ante", String.valueOf(ante));
		
		CardGameResponse response;
		try {
			response = controller.play(CardGameStateMachine.Trigger.POST_INIT, pathAndRequestParams);
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(e);
		}
		
		if (response.getReason().equals(CardGameResponse.Reason.SUCCESS)) {
			return ResponseEntity
					       .status(HttpStatus.CREATED)
					       .body(response);
		} else {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(response);
		}
	}
	
	// a body is always needed but can be {}
	@PostMapping(value = "/cardgames/init/human/{playerId}", params = {"gameType", "ante"})
	//new cardGame with existing human if present
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity initCardGameForHuman(
			                                          @PathVariable("playerId") Integer playerId,
			                                          @RequestBody CardGame cardGameToUpdate,
			                                          @RequestParam(value = "gameType", required = false) String gameType,
			                                          @RequestParam(value = "ante", required = false) Integer ante) {
		
		//POST   api/cardgames/init/human/2   ?gameType/ante
		
		Map<String, String> pathAndRequestParams = new HashMap<>();
		pathAndRequestParams.put("playerId", String.valueOf(playerId));
		pathAndRequestParams.put("gameType", gameType);
		pathAndRequestParams.put("ante", String.valueOf(ante));
		
		CardGameResponse response;
		try {
			response = controller.play(CardGameStateMachine.Trigger.POST_INIT_HUMAN, pathAndRequestParams);
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(e);
		}
		
		if (response.getReason().equals(CardGameResponse.Reason.SUCCESS)) {
			return ResponseEntity
					       .status(HttpStatus.CREATED)
					       .body(response);
		} else {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(response);
		}
	}
	
	// a body is always needed but can be {}
	@PostMapping(value = "/cardgames/{id}/setup/{humanOrAi}", params = {"alias", "avatar", "aiLevel", "securedLoan"})
	// add human or ai to cardGame
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity setupPlayerForCardGame(
			                                            @PathVariable("id") Integer id,
			                                            @PathVariable("humanOrAi") String humanOrAi,
			                                            @RequestBody CardGame cardGameToUpdate,
			                                            @RequestParam(value = "alias", required = false) String alias,
			                                            @RequestParam(value = "avatar", required = false) String avatar,
			                                            @RequestParam(value = "aiLevel", required = false) String aiLevel,
			                                            @RequestParam(value = "securedLoan", required = false) Integer securedLoan) {
		
		//POST   api/cardgames/1/setup/human        ?alias/avatar/securedLoan            // no dealing yet
		//POST   api/cardgames/1/setup/ai           ?alias/avatar/securedLoan/aiLevel
		
		Map<String, String> pathAndRequestParams = new HashMap<>();
		pathAndRequestParams.put("gameId", String.valueOf(id));
		pathAndRequestParams.put("humanOrAi", humanOrAi);
		pathAndRequestParams.put("alias", alias);
		pathAndRequestParams.put("avatar", avatar);
		pathAndRequestParams.put("aiLevel", aiLevel);
		pathAndRequestParams.put("securedLoan", String.valueOf(securedLoan));
		
		CardGameResponse response;
		try {
			if (humanOrAi.equals("human")) {
				response = controller.play(CardGameStateMachine.Trigger.POST_SETUP_HUMAN, pathAndRequestParams);
			} else {
				response = controller.play(CardGameStateMachine.Trigger.POST_SETUP_AI, pathAndRequestParams);
			}
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(e);
		}
		
		if (response.getReason().equals(CardGameResponse.Reason.SUCCESS)) {
			return ResponseEntity
					       .status(HttpStatus.CREATED)
					       .body(response);
		} else {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(response);
		}
	}
	
	// a body is always needed but can be {}
	@PostMapping(value = "/cardgames/{id}/shuffle/cards", params = {"jokers"})
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity shuffleDeckForCardGame(
			                                            @PathVariable("id") Integer id,
			                                            @RequestBody CardGame cardGameToUpdate,
			                                            @RequestParam(value = "jokers", required = false, defaultValue = "0") Integer jokers) {
		
		//POST   api/cardgames/1/shuffle/cards  ?jokers                             // no dealing yet
		
		Map<String, String> pathAndRequestParams = new HashMap<>();
		pathAndRequestParams.put("gameId", String.valueOf(id));
		pathAndRequestParams.put("jokers", String.valueOf(jokers));
		
		CardGameResponse response;
		try {
			response = controller.play(CardGameStateMachine.Trigger.POST_SHUFFLE, pathAndRequestParams);
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(e);
		}
		
		if (response.getReason().equals(CardGameResponse.Reason.SUCCESS)) {
			return ResponseEntity
					       .status(HttpStatus.CREATED)
					       .body(response);
			
		} else {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(response);
		}
	}
	
	// a body is always needed but can be {}
	@PutMapping(value = "/cardgames/{id}/init") // change an initial cardGame
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity initExistingCardGame(
			                                          @PathVariable("id") Integer id,
			                                          @RequestBody CardGame cardGameToUpdate,
			                                          @RequestParam(value = "gameType", required = false) String gameType,
			                                          @RequestParam(value = "ante", required = false) Integer ante) {
		
		//PUT    api/cardgames/1/init          ?gameType/ante
		
		Map<String, String> pathAndRequestParams = new HashMap<>();
		pathAndRequestParams.put("gameId", String.valueOf(id));
		pathAndRequestParams.put("gameType", gameType);
		pathAndRequestParams.put("ante", String.valueOf(ante));
		
		CardGameResponse response;
		try {
			response = controller.play(CardGameStateMachine.Trigger.PUT_INIT, pathAndRequestParams);
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(e);
		}
		
		if (response.getReason().equals(CardGameResponse.Reason.SUCCESS)) {
			return ResponseEntity
					       .status(HttpStatus.OK)
					       .body(response);
			
		} else {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(response);
		}
	}
	
	// a body is always needed but can be {}
	@PutMapping(value = "/cardgames/{id}/setup/players/{casinoId}") // change player in cardGame
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity setupPlayerInExistingCardGame(
			                                                   @PathVariable("id") Integer id,
			                                                   @PathVariable("casinoId") Integer casinoId,
			                                                   @RequestBody CardGame cardGameToUpdate,
			                                                   @RequestParam(value = "alias", required = false) String alias,
			                                                   @RequestParam(value = "avatar", required = false) String avatar,
			                                                   @RequestParam(value = "aiLevel", required = false) String aiLevel,
			                                                   @RequestParam(value = "securedLoan", required = false) Integer securedLoan,
			                                                   @RequestParam(value = "playingOrder", required = false) Integer playingOrder) {
		
		//PUT    api/cardgames/1/setup/players/2   ?alias/avatar/securedLoan/aiLevel/playingOrder
		
		Map<String, String> pathAndRequestParams = new HashMap<>();
		pathAndRequestParams.put("gameId", String.valueOf(id));
		pathAndRequestParams.put("casinoId", String.valueOf(casinoId));
		pathAndRequestParams.put("alias", alias);
		pathAndRequestParams.put("avatar", avatar);
		pathAndRequestParams.put("aiLevel", aiLevel);
		pathAndRequestParams.put("securedLoan", String.valueOf(securedLoan));
		pathAndRequestParams.put("playingOrder", String.valueOf(playingOrder));
		
		CardGameResponse response;
		try {
			response = controller.play(CardGameStateMachine.Trigger.PUT_SETUP_PLAYER, pathAndRequestParams);
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(e);
		}
		
		if (response.getReason().equals(CardGameResponse.Reason.SUCCESS)) {
			return ResponseEntity
					       .status(HttpStatus.OK)
					       .body(response);
			
		} else {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(response);
		}
	}
	
	// a body is always needed but can be {}
	@PutMapping(value = "/cardgames/{id}/turn/players/{casinoId}") // a player plays a cardGame
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity turnFromPlayerInCardGame(
			                                              @PathVariable("id") Integer id,
			                                              @PathVariable("casinoId") Integer casinoId,
			                                              @RequestBody CardGame cardGameToUpdate,
			                                              @RequestParam(value = "action", required = true) String action) {
		
		
		//PUT    api/cardgames/1/turn/    players/2   ?action=deal/higher/lower/pass/auto for human or ai player
		
		Map<String, String> pathAndRequestParams = new HashMap<>();
		pathAndRequestParams.put("gameId", String.valueOf(id));
		pathAndRequestParams.put("casinoId", String.valueOf(casinoId));
		pathAndRequestParams.put("action", action);
		
		CardGameResponse response;
		try {
			response = controller.play(CardGameStateMachine.Trigger.PUT_TURN, pathAndRequestParams);
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(e);
		}
		
		if (response.getReason().equals(CardGameResponse.Reason.SUCCESS)) {
			return ResponseEntity
					       .status(HttpStatus.OK)
					       .body(response);
			
		} else {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(response);
		}
	}
	
	@DeleteMapping(value = "/cardgames/{id}/setup/{humanOrAi}/{casinoId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity deletePlayerInCardGame(
			                                            @PathVariable("id") Integer id,
			                                            @PathVariable("humanOrAi") String humanOrAi,
			                                            @PathVariable("casinoId") Integer casinoId) {
		
		//DELETE api/cardgames/1/setup/human/3
		//DELETE api/cardgames/1/setup/ai/2
		
		Map<String, String> pathAndRequestParams = new HashMap<>();
		pathAndRequestParams.put("gameId", String.valueOf(id));
		pathAndRequestParams.put("casinoId", String.valueOf(casinoId));
		pathAndRequestParams.put("humanOrAi", humanOrAi);
		
		CardGameResponse response;
		
		try {
			if (humanOrAi.equals("human")) {
				response = controller.play(CardGameStateMachine.Trigger.DELETE_SETUP_HUMAN, pathAndRequestParams);
			} else {
				response = controller.play(CardGameStateMachine.Trigger.DELETE_SETUP_AI, pathAndRequestParams);
			}
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(e);
		}
		
		if (response.getReason().equals(CardGameResponse.Reason.SUCCESS)) {
			return ResponseEntity
					       .status(HttpStatus.NO_CONTENT)
					       .body(response);
			
		} else {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(response);
		}
		
	}
	
	@DeleteMapping(
			{"/cardgames/{id}",
					"/cardgames/{id}/{resource}",
					"/cardgames/{id}/{resource}/{resourceId}",
					"/cardgames/{id}/{resource}/{resourceId}/{extraResource}"})
	// delete (player(s), cardsInHand or cardsInDeck for or together with) cardGame
	//@Produces({MediaType.APPLICATION_JSON})
	public ResponseEntity deleteCardGames(
			                                     @PathVariable("id") Integer id,
			                                     @PathVariable("resource") Optional<String> resource,
			                                     @PathVariable("resourceId") Optional<Integer> resourceId,
			                                     @PathVariable("extraResource") Optional<String> extraResource) {
		
		//GET    api/cardgames/1
		//GET    api/cardgames/1/player                    // gives active casino (resource=player)
		//GET    api/cardgames/1/players                   // gives all casinos (resource=players)
		//GET    api/cardgames/1/cards                     // gives all decks (resources=cards)
		//GET    api/cardgames/1/players/2                 // gives a specific casino (resource=players, resourceId=int)
		//GET    api/cardgames/1/players/2/cards           // gives a all hands for a player (resource=players, resourceId=int, extraResource=cards)
		
		
		Map<String, String> pathAndRequestParams = new HashMap<>();
		pathAndRequestParams.put("gameId", String.valueOf(id));
		
		// functional style
		resource.ifPresent(s -> pathAndRequestParams.put("resource", s));
		resourceId.ifPresent(integer -> pathAndRequestParams.put("resourceId", String.valueOf(integer)));
		extraResource.ifPresent(s -> pathAndRequestParams.put("extraResource", s));
		
		CardGameResponse response;
		try {
			response = controller.play(CardGameStateMachine.Trigger.DELETE, pathAndRequestParams);
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(e);
		}
		
		if (response.getReason().equals(CardGameResponse.Reason.SUCCESS)) {
			return ResponseEntity
					       .status(HttpStatus.NO_CONTENT)
					       .body(response);
			
		} else {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(response);
		}
	}
}
