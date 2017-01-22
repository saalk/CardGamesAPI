package nl.knikit.cardgames.controller;
// @formatter:off
/*
@startuml src/main/resources/plantuml/GalacticCasinoController.png
skinparam classAttributeIconSize 0
package "nl.deknik.cardgames" {
package "controller" {
class GalacticCasinoController {
-State state
-Trigger trigger
+ {static} main()
..other methods..
}
enum State {
IS_SETUP
HAS_PLAYERS
..
PLAYING
GAME_WON
..
NO_WINNER
OFFER_FICHES
 }
enum Trigger {
POST_INIT
PUT_TURN
POST_INIT_HUMAN
..
TURN_STARTED
SHOW_RESULTS
TURN_ENDED
..
ROUNDS_ENDED
PLAYER_WINS
NO_CARDS_LEFT
..
GAME_FINISHED
OFFER_ACCEPTED
QUIT
}
enum State -> GalacticCasinoController
GalacticCasinoController --> enum Trigger
package "nl.deknik.cardgames" {
package "model" {
GalacticCasinoController .. GameOld
GalacticCasinoController .. PlayerOld
GalacticCasinoController .. PlayerOld.Predict
GalacticCasinoController .. AiLevel
GalacticCasinoController .. CardGame
GalacticCasinoController .. GameVariant
GalacticCasinoController .. Avatar
}
package "nl.deknik.cardgames" {
package "utils" {
GalacticCasinoController . Console
}
}
@enduml
*//*

*/
/*
@startuml

state GalacticCasinoController {
[*] -> IS_SETUP
IS_SETUP -down-> OFFER_FICHES: QUIT
IS_SETUP: start : +1000 fiches
IS_SETUP: offer : +500 fiches
note right of IS_SETUP: Games\n HIGHLOW\n- simple double or nothing\n- three in a row
IS_SETUP -down-> HAS_PLAYERS: POST_INIT
HAS_PLAYERS --> IS_SETUP: PUT_TURN
HAS_PLAYERS: select # bots
HAS_PLAYERS --> GameEngine: POST_INIT_HUMAN
OFFER_FICHES -up-> IS_SETUP: OFFER_ACCEPTED
OFFER_FICHES --> [*]

state GameEngine {
[*] -> PLAYING
PLAYING --> PLAYING: PLAYER_PASSES
PLAYING --> GAME_WON: TURN_STARTED
PLAYING --> NO_WINNER: NO_CARDS_LEFT \n or ROUNDS_ENDED
GAME_WON --> GAME_WON: SHOW_RESULTS
GAME_WON --> PLAYING: TURN_ENDED
GAME_WON --> NO_WINNER: NO_CARDS_LEFT \n or PLAYERS_WINS
NO_WINNER -up-> IS_SETUP: GAME_FINISHED
}
}
@enduml
*/
// @formatter:on

import com.github.oxo42.stateless4j.StateMachineConfig;

import nl.knikit.cardgames.VO.CardGameFlowDTO;
import nl.knikit.cardgames.commons.controller.AbstractController;
import nl.knikit.cardgames.commons.error.ErrorResponse;
import nl.knikit.cardgames.commons.error.HttpError;
import nl.knikit.cardgames.commons.event.FlowDTOBuilder;
import nl.knikit.cardgames.event.CreateCasinoForGameAndPlayerEvent;
import nl.knikit.cardgames.event.CreateDeckForGameEvent;
import nl.knikit.cardgames.event.CreateHandForCasinoForGameAndPlayerEvent;
import nl.knikit.cardgames.event.CreatePlayerEvent;
import nl.knikit.cardgames.event.DeleteCardGameEvent;
import nl.knikit.cardgames.event.DeleteCasinoForGameAndPlayerEvent;
import nl.knikit.cardgames.event.GetCardGameDetailsEvent;
import nl.knikit.cardgames.event.UpdateCardGameDetailsEvent;
import nl.knikit.cardgames.event.UpdateCasinoForGameAndPlayerEvent;
import nl.knikit.cardgames.event.UpdateDeckForGameAndCasinoEvent;
import nl.knikit.cardgames.event.UpdatePlayerDetailsEvent;
import nl.knikit.cardgames.mapper.ModelMapperUtil;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.response.CardGameResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import static nl.knikit.cardgames.model.state.CardGameStateMachine.State;
import static nl.knikit.cardgames.model.state.CardGameStateMachine.Trigger;

