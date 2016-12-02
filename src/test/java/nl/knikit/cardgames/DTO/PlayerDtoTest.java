package nl.knikit.cardgames.DTO;

import nl.knikit.cardgames.model.AiLevel;
import nl.knikit.cardgames.model.Avatar;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.model.Player;

import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PlayerDtoTest {
	
	private ModelMapper modelMapper = new ModelMapper();
	private Player playerFixture = new Player();
	private PlayerDto playerDtoFixture = new PlayerDto();
	
	@Before
	public void setUp() throws Exception {
		// Given
		
		playerFixture.setPlayerId((1));
		playerFixture.setAlias("John 'Test' Doe");
		playerFixture.setHuman(true);
		playerFixture.setAiLevel(AiLevel.HUMAN);
		playerFixture.setAvatar(Avatar.ELF);
		playerFixture.setCubits(1);
		playerFixture.setSecuredLoan(1);
		
		List<Game> games = new ArrayList<>();
		games.add(new Game());
		playerFixture.setGames(games);
		
		
		playerDtoFixture.setPlayerId(2);
		playerDtoFixture.setAlias("John 'DtoTest' Doe");
		playerDtoFixture.setHuman(false);
		playerDtoFixture.setAiLevel(AiLevel.LOW);
		playerDtoFixture.setAvatar(Avatar.GOBLIN);
		playerDtoFixture.setCubits(2);
		playerDtoFixture.setSecuredLoan(2);
		
		List<GameDto> gamesDto = new ArrayList<>();
		gamesDto.add(new GameDto());
		gamesDto.add(new GameDto());
		playerDtoFixture.setGames(gamesDto);
	}
	
	@Test
	public void whenConvertPlayerEntityToPlayerDto_thenCorrect() throws Exception {
		// When
		PlayerDto actual = modelMapper.map(playerFixture, PlayerDto.class);
		// extra also in the converter
		actual.setName();
		actual.setWinCount();
		
		// Then
		
		// expected, actual
		
		// "Script Joe(Human|Smart) [Elf]"
		String playerFixtureName = "John 'Test' Doe(Human) [Elf]";
		assertEquals(playerFixtureName, actual.getName());
		assertEquals(playerFixture.getGames(),actual.getGames());
		assertEquals(1,actual.getWinCount());
		
		assertEquals(playerFixture.getPlayerId(), actual.getPlayerId());
		assertEquals(playerFixture.getAlias(), actual.getAlias());
		assertEquals(String.valueOf(playerFixture.getHuman()), actual.getHuman());
		assertEquals(playerFixture.getAiLevel().toString(), actual.getAiLevel());
		assertEquals(playerFixture.getAvatar().toString(), actual.getAvatar());
		assertEquals(playerFixture.getCubits(), actual.getCubits());
		assertEquals(playerFixture.getSecuredLoan(), actual.getSecuredLoan());
		
	}
	
	@Test
	public void whenConvertPlayerDtoToPlayerEntity_thenCorrect() throws Exception {
		// When
		Player actual = modelMapper.map(playerDtoFixture, Player.class);
		
		// Then
		
		// expected, actual
		assertEquals(playerDtoFixture.getGames().size(),actual.getGames().size());
		assertEquals(2, actual.getGames().size());
		
		assertEquals(playerDtoFixture.getPlayerId(), actual.getPlayerId());
		assertEquals(playerDtoFixture.getAlias(), actual.getAlias());
		assertEquals(playerDtoFixture.getHuman(), String.valueOf(actual.getHuman()));
		assertEquals(playerDtoFixture.getAiLevel(), actual.getAiLevel().toString());
		assertEquals(playerDtoFixture.getAvatar(), actual.getAvatar().toString());
		assertEquals(playerDtoFixture.getCubits(), actual.getCubits());
		assertEquals(playerDtoFixture.getSecuredLoan(), actual.getSecuredLoan());
		
	}
}