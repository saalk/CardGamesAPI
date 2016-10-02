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

@Entity
@DynamicUpdate
@Table(name = "GAME", indexes = {
        @Index(columnList = "WINNER", name = "WINNER_INDEX")})
@Getter
@Setter
@Relation(value = "game", collectionRelation = "games")
public class Game  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    @JsonProperty("id") private int id;
    @Column(name = "GAME_ID")
    @JsonProperty("gameId") private String gameId;
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

    @Column(name = "DECK")
    @JsonProperty("deck") private Deck deck;

    @Column(name = "ANTE")
    @JsonProperty("ante") private int ante;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "WINNER", referencedColumnName = "ID", foreignKey = @ForeignKey(name = "FK_PLAYER"))
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
        this.gameId = result.substring(2, 25);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Game [id=").append(id).append("]");
        return builder.toString();
    }

}

