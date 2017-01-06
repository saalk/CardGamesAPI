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
	private ModelMapperUtil mapUtil;
	
	@GetMapping("/cardgames/{id}/{resource}/{resourceId}/{extraResource}") // get (player(s), cardsInHand or cardsInDeck for) cardGame
	@Produces({MediaType.APPLICATION_JSON})
	public ResponseEntity getCardGames(
			                                  @PathVariable("id") Integer id,
			                                  @PathVariable("resource") String resource,
			                                  @PathVariable("resourceId") Integer resourceId,
			                                  @PathVariable("extraResource") String extraResource) {

		//GET    api/cardgames/1
		//GET    api/cardgames/1/player                     // gives active player
		//GET    api/cardgames/1/players
		//GET    api/cardgames/1/cards
		//GET    api/cardgames/1/players                    // also gives cardsinhand and indication active player
		//GET    api/cardgames/1/players/2/cards           // only cardsinhand

		String responseText = "";
		if (id==null || id.toString().isEmpty()) {
			responseText += "No id in path specified";
		} else {
			responseText += "Id in path=" + id;
		}
		if (resource.equals("")) {
			responseText += " No resource in path specified";
		} else {
			responseText += " Resource in path=" + resource;
		}

		if (resourceId.toString().isEmpty()) {
			responseText += " No resourceId in path specified";
		} else {
			responseText += " ResourceId in path=" + resourceId;
		}
		if (extraResource.equals("")) {
			responseText += " No extraResource in path specified";
		} else {
			responseText += " ExtraResource in path=" + extraResource;
		}
		return ResponseEntity
				       .status(HttpStatus.OK)
				       .body(responseText);
	}
	
	// a body is always needed but can be {}
	@PostMapping(value = "/cardgames/init", params = {"gameType", "ante"}) 	//new cardGame with existing human if present
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity initCardGame(
			                                  @RequestBody CardGame cardGameToUpdate,
			                                  @RequestParam(value = "gameType", required = false) String gameType,
			                                  @RequestParam(value = "ante", required = false) Integer ante) throws Exception {
		
		//POST   api/cardgames/init           ?gameType/ante
		
		// TODO @Valid + @NotNull and validate from AbstractResource
		// TODO add a overloadMitigator and security events
		
		CardGameResponse response;
		CardGameController controller = new CardGameController();
		
		Map<String, String> pathAndRequestParams = new HashMap<>();
		pathAndRequestParams.put("gameType", gameType);
		pathAndRequestParams.put("ante", String.valueOf(ante));
		
		response = controller.play(CardGameStateMachine.Trigger.POST_INIT, pathAndRequestParams);
		
		if (response.isSuccess()) {
//			return ResponseEntity
//					       .status(HttpStatus.CREATED)
//					       .body(response);

		} else {
//			return ResponseEntity
//					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
//					       .body(e);
		}
		
		// TODO import org.springframework.http.ResponseEntity vs import javax.ws.rs.core.Response;
		
		// STUBBED RESULT
		String responseText = "api/cardgames/init = ";
		if (gameType.equals("")) {
			responseText += "No gameType in param specified";
		} else {
			responseText += "GameType in param=" + gameType;
		}
		if (ante == null || ante.toString().isEmpty()) {
			responseText += " No ante in param specified";
		} else {
			responseText += " Ante in param=" + ante;
		}
		return ResponseEntity
				       .status(HttpStatus.CREATED)
				       .body(responseText);
	}
	
	// a body is always needed but can be {}
	@PostMapping(value = "/cardgames/init/human/{playerId}", params = {"gameType", "ante"}) 	//new cardGame with existing human if present
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity initCardGameForHuman(
			                                  @PathVariable("playerId") Integer playerId,
			                                  @RequestBody CardGame cardGameToUpdate,
			                                  @RequestParam(value = "gameType", required = false) String gameType,
			                                  @RequestParam(value = "ante", required = false) Integer ante) throws Exception {
		
		//POST   api/cardgames/init/human/2   ?gameType/ante
		
		String responseText = "api/cardgames/init/human/{id} = ";
		
		if (playerId.toString().isEmpty()) {
			responseText += "No playerId in path specified";
		} else {
			responseText += "PlayerId in path=" + playerId;
		}
		
		if (gameType.equals("")) {
			responseText += " No gameType in param specified";
		} else {
			responseText += " GameType in param=" + gameType;
		}
		if (ante == null || ante.toString().isEmpty()) {
			responseText += " No ante in param specified";
		} else {
			responseText += " Ante in param=" + ante;
		}
		
		return ResponseEntity
				       .status(HttpStatus.CREATED)
				       .body(responseText);
		
	}
	
	// a body is always needed but can be {}
	@PostMapping(value = "/cardgames/{id}/setup/{humanOrAi}" , params = {"alias", "avatar", "aiLevel", "securedLoan"}) // add human or ai to cardGame
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity setupPlayerForCardGame(
			                                    @PathVariable("id") Integer id,
			                                    @PathVariable("humanOrAi") String humanOrAi,
			                                    @RequestBody CardGame cardGameToUpdate,
			                                    @RequestParam(value = "alias", required = false) String alias,
			                                    @RequestParam(value = "avatar", required = false) String avatar,
			                                    @RequestParam(value = "aiLevel", required = false) String aiLevel,
			                                    @RequestParam(value = "securedLoan", required = false) Integer securedLoan) throws Exception {

		//POST   api/cardgames/1/setup/human        ?alias/avatar/securedLoan            // no dealing yet
		//POST   api/cardgames/1/setup/ai           ?alias/avatar/securedLoan/aiLevel

		String responseText = "api/cardgames/{id}/setup/{humanOrAi} = ";
		
		if (id==null || id.toString().isEmpty()) {
			responseText += "No id in path specified";
		} else {
			responseText += "Id in path=" + id;
		}
		if (humanOrAi.equals("")) {
			responseText += " No humanOrAi in path specified";
		} else {
			responseText += " HumanOrAi in path=" + humanOrAi;
		}

		if (alias.equals("")) {
			responseText += " No alias in param specified";
		} else {
			responseText += " Alias in param=" + alias;
		}
		if (avatar.equals("")) {
			responseText += " No avatar in param specified";
		} else {
			responseText += " Avatar in param=" + avatar;
		}
		if (aiLevel.equals("")) {
			responseText += " No aiLevel in param specified";
		} else {
			responseText += " AiLevel in param=" + alias;
		}
		if (securedLoan == null || securedLoan.toString().isEmpty()) {
			responseText += " No securedLoan in param specified";
		} else {
			responseText += " SecuredLoan in param=" + securedLoan;
		}

		return ResponseEntity
				       .status(HttpStatus.CREATED)
				       .body(responseText);

	}

	// a body is always needed but can be {}
	@PostMapping(value = "/cardgames/{id}/shuffle/cards" , params = {"jokers"})
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity shuffleDeckForCardGame(
			                                             @PathVariable("id") Integer id,
			                                             @RequestBody CardGame cardGameToUpdate,
			                                             @RequestParam(value = "jokers", required = false) Integer jokers) throws Exception {

		//POST   api/cardgames/1/shuffle/cards  ?jokers                             // no dealing yet

		String responseText = "api/cardgames/{id}/shuffle = ";
		
		if (id==null || id.toString().isEmpty()) {
			responseText += "No id in path specified";
		} else {
			responseText += "Id in path=" + id;
		}

		if (jokers == null || jokers.toString().isEmpty()) {
			responseText += " No jokers in param specified";
		} else {
			responseText += " Jokers in param=" + jokers;
		}

		return ResponseEntity
				       .status(HttpStatus.CREATED)
				       .body(responseText);
	}

	// a body is always needed but can be {}
	@PutMapping(value = "/cardgames/{id}/init") // change an initial cardGame
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity initExistingCardGame(
			                                    @PathVariable("id") Integer id,
			                                    @RequestBody CardGame cardGameToUpdate,
			                                    @RequestParam(value = "gameType", required = false) String gameType,
			                                    @RequestParam(value = "ante", required = false) Integer ante) throws Exception {

		//PUT    api/cardgames/1/init          ?gameType/ante

		String responseText = "api/cardgames/{id}/init = ";

		if (id==null || id.toString().isEmpty()) {
			responseText += "No id in path specified";
		} else {
			responseText += "Id in path=" + id;
		}

		if (gameType.equals("")) {
			responseText += " No gameType in param specified";
		} else {
			responseText += " GameType in param=" + gameType;
		}
		if (ante==null || ante.toString().isEmpty()) {
			responseText += " No ante in param specified";
		} else {
			responseText += " Ante in param=" + ante;
		}

		return ResponseEntity
				       .status(HttpStatus.OK)
				       .body(responseText);
	}

	// a body is always needed but can be {}
	@PutMapping(value = "/cardgames/{id}/setup/players/{playerId}") // change player in cardGame
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity setupPlayerInExistingCardGame(
			                                    @PathVariable("id") Integer id,
			                                    @PathVariable("playerId") Integer playerId,
			                                    @RequestBody CardGame cardGameToUpdate,
			                                    @RequestParam(value = "alias", required = false) String alias,
			                                    @RequestParam(value = "avatar", required = false) String avatar,
			                                    @RequestParam(value = "aiLevel", required = false) String aiLevel,
			                                    @RequestParam(value = "securedLoan", required = false) Integer securedLoan,
			                                    @RequestParam(value = "playingOrder", required = false) Integer playingOrder) throws Exception {

		//PUT    api/cardgames/1/setup/players/2   ?alias/avatar/securedLoan/aiLevel/playingOrder

		String responseText = "api/cardgames/{id}/setup/players/{playerId} = ";

		if (id.toString().isEmpty()) {
			responseText += "No id in path specified";
		} else {
			responseText += "Id in path=" + id;
		}
		if (playerId.toString().isEmpty()) {
			responseText += " No playerId in path specified";
		} else {
			responseText += " PlayerId in path=" + playerId;
		}

		if (alias.equals("")) {
			responseText += " No alias in param specified";
		} else {
			responseText += " Alias in param=" + alias;
		}
		if (avatar.equals("")) {
			responseText += " No avatar in param specified";
		} else {
			responseText += " Avatar in param=" + avatar;
		}
		if (aiLevel.equals("")) {
			responseText += " No aiLevel in param specified";
		} else {
			responseText += " AiLevel in param=" + alias;
		}
		if (securedLoan == null || securedLoan.toString().isEmpty()) {
			responseText += " No securedLoan in param specified";
		} else {
			responseText += " SecuredLoan in param=" + securedLoan;
		}
		if (playingOrder == null || playingOrder.toString().isEmpty()) {
			responseText += " No playingOrder in param specified";
		} else {
			responseText += " PlayingOrder in param=" + playingOrder;
		}

		return ResponseEntity
				       .status(HttpStatus.OK)
				       .body(responseText);
	}

	// a body is always needed but can be {}
	@PutMapping(value = "/cardgames/{id}/turn/players/{playerId}") // a player plays a cardGame
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity turnFromPlayerInCardGame(
			                                    @PathVariable("id") Integer id,
			                                    @PathVariable("playerId") Integer playerId,
			                                    @RequestBody CardGame cardGameToUpdate,
			                                    @RequestParam(value = "action", required = true) String action) throws Exception {


		//PUT    api/cardgames/1/turn/    players/2   ?action=deal/higher/lower/pass/auto for human or ai player

		String responseText = "api/cardgames/{id}/turn/players/{playerId} = ";

		if (id == null || id.toString().isEmpty()) {
			responseText += "No id in path specified";
		} else {
			responseText += "Id in path=" + id;
		}
		if (playerId == null || playerId.toString().isEmpty()) {
			responseText += " No playerId in path specified";
		} else {
			responseText += " PlayerId in path=" + playerId;
		}

		if (action.equals("")) {
			responseText += " No action in param specified";
		} else {
			responseText += " Action in param=" + action;
		}

		return ResponseEntity
				       .status(HttpStatus.OK)
				       .body(responseText);
	}

	@DeleteMapping(value = "/cardgames/{id}/setup/{humanOrAi}/{playerId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity deletePlayerInCardGame(
			@PathVariable("id") Integer id,
			@PathVariable("humanOrAi") String humanOrAi,
			@PathVariable("playerId") Integer playerId) {

		//DELETE api/cardgames/1/setup/human/3
		//DELETE api/cardgames/1/setup/ai/2
		
		String responseText = "api/cardgames/{id}/setup/{humanOrAi}/(playerId} = ";

		if (id == null || id.toString().isEmpty()) {
			responseText += "No id in path specified";
		} else {
			responseText += "Id in path=" + id;
		}
		if (humanOrAi.equals("")) {
			responseText += " No humanOrAi in path specified";
		} else {
			responseText += " HumanOrAi in path=" + humanOrAi;
		}
		if (playerId == null || playerId.toString().isEmpty()) {
			responseText += " No playerId in path specified";
		} else {
			responseText += " PlayerId in path=" + playerId;
		}

		return ResponseEntity
				       .status(HttpStatus.OK)
				       .body(responseText);
	}

	
}
