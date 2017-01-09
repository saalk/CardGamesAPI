
package nl.knikit.cardgames.mapper;

import nl.knikit.cardgames.DTO.GameDto;
import nl.knikit.cardgames.model.Game;

import org.modelmapper.PropertyMap;

/* source is entity from DAO layer
	Basic int/String:
	- gameId;
	- ante;
	Enums:
	- STATE;        next Triggers?
	- TYPE;
	Specials:
	- round;        minRounds;currentRound;maxRounds;
	- turn;         minTurns;currentTurn;turnsToWin;maxTurns;
	Objects:
	- List<Decks> Decks;   deckId;game;card;cardOrder;dealtToDto;
	- Player winner;       alias;AVATAR
	
	*/

// PropertyMap Has < source to get, destination to set>
public class GameMapFromEntity extends PropertyMap<Game, GameDto> {
	@Override
	protected void configure() {
		// set the gameDto.winner suppliedPlayerId with a source from game.player fields
		// map().setWinner(source.getPlayer().getSuppliedPlayerId());
	}
};