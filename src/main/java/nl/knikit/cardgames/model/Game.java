package nl.knikit.cardgames.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.hateoas.core.Relation;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Entity
@DynamicUpdate
@Table(name = "GAME", indexes = {
        @Index(columnList = "WINNER", name = "WINNER_INDEX")})
@Getter
@Setter
@Relation(value = "game", collectionRelation = "games")
public class Game  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "ID")
    @JsonProperty("id") private int id;
    @Column(name = "CREATED", length = 25)
    @JsonProperty("created") private String created;
    @Enumerated(EnumType.STRING)
    @Column(name = "CARD_GAME_TYPE")
    @JsonProperty("cardGameType") private CardGameType cardGameType;

    @Column(name = "MAX_ROUNDS")
    @JsonProperty("maxRounds") private int maxRounds;
    @Column(name = "MIN_ROUNDS")
    @JsonProperty("minRounds") private int minRounds;
    @Column(name = "CURRENT_ROUND")
    @JsonProperty("currentRound") private int currentRound;

    @Column(name = "MAX_TURNS")
    @JsonProperty("maxTurns") private int maxTurns;
    @Column(name = "MIN_TURNS")
    @JsonProperty("minTurns") private int minTurns;
    @Column(name = "CURRENT_TURN")
    @JsonProperty("currentTurn") private int currentTurn;
    @Column(name = "TURNS_TO_WIN")
    @JsonProperty("turnsToWin") private int turnsToWin;

    @Embedded
    @JsonProperty("deck")
    private Deck deck;

/*

    - merge         (copy using same id)
    - persist       (persist the transient instance)
    - save          (persists the transient instance with new id)
    - saveOrUpdate  (either save or update depending on unsaved-value check)
    - update        (updates the persistent instance with the id)

    1 - Create parent:      game = new Game("HIGHLOW")
    2 - Persist it:         persist(game)
    3 - Create child class  deck = new Deck/Card(jokers)
    4 - Associate child with it's parent
                            card.setGameId(game.getId())
    5 - Persist child       persist(deck/card)

    Retrieval
    1 - Get childs          card = game.getDeck/Cards()

    if you do 1, 3, game.getDeck.Cards().add(deck), 5
    the deck is not initialized

    if you do 1,2,3,5 and then game.getDeck/Cards().add(Deck) the
    with cascade=CascadeType.Persist takes care of relations

*/


    // OneToMany on Parent - cascade all
    // mappedBy: contains parent class, the owner of the association
    //
    @Column(name = "ANTE")
    @JsonProperty("ante") private int ante;

    // ManyToOne on Child - @JoinColumn for the fk)

    @ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "WINNER", referencedColumnName = "ID", foreignKey = @ForeignKey(name = "FK_PLAYER"), insertable = false, updatable = false)
    @JsonProperty("winner") private  Player winner;


    public int increaseCurrentRound() {
        this.currentRound++;
        return currentRound;
    }

    public int increaseCurrentTurn() {
        this.currentTurn--;
        return currentTurn;
    }

    @JsonCreator
    public Game() {
        super();
        LocalDateTime localDateAndTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HH:mm-ssSSS-nnnnnnnnn");
        String result = localDateAndTime.format(formatter);
        this.created = result.substring(2, 25);
        setDeck(0);
        this.ante = 50;
    }

    public Game(int jokers) {
        super();
        setDeck(jokers);
    }

    public void setDeck(int jokers) {
        this.deck = new Deck(jokers);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Game [id=").append(id).append("]");
        return builder.toString();
    }

}

