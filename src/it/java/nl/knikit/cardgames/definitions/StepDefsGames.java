package nl.knikit.cardgames.definitions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nl.knikit.cardgames.DTO.GameDto;
import nl.knikit.cardgames.DTO.PlayerDto;
import nl.knikit.cardgames.model.AiLevel;
import nl.knikit.cardgames.model.Avatar;
import nl.knikit.cardgames.model.GameType;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;

import static nl.knikit.cardgames.model.state.GalacticCasinoStateMachine.State;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StepDefsGames extends SpringIntegrationTest {
	
	private static String latestGameID = "";
	private static List<String> latestGameIDs = new ArrayList<>();
	
	private static String latestPlayerID = "";
	
	private static String gamesUrl = "http://localhost:8383/api/games/";
	private static String allGamesUrl = "http://localhost:8383/api/games";
	
	private static String playersUrl = "http://localhost:8383/api/players/";
	private static String gamesUrlWithId = "http://localhost:8383/api/games/{id}";
	
	// API          HTTP
	//
	// UPDATE,PUT   OK(200, "OK"),
	// POST         CREATED(201, "Created"),
	// DELETE       NO_CONTENT(204, "No Content"),
	
	// no body      BAD_REQUEST(400, "Bad Request"),
	// wrong id     NOT_FOUND(404, "Not Found"),
	
	@Given("^I try to get a game with valid \"([^\"]*)\"$")
	public void iTryToGetAGameWithValid(String gameId) throws Throwable {
		if (gameId.equals("latest")) {
			gameId = StepDefsGames.latestGameID;
		}
		executeGet(gamesUrl + gameId);
	}
	
	@Given("^I try to get a game with invalid \"([^\"]*)\"$")
	public void iTryToGetAGameWithInvalid(String gameId) throws Throwable {
		if (gameId.equals("latest")) {
			gameId = StepDefsGames.latestGameID;
		}
		executeGet(gamesUrl + gameId);
	}
	
	@Given("^I try to get all games")
	public void iTryToGetAllGames() throws Throwable {
		
		executeGet(allGamesUrl);
	}
	
	@Given("^I try to post a gameType \"([^\"]*)\" game having \"([^\"]*)\" and ante \"([^\"]*)\" and state \"([^\"]*)\"$")
	public void iTryToPostANewTypeGameWithWinnerAndAnte(String gameType, String winner, String ante,  String state) throws Throwable {
		
		
		GameDto postGameDto = new GameDto();
		
		if (!winner.isEmpty()) {
			PlayerDto postPlayer = new PlayerDto();
			postPlayer.setPlayerId(Integer.parseInt(winner));
			postGameDto.setWinner(postPlayer);
		}
		postGameDto.setGameType(GameType.valueOf(gameType));
		postGameDto.setState(State.valueOf(state));
		postGameDto.setAnte(Integer.parseInt(ante));
		
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//Object to JSON in String
		String jsonInString = mapper.writeValueAsString(postGameDto);
		executePost(gamesUrl, jsonInString);
		
	}
	
	@Given("^I try to post a human \"([^\"]*)\" winner having \"([^\"]*)\" and \"([^\"]*)\" and \"([^\"]*)\"$")
	public void iTryToPostANewHumanPlayerWithAvatarAlias(String human, String avatar, String alias, String aiLevel) throws Throwable {
		
		PlayerDto postPlayerDto = new PlayerDto();
		postPlayerDto.setHuman(Boolean.parseBoolean(human));
		postPlayerDto.setAvatar(Avatar.valueOf(avatar));
		postPlayerDto.setAiLevel(AiLevel.valueOf(aiLevel));
		postPlayerDto.setAlias(alias);
		
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//Object to JSON in String
		String jsonInString = mapper.writeValueAsString(postPlayerDto);
		executePost(playersUrl, jsonInString);
		
	}
	
	@Given("^I try to put a game with \"([^\"]*)\" having gameType \"([^\"]*)\" winner \"([^\"]*)\" and ante \"([^\"]*)\" and state \"([^\"]*)\"$")
	public void iTryToPutANewTypeGameWithWinnerAndAnte(String gameId, String gameType, String winner, String ante, String state) throws Throwable {
		
		GameDto postGameDto = new GameDto();
		if (gameId.equals("latest")) {
			gameId = StepDefsGames.latestGameID;
		}
		postGameDto.setGameId(Integer.parseInt(gameId));
		postGameDto.setGameType(GameType.valueOf(gameType));
		postGameDto.setState(State.valueOf(state));
		postGameDto.setAnte(Integer.parseInt(ante));
		
		if (!winner.isEmpty()) {
			if (winner.equals("latest")) {
				winner = StepDefsGames.latestGameID;
			}
			PlayerDto postPlayer = new PlayerDto();
			postPlayer.setPlayerId(Integer.parseInt(winner));
			postGameDto.setWinner(postPlayer);
			// this makes only a player with just an id
		}
		
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//Object to JSON in String
		String jsonInString = mapper.writeValueAsString(postGameDto);
		executePut(gamesUrl + gameId, jsonInString);
		
	}
	
	@Given("^I try to put a game with \"([^\"]*)\" having winner \"([^\"]*)\"$")
	public void iTryToPutAnExistingGameWithWinner(String gameId, String winner) throws Throwable {
		if (gameId.equals("latest")) {
			gameId = StepDefsGames.latestGameID;
		}
		if (winner.equals("latest")) {
			winner = StepDefsGames.latestPlayerID;
		}
		
		// Uri (URL) parameters
		Map<String, String> uriParams = new HashMap<>();
		uriParams.put("id", gameId);
		
		// Query parameters
		Map<String, String>  queryParams = new HashMap<>();
		queryParams.put("winner", winner);
		
		// body cannot be null since there is a put that wants a request body
		executePutWithUriAndQueryParam(gamesUrlWithId, uriParams, "{}", queryParams);
	}
	
	@Given("^I try to delete a game with \"([^\"]*)\"$")
	public void iTryToDeleteAGameWith(String gameId) throws Throwable {
		if (gameId.equals("latest")) {
			gameId = StepDefsGames.latestGameID;
			StepDefsGames.latestGameIDs.remove(latestGameIDs.size()-1);
			
		}
		executeDelete(gamesUrl + gameId, null);
	}
	
	@Given("^I try to delete all games with \"([^\"]*)\"$")
	public void iTryToDeleteAllGamesWith(String ids) throws Throwable {
		if (ids.equals("all")) {
			// all
		}
		executeDelete(allGamesUrl + "?id=" + StringUtils.join(latestGameIDs,','), null);
		StepDefsGames.latestGameIDs.clear();
	}
	
	@Given("^I try to delete the winner \"([^\"]*)\"$")
	public void iTryToDeleteTheWinner(String winner) throws Throwable {
		if (winner.equals("latest")) {
			winner = StepDefsGames.latestPlayerID;
		}
		executeDelete(playersUrl + winner, null);
	}
	
	@And("^The json response should contain at least \"([^\"]*)\" games$")
	public void theJsonGameResponseBodyShouldContainAtLeast(int count) throws Throwable {
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//JSON string to Object
		List<GameDto> jsonGames = mapper.readValue(latestResponse.getBody(),new TypeReference<List<GameDto>>(){});
		
		latestGameIDs.clear();
		for (GameDto gameDto : jsonGames ) {
			latestGameIDs.add(String.valueOf(gameDto.getGameId()));
			StepDefsGames.latestGameID = String.valueOf(gameDto.getGameId());
		}
		
		// at least equal but more can exist
		assertTrue(latestGameIDs.size()>=count);
	}
	
	@And("^The json response should contain gameType \"([^\"]*)\" game having \"([^\"]*)\" and ante \"([^\"]*)\" and state \"([^\"]*)\"$")
	public void theJsonResponseBodyShouldBeANewTypeGameWithWinnerAndAnte(String gameType, String playerDto, String ante, String state) throws Throwable {
		
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//JSON string to Object
		GameDto jsonGame = mapper.readValue(latestResponse.getBody(), GameDto.class);
		StepDefsGames.latestGameID = String.valueOf(jsonGame.getGameId());
		// do not set the player here, the player has been set before when making the player
		
		assertThat(jsonGame.getGameType(), is(gameType));
		assertThat(jsonGame.getAnte(), is(Integer.parseInt(ante)));
		assertThat(jsonGame.getState(), is(state));
		
		
		if (playerDto.equals("latest")) {
			playerDto = StepDefsGames.latestPlayerID;
		}
		
		if (!playerDto.isEmpty()) {
			assertEquals(jsonGame.getWinner().getPlayerId(), Integer.parseInt(playerDto));
		}
	}

	@And("^The json response should contain a winner$")
	public void theJsonResponseBodyShouldBeANewHumanPlayerWithAvatarAlias() throws Throwable {
		
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//JSON string to Object
		PlayerDto jsonPlayer = mapper.readValue(latestResponse.getBody(), PlayerDto.class);
		StepDefsGames.latestPlayerID  = String.valueOf(jsonPlayer.getPlayerId());

	}
}