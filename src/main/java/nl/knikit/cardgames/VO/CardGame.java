package nl.knikit.cardgames.VO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import nl.knikit.cardgames.DTO.DeckDto;
import nl.knikit.cardgames.DTO.PlayerDto;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.model.GameType;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.io.Serializable;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import static nl.knikit.cardgames.model.state.CardGameStateMachine.State;

@Getter
@Setter
//@Relation(value = "cardgame", collectionRelation = "cardgames")
//@JsonIdentityInfo(generator = JSOGGenerator.class)
// - this annotation adds @Id to prevent chain loop
// - you could also use @JsonManagedReference and @JsonBackReference
public class CardGame extends Game implements Serializable {
	
	public CardGame() {
	}
	
	// CardGame has 14 fields, CardGameDto has 3 more
	
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
	@JsonProperty(value = "cards")
	@Setter(AccessLevel.NONE)
	private List<DeckDto> deckDtos;
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
	
	@JsonIgnore
	public State getStateConverted(String state) throws Exception {
		State converted = State.valueOf(state);
		if (converted == null) {
			throw new Exception("StateParseException");
		}
		return converted;
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
	
	public String setTurn() {
		// "Turn 2 (3 to win) [1-9]"
		return this.turn = "Turn " + this.currentTurn + " (" + this.turnsToWin + " to win) [" + this.minTurns + "-" + this.maxTurns + "]";
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
