package nl.knikit.cardgames.DTO;

import nl.knikit.cardgames.model.Card;
import nl.knikit.cardgames.model.Rank;
import nl.knikit.cardgames.model.Suit;
import nl.knikit.cardgames.testdata.TestData;

import org.junit.Test;
import org.modelmapper.ModelMapper;

import static org.junit.Assert.assertEquals;

public class CardDtoTest extends TestData {
	
	private ModelMapper modelMapper = new ModelMapper();

	@Test
	public void whenConvertCardEntityToCardDto_thenCorrect() throws Exception {
		
		// Given
		Card cardFixture = MakeCardEntityWithId("AS");
		
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
		
		// Given
		CardDto cardDtoFixture = MakeCardDtoWithId("AS");
		
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