package nl.knikit.cardgames.VO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import nl.knikit.cardgames.DTO.CasinoDto;
import nl.knikit.cardgames.DTO.DeckDto;
import nl.knikit.cardgames.DTO.GameDto;
import nl.knikit.cardgames.DTO.PlayerDto;
import nl.knikit.cardgames.model.GameType;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
public class CardGame extends GameDto implements Serializable {
	
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
	private String dealt; // extra field
	@Setter(AccessLevel.NONE)
	private String stock; // extra field
	@Setter(AccessLevel.NONE)
	private String round; // extra field "Round 3 [1-9]"
	@JsonIgnore
	private int minRounds;
	@JsonIgnore
	private int currentRound;
	@JsonIgnore
	private int maxRounds;
	@Setter(AccessLevel.NONE)
	private String turn; // extra field "Turn 2 (3 to win) [1-9]"
	@JsonIgnore
	private int minTurns;
	@JsonIgnore
	private int currentTurn;
	@JsonIgnore
	private int turnsToWin;
	@JsonIgnore
	private int maxTurns;
	
	@JsonIgnore
	//@JsonBackReference(value="gameDto")
	@JsonProperty(value = "cardsInDeck")
	@Setter(AccessLevel.NONE)
	@Getter(AccessLevel.NONE)
	private List<DeckDto> cards;
	
	@JsonProperty(value = "players")
	@Setter(AccessLevel.NONE)
	@Getter(AccessLevel.NONE)
	private List<CasinoDto> players;
	
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
		return this.round = "Round: " + this.currentRound;
	}
	
	public String setTurn() {
		// "Turn 2 (3 to win) [1-9]"
		return this.turn = "Playing: " + this.currentTurn;
	}
	
	public void setWinner(PlayerDto playerDto) {
		this.winner = playerDto;
	}
	
	public PlayerDto getWinner() {
		return this.winner;
	}
	
	public void setCardsDealt() {
		if (this.cards != null) {
			StringBuilder sb = new StringBuilder(" card(s) [");
			List<DeckDto> decks;
			decks = this.cards;
			// sort on card order
			Collections.sort(decks, Comparator.comparing(DeckDto::getCardOrder).thenComparing(DeckDto::getCardOrder));
			boolean first = true;
			int count = 0;
			
			for (DeckDto deck : decks) {
				
				if (!deck.getCardLocation().equals("STOCK")) {
					count++;
					if (!first) {
						sb.append(" ");
					}
					first = false;
					sb.append(deck.getCardDto().getCardId());
				}
			}
			sb.append("]");
			sb.insert(0,count);
			
			this.dealt = sb.toString();
		} else {
			this.dealt = "0 cards []";
		}
	}
	
	public void setCardsLeft() {
		if (this.cards != null) {
			StringBuilder sb = new StringBuilder(" card(s) [");
			List<DeckDto> decks;
			decks = this.cards;
			// sort on card order
			Collections.sort(decks, Comparator.comparing(DeckDto::getCardOrder).thenComparing(DeckDto::getCardOrder));
			boolean first = true;
			int count = 0;
			for (DeckDto deck : decks) {
				if (deck.getCardLocation().equals("STOCK")) {
					count++;
					if (!first) {
						sb.append(" ");
					}
					first = false;
					sb.append(deck.getCardDto().getCardId());
				}
			}
			sb.append("]");
			sb.insert(0,count);
			this.stock = sb.toString();
		} else {
			this.stock = "0 cards []";
		}
	}
	@JsonIgnore
	public void setCards(List<DeckDto> deckDtos) {
		this.cards = deckDtos;
		this.setCardsDealt();
		this.setCardsLeft();
	}
	
	@JsonIgnore
	public List<DeckDto> getCards() {
		return this.cards;
	}
	
	@JsonIgnore
	public void setPlayers(List<CasinoDto> casinoDtos) {
		List<CasinoDto> newCasinoDtos = new ArrayList<>();
		for (CasinoDto casinoDto    :
				casinoDtos) {
			casinoDto.setBet();
			newCasinoDtos.add(casinoDto);
		}
		this.players = newCasinoDtos;
	}
	
	@JsonIgnore
	public List<CasinoDto> getPlayers() {
		
		return this.players;
	}
	
}
