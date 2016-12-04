package nl.knikit.cardgames.DTO;

import nl.knikit.cardgames.model.AiLevel;
import nl.knikit.cardgames.model.Avatar;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.model.Player;

import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PlayerDtoTest {
	
	private ModelMapper modelMapper = new ModelMapper();
	private Player playerFixture = new Player();
	private PlayerDto playerDtoFixture = new PlayerDto();
	
	@Before
	public void setUp() throws Exception {
		// Given
		
		// a player having 9 fields
		playerFixture.setPlayerId(1);
		playerFixture.setCreated("1");
		playerFixture.setAlias("John 'Test' Doe");
		playerFixture.setHuman(true);
		playerFixture.setAiLevel(AiLevel.HUMAN);
		playerFixture.setAvatar(Avatar.ELF);
		playerFixture.setCubits(1);
		playerFixture.setSecuredLoan(1);
		List<Game> games = new ArrayList<>();
		games.add(new Game());
		games.add(new Game());
		games.add(new Game());
		playerFixture.setGames(games);
		
		// a playerDto having 9 + 2 fields
		playerDtoFixture.setPlayerId(2);
		playerDtoFixture.setCreated("2");
		playerDtoFixture.setAlias("John 'DtoTest' Doe");
		playerDtoFixture.setHuman(false);
		playerDtoFixture.setAiLevel(AiLevel.LOW);
		playerDtoFixture.setAvatar(Avatar.GOBLIN);
		playerDtoFixture.setName(); // extra field "Script Joe(Human|Smart) [Elf]"
		playerDtoFixture.setCubits(2);
		playerDtoFixture.setSecuredLoan(2);
		List<GameDto> gamesDto = new ArrayList<>();
		gamesDto.add(new GameDto());
		gamesDto.add(new GameDto());
		gamesDto.add(new GameDto());
		gamesDto.add(new GameDto());
		playerDtoFixture.setGames(gamesDto);
		playerDtoFixture.setWinCount();  // extra field
		
	}
	
	@Test
	public void whenConvertPlayerEntityToPlayerDto_thenCorrect() throws Exception {
		// When
		PlayerDto actual = modelMapper.map(playerFixture, PlayerDto.class);
		// extra fields also in the converter
		actual.setName();
		actual.setWinCount();
		
		// Then
		// expected 11 fields, actual 9 fields
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
		GameDto actualGameDto = new GameDto();
		int i = 0;
		for (Game gameFixture : playerFixture.getGames()) {
			actualGameDto = actual.getGames().get(i);
			assertEquals(gameFixture.getGameId(), actualGameDto.getGameId());
			i++;
		}
		assertEquals(3,actual.getWinCount());
		
		// TODO also test the get converted fields
	}
	
	@Test
	public void whenConvertPlayerDtoToPlayerEntity_thenCorrect() throws Exception {
		// When
		Player actual = modelMapper.map(playerDtoFixture, Player.class);
		
		// Then
		// expected 9 , actual 11
		assertEquals(playerDtoFixture.getPlayerId(), actual.getPlayerId());
		assertEquals(playerDtoFixture.getCreated(), actual.getCreated());
		assertEquals(playerDtoFixture.getAlias(), actual.getAlias());
		assertEquals(playerDtoFixture.getHuman(), String.valueOf(actual.getHuman()));
		assertEquals(playerDtoFixture.getAiLevel(), actual.getAiLevel().toString());
		assertEquals(playerDtoFixture.getAvatar(), actual.getAvatar().toString());
		assertEquals(playerDtoFixture.getCubits(), actual.getCubits());
		assertEquals(playerDtoFixture.getSecuredLoan(), actual.getSecuredLoan());
		assertEquals(playerDtoFixture.getGames().size(),actual.getGames().size());
		assertEquals(4, actual.getGames().size());
		
	}
}