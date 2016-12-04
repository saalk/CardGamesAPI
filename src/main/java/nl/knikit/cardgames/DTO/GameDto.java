package nl.knikit.cardgames.DTO;

import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.model.GameType;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.text.ParseException;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import static nl.knikit.cardgames.model.state.GalacticCasinoStateMachine.State;

@Getter
@Setter
public class GameDto {
	
	// Game has 14 fields, GameDto has 3 more
	
	// discard lombok setter for this field -> make your own
	@Setter(AccessLevel.NONE)
	private String name; // extra field "Highlow:0005 (Ante:100) [GameSelected]"
	private int gameId;
	private String created;
	private String state;
	private String gameType;
	private int ante;
	@Setter(AccessLevel.NONE)
	private String round; // extra field "Round 3 [1-9]"
	private int minRounds;
	private int currentRound;
	private int maxRounds;
	@Setter(AccessLevel.NONE)
	private String turn; // extra field "Turn 2 (3 to win) [1-9]"
	private int minTurns;
	private int currentTurn;
	private int turnsToWin;
	private int maxTurns;
	private List<DeckDto> decks;
	// "10C  Ten of Clubs"
	// "  *  40 cards left
	// "---  -------------
	// " AS+ Script Joe [ELF]-"
	// " RJ  Script Joe [ELF]"
	private PlayerDto winner;
	
	public GameType getGameTy1peConverted(String gameType) throws ParseException {
		return GameType.fromGameTypeLabel(gameType);
	}
	
	public void setGameType(GameType gameType) {
		// static Eum methods:
		// - valueOf() - returns enum instance taking a String
		// - values()  - returns all enum instances
		// instance Enum method:
		// - name()    - returns name of enum constant
		// -> better use toString() to get the user-friendly name
		this.gameType = (String.valueOf(gameType));
	}
	
	public State getStateConverted(String state) throws ParseException {
		return State.valueOf(state);
	}
	
	public void setState(State state) {
		// static Eum methods:
		// - valueOf() - returns enum instance taking a String
		// - values()  - returns all enum instances
		// instance Enum method:
		// - name()    - returns name of enum constant
		// -> better use toString() to get the user-friendly name
		
		//
		
		this.state = (String.valueOf(state));
	}
	
	public Game getNameConverted(String name) {
		// "Highlow#0005 (Ante:100) [Select_Game]"
		String[] splitName = StringUtils.split(StringUtils.remove(StringUtils.remove(StringUtils.remove(name, "Ante:"), "]"), " ["), "#()");
		
		if (splitName.length != 4 ||
				    splitName[0].isEmpty() || splitName[1].isEmpty() || splitName[2].isEmpty() || splitName[3].isEmpty()) {
			Game newGame = new Game();
			newGame.setGameType(GameType.fromGameTypeLabel(splitName[0]));
			newGame.setGameId(Integer.parseInt(splitName[1]));
			newGame.setAnte(Integer.parseInt(splitName[2]));
			newGame.setState(State.valueOf(splitName[3]));
			return newGame;
		}
		
		Game newGame = new Game();
		newGame.setGameType(GameType.fromGameTypeLabel(this.gameType));
		newGame.setGameId(this.gameId);
		newGame.setAnte(this.ante);
		newGame.setState(State.valueOf(this.state));
		return newGame;
	}
	
	public void setName() {
		
		// "Highlow#0005 (Ante:100) [Select_Game]"
		this.name = WordUtils.capitalizeFully(this.gameType) + "#" +
				            StringUtils.leftPad(String.valueOf(this.gameId), 4, "0") +
				            " (Ante:" + this.ante + ") [" + WordUtils.capitalizeFully(this.state) + "]";
	}
	
	public String setRound() {
		// "Round 3 [1-9]"
		return this.round = "Round " + this.currentRound + " [" + this.minRounds + "-" + this.maxRounds + "]";
	}
	
	public void getRoundConverted(String round) {
		String[] splitName = StringUtils.split(StringUtils.remove(StringUtils.remove(round, "Round "), " "), "[-]");
		if (splitName.length != 3 ||
				    splitName[0].isEmpty() || splitName[1].isEmpty() || splitName[2].isEmpty()) {
			this.currentRound = 1;
			this.minRounds = 1;
			this.maxRounds = 9;
		}
		this.currentRound = Integer.parseInt(splitName[0]);
		this.minRounds = Integer.parseInt(splitName[1]);
		this.maxRounds = Integer.parseInt(splitName[2]);
		
	}
	
	public String setTurn() {
		// "Turn 2 (3 to win) [1-9]"
		return this.turn = "Turn " + this.currentTurn + " (" + this.turnsToWin + " to win) [" + this.minTurns + "-" + this.maxTurns + "]";
	}
	
	public void setTurnConverted(String turn) {
		// "Turn 2 (3 to win) [1-9]"
		String[] splitName = StringUtils.split(StringUtils.remove(StringUtils.remove(StringUtils.remove(StringUtils.remove(round, "to win"), "Turn "), " ["), "]"), "()-");
		if (splitName.length != 4 ||
				    splitName[0].isEmpty() || splitName[1].isEmpty() || splitName[2].isEmpty() || splitName[3].isEmpty()) {
			this.currentTurn = 1;
			this.minTurns = 1;
			this.maxTurns = 9;
			this.turnsToWin = 3;
		}
		this.currentTurn = Integer.parseInt(splitName[0]);
		this.turnsToWin = Integer.parseInt(splitName[1]);
		this.minTurns = Integer.parseInt(splitName[2]);
		this.maxTurns = Integer.parseInt(splitName[3]);
		
	}
	
}
