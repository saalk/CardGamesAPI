package nl.knikit.cardgames.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.voodoodyne.jackson.jsog.JSOGGenerator;

import org.hibernate.annotations.DynamicUpdate;
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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import static nl.knikit.cardgames.model.state.GalacticCasinoStateMachine.State;
@Entity
@DynamicUpdate
@Table(name = "GAME")
@Getter
@Setter
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
	
	@Enumerated(EnumType.STRING)
	@Column(name = "STATE", length = 25, nullable = false)
	@JsonProperty("state")
	private State state;
	
	@Enumerated(EnumType.STRING)
	//@Type(type = "nl.knikit.cardgames.model.enumlabel.LabeledEnumType")
	@Column(name = "TYPE", length = 50, nullable = false)
	@JsonProperty("type")
	private GameType gameType;
	
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
	
	// Cascade = any change happened on this entity must cascade to the parent/child as well
	// since this is the parent Game: do all when Game is delete on the deck childs
	// meaning do set cascade type to all -> changed to delete not create
	@JsonIgnore
	@OneToMany(cascade = CascadeType.REMOVE ,mappedBy = "game", targetEntity = Deck.class)
	@JsonProperty("decks")
	private List<Deck> decks = new ArrayList<>();
	
	// Cascade = any change happened on this entity must cascade to the parent/child as well
	// since this is the child Game: do nothing when Game is delete on the winner Player
	// meaning do not set cascade options
	@JsonIgnore
	@ManyToOne(optional=true)
	@JoinColumn(name = "PLAYER_ID", referencedColumnName = "PLAYER_ID", nullable=true)
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
	
	@JsonIgnore
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
	
	public void addShuffledDeckToGame(int jokers) {
		
		List<Card> cards = Card.newDeck(jokers);
		Collections.shuffle(cards);
		
		int i = 1;
		for (Card card : cards) {
			Deck deck = new Deck();
			deck.setCard(card);
			deck.setCardOrder(i++);
			deck.setDealtTo(null);
			this.decks.add(deck);
		}
		
	}
	
}

