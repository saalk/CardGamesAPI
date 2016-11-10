/**
 *
 */
package nl.knikit.cardgames.model;

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
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
@Table(name = "HAND",
        indexes = {
            @Index(columnList = "PLAYER_ID", name = "PLAYER_ID_INDEX"),
            @Index(columnList = "CASINO_ID", name = "CASINO_ID_INDEX")},
        uniqueConstraints =
            @UniqueConstraint(name="UC_PLAYER_CASINO", columnNames={"PLAYER_ID","CASINO_ID"}))
@ToString
@Relation(value = "hand", collectionRelation = "hands")
@JsonIdentityInfo(generator=JSOGGenerator.class)
@Getter
@Setter
public class Hand implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "HAND_ID")
    @JsonProperty("handId") private int handId;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PLAYER_ID", referencedColumnName = "PLAYER_ID", foreignKey = @ForeignKey(name = "PLAYER_ID"))
    @JsonProperty("playerObj") private  Player playerObj;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CASINO_ID", referencedColumnName = "CASINO_ID", foreignKey = @ForeignKey(name = "CASINO_ID"))
    @JsonProperty("casinoObj") private  Casino casinoObj;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CARD_ID", referencedColumnName = "CARD_ID", foreignKey = @ForeignKey(name = "CARD_ID"))
    @JsonProperty("cardObj") private Card cardObj;

    @Column(name = "CARD_ORDER")
    @JsonProperty("cardOrder") private int cardOrder;

/*    @OneToMany
    @Column(name = "CARDS")
    @ElementCollection(targetClass=Card.class)
    @JsonProperty("cards") private  List<Card> cards;
    */

    public Hand(){
    }

    public Hand(Player playerObj, Casino casinoObj, Card cardObj, int cardOrder){
        this();
        this.playerObj = playerObj;
        this.casinoObj = casinoObj;
        this.cardObj = cardObj;
        this.cardOrder = cardOrder;
    }

}
