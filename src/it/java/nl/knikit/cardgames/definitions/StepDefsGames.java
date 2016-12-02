package nl.knikit.cardgames.definitions;

import com.fasterxml.jackson.databind.ObjectMapper;

import nl.knikit.cardgames.model.Avatar;
import nl.knikit.cardgames.model.GameType;
import nl.knikit.cardgames.model.GameType;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.model.Player;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

public class StepDefsGames extends SpringIntegrationTest {
	
	private static String latestGameID = "";
	private static String latestPlayerID = "";
	
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
		executeGet("http://localhost:8383/api/games/" + gameId);
	}
	
	@Given("^I try to get a game with invalid \"([^\"]*)\"$")
	public void iTryToGetAGameWithInvalid(String gameId) throws Throwable {
		if (gameId.equals("latest")) {
			gameId = StepDefsGames.latestGameID;
		}
		executeGet("http://localhost:8383/api/games/" + gameId);
	}
	
	@Given("^I try to post a type \"([^\"]*)\" game having \"([^\"]*)\" and \"([^\"]*)\"$")
	public void iTryToPostANewTypeGameWithWinnerAndAnte(String type, String winner, String ante) throws Throwable {
		
		
		Game postGame = new Game();
		
		if (!winner.isEmpty()) {
			Player postPlayer = new Player();
			postPlayer.setPlayerId(Integer.parseInt(winner));
			postGame.setWinner(postPlayer);
		}
		postGame.setGameType(GameType.valueOf(type));
		postGame.setAnte(Integer.parseInt(ante));
		
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//Object to JSON in String
		String jsonInString = mapper.writeValueAsString(postGame);
		executePost("http://localhost:8383/api/games", jsonInString);
		
	}
	
	@Given("^I try to post a isHuman \"([^\"]*)\" winner having \"([^\"]*)\" and \"([^\"]*)\"$")
	public void iTryToPostANewHumanPlayerWithAvatarAlias(String isHuman, String avatar, String alias) throws Throwable {
		
		Player postPlayer = new Player();
		postPlayer.setHuman(Boolean.parseBoolean(isHuman));
		postPlayer.setAvatar(Avatar.valueOf(avatar));
		postPlayer.setAlias(alias);
		
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//Object to JSON in String
		String jsonInString = mapper.writeValueAsString(postPlayer);
		executePost("http://localhost:8383/api/players", jsonInString);
		
	}
	
	@Given("^I try to put a game with \"([^\"]*)\" having type \"([^\"]*)\" winner \"([^\"]*)\" and ante \"([^\"]*)\"$")
	public void iTryToPutANewTypeGameWithWinnerAndAnte(String gameId, String type, String winner, String ante) throws Throwable {
		
		Game postGame = new Game();
		if (gameId.equals("latest")) {
			gameId = StepDefsGames.latestGameID;
		}
		//TODO set the playerId also in the body or not ?
		postGame.setGameType(GameType.valueOf(type));
		postGame.setAnte(Integer.parseInt(ante));
		
		if (!winner.isEmpty()) {
			if (winner.equals("latest")) {
				winner = StepDefsGames.latestGameID;
			}
			Player postPlayer = new Player();
			postPlayer.setPlayerId(Integer.parseInt(winner));
			postGame.setWinner(postPlayer);
		}
		
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//Object to JSON in String
		String jsonInString = mapper.writeValueAsString(postGame);
		executePut("http://localhost:8383/api/games/" + gameId, jsonInString);
		
	}
	
	@Given("^I try to put a game with \"([^\"]*)\" having winner \"([^\"]*)\"$")
	public void iTryToPutAnExistingGameWithWinner(String gameId, String winner) throws Throwable {
		if (gameId.equals("latest")) {
			gameId = StepDefsGames.latestGameID;
		}
		if (winner.equals("latest")) {
			winner = StepDefsGames.latestPlayerID;
		}

		executePut("http://localhost:8383/api/games/" + gameId + "?winner=" + winner, null);
	}
	
	@Given("^I try to delete a game with \"([^\"]*)\"$")
	public void iTryToDeleteAGameWith(String gameId) throws Throwable {
		if (gameId.equals("latest")) {
			gameId = StepDefsGames.latestGameID;
		}
		executeDelete("http://localhost:8383/api/games/" + gameId, null);
	}
	
	@Given("^I try to delete the winner \"([^\"]*)\"$")
	public void iTryToDeleteTheWinner(String winner) throws Throwable {
		if (winner.equals("latest")) {
			winner = StepDefsGames.latestPlayerID;
		}
		executeDelete("http://localhost:8383/api/players/" + winner, null);
	}
		
	@And("^The json response should contain type \"([^\"]*)\" game having \"([^\"]*)\" and \"([^\"]*)\"$")
	public void theJsonResponseBodyShouldBeANewTypeGameWithWinnerAndAnte(String type, String winner, String ante) throws Throwable {
		
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//JSON string to Object
		Game jsonGame = mapper.readValue(latestResponse.getBody(), Game.class);
		StepDefsGames.latestGameID = String.valueOf(jsonGame.getGameId());
		// do not set the player here, the player has been set before when making the player
		
		if (winner.equals("latest")) {
			winner = StepDefsGames.latestPlayerID;
		}
		
		if (!winner.isEmpty()) {
			assertEquals(jsonGame.getWinner().getPlayerId(), Integer.parseInt(winner));
		}
		
		assertThat(jsonGame.getGameType(), is(GameType.valueOf(type)));
		assertThat(jsonGame.getAnte(), is(Integer.parseInt(ante)));
	}

	@And("^The json response should contain a winner$")
	public void theJsonResponseBodyShouldBeANewHumanPlayerWithAvatarAlias() throws Throwable {
		
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//JSON string to Object
		Player jsonPlayer = mapper.readValue(latestResponse.getBody(), Player.class);
		StepDefsGames.latestPlayerID  = String.valueOf(jsonPlayer.getPlayerId());

	}
}