package nl.deknik.cardgames.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Ignore;
import org.junit.runners.MethodSorters;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PlayerTest {

	// make instance variables
	private Player testHumanPlayer1, testAiPlayer2, testAiPlayer3;

	@Before
	public void setUpBeforeEachTest() throws IOException, URISyntaxException {

		// cannot make instance variables here ?

		// initialize the variables
		testHumanPlayer1 = new Player(Origin.ELF, AiLevel.MEDIUM ,true);
		testAiPlayer2 = new Player(Origin.ELF, AiLevel.MEDIUM, false);
		testAiPlayer3 = new Player(Origin.ELF, AiLevel.MEDIUM, false);

	}

	@Ignore
	@Test
	public void test1_Make3PlayersIdsShouldBe123() {

		// run this test first since the Player id starts with 1
		
		// verify
		assertEquals("Player ids not 1", 1, testHumanPlayer1.getId());
		assertEquals("Player ids not 2", 2, testAiPlayer2.getId());
		assertEquals("Player ids not 3", 3, testAiPlayer3.getId());
	}
	@Ignore
	@Test
	public void testMake3PlayersAllShouldHaveNames() throws IOException {

		// verify
		assertNotNull("Player name is null", testHumanPlayer1.getAlias());
		assertNotNull("Player name is null", testAiPlayer2.getAlias());
		assertNotNull("Player name is null", testAiPlayer3.getAlias());
	}
	@Ignore
	@Test
	public void testMake3PlayersOneIsHuman() {

		// verify
		assertTrue("Number of Humans players not correct",
				testHumanPlayer1.isHuman() && !testAiPlayer2.isHuman() && !testAiPlayer3.isHuman());
	}

}
