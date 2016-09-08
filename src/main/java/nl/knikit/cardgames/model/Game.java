/**
 * 
 */
package nl.knikit.cardgames.model;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

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
	private List<PlayerOld> playerOlds;
	private PlayerOld begins;
	private int turnsToWin;
	private PlayerOld winner;
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
		this.playerOlds = new ArrayList<>();
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
		this.playerOlds = new ArrayList<>();
		;
	}

	public PlayerOld setPlayer(PlayerOld playerOld) {
		this.playerOlds.add(playerOld);
		return playerOld;
	}

	public PlayerOld getPlayer(int inputPlayer) {
		// use the other getter to get a list and then get the index passed
		return getPlayerOlds().get(inputPlayer);
	}

	public PlayerOld getPlayerById(int inputPlayerId) {

		// do a foreach and if there is a match on id return the player
		// TODO throw Exception when no player found by id
		PlayerOld found = null;
		for (PlayerOld p : playerOlds) {
			if (p.getId() == inputPlayerId) {
				found = p;
			}
		}
		return found;
	}

	public void setPlayerOlds(List<PlayerOld> inputPlayerOlds) {
		for (PlayerOld addPLayer : inputPlayerOlds) {
			this.playerOlds.add(addPLayer);
		}
	}

	public List<PlayerOld> getPlayerOlds() {
		return playerOlds;
	}

	public PlayerOld getNextPlayer(PlayerOld playerOld) {

		int next = 0;

		if (playerOlds.isEmpty()) {
			return null;
		}
		// INITIALLY START WITH FIRST PLAYER
		if (playerOld == null) {
			next = 0;
		} else {
			if ((playerOlds.indexOf(playerOld)) == (playerOlds.size() - 1)) {
				// ONLY ONE PLAYER OR START WITH FIRST PLAYER AGAIN
				next = 0;
			} else {
				// ADVANCE TO NEXT PLAYER
				next = (playerOlds.indexOf(playerOld)) + 1;

/*				Logger logger = LoggerFactory.getLogger(GalacticCasino.class);
				StringBuilder log = new StringBuilder();

				log = new StringBuilder();
				log.append(System.lineSeparator());
				log.append("= Get Next playerOld=========================");
				log.append(System.lineSeparator());
				log.append("  Current PlayerOld: " + (playerOlds.indexOf(playerOld)));
				log.append(System.lineSeparator());
				log.append("  Next PlayerOld: " + next);
				log.append(System.lineSeparator());
				log.append("= End ====================================");
				logger.debug(log.toString());*/
			}
		}
		return playerOlds.get(next);
	}

	public int decreaseTurnsToWin() {
		this.turnsToWin--;
		return turnsToWin;
	}

	public int getTurnsToWin() {
		return turnsToWin;
	}

	public void setWinner(PlayerOld winner) {
		this.winner = winner;
	}

	public PlayerOld getWinner() {
		return winner;
	}

	public void setBegins(PlayerOld begins) {
		this.begins = begins;
	}

	public PlayerOld getBegins() {
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
		displayGame.append("  > Players : " + playerOlds);
		displayGame.append(System.lineSeparator());
		displayGame.append("  > Winner  : " + winner);
		// displayGame.append(System.lineSeparator());

		return displayGame.toString();

	}

}
