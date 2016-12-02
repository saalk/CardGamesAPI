package nl.knikit.cardgames.DTO;

import nl.knikit.cardgames.model.Deck;
import nl.knikit.cardgames.model.Game;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

public class GameDtoTest {
	
	private ModelMapper modelMapper = new ModelMapper();
	private Game gameFixture = new Game();
	private GameDto gameDtoFixture = new GameDto();
	
	@Before
	public void setUp() throws Exception {
		// Given
		
		//private String name;
		// "Highlow:0005 (Ante:100) [GameSelected]"
		//private int gameId;
		//private String state;
		//private String gameType;
		//private int ante;
		//private String round;
		//private int minRounds;
		//private int currentRound;
		//private int maxRounds;
		//private String turn;
		//private int minTurns;
		//private int currentTurn;
		//private int turnsToWin;
		//private int maxTurns;
		//private Set<DeckDao> deckDtos;
		// "10C  Ten of Clubs"
		// " RJ  Script Joe [ELF]"
		//private PlayerDto winner;
		
		gameFixture.setGameId((1));
		
		List<Deck> decks = new ArrayList<>();
		decks.add(new Deck());
		gameFixture.setDecks(decks);
		
		
		gameDtoFixture.setGameId(2);
		
		List<DeckDto> decksDto = new ArrayList<>();
		decksDto.add(new DeckDto());
		decksDto.add(new DeckDto());
		//gameDtoFixture.setDecks(decksDto);
	}
	
	@Test @Ignore
	public void whenConvertGameEntityToGameDto_thenCorrect() throws Exception {
		// When
		GameDto actual = modelMapper.map(gameFixture, GameDto.class);
		// extra also in the converter
		actual.setName();
		
		// Then
		
		// expected, actual
		
		// "Script Joe(Human|Smart) [Elf]"
		String gameFixtureName = "John 'Test' Doe(Human) [Elf]";
//		assertEquals(gameFixtureName, actual.getName());
//		assertEquals(gameFixture.getGames(),actual.getGames());
//		assertEquals(1,actual.getWinCount());
//
//		assertEquals(gameFixture.getGameId(), actual.getGameId());
//		assertEquals(gameFixture.getAlias(), actual.getAlias());
//		assertEquals(String.valueOf(gameFixture.getHuman()), actual.getHuman());
//		assertEquals(gameFixture.getAiLevel().toString(), actual.getAiLevel());
//		assertEquals(gameFixture.getAvatar().toString(), actual.getAvatar());
//		assertEquals(gameFixture.getCubits(), actual.getCubits());
//		assertEquals(gameFixture.getSecuredLoan(), actual.getSecuredLoan());
		
	}
	
	@Test @Ignore
	public void whenConvertGameDtoToGameEntity_thenCorrect() throws Exception {
		// When
		Game actual = modelMapper.map(gameDtoFixture, Game.class);
		
		// Then
		
		// expected, actual
//		assertEquals(gameDtoFixture.getGames().size(),actual.getGames().size());
//		assertEquals(2, actual.getGames().size());
//
//		assertEquals(gameDtoFixture.getGameId(), actual.getGameId());
//		assertEquals(gameDtoFixture.getAlias(), actual.getAlias());
//		assertEquals(gameDtoFixture.getHuman(), String.valueOf(actual.getHuman()));
//		assertEquals(gameDtoFixture.getAiLevel(), actual.getAiLevel().toString());
//		assertEquals(gameDtoFixture.getAvatar(), actual.getAvatar().toString());
//		assertEquals(gameDtoFixture.getCubits(), actual.getCubits());
//		assertEquals(gameDtoFixture.getSecuredLoan(), actual.getSecuredLoan());
//
	}
}