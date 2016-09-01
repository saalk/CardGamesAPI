package nl.knikit.cardgames.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;

public class DeckTest {

	// make instance variables
	private Deck testDeck, testDeckWithAJoker, testDeckWith3Jokers;
	private Card testAceOfSpades, testJoker;
	
	@Before
	public void setUpBeforeEachTest() {
		
		// cannot make instance variables here ?
		
		// initialize the variables
		testDeck = new Deck(0);
		testDeckWithAJoker = new Deck(1);
		testDeckWith3Jokers = new Deck(3);
		
		testAceOfSpades = new Card(Rank.ACE, Suit.SPADES);
		testJoker = new Card(Rank.JOKER, Suit.JOKERS);
	}
	
	@Test
	public void testInitialDeckShouldHave52Cards() {

		// verify is the deck has 52 cards
		assertEquals("Deck has no 52 cards", 52, testDeck.getCards().size());
		
	}
	
	@Test
	public void testMakeDeckWithOneJoker() {

		// verify is the deck has 52 cards and one joker
		assertEquals("Deck has no 52 cards", 53, testDeckWithAJoker.getCards().size());
		assertEquals("Deck does not have one joker", 1, testDeckWithAJoker.countCard(testJoker));
		
	}

	@Test
	public void testMakeDeckWith3Jokers() {

		// verify is the deck has 52 cards and 3 jokers
		assertEquals("Deck has no 52 cards", 55, testDeckWith3Jokers.getCards().size());
		assertEquals("Deck does not have 3 jokers", 3, testDeckWith3Jokers.countCard(testJoker));
		
		
	}
	
	@Test
	public void testIfAceOfSpadesAfterShuffleHasNewPosition() {
		
		// extra initializations
		// Ace is first of Spaces suit that is the 4th suit
		int normalPositionAceOfSpades = 13+13+13+1;
		
		// run the void method
		testDeck.shuffle();
		int newPositionAceOfSpades = testDeck.searchCard(testAceOfSpades);
			
		//verify if the deck is shuffled by comparing the position of the Ace of Spades before and after
		assertNotEquals("AceOfSpades is not shuffled", normalPositionAceOfSpades, newPositionAceOfSpades);
	}

	@Test
	public void testInitialTopCardShouldBeOne() {
		// extra initializations
		int initialTopCard = testDeck.searchNextCardNotInHand();
		
		//verify if the deck top card has index 0
		assertEquals("TopCard is not 1", 0, initialTopCard);
	
	}

	@Test
	public void testLastTopCardShouldBe52() {
		// deal 51 cards 
		for (int i=0;i<testDeck.getCards().size()-1;i++) {
			testDeck.deal(1);
		}
		int lastTopCard = testDeck.searchNextCardNotInHand();
		
		//verify if the deck top card is index 51
		assertEquals("TopCard is not 1", 51, lastTopCard);
	
	}

	@Test
	public void testDealACardShouldGiveNewTopCard() {

		// extra initializations
		int initialTopCard = testDeck.searchNextCardNotInHand();
		
		// run the method
		testDeck.deal(1);
		int newTopCard = testDeck.searchNextCardNotInHand();
		
		
		//verify if the deck top card is another card
		assertNotEquals("TopCard is the same after dealing", initialTopCard, newTopCard);
		
	}

	@Test
	public void testDealAceOfSpadesToSecondHand() {

		//initialize
		int positionAceOfSpades = testDeck.searchCard(testAceOfSpades);
	
		for (int i=0;i<positionAceOfSpades-1;i++) {
			testDeck.deal(1);
		}
		testDeck.deal(2);

		// verify if ace of spades is dealed to hand 2
		assertNotEquals("Dealed card ace of spades is not in second hand", 2, testDeck.getDealedTo(40));
		
	}

	@Test
	public void testPositionAceOfSPadesShouldInitiallyBe40() {

		//initialize
		int positionAceOfSpades = testDeck.searchCard(testAceOfSpades);
		
		//verify if the position of the Ace of Spades initially is 40
		assertEquals("AceOfSpades is not on its initial position", 40, positionAceOfSpades);

	}

	@Test
	public void testTotalAceOfSpadesShouldBeOne() {
		
		//initialize
		int countAceOfSpades = testDeck.countCard(testAceOfSpades);
		
		//verify if the position of the Ace of Spades initially is 40
		assertEquals("AceOfSpades should be counted one times", 1, countAceOfSpades);
				
	}

}
