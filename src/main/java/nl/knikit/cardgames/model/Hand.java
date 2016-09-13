/**
 *
 */
package nl.knikit.cardgames.model;

import org.springframework.hateoas.core.Relation;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

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
@Entity
@Table(name = "HANDS", indexes = {
        @Index(columnList = "FK_PLAYER_ID", name = "FK_PLAYER_ID_INDEX"),
        @Index(columnList = "FK_TABLES_ID", name = "FK_TABLES_ID_INDEX")})
@Getter
@Setter
@Relation(value="hand", collectionRelation="hands")
public class Hand {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    @Column(name = "HAND_ID")
    private long id;

    @OneToOne
    @JoinColumn(name = "FK_PLAYER_ID", referencedColumnName = "PLAYER_ID")
    Player player;

    @OneToOne
    @JoinColumn(name = "FK_TABLES_ID", referencedColumnName = "TABLE_ID")
    Tables tables;

    @Embedded
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
