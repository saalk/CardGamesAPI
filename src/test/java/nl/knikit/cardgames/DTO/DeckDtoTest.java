package nl.knikit.cardgames.DTO;

import nl.knikit.cardgames.model.AiLevel;
import nl.knikit.cardgames.model.Card;
import nl.knikit.cardgames.model.Deck;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.model.Player;
import nl.knikit.cardgames.model.Rank;
import nl.knikit.cardgames.model.Suit;

import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;

import static org.junit.Assert.assertEquals;

public class DeckDtoTest {
	
	private ModelMapper modelMapper = new ModelMapper();
	private Deck deckFixture = new Deck();
	private DeckDto deckDtoFixture = new DeckDto();
	
	@Before
	public void setUp() throws Exception {
		
		// Given a deck having 5 fields
		deckFixture.setDeckId(1);
		Game game = new Game();
		deckFixture.setGame(game);
		Card card = new Card();
		card.setCardId("10C");
		card.setRank(Rank.TEN);
		card.setSuit(Suit.CLUBS);
		card.setValue(99);
		deckFixture.setCard(card);
		Player dealtTo = new Player();
		deckFixture.setDealtTo(dealtTo);
		deckFixture.setCardOrder(6);
		
		// Given a deckDto having 5 + 1 fields
		deckDtoFixture.setDeckId(3);
		GameDto gameDto = new GameDto();
		deckDtoFixture.setGame(gameDto);
		CardDto cardDto = new CardDto();
		cardDto.setCardId("KH");
		cardDto.setRank(Rank.KING);
		cardDto.setSuit(Suit.HEARTS);
		cardDto.setValue(3);
		deckDtoFixture.setCard(cardDto);
		PlayerDto dealtToDto = new PlayerDto();
		dealtToDto.setAlias("John 'DeckDtoTest' Doe");
		dealtToDto.setAiLevel(AiLevel.MEDIUM);
		deckDtoFixture.setDealtTo(dealtToDto);
		deckDtoFixture.setCardOrder(12);
		deckDtoFixture.setName(); // extra field "(03) 10C  John 'DeckDtoTest' Doe [Medium]"

		
	}
	
	@Test
	public void whenConvertDeckEntityToDeckDto_thenCorrect() throws Exception {
		// When
		DeckDto actual = modelMapper.map(deckFixture, DeckDto.class);
		// extra also in the converter
		actual.setName();
		
		// Then
		// expected 4, actual 5
		String deckFixtureName = "(06) 10C  Ten of Clubs";
		//String deckFixtureName = "(03) 10C  John 'DeckDtoTest' Doe [Medium]";
		assertEquals(deckFixtureName, actual.getName());
		assertEquals(deckFixture.getDeckId(), actual.getDeckId());
		assertEquals(deckFixture.getGame().getGameId(), actual.getGame().getGameId());
		assertEquals(deckFixture.getCard().getCardId(), actual.getCard().getCardId());
		assertEquals(deckFixture.getDealtTo().getPlayerId(), actual.getDealtTo().getPlayerId());
		
	}
	
	@Test
	public void whenConvertDeckDtoToDeckEntity_thenCorrect() throws Exception {
		// When
		Deck actual = modelMapper.map(deckDtoFixture, Deck.class);
		
		// Then
		// expected 5, actual 4
		assertEquals(deckDtoFixture.getDeckId(), actual.getDeckId());
		assertEquals(deckDtoFixture.getGame().getGameId(), actual.getGame().getGameId());
		assertEquals(deckDtoFixture.getCard().getCardId(), actual.getCard().getCardId());
		assertEquals(deckDtoFixture.getDealtTo().getPlayerId(), actual.getDealtTo().getPlayerId());

	}
}