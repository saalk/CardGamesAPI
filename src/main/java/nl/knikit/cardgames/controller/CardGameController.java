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
GAME_ADDED
CARD_DEALT
HUMAN_ADDED
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
IS_SETUP -down-> HAS_PLAYERS: GAME_ADDED
HAS_PLAYERS --> IS_SETUP: CARD_DEALT
HAS_PLAYERS: select # bots
HAS_PLAYERS --> GameEngine: HUMAN_ADDED
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

import nl.knikit.cardgames.DTO.CardGame;

import static nl.knikit.cardgames.model.state.CardGameStateMachine.State;
import static nl.knikit.cardgames.model.state.CardGameStateMachine.State.IS_SETUP;
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
 *
 * @author Klaas van der Meulen
 * @version 1.0
 * @since v1 - console game
 **/

public class CardGameController {
	
	private static StateMachineConfig<State, Trigger> cardGameConfig = new StateMachineConfig<>();
	
	static {
		
		// @formatter:off
		
		// start on player page
		cardGameConfig.configure(State.IS_CONFIGURED)
				.permitReentry(Trigger.GAME_ADDED)
				.permitReentry(Trigger.GAME_CHANGED)
				// continue on game page
				.permit(Trigger.HUMAN_ADDED, State.HAS_PLAYERS);
		
		cardGameConfig.configure(State.HAS_PLAYERS)
				.permitReentry(Trigger.PLAYERS_CHANGED)
				.permitReentry(Trigger.GAME_CHANGED)
				.permit(Trigger.HUMAN_DELETED, State.IS_CONFIGURED)
				// continue on casino page
				.permit(Trigger.DECK_SHUFFLED, State.IS_SETUP);
		
		cardGameConfig.configure(IS_SETUP)
				.permit(Trigger.CARD_DEALT, State.PLAYING);
		
		cardGameConfig.configure(State.PLAYING)
				.permitReentry(Trigger.CARD_DEALT)
				.permitReentry(Trigger.TURN_PASSED)
				.permitReentry(Trigger.AI_TURNED)
				.permit(Trigger.PLAYER_WINS, State.GAME_WON)
				.permit(Trigger.NO_CARDS_LEFT, State.EMPTY_DECK)
				.permit(Trigger.ROUNDS_ENDED, State.NO_WINNER);
		
		// continue on results page
		cardGameConfig.configure(State.GAME_WON)
				.permitReentry(Trigger.GET_PRIZE)
				.permitReentry(Trigger.SHOW_RESULTS)
				.permit(Trigger.CARD_DEALT, State.PLAYING) // allow continue with deck?
				.permit(Trigger.NO_CARDS_LEFT, State.EMPTY_DECK);
		
		cardGameConfig.configure(State.NO_WINNER)
				.permit(Trigger.CARD_DEALT, State.PLAYING) // allow continue with deck?
				.permit(Trigger.NO_CARDS_LEFT, State.EMPTY_DECK)
				.permitReentry(Trigger.SHOW_RESULTS);
		
		cardGameConfig.configure(State.EMPTY_DECK)
				.permitReentry(Trigger.SHOW_RESULTS);
		
		// @formatter:on
	}
	
	// CONTROLLER
	public void play(CardGame cardGame) {
		
		switch (cardGame.getState()) {
			case "Null":
				//POST   api/cardgames/select?gameType={g},ante={a}
				//PUT    api/cardgames/select/1?gameType={g},ante={a}
				//GET    api/cardgames/1
				
				// cardGame.fire(Trigger.HUMAN_ADDED);
				// state IS_SETUP
				break;
			case "IS_SETUP":
				//POST   api/cardgames/setup/human/2?gameType={g},ante={a} // no dealing yet
				//POST   api/cardgames/setup/1/human?name/avatar/securedLoan // no dealing yet
				//POST   api/cardgames/setup/1/ai?name/avatar/securedLoan/aiLevel
				//PUT    api/cardgames/setup/1?ante
				//DELETE api/cardgames/setup/1/players/3 // only for ai players, possible no dealing yet
				//PUT    api/cardgames/setup/1/players/2?name/avatar/securedLoan/aiLevel/playingOrder
				//GET    api/cardgames/1/players
				
				// state HasPlayers
				break;
			case "HasPlayers":
				
				//PUT    api/cardgames/deal/1/ // start dealing a card to the first player
				//PUT    api/cardgames/deal/1/player/2?action=higher/lower // for human player
				//PUT    api/cardgames/pass/1/player/2 // for human player pass on the card or new card ?
				//PUT    api/cardgames/turn/1/player/3 // auto deal or pass for ai player
				//GET    api/cardgames/1/cardsindeck
				//GET    api/cardgames/1/players // also gives cardsinhand and indication active player
				//GET    api/cardgames/1/player // gives active player
				//GET    api/cardgames/1/players/2/cardsinhand // only cardsinhand
				
				// state PLAYING
				// state GameWon, NoMoreCards or RoundsEnded
				break;
			case "GameWon": //GameWon, NoMoreCards or RoundsEnded
				// GET    api/cardgames/results/1/cardsindeck
				// GET    api/cardgames/results/1/players
				// GET    api/cardgames/results/1/players/2/cardsinhand
				// GET    api/cardgames/results/1
				
				// state IS_SETUP
				break;
			default:
				//
				break;
		}
	}
}

