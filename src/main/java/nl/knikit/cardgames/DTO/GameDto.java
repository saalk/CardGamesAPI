package nl.knikit.cardgames.DTO;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.voodoodyne.jackson.jsog.JSOGGenerator;

import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.model.GameType;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.springframework.hateoas.core.Relation;

import java.io.Serializable;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import static nl.knikit.cardgames.model.state.CardGameStateMachine.State;

@Getter
@Setter
@Relation(value = "game", collectionRelation = "games")
@JsonIdentityInfo(generator = JSOGGenerator.class)
// - this annotation adds @Id to prevent chain loop
// - you could also use @JsonManagedReference and @JsonBackReference
public class GameDto implements Serializable {
	
	public GameDto() {
	}
	
	// Game has 14 fields, GameDto has 3 more
	
	// discard lombok setter for this field -> make your own
	@Setter(AccessLevel.NONE)
	private String name; // extra field "Highlow:0005 (Ante:100) [GameSelected]"
	private int gameId;
	// private String created; to prevent setting, this is generated
	private State state;
	private GameType gameType;
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
	
	//@JsonBackReference(value="gameDto")
	//@JsonProperty(value = "decks")
	
	@JsonIgnore
	@Setter(AccessLevel.NONE)
	private List<DeckDto> deckDtos;
	
	//@JsonBackReference(value="gameDto")
	//@JsonProperty(value = "decks")
	@JsonIgnore
	@Setter(AccessLevel.NONE)
	private List<CasinoDto> casinoDtos;
	
	// "10C  Ten of Clubs"
	// "  *  40 cards left
	// "---  -------------
	// " AS+ Script Joe [ELF]-"
	// " RJ  Script Joe [ELF]"
	//@JsonManagedReference(value="playerDto")
	@JsonProperty(value = "winner")
	@Setter(AccessLevel.NONE)
	private PlayerDto winner;
	
	@JsonIgnore
	public GameType getGameTypeFromLabel(String gameType) throws Exception {
		GameType converted = GameType.fromLabel(gameType);
		if (converted == null) {
			throw new Exception("GameTypeParseLabelException");
		}
		return converted;
	}
	
	public void setGameType(GameType gameType) {
		// static Eum methods:
		// - valueOf() - returns enum instance taking a String
		// - values()  - returns all enum instances
		// instance Enum method:
		// - name()    - returns name of enum constant
		// -> better use toString() to get the user-friendly name
		this.gameType = gameType;
	}
	
	@JsonIgnore
	public State getStateConverted(String state) throws Exception {
		State converted = State.valueOf(state);
		if (converted == null) {
			throw new Exception("StateParseException");
		}
		return converted;
	}
	
	public void setState(State state) {
		// static Eum methods:
		// - valueOf() - returns enum instance taking a String
		// - values()  - returns all enum instances
		// instance Enum method:
		// - name()    - returns name of enum constant
		// -> better use toString() to get the user-friendly name
		
		//
		
		this.state = state;
	}
	
	@JsonIgnore
	public Game getNameConverted(String name) {
		// "Highlow#0005 (Ante:100) [Is_Setup]"
		String[] splitName = StringUtils.split(StringUtils.remove(StringUtils.remove(StringUtils.remove(name, "Ante:"), "]"), " ["), "#()");
		
		if (splitName.length != 4 ||
				    splitName[0].isEmpty() || splitName[1].isEmpty() || splitName[2].isEmpty() || splitName[3].isEmpty()) {
			Game newGame = new Game();
			newGame.setGameType(GameType.fromLabel(splitName[0]));
			newGame.setGameId(Integer.parseInt(splitName[1]));
			newGame.setAnte(Integer.parseInt(splitName[2]));
			newGame.setState(State.valueOf(splitName[3]));
			return newGame;
		}
		
		Game newGame = new Game();
		newGame.setGameType(this.gameType);
		newGame.setGameId(this.gameId);
		newGame.setAnte(this.ante);
		newGame.setState(this.state);
		return newGame;
	}
	
	public void setName() {
		
		// "Highlow#0005 (Ante:100) [Is_Setup]"
		this.name = WordUtils.capitalizeFully(this.gameType.toString()) + "#" +
				            StringUtils.leftPad(String.valueOf(this.gameId), 4, "0") +
				            " (Ante:" + this.ante + ") [" + WordUtils.capitalizeFully(this.state.toString()) + "]";
	}
	
	public String setRound() {
		// "Round 3 [1-9]"
		return this.round = "Round " + this.currentRound + " [" + this.minRounds + "-" + this.maxRounds + "]";
	}
	
	@JsonIgnore
	public void setRoundConverted(String round) {
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
	
	@JsonIgnore
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
	
	public void setWinner(PlayerDto playerDto) {
		this.winner = playerDto;
	}
	
	public PlayerDto getWinner() {
		return this.winner;
	}
	
	public void setDeckDtos(List<DeckDto> deckDtos) {
		this.deckDtos = deckDtos;
	}
	
	public List<DeckDto> getDeckDtos() {
		return this.deckDtos;
	}
}
