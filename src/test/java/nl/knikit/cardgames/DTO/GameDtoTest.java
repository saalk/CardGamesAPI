package nl.knikit.cardgames.DTO;

import nl.knikit.cardgames.mapper.GameMapFromDto;
import nl.knikit.cardgames.mapper.GameMapFromEntity;
import nl.knikit.cardgames.model.Deck;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.model.GameType;
import nl.knikit.cardgames.model.Player;
import nl.knikit.cardgames.model.state.GalacticCasinoStateMachine;

import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class GameDtoTest {
	
	
	private ModelMapper modelMapper = new ModelMapper();
	private Game gameFixture = new Game();
	private GameDto gameDtoFixture = new GameDto();
	
	@Before
	public void setUp() throws Exception {
		
		// Given a gameDto having 14 fields
		gameFixture.setGameId(1);
		gameFixture.setCreated("1");
		gameFixture.setState(GalacticCasinoStateMachine.State.SELECT_GAME);
		gameFixture.setGameType(GameType.HIGHLOW);
		gameFixture.setAnte(50);
		gameFixture.setMinRounds(1);
		gameFixture.setCurrentRound(1);
		gameFixture.setMaxRounds(1);
		gameFixture.setMinTurns(2);
		gameFixture.setCurrentTurn(2);
		gameFixture.setTurnsToWin(2);
		gameFixture.setMaxTurns(2);
		Player player = new Player();
		player.setPlayerId(2);
		gameFixture.setPlayer(player);
		List<Deck> decks = new ArrayList<>();
		decks.add(new Deck());
		decks.add(new Deck());
		gameFixture.setDecks(decks);
		
		// Given a gameDto having 14 + 3 fields
		gameDtoFixture.setGameId(3);
		gameDtoFixture.setCreated("3");
		gameDtoFixture.setState(GalacticCasinoStateMachine.State.ITERATE_PLAYERS);
		gameDtoFixture.setGameType(GameType.BLACKJACK);
		gameDtoFixture.setAnte(100);
		gameDtoFixture.setName(); // extra fields "Highlow:0005 (Ante:100) [GameSelected]"
		gameDtoFixture.setMinRounds(3);
		gameDtoFixture.setCurrentRound(3);
		gameDtoFixture.setMaxRounds(3);
		gameDtoFixture.setRound(); // extra field
		gameDtoFixture.setMinTurns(4);
		gameDtoFixture.setCurrentTurn(4);
		gameDtoFixture.setTurnsToWin(4);
		gameDtoFixture.setMaxTurns(4);
		gameDtoFixture.setTurn(); // extra field
		ArrayList<DeckDto> decksDto = new ArrayList<>();
		decksDto.add(new DeckDto());
		decksDto.add(new DeckDto());
		decksDto.add(new DeckDto());
		gameDtoFixture.setDeckDtos(decksDto); // "10C  Ten of Clubs"
		PlayerDto playerDto = new PlayerDto();
		playerDto.setPlayerId(5);
		gameDtoFixture.setWinner(playerDto);
		
	}
	
	@Test
	public void whenConvertGameEntityToGameDto_thenCorrect() throws Exception {
		// When
		GameDto actual = modelMapper.map(gameFixture, GameDto.class);
		modelMapper.addMappings(new GameMapFromEntity()); // customer mapping
		modelMapper.addMappings(new GameMapFromDto()); // customer mapping
		
		// extra also in the converter
		actual.setName();
		actual.setRound();
		actual.setTurn();
		
		// Then
		// expected 14, actual 17
		String gameFixtureName = "Highlow#0001 (Ante:50) [Select_game]";
		assertEquals(gameFixtureName, actual.getName());
		assertEquals(gameFixture.getGameId(), actual.getGameId());
		assertEquals(gameFixture.getCreated(), actual.getCreated());
		assertEquals(gameFixture.getState().toString(), actual.getState());
		assertEquals(gameFixture.getGameType().toString(), actual.getGameType());
		assertEquals(gameFixture.getAnte(), actual.getAnte());
		assertEquals(gameFixture.getMinRounds(), actual.getMinRounds());
		assertEquals(gameFixture.getCurrentRound(), actual.getCurrentRound());
		assertEquals(gameFixture.getMaxRounds(), actual.getMaxRounds());
		String gameFixtureRound = "Round 1 [1-1]";
		assertEquals(gameFixtureRound, actual.getRound());
		assertEquals(gameFixture.getMinTurns(), actual.getMinTurns());
		assertEquals(gameFixture.getCurrentTurn(), actual.getCurrentTurn());
		assertEquals(gameFixture.getTurnsToWin(), actual.getTurnsToWin());
		assertEquals(gameFixture.getMaxTurns(), actual.getMaxTurns());
		String gameFixtureTurn = "Turn 2 (2 to win) [2-2]";
		assertEquals(gameFixtureTurn, actual.getTurn());
		// TODO fix me
		//assertEquals(gameFixture.getPlayer().getPlayerId(), actual.getWinner().getPlayerId());
		//assertEquals(gameFixture.getDecks().size(), actual.getDeckDtos().size());
	}
	
	@Test
	public void whenConvertGameDtoToGameEntity_thenCorrect() throws Exception {
		// When
		Game actual = modelMapper.map(gameDtoFixture, Game.class);
		modelMapper.addMappings(new GameMapFromEntity()); // customer mapping
		modelMapper.addMappings(new GameMapFromDto()); // customer mapping
		
		
		// Then
		// expected 17, actual 14
		assertEquals(gameDtoFixture.getGameId(), actual.getGameId());
		assertEquals(gameDtoFixture.getCreated(), actual.getCreated());
		assertEquals(gameDtoFixture.getState(), actual.getState().toString());
		assertEquals(gameDtoFixture.getGameType(), actual.getGameType().toString());
		assertEquals(gameDtoFixture.getAnte(), actual.getAnte());
		assertEquals(gameDtoFixture.getMinRounds(), actual.getMinRounds());
		assertEquals(gameDtoFixture.getCurrentRound(), actual.getCurrentRound());
		assertEquals(gameDtoFixture.getMaxRounds(), actual.getMaxRounds());
		assertEquals(gameDtoFixture.getMinTurns(), actual.getMinTurns());
		assertEquals(gameDtoFixture.getCurrentTurn(), actual.getCurrentTurn());
		assertEquals(gameDtoFixture.getTurnsToWin(), actual.getTurnsToWin());
		assertEquals(gameDtoFixture.getMaxTurns(), actual.getMaxTurns());
		// TODO fix me
		//assertEquals(gameDtoFixture.getWinner().getPlayerId(), actual.getPlayer().getPlayerId());
		// assertEquals(gameDtoFixture.getDeckDtos().size(), actual.getDecks().size());
	}
}