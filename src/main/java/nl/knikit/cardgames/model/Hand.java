/**
 *
 */
package nl.knikit.cardgames.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.hateoas.core.Relation;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

/**
 * <H1>Hand</H1> A players hand that can hold one or more cards. <p>For Hand to reuses the Card
 * java-code we could implement this in 2 ways: class / interface inheritance or object composition
 * <p><h2> Hand IS-A list of Cards</h2>
 * Codify this via Hand extends Card. This is <u>Class Inheritance</u> via static (compile-time) binding.
 * When Hand implements Card this is called <u>Interface Inheritance</u>.
 * So Hand could extend or implement Card meaning a player-child relationship having subclasses.
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
@DynamicUpdate
@Table( name = "HAND",
        indexes = {
            @Index(columnList = "FK_PLAYER", name = "FK_PLAYER_INDEX"),
            @Index(columnList = "FK_CASINO", name = "FK_CASINO_INDEX")},
        uniqueConstraints =
            @UniqueConstraint(name="UC_PLAYER_CASINO", columnNames={"FK_PLAYER","FK_CASINO"}))
@Getter
@Setter
@Relation(value = "hand", collectionRelation = "hands")
public class Hand {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    @JsonProperty("id")
    private int id;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_PLAYER", referencedColumnName = "ID", foreignKey = @ForeignKey(name = "FK_PLAYER"))
    @JsonProperty("fkPlayer") private  Player fkPlayer;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "FK_CASINO", referencedColumnName = "ID", foreignKey = @ForeignKey(name = "FK_CASINO"))
    @JsonProperty("fkCasino") private  Casino fkCasino;

    @Column(name = "CARDS")
    @JsonProperty("cards") private  ArrayList<Card> cards;

    public Hand() {
        this.cards = new ArrayList<>();
    }

    public Card getCard(int index) {
        return cards.get(index);
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
        final StringBuilder builder = new StringBuilder();
        builder.append("Game [id=").append(id).append("]");
        return builder.toString();
    }
}