//TODO import com.github.oxo42.stateless4j.delegates.Action;

/**
 * <H1>GalacticCasinoController</H1>Main functionality: <ul> <li>a native casino adventure
 * application that runs on your desktop's console <li>offers card game 'HighLow' <li>each visitor
 * starts with 1000 credits <li>choose between various card game variants <li>select up to 2 ai
 * Players (bots) to join a game <li>select their ai level (high, average, low) and try to beat them
 * </ul> <p> <p><h2>GalacticCasinoController Class diagram</h2>Since this is class with the main
 * method it uses lots of 'HAS-A' relationship. Codified via <b>Associations</b><img
 * src="../../../../../../src/main/resources/plantuml/GalacticCasinoController.png" alt="UML1">
 * <h2>Controller Class diagram 'state and triggers</h2><img src="../../../../../../src/main/resources/plantuml/StateMachine.png"
 * alt="UML2">
 **/

// TODO make this CardGame controller special to a context HightLowCardGame, BlackJackCardGame, etc.. ?
@Slf4j
@Component
@Scope("prototype")
public class CardGameController extends AbstractController<Game> {
	
	// 1 - configure the state machine in the AbstractController
	private static StateMachineConfig<State, Trigger> config = new StateMachineConfig<>();
	
	static {
		// @formatter:off
		// start on player page
		config.configure(State.IS_CONFIGURED)
				// continue on players page
				.permitReentry(Trigger.POST_INIT)
				.permitReentry(Trigger.PUT_INIT)
				.permitReentry(Trigger.OK)
				// continue on game page
				.permit(Trigger.POST_INIT_HUMAN, State.HAS_PLAYERS)
				.permit(Trigger.POST_SETUP_HUMAN, State.HAS_PLAYERS)
				.permit(Trigger.ERROR, State.ERROR);
		
		config.configure(State.HAS_PLAYERS)
				.permitReentry(Trigger.ERROR)
				.permitReentry(Trigger.POST_SETUP_AI)
				.permitReentry(Trigger.DELETE_SETUP_AI)
				.permitReentry(Trigger.PUT_INIT)
				.permitReentry(Trigger.PUT_SETUP_PLAYER)
				.permitReentry(Trigger.OK)
				// continue on players page
				.permit(Trigger.DELETE_SETUP_HUMAN, State.IS_CONFIGURED)
				// continue on casino page
				.permit(Trigger.POST_SHUFFLE, State.IS_SETUP)
				.permit(Trigger.ERROR, State.ERROR);
		
		config.configure(State.IS_SETUP)
				.permitReentry(Trigger.OK)
				
				.permit(Trigger.PUT_TURN, State.PLAYING)
				.permit(Trigger.ERROR, State.ERROR);
		
		
		config.configure(State.PLAYING)
				.permitReentry(Trigger.PUT_TURN)
				.permitReentry(Trigger.OK)
				
				// continue on results page
				.permit(Trigger.PLAYER_WINS, State.GAME_WON)
				.permit(Trigger.NO_CARDS_LEFT, State.NO_WINNER)
				.permit(Trigger.ROUNDS_ENDED, State.NO_WINNER)
				.permit(Trigger.ERROR, State.ERROR);
		
		
		config.configure(State.GAME_WON)
				.permitReentry(Trigger.OK)
				.permit(Trigger.PUT_TURN, State.PLAYING); // allows continue!
		
		config.configure(State.NO_WINNER)
				.permitReentry(Trigger.OK)
				.permitReentry(Trigger.NO_CARDS_LEFT)
				.permitReentry(Trigger.ROUNDS_ENDED)
				.permit(Trigger.PUT_TURN, State.PLAYING) // allows continue!
				.permit(Trigger.ERROR, State.ERROR);
		
		// @formatter:on
	}
	
	protected StateMachineConfig<State, Trigger> getStateMachineConfiguration() {
		return config;
	}
	
	// 2 - Prepare a builder for CardGameFlowDTO and get methods like
	//     addEvent, addStateMachine, getNextInFlow and
	//     start, transition, build
	private FlowDTOBuilder<CardGameFlowDTO> builder;
	
