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

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.voodoodyne.jackson.jsog.JSOGGenerator;

import org.springframework.hateoas.core.Relation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

import static org.apache.commons.lang3.StringUtils.left;
import static org.apache.commons.lang3.StringUtils.right;

/**
 * <H2>Description</H2> A playing card used for playing card games. A complete set of cards is
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

//@Entity
//@Table(name = "CARD")
//@DynamicUpdate
@Getter
@Setter
@Relation(value = "card", collectionRelation = "cards")
@JsonIdentityInfo(generator = JSOGGenerator.class)
public class Card implements Serializable {
	
	// 13 progressing ranks 2 to 10, jack, queen, king, ace.
	//@JsonIgnore
	@Id
	@Column(name = "CARD_ID", length = 3)
	@JsonProperty("cardId")
	private String cardId;
	
	
	//@Enumerated(EnumType.STRING)
	//@Type(type = "nl.knikit.cardgames.model.enumlabel.LabeledEnumType")
	//@Column(name = "RANK")
	@JsonProperty("rank")
	private Rank rank;
	
	//@Enumerated(EnumType.STRING)
	//@Type(type = "nl.knikit.cardgames.model.enumlabel.LabeledEnumType")
	//@Column(name = "SUIT")
	@JsonProperty("suit")
	private Suit suit;
	
	//@Column(name = "VALUE")
	@JsonProperty("value")
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
		this.cardId = builder.append(rank).append(suit).toString();
		
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
		this.value = value != 0 ? rank.getValue(CardGameType.HIGHLOW) : 0;
	}
	
	public Card(String cardId) {
		this();
		if (cardId.isEmpty() || !(cardId.length()==2))
			throw new NullPointerException(cardId + " empty cardId");
		this.rank = Rank.fromLabel(left(cardId,1));
		this.suit = Suit.fromLabel(right(cardId,1));
		
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
		this.value = value != 0 ? rank.getValue(CardGameType.HIGHLOW) : 0;
	}
	
	
	// static fields and methods to easily make decks and add jokers
	private static final Card joker = new Card(Rank.JOKER, Suit.JOKERS);
	
	private static final List<Card> prototypeDeck = new ArrayList<>(52);
	
	static {
		for (Suit suit : Suit.values())
			for (Rank rank : Rank.values())
				if (rank != Rank.JOKER) {
					prototypeDeck.add(new Card(rank, suit));
				}
	}
	
	/**
	 * @param jokers
	 * 		number of jokers added to the deck
	 *
	 * @return a Deck with 52 cards plus the jokers
	 */
	
	public void setCardId() {
		final StringBuilder builder = new StringBuilder();
		this.cardId = builder.append(rank).append(suit).toString();
	}
	
	public static List<Card> newDeck(int jokers) {
		List<Card> newDeck = prototypeDeck;
		for (int i = 0; i < jokers; i++) {
			newDeck.add(joker);
		}
		return newDeck;
	}
	
	public static boolean isValidCard(String card) {
		return (Arrays.asList(Card.newDeck(1)).contains(card));
	}
	
	public static boolean isJoker(String card) {
		return card.contains(Card.joker.getCardId())?true:false;
	}
		
	/**
	 * @param cardToCompareWith
	 * 		Card to compare with
	 * @param cardGameType
	 * 		Supply the card game Type; default is HIGHLOW
	 *
	 * @return -1(LOWER), 0(EQUAL), +1(HIGHER)
	 */
	public int compareTo(Card cardToCompareWith, CardGameType cardGameType) {
		if (cardGameType == null) {
			cardGameType = CardGameType.HIGHLOW;
		}
		
		if (rank.getValue(cardGameType) < cardToCompareWith.getRank().getValue(cardGameType)) {
			return 1; // HIGHER
		} else {
			if (rank.getValue(cardGameType) > cardToCompareWith.getRank().getValue(cardGameType)) {
				return -1; // LOWER
			} else {
				return 0; // EQUAL
			}
		}
	}
	
	@Override
	public String toString() {
		return rank + " of " + suit;
	}
	
}
