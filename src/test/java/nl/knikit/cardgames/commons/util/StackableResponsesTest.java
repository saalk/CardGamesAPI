package nl.knikit.cardgames.commons.util;

import nl.knikit.cardgames.VO.CardGame;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.model.Player;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class StackableResponsesTest {
	
	private StackableResponses fixture = new StackableResponses();
	
	private CardGame cardGame = new CardGame();
	private CardGame cardGame2 = new CardGame();
	private Player player = new Player();
	private Game game = new Game();
	
	@Before
	public void setUp() throws Exception {
		
		cardGame.setGameId(10);
		fixture.push(cardGame);
		
		cardGame2.setGameId(11);
		fixture.push(cardGame2);
		
		player.setPlayerId(20);
		game.setGameId(30);
		
		fixture.push(player);
		fixture.push(game);
	}
	
	@Test
	public void peekLast() throws Exception {
		
		Object object = fixture.peekLast("cardGames");
		assertThat(object instanceof CardGame, is(true));
		
		CardGame cardGame = (CardGame) object;
		assertThat(cardGame.getGameId(), is(11));
		
	}
	@Test
	public void peekAt() throws Exception {
		
		Object object = fixture.peekAt("cardGames", 0);
		assertThat(object instanceof CardGame, is(true));
		
		CardGame cardGame = (CardGame) object;
		assertThat(cardGame.getGameId(), is(10));
		
	}
	
	@Test
	public void peekAndPop() throws Exception {
		
		Object object = fixture.peekAndPop("cardGames");
		assertThat(object instanceof CardGame, is(true));
		
		CardGame cardGame = (CardGame) object;
		assertThat(cardGame.getGameId(), is(11));
		
		
		Object object2 = fixture.peekAndPop("cardGames");
		assertThat(object2 instanceof CardGame, is(true));
		
		CardGame cardGame2 = (CardGame) object2;
		assertThat(cardGame2.getGameId(), is(10));
		
		
	}
	
}