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

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <H2>Description</H2> A playing card used for playing card games. A complete set of cards is called a
 * pack (UK English), deck (US English), or set (Universal), and the subset of cards held at one
 * time by a {@link PlayerOld player} during a {@link GameOld game} is commonly called a {@link HandOld
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

@Getter
@Setter
@ToString
@Embeddable
public class Card {

    // 13 progressing ranks 2 to 10, jack, queen, king, ace.
    @Embedded
    private Rank rank;

    @Embedded
    private Suit suit;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public int compareTwoCards(Card o1, Card o2, CardGame cardGame) {
        if (o1.getRank().getValue(cardGame) < o2.getRank().getValue(cardGame)) {
            return 1;
        } else {
            if (o1.getRank().getValue(cardGame) > o2.getRank().getValue(cardGame)) {
                return -1;
            } else {
                return 0;
            }
        }
    }

}
