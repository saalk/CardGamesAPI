package nl.knikit.cardgames.DTO;

import nl.knikit.cardgames.mapper.ModelMapperUtil;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.testdata.TestData;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GameDtoTest extends TestData {
	
	
	private ModelMapperUtil modelMapperUtil = new ModelMapperUtil();
	
	@Test
	public void whenConvertGameEntityToGameDto_thenCorrect() throws Exception {
		
		// Given
		Game gameFixture = MakeGameEntityWithIdAndDecksAndWinner(1, 1, 1);
		
		// When
		GameDto actual = modelMapperUtil.convertToDto(gameFixture);
		
		// extra also in the converter
		actual.setName();
		actual.setRound();
		actual.setTurn();
		
		// Then - expected 14, actual 17
		String gameFixtureName = "Highlow#0001 (Ante:50) [Is_setup]";
		assertEquals(gameFixtureName, actual.getName());
		assertEquals(gameFixture.getGameId(), actual.getGameId());
		assertEquals(gameFixture.getState(), actual.getState());
		assertEquals(gameFixture.getGameType(), actual.getGameType());
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
		String gameFixtureTurn = "Playing 2 [2-2]";
		assertEquals(gameFixtureTurn, actual.getTurn());
		// TODO fix me
		assertEquals(gameFixture.getPlayer().getPlayerId(), actual.getWinner().getPlayerId());
		//assertEquals(gameFixture.getDecks().size(), actual.getDeckDtos().size());
	}
	
	@Test
	public void whenConvertGameDtoToGameEntity_thenCorrect() throws Exception {
		
		// Given
		GameDto gameDtoFixture = MakeGameDtoWithIdAndDecksAndWinner(3, 3, 5);
		
		// When
		Game actual = modelMapperUtil.convertToEntity(gameDtoFixture);
		
		// Then - expected 17, actual 14
		assertEquals(gameDtoFixture.getGameId(), actual.getGameId());
		assertEquals(gameDtoFixture.getState(), actual.getState());
		assertEquals(gameDtoFixture.getGameType(), actual.getGameType());
		assertEquals(gameDtoFixture.getAnte(), actual.getAnte());
		assertEquals(gameDtoFixture.getMinRounds(), actual.getMinRounds());
		assertEquals(gameDtoFixture.getCurrentRound(), actual.getCurrentRound());
		assertEquals(gameDtoFixture.getMaxRounds(), actual.getMaxRounds());
		assertEquals(gameDtoFixture.getMinTurns(), actual.getMinTurns());
		assertEquals(gameDtoFixture.getCurrentTurn(), actual.getCurrentTurn());
		assertEquals(gameDtoFixture.getTurnsToWin(), actual.getTurnsToWin());
		assertEquals(gameDtoFixture.getMaxTurns(), actual.getMaxTurns());
		// TODO fix me
		// assertEquals(gameDtoFixture.getWinner().getSuppliedPlayerId(), actual.getPlayer().getSuppliedPlayerId());
		// assertEquals(gameDtoFixture.getDeckDtos().size(), actual.getDecks().size());
	}
}