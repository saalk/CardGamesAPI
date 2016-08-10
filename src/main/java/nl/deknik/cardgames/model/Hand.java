/**
 *
 */
package nl.deknik.cardgames.model;

import java.util.ArrayList;
import java.util.List;

/**
 * <H1>Hand</H1> A players hand that can hold one or more cards. <p>For Hand to reuses the Card
 * java-code we could implement this in 2 ways: class / interface inheritance or object composition
 * <p><h2> Hand IS-A list of Cards</h2>
 * Codify this via Hand extends Card. This is <u>Class Inheritance</u> via static (compile-time) binding.
 * When Hand implements Card this is called <u>Interface Inheritance</u>.
 * So Hand could extend or implement Card meaning a parent-child relationship having subclasses.
 * Since Hand is NOT a specific type of Cards we could better use a 'HAS-A' relationship.
 * <h2> Hand HAS-A list of Cards</h2>
 * Codify this via Card handCards = new Card(). This is <u>Object Composition</u> via dynamic (run-time)
 * binding. So if only you want to reuse code and there is no IS-A relationship in sight, use
 * composition. When the association is loose composition is better known as aggregation.
 *
 * @author Klaas van der Meulen
 * @version 1.0
 * @since v1 - console game
 */
public class Hand {

    private List<Card> cards;

    public Hand() {
        this.cards = new ArrayList<>();
    }

    /*
     * public Hand(List<Card> cards) { this.cards = cards; }
     */
    public Card getCard(int index) {
        return cards.get(index);

    }

    public List<Card> getCards() {
        return cards;

    }

    public void setCard(Card card) {
        this.cards.add(card);
    }

    public Card getLastCard() {
        int size = this.cards.size();
        // bugfix size for index always -1
        Card lastCard = this.cards.get(size - 1);
        return lastCard;

    }

    public void playCard(Card card) {
        this.cards.remove(card);
    }

    public List<Card> showCards(boolean ordered) {
        // do not actually order the cards in the hand but only show them
        // ordered
        // TODO show cards ordered by a default order
        return cards;
    }

    public int countNumberOfCards() {
        return cards.size();
    }

    @Override
    public String toString() {
        return "" + cards;
    }
}
