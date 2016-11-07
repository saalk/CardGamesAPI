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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.annotations.Type;
import org.springframework.hateoas.core.Relation;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * <H2>Description</H2> A playing card used for playing card games. A complete set of cards is called a
 * pack (UK English), deck (US English), or set (Universal), and the subset of cards held at one
 * time by a {@link Player player} during a {@link Game game} is commonly called a {@link Hand
 * hand}.
 * <H2>Relations</H2>
 * Card is associated to {@link Rank enum Rank} and {@link Suit enum Suit}. Because you must have
 * both a Suit and a Rank for a valid Card, they are parameters for the constructor.
 * <p><img src="../../../../../../src/main/resources/plantuml/Card.png" alt="UML">
 *
 * @author Klaas van der Meulen
 * @version 1.0
 * @since v1 - console game
 */

@Entity
@Table(name = "CARD")
@Getter
@Setter
@Relation(value = "card", collectionRelation = "cards")
public class Card implements Serializable {

    // 13 progressing ranks 2 to 10, jack, queen, king, ace.
    @Id
    @Column(name = "SHORT_NAME", length = 3)
    @JsonProperty("shortName") private String shortName;


    @Enumerated(EnumType.STRING)
    //@Type(type = "nl.knikit.cardgames.model.enumlabel.LabeledEnumType")
    @Column(name = "RANK")
    @JsonProperty("rank") private Rank rank;

    @Enumerated(EnumType.STRING)
    //@Type(type = "nl.knikit.cardgames.model.enumlabel.LabeledEnumType")
    @Column(name = "SUIT")
    @JsonProperty("suit") private Suit suit;

    @Column(name = "VALUE")
    @JsonProperty("value") private int value;

    public Card(){
        this.shortName = "RJ";
        this.rank = Rank.JOKER;
        this.suit = Suit.JOKERS;
        this.value = 0;
    }

    //  @JsonCreator annotation is used for constructors or static factory methods to construct
    //  instances from Json
    @JsonCreator
    public Card(@JsonProperty("rank") String rank, @JsonProperty("suit") String suit) {
        this.rank = Rank.fromRankName(rank);
        this.suit = Suit.fromSuitName(suit);
        final StringBuilder builder = new StringBuilder();
        this.shortName = builder.append(rank).append(suit).toString();
        switch (Rank.fromRankName(rank)) {
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
                value = Integer.parseInt(rank);
        }
        this.value = Rank.fromRankName(rank).getValue(CardGameType.HIGHLOW);
    }

    public int compareTwoCards(Card o1, Card o2, CardGameType cardGameType) {
        if (o1.getRank().getValue(cardGameType) < o2.getRank().getValue(cardGameType)) {
            return 1;
        } else {
            if (o1.getRank().getValue(cardGameType) > o2.getRank().getValue(cardGameType)) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Card [value=").append(rank.getLabel()).append(suit.getLabel()).append("]");
        return builder.toString();
    }

}
