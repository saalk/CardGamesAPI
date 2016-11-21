package nl.knikit.cardgames.definitions;

import com.fasterxml.jackson.databind.ObjectMapper;

import nl.knikit.cardgames.model.Avatar;
import nl.knikit.cardgames.model.CardGameType;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.model.Player;

import org.springframework.http.HttpStatus;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class GamesStepDefs extends SpringIntegrationTest {
	
	private static String latestGameID = "";
	private static String latestPlayerID = "";
	
	@Given("^I try to get a game with valid \"([^\"]*)\"$")
	public void iTryToGetAGameValidWith(String gameId) throws Throwable {
		if (gameId.equals("latest")) {
			gameId = GamesStepDefs.latestGameID;
		}
		executeGet("http://localhost:8383/api/games/" + gameId);
	}
	
	@Given("^I try to get a game with invalid \"([^\"]*)\"$")
	public void iTryToGetAGameInvalidWith(String gameId) throws Throwable {
		if (gameId.equals("latest")) {
			gameId = GamesStepDefs.latestGameID;
		}
		executeGet("http://localhost:8383/api/games/" + gameId);
	}
	
	@Given("^I try to post a cardGameType \"([^\"]*)\" game having \"([^\"]*)\" and \"([^\"]*)\"$")
	public void iTryToPostANewHumanGameWithAvatarAlias(String cardGameType, String winner, String ante) throws Throwable {
		
		Player postPlayer = new Player();
		postPlayer.setPlayerId(Integer.parseInt(winner));
		
		Game postGame = new Game();
		postGame.setWinner(postPlayer);
		postGame.setCardGameType(CardGameType.valueOf(cardGameType));
		postGame.setAnte(Integer.parseInt(ante));
		
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//Object to JSON in String
		String jsonInString = mapper.writeValueAsString(postGame);
		executePost("http://localhost:8383/api/games", jsonInString);
		
	}
	@Given("^I try to put a game with \"([^\"]*)\" having cardGameType \"([^\"]*)\" winner \"([^\"]*)\" and ante \"([^\"]*)\"$")
	public void iTryToPutANewHumanGameWithAvatarAlias(String gameId, String cardGameType, String winner, String ante) throws Throwable {
		if (gameId.equals("latest")) {
			gameId = GamesStepDefs.latestGameID;
		}
		if (winner.equals("latest")) {
			winner = GamesStepDefs.latestGameID;
		}
		Player postPlayer = new Player();
		postPlayer.setPlayerId(Integer.parseInt(winner));
		
		Game postGame = new Game();
		postGame.setWinner(postPlayer);
		postGame.setCardGameType(CardGameType.valueOf(cardGameType));
		postGame.setAnte(Integer.parseInt(ante));
		
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//Object to JSON in String
		String jsonInString = mapper.writeValueAsString(postGame);
		executePut("http://localhost:8383/api/games/" + gameId, jsonInString);
		
	}
	
	@Given("^I try to delete a game with \"([^\"]*)\"$")
	public void iTryToDeleteAGameWith(String gameId) throws Throwable {
		if (gameId.equals("latest")) {
			gameId = GamesStepDefs.latestGameID;
		}
		executeDelete("http://localhost:8383/api/games/" + gameId, null);
	}
	
	@And("^The json response should contain cardGameType \"([^\"]*)\" game having \"([^\"]*)\" and \"([^\"]*)\"$")
	public void theJsonResponseBodyShouldBeANewHumanGameWithAvatarAlias(String cardGameType, String winner, String ante) throws Throwable {
		
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//JSON string to Object
		Game jsonGame = mapper.readValue(latestResponse.getBody(), Game.class);
		GamesStepDefs.latestGameID = String.valueOf(jsonGame.getGameId());
		GamesStepDefs.latestPlayerID = String.valueOf(jsonGame.getWinner());
		
		Player postPlayer = new Player();
		postPlayer.setPlayerId(Integer.parseInt(winner));
		
		assertThat(jsonGame.getWinner(), is(postPlayer.getPlayerId()));
		assertThat(jsonGame.getCardGameType(), is(CardGameType.valueOf(cardGameType)));
		assertThat(jsonGame.getAnte(), is(Integer.parseInt(ante)));
	}
}