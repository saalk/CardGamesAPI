package nl.knikit.cardgames.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class HandOldTest {

	// make instance variables
	private HandOld testHandOld1;
	private Card testAceOfSpades, testJoker;

	@Before
	public void setUpBeforeEachTest() {
		// initialize the variables
		testHandOld1 = new HandOld();
		
		testAceOfSpades = new Card(Rank.ACE, Suit.SPADES);
		testJoker = new Card(Rank.JOKER, Suit.JOKERS);		
	}

	@Test
	public void testShowInitialHandShouldBeEmpty() {
	
		// verify
		assertTrue("Hand should be initialized", testHandOld1.showCards(true).isEmpty());
	
	}

	@Test
	public void testAdd3CardToHandShouldShow3Cards() {
	
		// run
		testHandOld1.setCard(testAceOfSpades);
		testHandOld1.setCard(testJoker);
		testHandOld1.setCard(testAceOfSpades);
		
		// verify
		assertTrue("Hand should show 3 cards", testHandOld1.showCards(false).size()==3);
		
	}

	@Test
	public void testPlayAceOfSpadesFromHand() {
		// run
		testHandOld1.setCard(testAceOfSpades);
		testHandOld1.playCard(testAceOfSpades);
		
		//verify 
		assertTrue("Hand should show no cards", testHandOld1.showCards(true).size()==0);
		
	}

}
