package nl.knikit.cardgames.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;


public class CardTest {

	// make instance variables
	private Card testCard;

	@Before
	public void setUp() {

		// GIVEN
		testCard = new Card();

	}
	
	// methodname_StateUnderTest_ExpectedBehavior
	@Test
	public void new_AceOfSpades_MustBeCreated() {

		// WHEN
		testCard.setRank(Rank.ACE);
		testCard.setSuit(Suit.SPADES);

		// THEN
		assertEquals("Ace of Spades cannot be created", Rank.ACE, testCard.getRank());
		assertEquals("Ace of Spades cannot be created", Suit.SPADES, testCard.getSuit());
	}

	@Test
	public void list_allCardCombinationsOfSuitAndRank_MustBe70() {
		
		// GIVEN
		List<Card> testCards = new ArrayList<>();
				
		// WHEN
		for (Suit suits: Suit.values()) {
			for (Rank ranks: Rank.values()) {

				testCards.add(new Card(ranks, suits));
			}
		}

		// THEN
		assertEquals("Total unique Cards must be 70", 70, testCards.size());
	}
	
	@Test
	public void staticList_prototypeDeck_MustHaveSize52() {
		
		// WHEN
		// static list so already created for new Card()
		
		// THEN
		assertEquals("PrototypeDeck must have 52 cards", 52, Card.prototypeDeck.size());
	}
	
	@Test
	public void newDeck_WithoutJokers_MustReturn52Cards() {
		
		// WHEN
		List<Card> testCards = Card.newDeck(0);
		
		// THEN
		assertEquals("Static method newDeck(0) must return 52 cards", 52, testCards.size());
	}
	
	@Test
	public void newDeck_Without3Jokers_MustReturn55Cards() {
		
		// WHEN
		List<Card> testCards = Card.newDeck(3);
		
		// THEN
		assertEquals("Static method newDeck(3) must return 55 cards", 55, testCards.size());
	}
	
	@Test
	public void staticField_joker_MustHaveCardIdRJ() {
		
		// GIVEN
		testCard = Card.joker;
		
		// WHEN
		String JOKER_CARD_ID = "RJ";
		
		// THEN
		assertEquals("Joker has label RJ", testCard.getCardId(), JOKER_CARD_ID);
	}
	
	@Test
	public void new_CardWithRJ_MustBeJokerCardId() {
		
		// GIVEN
		Card joker = new Card("RJ");
		
		// THEN
		assertTrue(joker.isJoker());
	}
	
	@Test
	public void isValidCard_RJ_true() {
		
		// GIVEN
		
		// THEN
		assertTrue(Card.isValidCard("RJ"));
	}
	
}
