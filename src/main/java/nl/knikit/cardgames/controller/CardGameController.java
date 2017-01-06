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
//TODO import com.github.oxo42.stateless4j.delegates.Action;

import nl.knikit.cardgames.VO.CardGame;
import nl.knikit.cardgames.VO.CardGameFlowDTO;
import nl.knikit.cardgames.commons.controller.AbstractController;
import nl.knikit.cardgames.commons.event.FlowDTOBuilder;
import nl.knikit.cardgames.event.GetCardGameEvent;
import nl.knikit.cardgames.response.CardGameResponse;

import org.springframework.context.ApplicationContext;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import static nl.knikit.cardgames.model.state.CardGameStateMachine.State;
import static nl.knikit.cardgames.model.state.CardGameStateMachine.Trigger;

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

// TODO make this CardGame controller special to a HightLowCardGame, BlackJackCardGame, etc.. ?
@Slf4j
public class CardGameController extends AbstractController<CardGame> {
	
	// 1 - configure the state machine in the AbstractController
	private static StateMachineConfig<State, Trigger> config = new StateMachineConfig<>();
	
	static {
		// @formatter:off
		// start on player page
		config.configure(State.IS_CONFIGURED)
				// continue on players page
				.permitReentry(Trigger.POST_INIT)
				.permitReentry(Trigger.PUT_INIT)
				// continue on game page
				.permit(Trigger.POST_INIT_HUMAN, State.HAS_PLAYERS)
				.permit(Trigger.POST_SETUP_HUMAN, State.HAS_PLAYERS);
		
		config.configure(State.HAS_PLAYERS)
				.permitReentry(Trigger.POST_SETUP_AI)
				.permitReentry(Trigger.DELETE_SETUP_AI)
				.permitReentry(Trigger.PUT_INIT)
				.permitReentry(Trigger.PUT_SETUP_PLAYER)
				// continue on players page
				.permit(Trigger.DELETE_SETUP_HUMAN, State.IS_CONFIGURED)
				// continue on casino page
				.permit(Trigger.POST_SHUFFLE, State.IS_SETUP);
		
		config.configure(State.IS_SETUP)
				.permit(Trigger.PUT_TURN, State.PLAYING);
		
		config.configure(State.PLAYING)
				.permitReentry(Trigger.PUT_TURN)
				// continue on results page
				.permit(Trigger.PLAYER_WINS, State.GAME_WON)
				.permit(Trigger.NO_CARDS_LEFT, State.NO_WINNER)
				.permit(Trigger.ROUNDS_ENDED, State.NO_WINNER);
		
		config.configure(State.GAME_WON)
				.permit(Trigger.PUT_TURN, State.PLAYING); // allows continue!
		
		config.configure(State.NO_WINNER)
				.permitReentry(Trigger.NO_CARDS_LEFT)
				.permitReentry(Trigger.ROUNDS_ENDED)
				.permit(Trigger.PUT_TURN, State.PLAYING); // allows continue!
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
	public CardGameResponse play(Trigger trigger, Map<String, String> pathAndQueryData) {
		CardGameFlowDTO flowDTO = new CardGameFlowDTO();
		switch (trigger) {
			
			case POST_INIT:
				
				//POST   api/cardgames/init?gameType={g},ante={a}
				flowDTO = builder
						          .addEvent(GetCardGameEvent.class)
						          .addStateMachine(this.stateMachine)
						          .build();
				flowDTO.start();
				this.updateState(flowDTO.getStateMachine().getCurrentState());
				break;
			
			case PUT_INIT:
				//PUT    api/cardgames/init/1?gameType={g},ante={a}
				
				// cardGame.fire(Trigger.INIT);
				// state IS_CONFIGURED
				break;
			
			case POST_INIT_HUMAN:
				
				//POST   api/cardgames/init/human/2?gameType={g},ante={a} // no dealing yet
				//POST   api/cardgames/1/setup/human?name/avatar/securedLoan // no dealing yet
				//POST   api/cardgames/1/setup/ai?name/avatar/securedLoan/aiLevel
				//PUT    api/cardgames/1/setup?ante
				//DELETE api/cardgames/1/setup/players/3 // only for ai players, possible no dealing yet
				//PUT    api/cardgames/1/setup/players/2?name/avatar/securedLoan/aiLevel/playingOrder
				
				// state HasPlayers
				
				break;
			case POST_SETUP_HUMAN:
				
				//PUT    api/cardgames/1/deal/ // start dealing a card to the first player
				//PUT    api/cardgames/1/deal/player/2?action=higher/lower // for human player
				//PUT    api/cardgames/1/pass/player/2 // for human player pass on the card or new card ?
				//PUT    api/cardgames/1/turn/player/3 // auto deal or pass for ai player
				
				//GET    api/cardgames/1/cardsindeck
				//GET    api/cardgames/1/players // also gives cardsinhand and indication active player
				//GET    api/cardgames/1/player // gives active player
				//GET    api/cardgames/1/players/2/cardsinhand // only cardsinhand
				
				// state PLAYING
				// state GameWon, NoMoreCards or RoundsEnded
				
				break;
			case DELETE_SETUP_HUMAN:
				break;
			case DELETE_SETUP_AI:
				break;
			case POST_SETUP_AI:
				break;
			case PUT_SETUP_PLAYER:
				break;
			case POST_SHUFFLE:
				break;
			case PUT_TURN:
				
				//PUT    api/cardgames/1/deal/ // start dealing a card to a new player
				//PUT    api/cardgames/1/deal/player/2?action=higher/lower // for human player
				//PUT    api/cardgames/1/pass/player/2 // for human player pass on the card or new card ?
				//PUT    api/cardgames/1/turn/player/3 // auto deal or pass for ai player
				
				//GET    api/cardgames/1/cardsindeck
				//GET    api/cardgames/1/players // also gives cardsinhand and indication active player
				//GET    api/cardgames/1/player // gives active player
				//GET    api/cardgames/1/players/2/cardsinhand // only cardsinhand
				
				// state GameWon, NoMoreCards or RoundsEnded
				
				break;
			
			default:
				
				// GET    api/cardgames/1
				// GET    api/cardgames/1/cards
				// GET    api/cardgames/1/players
				// GET    api/cardgames/1/players/2/cards
				
				break;
		}
		
		this.updateState(flowDTO.getStateMachine().getCurrentState());
		return flowDTO.getResponse();
	}
}

