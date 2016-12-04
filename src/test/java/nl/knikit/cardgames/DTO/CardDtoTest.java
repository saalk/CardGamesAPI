package nl.knikit.cardgames.DTO;

import nl.knikit.cardgames.model.Card;
import nl.knikit.cardgames.model.Rank;
import nl.knikit.cardgames.model.Suit;

import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;

import static org.junit.Assert.assertEquals;

public class CardDtoTest {
	
	private ModelMapper modelMapper = new ModelMapper();
	private Card cardFixture = new Card();
	private CardDto cardDtoFixture = new CardDto();

	@Before
	public void setUp() throws Exception {
		// Given
		cardFixture.setCardId("AS");
		cardFixture.setRank(Rank.FOUR);
		cardFixture.setSuit(Suit.HEARTS);
		cardFixture.setValue(1);
		
		cardDtoFixture.setRank(Rank.FIVE);
		cardDtoFixture.setCardId(String.valueOf(2));
		cardDtoFixture.setValue(2);
		cardDtoFixture.setSuit(Suit.CLUBS);
		
	}
	
	@Test
	public void whenConvertCardEntityToCardDto_thenCorrect() throws Exception {
		// When
		CardDto actual = modelMapper.map(cardFixture, CardDto.class);
		String actualSuitLabel = "H";
		String actualRankLabel = "4";
		
		// Then
		// expected, actual
		assertEquals(cardFixture.getCardId(), actual.getCardId());
		assertEquals(cardFixture.getRank().toString(), actual.getRank());
		assertEquals(cardFixture.getSuit().toString(), actual.getSuit());
		assertEquals(cardFixture.getValue(), actual.getValue());
		assertEquals(Suit.HEARTS, actual.getSuitConvertedFromLabel(actualSuitLabel));
		assertEquals(Rank.FOUR, actual.getRankConvertedFromLabel(actualRankLabel));
	}
	
	@Test
	public void whenConvertCardDtoToCardEntity_thenCorrect() throws Exception {
		// When
		Card actual = modelMapper.map(cardDtoFixture, Card.class);
		
		// Then
		// expected, actual
		assertEquals(cardDtoFixture.getCardId(), actual.getCardId());
		assertEquals(cardDtoFixture.getRank(), actual.getRank().toString());
		assertEquals(cardDtoFixture.getSuit(), actual.getSuit().toString());
		assertEquals(cardDtoFixture.getValue(), actual.getValue());
	}
}