	@Resource
	private ApplicationContext applicationContext;
	
	@PostConstruct
	public void init() {
		this.builder = new FlowDTOBuilder<>(new CardGameFlowDTO());
		this.builder.setApplicationContext(applicationContext);
		
		String message = String.format("PostConstruct in controller is: %s", applicationContext);
		log.info(message);
	}
/*  example post construct:
	public class Foo {
		@Inject
		Logger LOG;
		@PostConstruct
		public void fooInit(){
			LOG.info("This will be printed; LOG has already been injected");
		}
		public Foo() {
			LOG.info("This will NOT be printed, LOG is still null");
			// NullPointerException will be thrown here
		}
	}  */
	
	
	// 3 - Play a the CardGame based on the Trigger and a list of input data
	@Autowired
	private ModelMapperUtil mapUtil;
	
	public CardGameResponse play(Trigger trigger, Map<String, String> pathAndQueryData) throws Exception {
		
		CardGameFlowDTO flowDTO = new CardGameFlowDTO();
		final CardGameResponse.CardGameResponseBuilder responseBuilder;
		
		
		String message = String.format("CardGameController Trigger to execute is: %s and data %s", trigger, pathAndQueryData);
		log.info(message);
		
		switch (trigger) {
			
			case POST_INIT:
				
				//POST   api/cardgames/init?gameType={g},ante={a}
				// init makes a default card game and adds it as context to flowDTO
				flowDTO = builder
						          .addContext(super.init(new Game()))
						          .addStateMachine(this.stateMachine)
						          .addEvent(UpdateCardGameDetailsEvent.class)
						          .build();
				flowDTO.processPathAndQueryParamsAndTrigger(pathAndQueryData, trigger);
				flowDTO.setGameByContext();
				flowDTO.start();
				
				message = String.format("CardGameController gametype are: %s", flowDTO.getSuppliedGameType());
				log.info(message);
				
				// state IS_CONFIGURED
				break;
			
			case PUT_INIT:
				
				//PUT    api/cardgames/init/1?gameType={g},ante={a}
				flowDTO = builder
						          .addContext(super.reinstate(Integer.parseInt(pathAndQueryData.get("gameId"))))
						          .addStateMachine(this.stateMachine)
						          .addEvent(UpdateCardGameDetailsEvent.class)
						          .build();
				
				// reinstate get the card game and adds it as context to flowDTO
				List<State> possiblePUT_INITStates = new ArrayList<>();
				possiblePUT_INITStates.add(State.IS_CONFIGURED);
				possiblePUT_INITStates.add(State.HAS_PLAYERS);
				try {
					stateMachine.checkAll(possiblePUT_INITStates);
				} catch (Exception e) {
					// make error response
					responseBuilder = CardGameResponse.builder();
					ErrorResponse error = new ErrorResponse(HttpError.CODE_CONFLICT,  "shuffle or turn", "init", "state");
					responseBuilder.errorCode(error.getCode());
					responseBuilder.reason(CardGameResponse.Reason.FAILURE);
					return responseBuilder.build();
				}
			
			flowDTO.processPathAndQueryParamsAndTrigger(pathAndQueryData, trigger);
				flowDTO.start();
				// state IS_CONFIGURED or HAS_PLAYERS
				break;
			
			case POST_INIT_HUMAN:
				
				//POST   api/cardgames/init/human/2   ?gameType/ante
				
				// init makes a default card game and adds it as context to flowDTO
				flowDTO = builder
						          .addContext(super.init(new Game()))
						          .addStateMachine(this.stateMachine)
						          .addEvent(UpdateCardGameDetailsEvent.class)
						          .addEvent(CreateCasinoForGameAndPlayerEvent.class)
						          .build();
				flowDTO.processPathAndQueryParamsAndTrigger(pathAndQueryData, trigger);
				flowDTO.setGameByContext();
				flowDTO.start();
				// state HAS_PLAYERS
				break;
			
			case POST_SETUP_HUMAN:
				
				//POST   api/cardgames/1/setup/human        ?alias/avatar/securedLoan            // no dealing yet
				
				// reinstate get the card game and adds it as context to flowDTO
				flowDTO = builder
						          .addContext(super.reinstate(Integer.parseInt(pathAndQueryData.get("gameId"))))
						          .addEvent(CreatePlayerEvent.class)
						          .addEvent(CreateCasinoForGameAndPlayerEvent.class)
						          .addStateMachine(this.stateMachine)
						          .build();
				
				List<State> possiblePOST_SETUPStates = new ArrayList<>();
				possiblePOST_SETUPStates.add(State.IS_CONFIGURED);
				possiblePOST_SETUPStates.add(State.HAS_PLAYERS);
				try {
					stateMachine.checkAll(possiblePOST_SETUPStates);
				} catch (Exception e) {
					// make error response
					responseBuilder = CardGameResponse.builder();
					ErrorResponse error = new ErrorResponse(HttpError.CODE_CONFLICT,  "shuffle or turn", "setup", "state");
					responseBuilder.errorCode(error.getCode());
					responseBuilder.reason(CardGameResponse.Reason.FAILURE);
					return responseBuilder.build();
				}
				
				flowDTO.processPathAndQueryParamsAndTrigger(pathAndQueryData, trigger);
				flowDTO.start();
				// state HAS_PLAYERS
				break;
			
			case POST_SETUP_AI:
				
				//POST   api/cardgames/1/setup/ai           ?alias/avatar/securedLoan/aiLevel
				
				// reinstate get the card game and adds it as context to flowDTO
				flowDTO = builder
						          .addContext(super.reinstate(Integer.parseInt(pathAndQueryData.get("gameId"))))
						          .addEvent(CreatePlayerEvent.class)
						          .addEvent(CreateCasinoForGameAndPlayerEvent.class)
						          .addStateMachine(this.stateMachine)
						          .build();
				
				List<State> possiblePOST_SETUP_AIStates = new ArrayList<>();
				possiblePOST_SETUP_AIStates.add(State.IS_CONFIGURED);
				possiblePOST_SETUP_AIStates.add(State.HAS_PLAYERS);
				try {
					stateMachine.checkAll(possiblePOST_SETUP_AIStates);
				} catch (Exception e) {
					// make error response
					responseBuilder = CardGameResponse.builder();
					ErrorResponse error = new ErrorResponse(HttpError.CODE_CONFLICT,  "shuffle or turn", "setup", "state");
					responseBuilder.errorCode(error.getCode());
					responseBuilder.reason(CardGameResponse.Reason.FAILURE);
					return responseBuilder.build();
				}
				
				flowDTO.processPathAndQueryParamsAndTrigger(pathAndQueryData, trigger);
				flowDTO.start();
				// state HAS_PLAYERS or stays IS_CONFIGURED
				break;
			
			case PUT_SETUP_PLAYER:
				
				//PUT    api/cardgames/1/setup/players/2?name/avatar/securedLoan/aiLevel/playingOrder
				
				
				// reinstate get the card game and adds it as context to flowDTO
				flowDTO = builder
						          .addContext(super.reinstate(Integer.parseInt(pathAndQueryData.get("gameId"))))
						          .addEvent(UpdatePlayerDetailsEvent.class)
						          .addEvent(UpdateCasinoForGameAndPlayerEvent.class)
						          .addStateMachine(this.stateMachine)
						          .build();
				
				//stateMachine.check(State.IS_CONFIGURED); surround with if
				List<State> possiblePUT_SETUP_PLAYERStates = new ArrayList<>();
				possiblePUT_SETUP_PLAYERStates.add(State.IS_CONFIGURED);
				possiblePUT_SETUP_PLAYERStates.add(State.HAS_PLAYERS);
				try {
					stateMachine.checkAll(possiblePUT_SETUP_PLAYERStates);
				} catch (Exception e) {
					// make error response
					responseBuilder = CardGameResponse.builder();
					ErrorResponse error = new ErrorResponse(HttpError.CODE_CONFLICT,  "shuffle or turn", "setup", "state");
					responseBuilder.errorCode(error.getCode());
					responseBuilder.reason(CardGameResponse.Reason.FAILURE);
					return responseBuilder.build();
				}
				
				flowDTO.processPathAndQueryParamsAndTrigger(pathAndQueryData, trigger);
				flowDTO.start();
				// stays state HAS_PLAYERS or stays IS_CONFIGURED
				break;
			
			case POST_SHUFFLE:
				
				//POST    api/cardgames/1/shuffle?jokers=0
				
				
				// reinstate get the card game and adds it as context to flowDTO
				flowDTO = builder
						          .addContext(super.reinstate(Integer.parseInt(pathAndQueryData.get("gameId"))))
						          .addEvent(CreateDeckForGameEvent.class)
						          .addStateMachine(this.stateMachine)
						          .build();
				
				try {
					stateMachine.check(State.HAS_PLAYERS);
				} catch (Exception e) {
					// make error response
					responseBuilder = CardGameResponse.builder();
					ErrorResponse error = new ErrorResponse(HttpError.CODE_CONFLICT,  "init, setup or turn", "shuffle", "state");
					responseBuilder.errorCode(error.getCode());
					responseBuilder.reason(CardGameResponse.Reason.FAILURE);
					return responseBuilder.build();
				}
				
				flowDTO.processPathAndQueryParamsAndTrigger(pathAndQueryData, trigger);
				flowDTO.start();
				// state IS_SETUP
				break;
			
			case PUT_TURN:
				
				//PUT    api/cardgames/1/turn/players/2?action=deal/higher/lower/pass // for human player
				//PUT    api/cardgames/1/turn/players/3?action=next  // auto deal or pass for ai player
				
				
				// reinstate get the card game and adds it as context to flowDTO
				// TODO
				flowDTO = builder
						          .addContext(super.reinstate(Integer.parseInt(pathAndQueryData.get("gameId"))))
						          .addEvent(UpdateDeckForGameAndCasinoEvent.class)
						          .addEvent(CreateHandForCasinoForGameAndPlayerEvent.class)
						          .addStateMachine(this.stateMachine)
						          .build();
				
				List<State> possiblePUT_TURNStates = new ArrayList<>();
				possiblePUT_TURNStates.add(State.IS_SETUP);
				possiblePUT_TURNStates.add(State.PLAYING);
				try {
					stateMachine.checkAll(possiblePUT_TURNStates);
				} catch (Exception e) {
					// make error response
					responseBuilder = CardGameResponse.builder();
					ErrorResponse error = new ErrorResponse(HttpError.CODE_CONFLICT,  "init, setup or shuffle", "turn", "state");
					responseBuilder.errorCode(error.getCode());
					responseBuilder.reason(CardGameResponse.Reason.FAILURE);
					return responseBuilder.build();
				}
				
				flowDTO.processPathAndQueryParamsAndTrigger(pathAndQueryData, trigger);
				flowDTO.start();
				// state PLAYING or still in IS_SETUP
				// TODO determine event for state GameWon, NoMoreCards or RoundsEnded
				break;
			
			case DELETE_SETUP_HUMAN:
				
				//DELETE api/cardgames/1/setup/human/2
				
				// reinstate get the card game and adds it as context to flowDTO
				flowDTO = builder
						          .addContext(super.reinstate(Integer.parseInt(pathAndQueryData.get("gameId"))))
						          .addEvent(DeleteCasinoForGameAndPlayerEvent.class)
						          .addStateMachine(this.stateMachine)
						          .build();
				
				try {
					stateMachine.check(State.HAS_PLAYERS);
				} catch (Exception e) {
					// make error response
					responseBuilder = CardGameResponse.builder();
					ErrorResponse error = new ErrorResponse(HttpError.CODE_CONFLICT,  "init, shuffle or play", "setup", "state");
					responseBuilder.errorCode(error.getCode());
					responseBuilder.reason(CardGameResponse.Reason.FAILURE);
					return responseBuilder.build();
				}
				
				flowDTO.processPathAndQueryParamsAndTrigger(pathAndQueryData, trigger);
				flowDTO.start();
				// state back to IS_CONFIGURED
				break;
			
			case DELETE_SETUP_AI:
				
				//DELETE api/cardgames/1/setup/ai/3
				
				
				// reinstate get the card game and adds it as context to flowDTO
				flowDTO = builder
						          .addContext(super.reinstate(Integer.parseInt(pathAndQueryData.get("gameId"))))
						          .addEvent(DeleteCasinoForGameAndPlayerEvent.class)
						          .addStateMachine(this.stateMachine)
						          .build();
				
				List<State> possibleDELETE_SETUP_AIStates = new ArrayList<>();
				possibleDELETE_SETUP_AIStates.add(State.IS_CONFIGURED);
				possibleDELETE_SETUP_AIStates.add(State.HAS_PLAYERS);
				
				try {
					stateMachine.checkAll(possibleDELETE_SETUP_AIStates);
				} catch (Exception e) {
					// make error response
					responseBuilder = CardGameResponse.builder();
					ErrorResponse error = new ErrorResponse(HttpError.CODE_CONFLICT,  "init, shuffle or play", "setup", "state");
					responseBuilder.errorCode(error.getCode());
					responseBuilder.reason(CardGameResponse.Reason.FAILURE);
					return responseBuilder.build();
				}
				
				flowDTO.processPathAndQueryParamsAndTrigger(pathAndQueryData, trigger);
				flowDTO.start();
				// state stays HAS_PLAYERS or stays IS_CONFIGURED
				break;
			
			case DELETE:
				
				// Trigger is OK
				
				//DELETE    api/cardgames/1                           // deletes the hands, decks, players, casinos and finally the game
				//DELETE    api/cardgames/1/players                   // deletes all players as resource
				//DELETE    api/cardgames/1/cards                     // gives cards in the deck as resources
				//DELETE    api/cardgames/1/players/2                 // gives a player as resource with resourceId
				//DELETE    api/cardgames/1/players/2/cards           // gives a player as resource with resourceId and cards in the hand as extraResource
				
				
				// reinstate get the card game and adds it as context to flowDTO
				flowDTO = builder
						          .addContext(super.reinstate(Integer.parseInt(pathAndQueryData.get("gameId"))))
						          .addEvent(DeleteCardGameEvent.class)
						          .addStateMachine(this.stateMachine)
						          .build();
				flowDTO.processPathAndQueryParamsAndTrigger(pathAndQueryData, trigger);
				flowDTO.start();
				// state stays HAS_PLAYERS or stays IS_CONFIGURED
				
				break;
			
			default:
				
				// Trigger is GET
				
				//GET    api/cardgames/1
				//GET    api/cardgames/1/player                    // gives active casino (resource=player)
				//GET    api/cardgames/1/players                   // gives all casinos (resource=players)
				//GET    api/cardgames/1/cards                     // gives all decks (resources=cards)
				//GET    api/cardgames/1/players/2                 // gives a specific casino (resource=players, resourceId=int)
				//GET    api/cardgames/1/players/2/cards           // gives a all hands for a player (resource=players, resourceId=int, extraResource=cards)
				
				// reinstate get the card game and adds it as context to flowDTO
				flowDTO = builder
						          .addEvent(GetCardGameDetailsEvent.class)
						          .addStateMachine(this.stateMachine)
						          .build();
				flowDTO.processPathAndQueryParamsAndTrigger(pathAndQueryData, trigger);
				flowDTO.start();
				// state does not matter
				
				break;
		}
		
		
		// generic tasks
		message = String.format("CardGameController Trigger without transition (is done in event) is: %s", trigger);
		log.info(message);
		
		if (trigger != Trigger.GET && trigger != Trigger.DELETE) {
			flowDTO.setCurrentGame(this.updateState(flowDTO.getStateMachine().getCurrentState()));
		}
		
		// make response
		responseBuilder = CardGameResponse.builder();
		if (trigger != Trigger.DELETE) {
			message = String.format("CardGameController convertFromGameEntity is: %s", flowDTO.getCurrentGame());
			log.info(message);
			responseBuilder.cardGame(mapUtil.convertFromGameEntity(flowDTO.getCurrentGame()));
		}
		
		// TODO more mappings
		
		// TODO make rules
		final Integer rulesCode = flowDTO.getRulesCode();
		if (rulesCode != null && rulesCode != 0) {
			responseBuilder.errorCode(rulesCode.toString());
			responseBuilder.reason(CardGameResponse.Reason.FAILURE);
		} else {
			responseBuilder.reason(CardGameResponse.Reason.SUCCESS);
		}
		
		return responseBuilder.build();
		

		
	}
}

