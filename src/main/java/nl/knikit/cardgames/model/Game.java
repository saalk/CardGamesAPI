package nl.knikit.cardgames.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.voodoodyne.jackson.jsog.JSOGGenerator;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Proxy;
import org.springframework.hateoas.core.Relation;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static nl.knikit.cardgames.model.state.GalacticCasinoStateMachine.State;

@Getter
@Setter
@ToString
@Entity
@DynamicUpdate
/*@Table(name = "GAME", indexes = {
        @Index(columnList = "WINNER", name = "WINNER_INDEX")})*/
@Table(name = "GAME")
@Relation(value = "game", collectionRelation = "games")
@JsonIdentityInfo(generator = JSOGGenerator.class)
public class Game implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column(name = "GAME_ID")
	@JsonProperty("gameId")
	private int gameId;
	
	@Column(name = "CREATED", length = 25)
	@JsonProperty("created")
	private String created;
	
	@Column(name = "STATE", length = 25, nullable = false)
	@JsonProperty("state")
	private String state;
	
	@Enumerated(EnumType.STRING)
	//@Type(type = "nl.knikit.cardgames.model.enumlabel.LabeledEnumType")
	@Column(name = "CARD_GAME_TYPE", nullable = false)
	@JsonProperty("cardGameType")
	private CardGameType cardGameType;
	
	@Column(name = "MAX_ROUNDS")
	@JsonProperty("maxRounds")
	private int maxRounds;
	
	@Column(name = "CURRENT_ROUND")
	@JsonProperty("currentRound")
	private int currentRound;
	@Column(name = "MAX_TURNS")
	@JsonProperty("maxTurns")
	private int maxTurns;
	
	@Column(name = "MIN_TURNS")
	@JsonProperty("minTurns")
	private int minTurns;
	@Column(name = "CURRENT_TURN")
	@JsonProperty("currentTurn")
	private int currentTurn;
	@Column(name = "TURNS_TO_WIN")
	@JsonProperty("turnsToWin")
	private int turnsToWin;
	@Column(name = "ANTE")
	@JsonProperty("ante")
	private int ante;
	
	@JsonIgnore
	@OneToMany(mappedBy = "gameObj", targetEntity = Deck.class)
	@JsonProperty("decks")
	private List<Deck> decks = new ArrayList<>();
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "PLAYER_ID", referencedColumnName = "PLAYER_ID")
	@JsonProperty("winner")
	private Player winner;
	
	@Column(name = "MIN_ROUNDS")
	@JsonProperty("minRounds")
	private int minRounds;
	
	// make a lookup from all the STATES
	private static final Map<String, State> lookup
			= new HashMap<>();
	
	static {
		for (State state : EnumSet.allOf(State.class))
			lookup.put(state.toString(), state);
	}
		
	public State getStateFromString() {
		return lookup.get(state); // lookup is a Map<String, State>
	}
	
	public int increaseCurrentRound() {
		this.currentRound++;
		return currentRound;
	}
	
	public int increaseCurrentTurn() {
		this.currentTurn--;
		return currentTurn;
	}
	
	public Game() {
		LocalDateTime localDateAndTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HH:mm-ssSSS-nnnnnnnnn");
		String result = localDateAndTime.format(formatter);
		this.created = result.substring(2, 20);
		
	}
	
	public Game(String state, CardGameType cardGameType, List<Deck> decks, Player winner, int ante) {
		this();
		this.state = state.isEmpty() ? "SELECT_GAME" : state;
		this.cardGameType = cardGameType;
		this.decks = decks;
		this.winner = winner;
		this.ante = ante == 0 ? 50 : ante;
	}
	
	public void addShuffledDeckToGame(int jokers) {
		
		List<Card> cards = Card.newDeck(jokers);
		Collections.shuffle(cards);
		
		int i = 1;
		for (Card card : cards) {
			Deck deck = new Deck();
			deck.setCardObj(card);
			deck.setCardOrder(i++);
			deck.setDealtTo(null);
			this.decks.add(deck);
		}
		
	}
}

