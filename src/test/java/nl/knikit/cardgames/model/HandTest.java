package nl.knikit.cardgames.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class HandTest {

	// make instance variables
	private Hand testHand1;
	private Card testAceOfSpades, testJoker;

	@Before
	public void setUpBeforeEachTest() {
		// initialize the variables
		testHand1 = new Hand();
		
		testAceOfSpades = new Card(Rank.ACE, Suit.SPADES);
		testJoker = new Card(Rank.JOKER, Suit.JOKERS);		
	}

	@Test
	public void testShowInitialHandShouldBeEmpty() {
	
		// verify
		assertTrue("Hand should be initialized", testHand1.showCards(true).isEmpty());
	
	}

	@Test
	public void testAdd3CardToHandShouldShow3Cards() {
	
		// run
		testHand1.setCard(testAceOfSpades);
		testHand1.setCard(testJoker);
		testHand1.setCard(testAceOfSpades);
		
		// verify
		assertTrue("Hand should show 3 cards", testHand1.showCards(false).size()==3);
		
	}

	@Test
	public void testPlayAceOfSpadesFromHand() {
		// run
		testHand1.setCard(testAceOfSpades);
		testHand1.playCard(testAceOfSpades);
		
		//verify 
		assertTrue("Hand should show no cards",testHand1.showCards(true).size()==0);
		
	}

}
