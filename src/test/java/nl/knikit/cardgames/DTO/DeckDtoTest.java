package nl.knikit.cardgames.DTO;

import nl.knikit.cardgames.mapper.ModelMapperUtil;
import nl.knikit.cardgames.model.Deck;
import nl.knikit.cardgames.testdata.TestData;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DeckDtoTest extends TestData {
	
	private ModelMapperUtil modelMapperUtil = new ModelMapperUtil();
	
	@Test
	public void whenConvertDeckEntityToDeckDto_thenCorrect() throws Exception {
		
		// Given
		Deck deckFixture = MakeDeckEntityWithIdAndDealtTo(1, 3);
		
		// When
		DeckDto actual = modelMapperUtil.convertToDto(deckFixture);
		
		// Then - expected 4, actual 5
		String deckFixtureName = "(06) 10C  John 'test' Doe [Human]";
		assertEquals(deckFixtureName, actual.getName());
		assertEquals(deckFixture.getDeckId(), actual.getDeckId());
		assertEquals(deckFixture.getGame().getGameId(), actual.getGameDto().getGameId());
		assertEquals(deckFixture.getCard().getCardId(), actual.getCardDto().getCardId());
		assertEquals(deckFixture.getDealtTo().getPlayerId(), actual.getDealtToDto().getPlayerId());
		
	}
	
	@Test
	public void whenConvertDeckDtoToDeckEntity_thenCorrect() throws Exception {
		
		//Given
		DeckDto deckDtoFixture = MakeDeckDtoWithId(3);
		
		// When
		Deck actual = modelMapperUtil.convertToEntity(deckDtoFixture);
		
		// Then - expected 5, actual 4
		assertEquals(deckDtoFixture.getDeckId(), actual.getDeckId());
		assertEquals(deckDtoFixture.getGameDto().getGameId(), actual.getGame().getGameId());
		assertEquals(deckDtoFixture.getCardDto().getCardId(), actual.getCard().getCardId());
		assertEquals(deckDtoFixture.getDealtToDto().getPlayerId(), actual.getDealtTo().getPlayerId());
		
	}
}