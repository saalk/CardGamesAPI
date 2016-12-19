package nl.knikit.cardgames.DTO;

import nl.knikit.cardgames.mapper.ModelMapperUtil;
import nl.knikit.cardgames.model.Player;
import nl.knikit.cardgames.testdata.TestData;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PlayerDtoTest extends TestData {
	
	private ModelMapperUtil modelMapperUtil = new ModelMapperUtil();
	
	@Test
	public void whenConvertPlayerEntityToPlayerDto_thenCorrect() throws Exception {
		
		// Given
		Player playerFixture = MakePlayerEntityWithIdAndGamesWon(1, 3);
		
		// When
		PlayerDto actual = modelMapperUtil.convertToDto(playerFixture);
		// extra fields also in the converter
		actual.setName();
		actual.setWinCount();
		
		// Then - expected 11 fields, actual 9 fields
		String playerFixtureName = "John 'Test' Doe(Human) [Elf]";
		assertEquals(playerFixtureName, actual.getName());
		assertEquals(playerFixture.getPlayerId(), actual.getPlayerId());
		assertEquals(playerFixture.getCreated(), actual.getCreated());
		assertEquals(playerFixture.getAlias(), actual.getAlias());
		assertEquals(String.valueOf(playerFixture.getHuman()), actual.getHuman());
		assertEquals(playerFixture.getAiLevel().toString(), actual.getAiLevel());
		assertEquals(playerFixture.getAvatar().toString(), actual.getAvatar());
		assertEquals(playerFixture.getCubits(), actual.getCubits());
		assertEquals(playerFixture.getSecuredLoan(), actual.getSecuredLoan());
		
		assertEquals(playerFixture.getGames().size(), actual.getGameDtos().size());
		assertEquals(3, actual.getWinCount());
	}
	
	@Test
	public void whenConvertPlayerDtoToPlayerEntity_thenCorrect() throws Exception {
		
		// Given
		PlayerDto playerDtoFixture = MakePlayerDtoWithIdAndGamesWon(2, 4);
		
		// When
		Player actual = modelMapperUtil.convertToEntity(playerDtoFixture);
		
		// Then - expected 9 , actual 11
		assertEquals(playerDtoFixture.getPlayerId(), actual.getPlayerId());
		assertEquals(playerDtoFixture.getCreated(), actual.getCreated());
		assertEquals(playerDtoFixture.getAlias(), actual.getAlias());
		assertEquals(playerDtoFixture.getHuman(), String.valueOf(actual.getHuman()));
		assertEquals(playerDtoFixture.getAiLevel(), actual.getAiLevel().toString());
		assertEquals(playerDtoFixture.getAvatar(), actual.getAvatar().toString());
		assertEquals(playerDtoFixture.getCubits(), actual.getCubits());
		assertEquals(playerDtoFixture.getSecuredLoan(), actual.getSecuredLoan());
		// list of games is set to null when passing to player
		assertNull(actual.getGames());
	}
}