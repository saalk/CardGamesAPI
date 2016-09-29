package nl.knikit.cardgames.model;

import org.springframework.hateoas.core.Relation;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "GAMES" , indexes = {
        @Index(columnList = "WINNER_ID", name = "WINNER_ID_INDEX")})
@Getter
@Setter
@ToString
@Relation(value="game", collectionRelation="games")
public class Game {

	@Id
    @GeneratedValue(strategy= GenerationType.AUTO)
	@Column(name = "GAME_ID")
	private long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "CARD_GAME")
	private CardGame cardGame;
	@Column(name = "ROUNDS")
	private int rounds;
	@Column(name = "TURNS")
	private int turns;
	@Column(name = "MINIMAL_TURNS_BEFORE_PASS")
	private int minimalTurnsBeforePass;
	@Column(name = "MINIMAL_TURNS_TO_WIN")
	private int minimalTurnsToWin;

    @Embedded
    private Deck deck;

    @Column(name = "CURRENT_ROUND")
    private int currentRound;
    @Column(name = "CURRENT_PLAYER")
    private int currentPlayer;
    @Column(name = "CURRENT_TURN")
    private int currentTurn;
    @Column(name = "ANTE")
    private int ante;

    @OneToOne
    @JoinColumn(name = "WINNER_ID", referencedColumnName = "PLAYER_ID")
    Player winner;

    public int decreaseRoundsLeft() {
        this.currentRound--;
        return currentRound;
    }
}