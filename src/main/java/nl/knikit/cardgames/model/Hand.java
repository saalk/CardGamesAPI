package nl.knikit.cardgames.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.voodoodyne.jackson.jsog.JSOGGenerator;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.hateoas.core.Relation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <H1>Hand</H1> A players Hand that can hold one or more cards. <p>For Hand to reuses the Card
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
@Table(name = "HAND",
        indexes = {
            @Index(columnList = "PLAYER_ID", name = "PLAYER_ID_INDEX"),
            @Index(columnList = "CASINO_ID", name = "CASINO_ID_INDEX")},
        uniqueConstraints =
            @UniqueConstraint(name="UC_PLAYER_CASINO", columnNames={"PLAYER_ID","CASINO_ID"}))
//@Relation(value = "Hand", collectionRelation = "hands")
//@JsonIdentityInfo(generator=JSOGGenerator.class)
// - this annotation adds @Id to prevent chain loop
// - you could also use @JsonManagedReference and @JsonBackReference
@Getter
@Setter
public class Hand implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "HAND_ID")
    ////@JsonProperty("handId")
    private int handId;

    //@JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PLAYER_ID", referencedColumnName = "PLAYER_ID", foreignKey = @ForeignKey(name = "PLAYER_ID"))
    ////@JsonProperty("player")
    private  Player player;

    //@JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CASINO_ID", referencedColumnName = "CASINO_ID", foreignKey = @ForeignKey(name = "CASINO_ID"))
    ////@JsonProperty("casino")
    private  Casino casino;

    //@JsonIgnore
    //@OneToOne(cascade = CascadeType.ALL)
    //@JoinColumn(name = "CARD_ID", referencedColumnName = "CARD_ID", foreignKey = @ForeignKey(name = "CARD_ID"))
    ////@JsonProperty("card")
    private Card card;

    @Column(name = "CARD_ORDER")
    ////@JsonProperty("cardOrder")
    private int cardOrder;

/*    @OneToMany
    @Column(name = "CARDS")
    @ElementCollection(targetClass=Card.class)
    //@JsonProperty("cards") private  List<Card> cards;
    */

    public Hand(){
    }

    public Hand(Player player, Casino casino, Card card, int cardOrder){
        this();
        this.player = player;
        this.casino = casino;
        this.card = card;
        this.cardOrder = cardOrder;
    }
    
    /**
     * Returns a new ArrayList consisting of the last n
     * elements of deck, which are removed from deck.
     * The returned list is sorted using the elements'
     * natural ordering.
     */
    
    public void deal(List<Card> cards, int numHands, int cardsPerHand){
        for (int i=0; i < numHands; i++) {}
            //System.out.println(dealHand(cards, cardsPerHand));
    }
    
    public static <E extends Comparable<E>> ArrayList<E> dealHand(List<E> deck, int n) {
        int deckSize = deck.size();
        List<E> handView = deck.subList(deckSize - n, deckSize);
        ArrayList<E> hand = new ArrayList<E>(handView);
        handView.clear();
        Collections.sort(hand);
        return hand;
    }

}
