package nl.knikit.cardgames.model;
/* @startuml src/main/resources/plantuml/Card.png
 * skinparam classAttributeIconSize 0
 * package "nl.deknik.cardgames" {
 * package "model" {
 * class Card {
    -Rank rank
    -Suit suit
    +compareTo()
 * }
 * Card -right-..> Suit
 * Card ..> Rank
 * enum Rank {
 * A
 * K
 * Q
 * J
 * 2 - 10
 * }
 * enum Suit {
 * Clubs
 * Hearts
 * Spades
 * Diamonds
 * }
 * @enduml
 */

import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import static org.apache.commons.lang3.StringUtils.left;
import static org.apache.commons.lang3.StringUtils.right;

/**
 * <H2>Description</H2> A playing card used for playing card gameDtos. A complete set of cards is
 * called a pack (UK English), deck (US English), or set (Universal), and the subset of cards held
 * at one time by a {@link Player player} during a {@link Game game} is commonly called a {@link
 * Hand Hand}. <H2>Relations</H2> Card is associated to {@link Rank enum Rank} and {@link Suit enum
 * Suit}. Because you must have both a Suit and a Rank for a valid Card, they are parameters for the
 * constructor. <p><img src="../../../../../../src/main/resources/plantuml/Card.png" alt="UML">
 *
 * @author Klaas van der Meulen
 * @version 1.0
 * @since v1 - console game
 */

@Entity
@Table(name = "CARD",
		indexes = {
			@Index(name = "CARD_INDEX", columnList = "CARD_ID")},
		uniqueConstraints = {
		@UniqueConstraint(name="UC_RANK_SUIT", columnNames = {"RANK", "SUIT"})
		}		)
@DynamicUpdate
@Getter
@Setter
//@Relation(value = "card", collectionRelation = "cards")
//@JsonIdentityInfo(generator=JSOGGenerator.class)
// - this annotation adds @Id to prevent chain loop
// - you could also use @JsonManagedReference and @JsonBackReference
public class Card implements Serializable {
	
	// 13 progressing ranks 2 to 10, jack, queen, king, ace.
	//@JsonIgnore
	@Id
	@Column(name = "CARD_ID", length = 3)
	//////@JsonProperty("cardId")
	@Setter(AccessLevel.NONE)
	private String cardId;
	
	@Enumerated(EnumType.STRING)
	//@org.hibernate.annotations.Type(type = "nl.knikit.cardgames.model.enumlabel.LabeledEnumType")
	@Column(name = "RANK", nullable = false)
	////@JsonProperty("rank")
	private Rank rank;
	
	@Enumerated(EnumType.STRING)
	//@org.hibernate.annotations.Type(type = "nl.knikit.cardgames.model.enumlabel.LabeledEnumType")
	@Column(name = "SUIT", nullable = false)
	////@JsonProperty("suit")
	private Suit suit;
	
	@Column(name = "VALUE")
	////@JsonProperty("value")
	private int value;
	
	public Card() {
	}
	
	public Card(Rank rank, Suit suit) {
		this();
		if (rank == null || suit == null)
			throw new NullPointerException(rank + ", " + suit);
		this.rank = rank;
		this.suit = suit;
		
		final StringBuilder builder = new StringBuilder();
		this.cardId = builder.append(rank.getLabel()).append(suit.getLabel()).toString();
		
		switch (rank) {
			case JOKER:
				value = 0;
				break;
			case ACE:
				value = 1;
				break;
			case KING:
				value = 13;
				break;
			case QUEEN:
				value = 12;
				break;
			case JACK:
				value = 11;
				break;
			default:
				value = Integer.parseInt(rank.getLabel());
		}
		this.value = value != 0 ? rank.getValue(GameType.HIGHLOW) : 0;
	}
	
	public Card(String cardId) {
		this();
		
		if (cardId.isEmpty() || !(cardId.length() > 1)) {
			throw new NullPointerException(cardId + " empty cardId");
		}
		this.cardId = cardId;
		
		// auto fill the rest for convenience
		String rankPart = cardId.length()==2?left(cardId,1):left(cardId,2);
		this.rank = Rank.fromLabel(rankPart);
		this.suit = Suit.fromLabel(right(cardId, 1));
		
		switch (rank) {
			case JOKER:
				value = 0;
				break;
			case ACE:
				value = 1;
				break;
			case KING:
				value = 13;
				break;
			case QUEEN:
				value = 12;
				break;
			case JACK:
				value = 11;
				break;
			default:
				value = Integer.parseInt(rank.getLabel());
		}
		this.value = value != 0 ? rank.getValue(GameType.HIGHLOW) : 0;
	}
	
	public void setCardId(String cardId){
		// auto fill the rest for convenience
		String rankPart = cardId.length()==2?left(cardId,1):left(cardId,2);
		this.rank = Rank.fromLabel(rankPart);
		this.suit = Suit.fromLabel(right(cardId, 1));
		
		this.cardId = cardId;
		
		switch (rank) {
			case JOKER:
				value = 0;
				break;
			case ACE:
				value = 1;
				break;
			case KING:
				value = 13;
				break;
			case QUEEN:
				value = 12;
				break;
			case JACK:
				value = 11;
				break;
			default:
				value = Integer.parseInt(rank.getLabel());
		}
		this.value = value != 0 ? rank.getValue(GameType.HIGHLOW) : 0;
	}
	// static fields and methods to easily make decks and add jokers
	protected static final Card joker = new Card(Rank.JOKER, Suit.JOKERS);
	
	protected static final List<Card> prototypeDeck = new ArrayList<>();
	
	static {
		for (Suit suit : Suit.values()) {
			if (suit != Suit.JOKERS) {
				for (Rank rank : Rank.values()) {
					if (rank != Rank.JOKER) {
						prototypeDeck.add(new Card(rank, suit));
					}
				}
			}
		}
	}
	
	/*public void setCardId() {
		final StringBuilder builder = new StringBuilder();
		this.cardId = builder.append(rank.getLabel()).append(suit.getLabel()).toString();
	
	}*/
	
	public static List<Card> newDeck(int addJokers) {
		List<Card> newDeck = prototypeDeck;
		for (int i = 0; i < addJokers; i++) {
			newDeck.add(joker);
		}
		return newDeck;
	}
	
	//@JsonIgnore
	public static boolean isValidCard(String input) {
		
		try {
			Card check = new Card (input);
		} catch (NullPointerException e) {
			return false;
		}
		return true;
	}
	
	//@JsonIgnore
	public boolean isJoker() {
		String jokerCard = cardId;
		return jokerCard=="RJ";
	}
	
	/**
	 * @param cardToCompareWith
	 * 		Card to compare with
	 * @param gameType
	 * 		Supply the card game Type; default is HIGHLOW
	 *
	 * @return -1(LOWER), 0(EQUAL), +1(HIGHER)
	 */
	//@JsonIgnore
	public int compareTo(Card cardToCompareWith, GameType gameType) {
		if (gameType == null) {
			gameType = GameType.HIGHLOW;
		}
		
		if (rank.getValue(gameType) < cardToCompareWith.getRank().getValue(gameType)) {
			return 1; // HIGHER
		} else {
			if (rank.getValue(gameType) > cardToCompareWith.getRank().getValue(gameType)) {
				return -1; // LOWER
			} else {
				return 0; // EQUAL
			}
		}
	}
	
/*	@Override
	public String toString() {
		return rank + " of " + suit;
	}
	*/
}
