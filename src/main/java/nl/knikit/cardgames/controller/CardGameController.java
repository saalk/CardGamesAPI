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
IS_SHUFFLED
HAS_HUMAN
..
TURN_STARTED
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
[*] -> IS_SHUFFLED
IS_SHUFFLED -down-> OFFER_FICHES: QUIT
IS_SHUFFLED: start : +1000 fiches
IS_SHUFFLED: offer : +500 fiches
note right of IS_SHUFFLED: Games\n HIGHLOW\n- simple double or nothing\n- three in a row
IS_SHUFFLED -down-> HAS_HUMAN: POST_INIT
HAS_HUMAN --> IS_SHUFFLED: PUT_TURN
HAS_HUMAN: select # bots
HAS_HUMAN --> GameEngine: POST_INIT_HUMAN
OFFER_FICHES -up-> IS_SHUFFLED: OFFER_ACCEPTED
OFFER_FICHES --> [*]

state GameEngine {
[*] -> TURN_STARTED
TURN_STARTED --> TURN_STARTED: PLAYER_PASSES
TURN_STARTED --> GAME_WON: TURN_STARTED
TURN_STARTED --> NO_WINNER: NO_CARDS_LEFT \n or ROUNDS_ENDED
GAME_WON --> GAME_WON: SHOW_RESULTS
GAME_WON --> TURN_STARTED: TURN_ENDED
GAME_WON --> NO_WINNER: NO_CARDS_LEFT \n or PLAYERS_WINS
NO_WINNER -up-> IS_SHUFFLED: GAME_FINISHED
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
import nl.knikit.cardgames.event.DetermineTurnResultsEvent;
import nl.knikit.cardgames.event.CreateCasinoForGameAndPlayerEvent;
import nl.knikit.cardgames.event.CreateDeckForGameEvent;
import nl.knikit.cardgames.event.CreateHandForCasinoForGameAndPlayerEvent;
import nl.knikit.cardgames.event.CreatePlayerEvent;
import nl.knikit.cardgames.event.DeleteCardGameEvent;
import nl.knikit.cardgames.event.DeleteCasinoForGameAndPlayerEvent;
import nl.knikit.cardgames.event.GetCardGameDetailsEvent;
import nl.knikit.cardgames.event.SetupFlowDTOForEveryEvent;
import nl.knikit.cardgames.event.UpdateCardGameDetailsEvent;
import nl.knikit.cardgames.event.UpdateCasinoForPlayingOrderEvent;
import nl.knikit.cardgames.event.UpdateCasinoForTurnAndBetEvent;
import nl.knikit.cardgames.event.UpdateDeckForGameAndCasinoEvent;
import nl.knikit.cardgames.event.UpdatePlayerCubitsAndSecuredLoanEvent;
import nl.knikit.cardgames.event.UpdatePlayerForCasinoDetailsEvent;
import nl.knikit.cardgames.mapper.CardGameMapperUtil;
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
		config.configure(State.SELECTED)
				// continue on players page
				.permitReentry(Trigger.POST_INIT)
				.permitReentry(Trigger.POST_AI)
				.permitReentry(Trigger.POST_SETUP_AI)
				.permitReentry(Trigger.PUT_INIT)
				.permitReentry(Trigger.OK)
				// continue on game page
				.permit(Trigger.POST_HUMAN, State.HAS_HUMAN)
				.permit(Trigger.POST_INIT_HUMAN, State.HAS_HUMAN)
				.permit(Trigger.POST_SETUP_HUMAN, State.HAS_HUMAN)
				.permit(Trigger.ERROR, State.ERROR);
		
		config.configure(State.HAS_HUMAN)
				.permitReentry(Trigger.ERROR)
				.permitReentry(Trigger.POST_SETUP_AI)
				.permitReentry(Trigger.DELETE_SETUP_AI)
				.permitReentry(Trigger.PUT_INIT)
				.permitReentry(Trigger.PUT_SETUP_PLAYER)
				// continue on players page
				.permit(Trigger.DELETE_SETUP_HUMAN, State.SELECTED)
				// continue on casino page
				.permit(Trigger.POST_SHUFFLE, State.IS_SHUFFLED)
				.permit(Trigger.ERROR, State.ERROR);
		
		config.configure(State.IS_SHUFFLED)
				.permit(Trigger.PUT_DEAL_TURN, State.TURN_STARTED)
				.permit(Trigger.ERROR, State.ERROR);
		
		config.configure(State.TURN_STARTED)
				.permit(Trigger.PUT_PLAYING_TURN, State.PLAYING)
				.permit(Trigger.PUT_PASS_TURN, State.TURN_ENDED)
				
				
				// continue on results page
				.permit(Trigger.NO_CARDS_LEFT, State.NO_WINNER)
				.permit(Trigger.ROUNDS_ENDED, State.NO_WINNER)
				.permit(Trigger.ERROR, State.ERROR);
		
		config.configure(State.PLAYING)
				.permitReentry(Trigger.PUT_PLAYING_TURN)
				.permit(Trigger.PUT_PASS_TURN, State.TURN_ENDED)
				
				// continue on results page
				.permit(Trigger.PLAYER_WINS, State.GAME_WON)
				.permit(Trigger.NO_CARDS_LEFT, State.NO_WINNER)
				.permit(Trigger.ROUNDS_ENDED, State.NO_WINNER)
				.permit(Trigger.ERROR, State.ERROR);
		
		config.configure(State.TURN_ENDED)
				.permit(Trigger.PUT_DEAL_TURN, State.TURN_STARTED)
				
				// continue on results page
				.permit(Trigger.NO_CARDS_LEFT, State.NO_WINNER)
				.permit(Trigger.ROUNDS_ENDED, State.NO_WINNER)
				.permit(Trigger.ERROR, State.ERROR);
		
		config.configure(State.GAME_WON)
				.permit(Trigger.PUT_DEAL_TURN, State.TURN_STARTED); // allows continue other players!
		
		config.configure(State.NO_WINNER)
				.permitReentry(Trigger.NO_CARDS_LEFT)
				.permitReentry(Trigger.ROUNDS_ENDED)
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
	
	// 3 - Play a the CardGame based on the Trigger and a list of input data
	@Autowired
	private CardGameMapperUtil mapUtil;
	
	public CardGameResponse play(Trigger trigger, Map<String, String> pathAndQueryData) throws Exception {
		
		CardGameFlowDTO flowDTO = new CardGameFlowDTO();
		final CardGameResponse.CardGameResponseBuilder responseBuilder;
		
		
		String message = String.format("CardGameController Trigger to execute is: %s and data %s", trigger, pathAndQueryData);
		log.info(message);
		
		switch (trigger) {
			
			case POST_HUMAN:
			case POST_AI:
				
				//POST   api/cardgames/init?gameType={g},ante={a}
				// init makes a default card game and adds it as context to flowDTO
				flowDTO = builder
						          .addContext(super.init(new Game()))
						          .addStateMachine(this.stateMachine)
						          .addEvent(SetupFlowDTOForEveryEvent.class)
						          .addEvent(CreatePlayerEvent.class)
						          .addEvent(UpdateCardGameDetailsEvent.class)
						          .addEvent(CreateCasinoForGameAndPlayerEvent.class)
						          .build();
				flowDTO.processPathAndQueryParamsAndTrigger(pathAndQueryData, trigger);
				flowDTO.setGameByContext();
				flowDTO.start();
				
				message = String.format("CardGameController gametype are: %s", flowDTO.getSuppliedGameType());
				log.info(message);
				
				// state SELECTED
				break;
			
			
			case POST_INIT:
				
				//POST   api/cardgames/init?gameType={g},ante={a}
				// init makes a default card game and adds it as context to flowDTO
				flowDTO = builder
						          .addContext(super.init(new Game()))
						          .addStateMachine(this.stateMachine)
						          .addEvent(SetupFlowDTOForEveryEvent.class)
						          .addEvent(UpdateCardGameDetailsEvent.class)
						          .build();
				flowDTO.processPathAndQueryParamsAndTrigger(pathAndQueryData, trigger);
				flowDTO.setGameByContext();
				flowDTO.start();
				
				message = String.format("CardGameController gametype are: %s", flowDTO.getSuppliedGameType());
				log.info(message);
				
				// state SELECTED
				break;
			
			case PUT_INIT:
				
				//PUT    api/cardgames/init/1?gameType={g},ante={a}
				flowDTO = builder
						          .addContext(super.reinstate(Integer.parseInt(pathAndQueryData.get("gameId"))))
						          .addStateMachine(this.stateMachine)
						          .addEvent(SetupFlowDTOForEveryEvent.class)
						          .addEvent(UpdateCardGameDetailsEvent.class)
						          .build();
				
				// reinstate get the card game and adds it as context to flowDTO
				List<State> possiblePUT_INITStates = new ArrayList<>();
				possiblePUT_INITStates.add(State.SELECTED);
				possiblePUT_INITStates.add(State.HAS_HUMAN);
				try {
					stateMachine.checkAll(possiblePUT_INITStates);
				} catch (Exception e) {
					// make error response
					responseBuilder = CardGameResponse.builder();
					ErrorResponse error = new ErrorResponse(HttpError.CODE_CONFLICT, "shuffle or turn", "init", "state");
					responseBuilder.errorCode(error.getCode());
					responseBuilder.errorMessage(error.getMessage());
					responseBuilder.solution(error.getSolution());
					responseBuilder.reason(CardGameResponse.Reason.FAILURE);
					return responseBuilder.build();
				}
				
				flowDTO.processPathAndQueryParamsAndTrigger(pathAndQueryData, trigger);
				flowDTO.start();
				// state SELECTED or HAS_HUMAN
				break;
			
			case POST_INIT_HUMAN:
				
				//POST   api/cardgames/init/human/2   ?gameType/ante
				
				// init makes a default card game and adds it as context to flowDTO
				flowDTO = builder
						          .addContext(super.init(new Game()))
						          .addStateMachine(this.stateMachine)
						          .addEvent(SetupFlowDTOForEveryEvent.class)
						          .addEvent(UpdateCardGameDetailsEvent.class)
						          .addEvent(CreateCasinoForGameAndPlayerEvent.class)
						          .build();
				
				try {
					stateMachine.check(State.SELECTED);
				} catch (Exception e) {
					// make error response
					responseBuilder = CardGameResponse.builder();
					ErrorResponse error = new ErrorResponse(HttpError.CODE_CONFLICT, "init", "null", "state");
					responseBuilder.errorCode(error.getCode());
					responseBuilder.errorMessage(error.getMessage());
					responseBuilder.solution(error.getSolution());
					responseBuilder.reason(CardGameResponse.Reason.FAILURE);
					return responseBuilder.build();
				}
				
				flowDTO.processPathAndQueryParamsAndTrigger(pathAndQueryData, trigger);
				flowDTO.setGameByContext();
				flowDTO.start();
				// state HAS_HUMAN
				break;
			
			case POST_SETUP_HUMAN:
			case POST_SETUP_AI:
				
				//POST   api/cardgames/1/setup/human        ?alias/avatar/securedLoan            // no dealing yet
				//POST   api/cardgames/1/setup/ai           ?alias/avatar/securedLoan/aiLevel
				
				
				// reinstate get the card game and adds it as context to flowDTO
				flowDTO = builder
						          .addContext(super.reinstate(Integer.parseInt(pathAndQueryData.get("gameId"))))
						          .addEvent(SetupFlowDTOForEveryEvent.class)
						          .addEvent(CreatePlayerEvent.class)
						          .addEvent(CreateCasinoForGameAndPlayerEvent.class)
						          .addStateMachine(this.stateMachine)
						          .build();
				
				List<State> possiblePOST_SETUPStates = new ArrayList<>();
				possiblePOST_SETUPStates.add(State.SELECTED);
				possiblePOST_SETUPStates.add(State.HAS_HUMAN);
				try {
					stateMachine.checkAll(possiblePOST_SETUPStates);
				} catch (Exception e) {
					// make error response
					responseBuilder = CardGameResponse.builder();
					ErrorResponse error = new ErrorResponse(HttpError.CODE_CONFLICT, "shuffle or turn", "setup", "state");
					responseBuilder.errorCode(error.getCode());
					responseBuilder.errorMessage(error.getMessage());
					responseBuilder.solution(error.getSolution());
					responseBuilder.reason(CardGameResponse.Reason.FAILURE);
					return responseBuilder.build();
				}
				
				flowDTO.processPathAndQueryParamsAndTrigger(pathAndQueryData, trigger);
				flowDTO.start();
				// state HAS_HUMAN
				break;
			
			case PUT_SETUP_PLAYER:
				
				//PUT    api/cardgames/1/setup/players/2?name/avatar/securedLoan/aiLevel/playingOrder
				
				
				// reinstate get the card game and adds it as context to flowDTO
				flowDTO = builder
						          .addContext(super.reinstate(Integer.parseInt(pathAndQueryData.get("gameId"))))
						          .addEvent(UpdateCasinoForPlayingOrderEvent.class)
						          .addEvent(UpdatePlayerForCasinoDetailsEvent.class)
						          .addEvent(UpdatePlayerCubitsAndSecuredLoanEvent.class)
						          .addStateMachine(this.stateMachine)
						          .build();
				
				//stateMachine.check(State.SELECTED); surround with if
				List<State> possiblePUT_SETUP_PLAYERStates = new ArrayList<>();
				possiblePUT_SETUP_PLAYERStates.add(State.SELECTED);
				possiblePUT_SETUP_PLAYERStates.add(State.HAS_HUMAN);
				try {
					stateMachine.checkAll(possiblePUT_SETUP_PLAYERStates);
				} catch (Exception e) {
					// make error response
					responseBuilder = CardGameResponse.builder();
					ErrorResponse error = new ErrorResponse(HttpError.CODE_CONFLICT, "shuffle or turn", "setup", "state");
					responseBuilder.errorCode(error.getCode());
					responseBuilder.errorMessage(error.getMessage());
					responseBuilder.solution(error.getSolution());
					responseBuilder.reason(CardGameResponse.Reason.FAILURE);
					return responseBuilder.build();
				}
				
				flowDTO.processPathAndQueryParamsAndTrigger(pathAndQueryData, trigger);
				flowDTO.start();
				// stays state HAS_HUMAN or stays SELECTED
				break;
			
			case POST_SHUFFLE:
				
				//POST    api/cardgames/1/shuffle?jokers=0
				
				
				// reinstate get the card game and adds it as context to flowDTO
				flowDTO = builder
						          .addContext(super.reinstate(Integer.parseInt(pathAndQueryData.get("gameId"))))
						          .addEvent(SetupFlowDTOForEveryEvent.class)
						          .addEvent(CreateDeckForGameEvent.class)
						          .addEvent(UpdateCardGameDetailsEvent.class) // for the round
						          .addStateMachine(this.stateMachine)
						          .build();
				
				try {
					stateMachine.check(State.HAS_HUMAN);
				} catch (Exception e) {
					// make error response
					responseBuilder = CardGameResponse.builder();
					ErrorResponse error = new ErrorResponse(HttpError.CODE_CONFLICT, "init, setup or turn", "shuffle", "state");
					responseBuilder.errorCode(error.getCode());
					responseBuilder.errorMessage(error.getMessage());
					responseBuilder.solution(error.getSolution());
					responseBuilder.reason(CardGameResponse.Reason.FAILURE);
					return responseBuilder.build();
				}
				
				flowDTO.processPathAndQueryParamsAndTrigger(pathAndQueryData, trigger);
				flowDTO.start();
				// state IS_SHUFFLED
				break;
			
			case PUT_DEAL_TURN:
				
				//PUT    api/cardgames/1/turn/players/2?action=deal/higher/lower/pass // for human player
				//PUT    api/cardgames/1/turn/players/3?action=next  // auto deal or pass for ai player
				
				// reinstate get the card game and adds it as context to flowDTO
				flowDTO = builder
						          .addContext(super.reinstate(Integer.parseInt(pathAndQueryData.get("gameId"))))
						          .addEvent(SetupFlowDTOForEveryEvent.class)
						          .addEvent(UpdateDeckForGameAndCasinoEvent.class) // DEAL
						          .addEvent(CreateHandForCasinoForGameAndPlayerEvent.class) // DEAL
						          .addEvent(DetermineTurnResultsEvent.class) // TURN, ROUND
						          .addEvent(UpdateCardGameDetailsEvent.class) //Set ROUND + 1
						          .addEvent(UpdateCasinoForTurnAndBetEvent.class) // set TURN = 1
						          .addStateMachine(this.stateMachine)
						          .build();
				
				List<State> possiblePUT_TURNStates = new ArrayList<>();
				possiblePUT_TURNStates.add(State.IS_SHUFFLED);
				possiblePUT_TURNStates.add(State.TURN_ENDED);
				possiblePUT_TURNStates.add(State.GAME_WON);
				try {
					stateMachine.checkAll(possiblePUT_TURNStates);
				} catch (Exception e) {
					// make error response
					responseBuilder = CardGameResponse.builder();
					ErrorResponse error = new ErrorResponse(HttpError.CODE_CONFLICT, "init, setup or shuffle", "turn", "state");
					responseBuilder.errorCode(error.getCode());
					responseBuilder.errorMessage(error.getMessage());
					responseBuilder.solution(error.getSolution());
					responseBuilder.reason(CardGameResponse.Reason.FAILURE);
					return responseBuilder.build();
				}
				
				flowDTO.processPathAndQueryParamsAndTrigger(pathAndQueryData, trigger);
				flowDTO.start();
				// state TURN_STARTED or still in IS_SHUFFLED
				// TODO determine event for state GameWon, NoMoreCards or RoundsEnded
				break;
			
			case PUT_PLAYING_TURN:
				
				//PUT    api/cardgames/1/turn/players/2?action=deal/higher/lower // for human player
				//PUT    api/cardgames/1/turn/players/3?action=next  // auto deal or pass for ai player
				
				
				// reinstate get the card game and adds it as context to flowDTO
				// TODO
				flowDTO = builder
						          .addContext(super.reinstate(Integer.parseInt(pathAndQueryData.get("gameId"))))
						          .addEvent(SetupFlowDTOForEveryEvent.class)
						          .addEvent(UpdateDeckForGameAndCasinoEvent.class) // DEAL
						          .addEvent(CreateHandForCasinoForGameAndPlayerEvent.class) // DEAL
						          .addEvent(DetermineTurnResultsEvent.class) // TURN
						          .addEvent(UpdateCasinoForTurnAndBetEvent.class) // set TURN + 1
						          .addStateMachine(this.stateMachine)
						          .build();
				
				List<State> possiblePUT__PLAYING_TURNStates = new ArrayList<>();
				possiblePUT__PLAYING_TURNStates.add(State.TURN_STARTED);
				possiblePUT__PLAYING_TURNStates.add(State.PLAYING);
				try {
					stateMachine.checkAll(possiblePUT__PLAYING_TURNStates);
				} catch (Exception e) {
					// make error response
					responseBuilder = CardGameResponse.builder();
					ErrorResponse error = new ErrorResponse(HttpError.CODE_CONFLICT, "init, setup or shuffle", "turn", "state");
					responseBuilder.errorCode(error.getCode());
					responseBuilder.errorMessage(error.getMessage());
					responseBuilder.solution(error.getSolution());
					responseBuilder.reason(CardGameResponse.Reason.FAILURE);
					return responseBuilder.build();
				}
				
				flowDTO.processPathAndQueryParamsAndTrigger(pathAndQueryData, trigger);
				flowDTO.start();
				// state TURN_STARTED or still in IS_SHUFFLED
				// TODO determine event for state GameWon, NoMoreCards or RoundsEnded
				break;
			
			
			case PUT_PASS_TURN:
				
				//PUT    api/cardgames/1/turn/players/2?action=pass // for human player
				//PUT    api/cardgames/1/turn/players/3?action=next  // auto deal or pass for ai player
				
				// reinstate get the card game and adds it as context to flowDTO
				// TODO
				flowDTO = builder
						          .addContext(super.reinstate(Integer.parseInt(pathAndQueryData.get("gameId"))))
						          .addEvent(SetupFlowDTOForEveryEvent.class)
						          .addEvent(DetermineTurnResultsEvent.class) // BET, ACTIVE CASINO
						          .addEvent(UpdatePlayerCubitsAndSecuredLoanEvent.class) // CUBITS
						          .addEvent(UpdateCardGameDetailsEvent.class) // NEXT ACTIVE CASINO
						          .addEvent(UpdateCasinoForTurnAndBetEvent.class) // BET
								  
						          .addStateMachine(this.stateMachine)
						          .build();
				
				List<State> possiblePUT__PASS_TURNStates = new ArrayList<>();
				possiblePUT__PASS_TURNStates.add(State.PLAYING);
				try {
					stateMachine.checkAll(possiblePUT__PASS_TURNStates);
				} catch (Exception e) {
					// make error response
					responseBuilder = CardGameResponse.builder();
					ErrorResponse error = new ErrorResponse(HttpError.CODE_CONFLICT, "init, setup or shuffle", "turn", "state");
					responseBuilder.errorCode(error.getCode());
					responseBuilder.errorMessage(error.getMessage());
					responseBuilder.solution(error.getSolution());
					responseBuilder.reason(CardGameResponse.Reason.FAILURE);
					return responseBuilder.build();
				}
				
				flowDTO.processPathAndQueryParamsAndTrigger(pathAndQueryData, trigger);
				flowDTO.start();
				// state TURN_STARTED or still in IS_SHUFFLED
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
					stateMachine.check(State.HAS_HUMAN);
				} catch (Exception e) {
					// make error response
					responseBuilder = CardGameResponse.builder();
					ErrorResponse error = new ErrorResponse(HttpError.CODE_CONFLICT, "init, shuffle or play", "setup", "state");
					responseBuilder.errorCode(error.getCode());
					responseBuilder.errorMessage(error.getMessage());
					responseBuilder.solution(error.getSolution());
					responseBuilder.reason(CardGameResponse.Reason.FAILURE);
					return responseBuilder.build();
				}
				
				flowDTO.processPathAndQueryParamsAndTrigger(pathAndQueryData, trigger);
				flowDTO.start();
				// state back to SELECTED
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
				possibleDELETE_SETUP_AIStates.add(State.SELECTED);
				possibleDELETE_SETUP_AIStates.add(State.HAS_HUMAN);
				
				try {
					stateMachine.checkAll(possibleDELETE_SETUP_AIStates);
				} catch (Exception e) {
					// make error response
					responseBuilder = CardGameResponse.builder();
					ErrorResponse error = new ErrorResponse(HttpError.CODE_CONFLICT, "init, shuffle or play", "setup", "state");
					responseBuilder.errorCode(error.getCode());
					responseBuilder.errorMessage(error.getMessage());
					responseBuilder.solution(error.getSolution());
					responseBuilder.reason(CardGameResponse.Reason.FAILURE);
					return responseBuilder.build();
				}
				
				flowDTO.processPathAndQueryParamsAndTrigger(pathAndQueryData, trigger);
				flowDTO.start();
				// state stays HAS_HUMAN or stays SELECTED
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
				try {
					flowDTO.start();
				} catch (Exception e) {
					// make error response
					responseBuilder = CardGameResponse.builder();
					ErrorResponse error = new ErrorResponse(HttpError.NOT_FOUND_CARDGAME, "valid cardgame", "invalid id", "id");
					responseBuilder.errorCode(error.getCode());
					responseBuilder.errorMessage(error.getMessage());
					responseBuilder.solution(error.getSolution());
					responseBuilder.reason(CardGameResponse.Reason.FAILURE);
					return responseBuilder.build();
				}
				
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
				
				try {
					flowDTO.start();
				} catch (Exception e) {
					// make error response
					responseBuilder = CardGameResponse.builder();
					ErrorResponse error = new ErrorResponse(HttpError.NOT_FOUND_CARDGAME, "valid cardgame", "invalid id", "id");
					responseBuilder.errorCode(error.getCode());
					responseBuilder.errorMessage(error.getMessage());
					responseBuilder.solution(error.getSolution());
					responseBuilder.reason(CardGameResponse.Reason.FAILURE);
					return responseBuilder.build();
				}
				
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
			responseBuilder.errorCode(null);
			responseBuilder.errorMessage(null);
			responseBuilder.solution(null);
			responseBuilder.reason(CardGameResponse.Reason.SUCCESS);
			
		}
		
		return responseBuilder.build();
		
		
	}
}

