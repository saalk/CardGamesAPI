
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
	- List<Decks> Decks;   deckId;game;card;cardOrder;dealtTo;
	- Player winner;       alias;AVATAR
	
	*/

public class GameMapper extends PropertyMap<Game, GameDto> {
	@Override
	protected void configure() {
		//map().setName(source.getFirstName());
	}
};