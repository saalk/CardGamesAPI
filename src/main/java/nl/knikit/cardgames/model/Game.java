package nl.knikit.cardgames.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.annotations.Type;
import org.hibernate.mapping.Collection;
import org.springframework.hateoas.core.Relation;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import static nl.knikit.cardgames.model.state.GalacticCasinoStateMachine.State;

@Entity
/*@Table(name = "GAME", indexes = {
        @Index(columnList = "WINNER", name = "WINNER_INDEX")})*/
@Table(name = "GAME")
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

    @Column(name = "STATE", length = 25, nullable = false)
    @JsonProperty("state") private String state;

    @Enumerated(EnumType.STRING)
    //@Type(type = "nl.knikit.cardgames.model.enumlabel.LabeledEnumType")
    @Column(name = "CARD_GAME_TYPE", nullable = false)
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

    @Column(name = "ANTE")
    @JsonProperty("ante") private int ante;

/*
    OneToMany can be in Parent class Player (one player can be a winner in many games)
    - you can do only the ManyToOne on the child and do not do this OneToMany....
    - but then you cannot navigate to list all games for a specific winner!

    - cascade = CascadeType.ALL -> means delete childs
    - cascade = CascadeType.DETACH -> means a special state; not managed by entitymanger
    - mappedBy = "parent class" -> the owner of the association

    ManyToOne always in Child class Game (Game is the 'many' part of the relationship)
    - optional=false -> means each Game should always a winner immediately (default is true! )

    ManyToMany to create a join table,

    - inside game do, this the owner of the relation (eg. order owns product)
        @ManyToMany(fetch=FetchType.EAGER)
        @JoinTable(name="DECK",
                   joinColumns=
                   @JoinColumn(name="GAME_ID", referencedColumnName="GAME_ID"),
             inverseJoinColumns=
                   @JoinColumn(name="CARD_ID", referencedColumnName="CARD_ID")
        )
        private List<Card> cardList;

    - inside card do, (eg. product is owned by order, refer to order)
           @ManyToMany(mappedBy="cardList",fetch=FetchType.EAGER)
      private List<Game> gameList;

*/


    @OneToMany(mappedBy="game",targetEntity=Deck.class,
            fetch=FetchType.EAGER)
    @JsonProperty("decks") private List<Deck> deck;

    @ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "WINNER", referencedColumnName = "ID", foreignKey = @ForeignKey(name = "FK_PLAYER"), insertable = false, updatable = false)
    @JsonProperty("winner") private  Player winner;


    public State getState() {
        return State.valueOf(state);
    }

    public void setState(String state) {
        this.state = state;
    }

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
        this.ante = 50;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Game [id=").append(id).append("]");
        return builder.toString();
    }

}

