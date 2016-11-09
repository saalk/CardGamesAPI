/**
 *
 */
package nl.knikit.cardgames.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.voodoodyne.jackson.jsog.JSOGGenerator;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.hateoas.core.Relation;

import java.io.Serializable;

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
            @Index(columnList = "FK_PLAYER", name = "FK_PLAYER_INDEX"),
            @Index(columnList = "FK_CASINO", name = "FK_CASINO_INDEX")},
        uniqueConstraints =
            @UniqueConstraint(name="UC_PLAYER_CASINO", columnNames={"FK_PLAYER","FK_CASINO"}))
@Getter
@Setter
@ToString
@Relation(value = "hand", collectionRelation = "hands")
@JsonIdentityInfo(generator=JSOGGenerator.class)
public class Hand implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "ID")
    @JsonProperty("id") private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_PLAYER", referencedColumnName = "ID", foreignKey = @ForeignKey(name = "FK_PLAYER"))
    @JsonProperty("fkPlayer") private  Player fkPlayer;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_CASINO", referencedColumnName = "ID", foreignKey = @ForeignKey(name = "FK_CASINO"))
    @JsonProperty("fkCasino") private  Casino fkCasino;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_CARD", referencedColumnName = "SHORT_NAME", foreignKey = @ForeignKey(name = "FK_CARD"))
    @JsonProperty("fkCard") private Card fkCard;

    @Column(name = "CARD_ORDER")
    @JsonProperty("cardOrder") private int cardOrder;

/*    @OneToMany
    @Column(name = "CARDS")
    @ElementCollection(targetClass=Card.class)
    @JsonProperty("cards") private  List<Card> cards;
    */

    public Hand(){
    }

    public Hand(Player fkPlayer, Casino fkCasino, Card fkCard, int cardOrder){
        this();
        this.fkPlayer = fkPlayer;
        this.fkCasino = fkCasino;
        this.fkCard = fkCard;
        this.cardOrder = cardOrder;
    }

}
