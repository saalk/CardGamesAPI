/**
 * 
 */
package nl.deknik.cardgames.model;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.deknik.cardgames.model.CardGameVariant;
import nl.deknik.cardgames.model.Deck;
import nl.deknik.cardgames.controller.GalacticCasino;

/**
 * @author KvdM
 *
 */
public class Game {

	static int startId = 1;

	// id is final, initialization in constructor and no setter
	private int id;
	private String name;
	private CardGameVariant cardGameVariant;
	private int roundsToPlay;
	private Deck deck;
	private List<Player> players;
	private Player begins;
	private int turnsToWin;
	private Player winner;
	private int ante;

	/**
	 */
	public Game(CardGameVariant chosenCardGameVariant) {
		// TODO make constructor simple
		super();
		this.id = startId++;
		this.name = "Table #" + this.id + " - " + chosenCardGameVariant;
		this.cardGameVariant = chosenCardGameVariant;
		if (!(chosenCardGameVariant == null)) {
			switch (chosenCardGameVariant) {
			case HILOW_1ROUND:
				roundsToPlay = 1;
				break;
			case HILOW_3_IN_A_ROW_1SUIT:
				roundsToPlay = 52;
				break;
			case HILOW_5_IN_A_ROW:
				roundsToPlay = 52;
				break;
			default:
				roundsToPlay = 52;
				break;
			}
		}
		this.deck = new Deck(0);
		this.players = new ArrayList<>();
		if (!(chosenCardGameVariant == null)) {
			switch (chosenCardGameVariant) {
			case HILOW_1ROUND:
				turnsToWin = 51;
				break;
			case HILOW_3_IN_A_ROW_1SUIT:
				turnsToWin = 3;
				Set<Suit> remove = EnumSet.of(Suit.CLUBS, Suit.DIAMONDS, Suit.SPADES);
				this.deck.removeSuit(remove);
				break;
			case HILOW_5_IN_A_ROW:
				turnsToWin = 5;
				break;
			default:
				turnsToWin = 52;
				break;
			}
			this.winner = null;
			this.ante = 0;
		}
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public CardGameVariant getCardGameVariant() {
		return cardGameVariant;
	}

	public int decreaseRoundsLeft() {
		this.roundsToPlay--;
		return roundsToPlay;
	}

	public int getRoundsLeft() {
		return roundsToPlay;
	}

	public Deck getDeck() {
		return this.deck;
	}

	public void removeAllPlayers() {
		this.players = new ArrayList<>();
		;
	}

	public Player setPlayer(Player player) {
		this.players.add(player);
		return player;
	}

	public Player getPlayer(int inputPlayer) {
		// use the other getter to get a list and then get the index passed
		return getPlayers().get(inputPlayer);
	}

	public Player getPlayerById(int inputPlayerId) {

		// do a foreach and if there is a match on id return the player
		// TODO throw Exception when no player found by id
		Player found = null;
		for (Player p : players) {
			if (p.getId() == inputPlayerId) {
				found = p;
			}
		}
		return found;
	}

	public void setPlayers(List<Player> inputPlayers) {
		for (Player addPLayer : inputPlayers) {
			this.players.add(addPLayer);
		}
	}

	public List<Player> getPlayers() {
		return players;
	}

	public Player getNextPlayer(Player player) {

		int next = 0;

		if (players.isEmpty()) {
			return null;
		}
		// INITIALLY START WITH FIRST PLAYER
		if (player == null) {
			next = 0;
		} else {
			if ((players.indexOf(player)) == (players.size() - 1)) {
				// ONLY ONE PLAYER OR START WITH FIRST PLAYER AGAIN
				next = 0;
			} else {
				// ADVANCE TO NEXT PLAYER
				next = (players.indexOf(player)) + 1;

/*				Logger logger = LoggerFactory.getLogger(GalacticCasino.class);
				StringBuilder log = new StringBuilder();

				log = new StringBuilder();
				log.append(System.lineSeparator());
				log.append("= Get Next player=========================");
				log.append(System.lineSeparator());
				log.append("  Current Player: " + (players.indexOf(player)));
				log.append(System.lineSeparator());
				log.append("  Next Player: " + next);
				log.append(System.lineSeparator());
				log.append("= End ====================================");
				logger.debug(log.toString());*/
			}
		}
		return players.get(next);
	}

	public int decreaseTurnsToWin() {
		this.turnsToWin--;
		return turnsToWin;
	}

	public int getTurnsToWin() {
		return turnsToWin;
	}

	public void setWinner(Player winner) {
		this.winner = winner;
	}

	public Player getWinner() {
		return winner;
	}

	public void setBegins(Player begins) {
		this.begins = begins;
	}

	public Player getBegins() {
		return begins;
	}

	public void setAnte(int ante) {
		this.ante = ante;
	}

	public int getAnte() {
		return ante;
	}

	@Override
	public String toString() {

		StringBuilder displayGame = new StringBuilder();
		// displayPlayer.append("#"+id+"-");
		displayGame.append("  " + name);
		displayGame.append(System.lineSeparator());
		displayGame.append(deck);
		displayGame.append(System.lineSeparator());
		displayGame.append("  > Players : " + players);
		displayGame.append(System.lineSeparator());
		displayGame.append("  > Winner  : " + winner);
		// displayGame.append(System.lineSeparator());

		return displayGame.toString();

	}

}
