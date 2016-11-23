package nl.knikit.cardgames.model;
/* @startuml src/main/resources/plantuml/Deck.png
 * skinparam classAttributeIconSize 0
 * package "nl.deknik.cardgames" {
 * package "model" {
 * class Deck {
    -ArrayList<Card> cards
    -int dealedTo[]
    + shuffle()
    + deal()
 * }
 * note top : Constructor creates 52 cards and \noptional n jokers
 * Deck *- "52\n+jokers" Card : composition
 * @enduml
 */

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.voodoodyne.jackson.jsog.JSOGGenerator;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Proxy;
import org.springframework.hateoas.core.Relation;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <H2>Description</H2>A "standard" deck of playing cards consists of 52 {@link Card Cards}; 13 in each of
 * the 4 Enum {@link Suit suits} of the following list <ul> <li>{@link Suit Spades} <li>{@link Suit
 * Hearts} <li>{@link Suit Diamonds} <li>{@link Suit Clubs} </ul> <p>The ranks have symbols, so
 * called face cards: ace, king, queen, and jack or numbers ranging from 2 to 10. Optionally one or
 * more jokers can be added while generating a deck.
 * <H2>Relations</H2>
 * Deck 'HAS-A' relationship with a preset list of Cards. It is codified via <b>Composition</b> because
 * you must have Card(s) in a Deck and dealing does not remove Cards (like in real world) so 'new()' is
 * placed in the constructor or a setter.<p> {@code ArrayList<Card>
 * cards;}<br>{@code Deck() {cards = new ArrayList<>()}; loop to add Card to list of cards;}
 * <p><img src="../../../../../../src/main/resources/plantuml/Deck.png" alt="UML">
 * <p>
 * <H3>NB -&gt; Relationships on instance-level</H3>
 * Relations between instances of Classes have the following UML notations<ol>
 * <li><b>Dependency (often confused as Association)</b> - a uni directional reference between a dependent and independent elements.<br>
 * class A -USES- class B: represented by a dashed arrow starting from the dependent class to its dependency.<br>
 * codified: as a parameter via a method or as a local instance reference with new in a method. You only need to import B in A.<br>
 * example: PlayerOld - Die via the PlayerOld.takeTurn(Die die) method that has die.Roll() reference to the Roll method of die.
 * <li><b>Association</b> - a uni or bi directional reference between two classes.<br>
 * class A -NOUN- class B: represented by a line, arrows indicate navigation, has multiplicity at one (uni) or both (bi) ends.<br>
 * codified: define an instance of the associated class and passed in it's setter or constructor.<br>
 * example: Passenger -books- Flight via the class Passenger;Flight flight;setFlight(Flight bookedFlight){} setter.
 * Also Student -enrolls- Course.
 * <li><b>Aggregation</b> - a one-to-many association with separate lifecycles.<br>
 * class A -HAS-A- class B: represented by a line with a hollow diamond at the containing class and 1..* at the collection side.<br>
 * codified: like association but with a list of instances of the associated class.<br>
 * The referenced Object is created (new) outside referring class and then passed as a parameter via setter or method.<br>
 * example: Professor - Class: class Professor;List&gt;Class&lt; classes;addClass(Class newClass){} method.
 * <li><b>Composition</b> - a strong lifecycle dependent association.<br>
 * class A -OWNS- class B: represented by a line with a solid diamond at the containing class and 1..1/2/n at the collection side.<br>
 * codified: like association but via a private final instance variables reference and constructed (new()) in the class constructor.
 * The encapsulated / composed Object (part) is destroyed when the referring / composite Object (whole) is destroyed.
 * Other classes cannot see or use the composed Object.<br>
 * example: Car - Engine: class Car;Engine engine = new Engine;Car(Engine newEngine){}; constructor.
 * </li></ol>
 * <H3>NB -&gt; Relationships on class-level</H3><ol>
 * <li><b>Generalization (also called Specialization)</b> - indicates that one of the two related classes is specialized form or the other.<br>
 * class A IS-A-(SUB)TYPE-OF class B: represented by a line with a hollow triangle shape on the superclass.<br>
 * codified: on class level with A Extends B. Known as inheritance, the superclass (base class) is usually abstract.<br>
 * example: Professor|Student extends Person {}.
 * <li><b>Realization</b> - indicates a contract between a subsystem or framework and its consumer.<br>
 * class A IS-A-KIND-OF class B: represented by a dotted or dashed line and a hollow triangle shape on the interface class.
 * codified: on class level with interface A {};class B implements A {};
 * example: A implements Clonable, Comparable {}.
 * </li></ol>
 *
 * @author Klaas van der Meulen
 * @version 1.0
 * @since v1 - console game
 */
@Entity
@Table(name = "DECK", indexes = {
        @Index(columnList = "GAME_ID", name = "GAME_ID_INDEX")})
