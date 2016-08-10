/**
 * 
 */
package nl.deknik.cardgames.model;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * @author KvdM
 *
 */
public class CardTest {

	// make instance variables
	private List<Card> testCards;
	private Card testCard;
	
	@Before
	public void setUpBeforeEachTest() {
		
		// initialize the variables
		testCards = new ArrayList<>();
		testCard = new Card(null, null);
				
	}

	@Test
	public void testAceOfSpadesShouldBeCreated() {
		
		// assign a value
		testCard.setRank(Rank.ACE);
		testCard.setSuit(Suit.SPADES);
				
		// verify
		assertEquals("Ace of Spades cannot be created", Rank.ACE, testCard.getRank());
		assertEquals("Ace of Spades cannot be created", Suit.SPADES, testCard.getSuit());
	}
	
	@Test
	public void testTotalUniqueCardsShouldReturn70() {
		
		// Run
		for (Suit suits: Suit.values()) {
			for (Rank ranks: Rank.values()) {
				
				testCards.add(new Card(ranks, suits));
			}
		}
		
		// Verify
		assertEquals("Total unique Cards must be 70", 70, testCards.size());
	}
	
}
