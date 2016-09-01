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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

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
 * example: Player - Die via the Player.takeTurn(Die die) method that has die.Roll() reference to the Roll method of die.
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
public class Deck {

    private ArrayList<Card> cards;
    private int dealedTo[];

    /**
     * First constructor, always work from the smallest constructor to the largest. When a no-arg
     * constructor calls an arg constructor is uses this(args). The this(args) must be the first
     * line and should initialize some or all of the variables.
     */
    public Deck() {
        this(0);
    }

    /**
     * Second constructor, always work from the smallest constructor to the largest.
     */
    public Deck(int totalJokers) {

        /**
         * Do not do: List<Card> cards = new ArrayList<Card>()
         * <P>
         * since that means another declaration in the constructor When
         * initializing you use a assignment statement '='.
         * <P>
         * Do not do: cards = new ArrayList<Card>();
         * <P>
         * since cards already is initialize as Card.
         * <P>
         * You can combine the declaration at the same time as its
         * initialization. The variable name is known as identifier, do not
         * start with digits and only $ symbol is allowed besides letters and
         * underscores.
         *
         */
        cards = new ArrayList<>();

        //TODO get out of constructor and into setter
        dealedTo = new int[52 + totalJokers]; // auto init with zero
        for (Suit suits : Suit.values()) {
            /*
             * without the values() method use allOf
			 */
            for (Rank ranks : EnumSet.allOf(Rank.class)) {

                if (!(ranks.equals(Rank.JOKER) || suits.equals(Suit.JOKERS))) {
                    cards.add(new Card(ranks, suits));
                }
            }
        }

        for (int i = 0; i < totalJokers; i++) {
            cards.add(new Card(Rank.JOKER, Suit.JOKERS));
        }
    }

    // generated getter, setter is the constructor
    public List<Card> getCards() {
        return cards;
    }

    public int getDealedTo(int position) {
        int dealedToHand = dealedTo[position];
        return dealedToHand;
    }

    public int[] getDealedTo() {
        return dealedTo;
    }

    /**
     * alternative with bubble sort routine:
     * <pre>
     * {@code
     * Random rnd = new Random();
     *  for (int i = 0; i < deck.size(); i++)
     *   { for (int j = 0; j < deck.size() - 1; j++)
     *     { if (rnd.nextBoolean())
     *       { Card tmp = deck.get(j);
     *       deck.set(j, deck.get(j + 1));
     *       deck.set(j + 1, tmp);
     *      }
     *      }
     *    }
     * }
     * </pre>
     */
    public void shuffle() {

        Random randomNumber = new Random();
        ArrayList<Card> shuffledCards = new ArrayList<Card>();

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
        /*
         * init the dealedTo with zero's
		 */
        for (int i = 0; i < dealedTo.length; i++) {
            dealedTo[i] = 0;
        }
    }

    public int searchNextCardNotInHand() {
        int topCard;
        for (int i = 0; i < dealedTo.length; i++) {
            if (dealedTo[i] == 0) {
                topCard = i;
                return topCard;
            }
        }
        return -1;
    }

    public Card deal(int hand) throws IllegalArgumentException {
        if (hand == 0) {
            throw new IllegalArgumentException("Hand for dealing cards is zero!");
        }
        int topCard = this.searchNextCardNotInHand();
        // register hand before returning topCard
        if (topCard == -1) {
            return null;
        } else {
            dealedTo[topCard] = hand;
        }
        return cards.get(topCard);
    }

    public int searchCard(Card searchCard) {
        // TODO why does this not work ?
        // int position = cards.indexOf(searchCard);
        // return position;
        // this is a useless method since get index does the job
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

    public int averageValueInDeck(CardGame inputCardGame) {
        int value = 0;
        int count = 0;
        // TODO check for empty list of cards in deck
        for (Card card : cards) {
            if (this.getDealedTo(count) == 0) {
                // not yet dealed
                value = value + card.getRank().getValue(inputCardGame);
                count++;
            }
        }
        return value / count;
    }

    public void removeSuit(Set<Suit> removeSuits) {

        ArrayList<Card> newDeck = cards;
        int index = cards.lastIndexOf(cards);

        for (Suit removeSuit : removeSuits) {
            for (Card card : cards) {
                if (card.getSuit() == removeSuit) {
                    newDeck.remove(card);
                }
            }
            index = index - 13;
            dealedTo = new int[index];
        }
        this.cards = newDeck;

    }

    @Override
    public String toString() {

        StringBuilder displayGame = new StringBuilder();
        // displayPlayer.append("#"+id+"-");

        displayGame.append("  > Deck    : " + cards);
        displayGame.append(System.lineSeparator());
        displayGame.append("  >         : " + Arrays.toString(this.dealedTo));

        return displayGame.toString();

    }
}