@Relation(value = "deck", collectionRelation = "decks")
@Getter
@Setter
@DynamicUpdate
@JsonIdentityInfo(generator=JSOGGenerator.class)
public class Deck implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "DECK_ID")
    @JsonProperty("deckId") private int deckId;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, optional=true)
    @JoinColumn(name = "GAME_ID", referencedColumnName = "GAME_ID", foreignKey = @ForeignKey(name = "GAME_ID"), nullable=true)
    @JsonProperty("gameObj") private  Game gameObj;

    //@OneToOne(cascade = CascadeType.ALL)
    //@JoinColumn(name = "CARD_ID", referencedColumnName = "CARD_ID", foreignKey = @ForeignKey(name = "CARD_ID"))
    @JsonProperty("cardObj") private Card cardObj;

    @Column(name = "CARD_ORDER")
    @JsonProperty("cardOrder") private int cardOrder;

    @JsonIgnore
    @OneToOne(optional=true)
    @JoinColumn(name = "PLAYER_ID", referencedColumnName = "PLAYER_ID", foreignKey = @ForeignKey(name = "PLAYER_ID"), nullable=true)
    @JsonProperty("dealtTo") private Player dealtTo;

    /**
     * Hibernate, and code in general that creates objects via reflection use Class<T>.newInstance()
     * to create a new instance of your classes. This method requires a public or private
     * no-arg constructor to be able to instantiate the object.
     */
    public Deck() {
    }

    public Deck(Game gameObj, Card cardObj, int cardOrder, Player dealtTo) {
        this();
        this.gameObj = gameObj;
        this.cardObj = cardObj;
        this.cardOrder = cardOrder;
        this.dealtTo = dealtTo;
    }

    /**
     * alternative with bubble sort routine:
     * <pre>
     * {@code
     * Random rnd = new Random();
     *  for (int i = 0; i < deck.size(); i++)
     *   { for (int j = 0; j < deck.size() - 1; j++)
     *     { if (rnd.nextBoolean())
     *       { Card tmp = deck.fromLabel(j);
     *       deck.set(j, deck.fromLabel(j + 1));
     *       deck.set(j + 1, tmp);
     *      }
     *      }
     *    }
     * }
     * </pre>
     */
 /*   public void shuffle() {

        Random randomNumber = new Random();
        List<Card> shuffledCards = new ArrayList<Card>();

        for (Card card : cards) {
            if (shuffledCards.size() == 0) {
                // add first
                // reset top card flag
                shuffledCards.add(0, card);
            } else {
                // add this card at random position between 0 and the current
                // shuffled cards size
                int randomPosition = randomNumber.nextInt(shuffledCards.size());

                shuffledCards.add(randomPosition, card);
            }
        }

        cards = shuffledCards;
        *//*
         * init the dealedTo with zero's
		 *//*
*//*        for (int i = 0; i < dealedTo.length; i++) {
            dealedTo[i] = 0;
        }*//*
    }

*//*    public int searchNextCardNotInHand() {
        int topCard;
        for (int i = 0; i < dealedTo.length; i++) {
            if (dealedTo[i] == 0) {
                topCard = i;
                return topCard;
            }
        }
        return -1;
    }*//*

*//*    public Card deal(int Hand) throws IllegalArgumentException {
        if (Hand == 0) {
            throw new IllegalArgumentException("Hand for dealing cards is zero!");
        }
        int topCard = this.searchNextCardNotInHand();
        // register Hand before returning topCard
        if (topCard == -1) {
            return null;
        } else {
            dealedTo[topCard] = Hand;
        }
        return cards.fromLabel(topCard);
    }*//*

    public int searchCard(Card searchCard) {
        // TODO why does this not work ?
        // int position = cards.indexOf(searchCard);
        // return position;
        // this is a useless method since fromLabel index does the job
        int position = 1;
        for (Card card : cards) {
            if (card.getRank().equals(searchCard.getRank()) && card.getSuit().equals(searchCard.getSuit())) {
                return position;
            }
            ++position;
        }
        return 0;
    }

    public int countCard(Card countCard) {
        int total = 0;
        for (Card card : cards) {
            if (card.getRank().equals(countCard.getRank()) && card.getSuit().equals(countCard.getSuit())) {
                total++;
            }
        }
        return total;
    }

    *//*public int averageValueInDeck(CardGameType inputCardGameType) {
        int value = 0;
        int count = 0;
        // TODO check for empty list of cards in deck
        for (Card card : cards) {
            if (this.getDealedTo(count) == 0) {
                // not yet dealed
                value = value + card.getRank().getValue(inputCardGameType);
                count++;
            }
        }
        return value / count;
    }*//*

    public void removeSuit(Set<Suit> removeSuits) {

        List<Card> newDeck = this.cards;
        int index = cards.lastIndexOf(cards);

        for (Suit removeSuit : removeSuits) {
            for (Card card : cards) {
                if (card.getSuit() == removeSuit) {
                    newDeck.remove(card);
                }
            }
*//*            index = index - 13;
            dealedTo = new int[index];*//*
        }
        this.cards = newDeck;

    }
*/
}
