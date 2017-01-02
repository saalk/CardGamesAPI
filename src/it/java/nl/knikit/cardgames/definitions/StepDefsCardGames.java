package nl.knikit.cardgames.definitions;

import com.fasterxml.jackson.databind.ObjectMapper;

import nl.knikit.cardgames.VO.CardGame;

import java.util.HashMap;
import java.util.Map;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class StepDefsCardGames extends SpringIntegrationTest {
	
	// API          HTTP
	//
	// UPDATE,PUT   OK(200, "OK"),
	// POST         CREATED(201, "Created"),
	// DELETE       NO_CONTENT(204, "No Content"),
	
	// no body      BAD_REQUEST(400, "Bad Request"),
	// wrong id     NOT_FOUND(404, "Not Found"),
	
	@Given("^I try to get a cardGame with id \"([^\"]*)\"$")
	public void iTryToGetACardGameWithValid(String cardGameId) throws Throwable {
		if (cardGameId.equals("latest")) {
			cardGameId = latestCardGamesID;
		}
		executeGet(baseCardGamesUrl + cardGameId);
	}
		
	// INIT NEW or for existing PLAYER
	@Given("^I try to init a gameType \"([^\"]*)\" cardGame with playerId \"([^\"]*)\" and ante \"([^\"]*)\"$")
	public void iTryToPostANewTypeCardGameWithAnte(String gameType, String playerId, String ante) throws Throwable {


		//executePost(cardGamesUrl + "init" + humanPathId + "?gameType=" + gameType + "&ante=" + ante, "{}");
		
		//POST   api/cardgames/init           ?gameType/ante
		// or
		//POST   api/cardgames/init/human/2   ?gameType/ante
		
		String url = baseCardGamesUrl + "/init"; // base is without the / at the end !
		
		// Uri (URL) parameters
		Map<String, String> uriParams = new HashMap<>();
		if (!playerId.isEmpty() || playerId=="0") {
			if (playerId.equals("latest")) {
				playerId = latestPlayersID;
				url += "/human/{playerId}";
				uriParams.put("playerId", playerId);
			} else {
				url += "/human/{playerId}";
				uriParams.put("playerId", playerId);
			}
		}
		
		// Query parameters
		Map<String, String>  queryParams = new HashMap<>();
		queryParams.put("gameType", gameType);
		queryParams.put("ante", ante);
		
		// body cannot be null since there is a put that wants a request body
		executePostWithUriAndQueryParam(url, uriParams, "{}", queryParams);
		
	}
	
	// INIT CHANGES
	@Given("^I try to init changes to a cardGame with id \"([^\"]*)\" having gameType \"([^\"]*)\" and ante \"([^\"]*)\"$")
	public void iTryToPutANewTypeCardGameWithAnte(String cardGameId, String gameType, String ante) throws Throwable {
		
		//PUT    api/cardgames/1/init         ?gameType/ante
		
		if (cardGameId.equals("latest")) {
			cardGameId = latestCardGamesID;
		}
		
		// Uri (URL) parameters
		Map<String, String> uriParams = new HashMap<>();
		uriParams.put("id", cardGameId);
		
		// Query parameters
		Map<String, String>  queryParams = new HashMap<>();
		queryParams.put("gameType", gameType);
		queryParams.put("ante", ante);
		
		// body cannot be null since there is a put that wants a request body
		executePutWithUriAndQueryParam(cardGamesUrlWithId + "/init", uriParams, "{}", queryParams);
		
	}
	
	// SETUP NEW player
	@Given("^I try to setup a human \"([^\"]*)\" player for cardGame with id \"([^\"]*)\" having \"([^\"]*)\" and \"([^\"]*)\" and \"([^\"]*)\" and \"([^\"]*)\"$")
	public void iTryToPostAPlayerToACardGamee(String human, String cardGameId, String alias, String avatar, String securedLoan, String aiLevel) throws Throwable {
		
		//POST    api/cardgames/1/setup/human ?alias/avatar/securedLoan
		//POST    api/cardgames/1/setup/ai    ?alias/avatar/securedLoan/aiLevel
		
		if (cardGameId.equals("latest")) {
			cardGameId = latestCardGamesID;
		}
		
		// Uri (URL) parameters
		Map<String, String> uriParams = new HashMap<>();
		uriParams.put("id", cardGameId);
		uriParams.put("human", human);
		
		// Query parameters
		Map<String, String>  queryParams = new HashMap<>();
		queryParams.put("alias", alias);
		queryParams.put("avatar", avatar);
		queryParams.put("securedLoan", securedLoan);
		queryParams.put("aiLevel", aiLevel);
		
		// body cannot be null since there is a put that wants a request body
		executePostWithUriAndQueryParam(cardGamesUrlWithId + "/setup/{human}?", uriParams, "{}", queryParams);
		
	}
	
	
	// SETUP CHANGES
	@Given("^I try to setup changes to a cardGame with id \"([^\"]*)\" having \"([^\"]*)\" player with \"([^\"]*)\" and \"([^\"]*)\" and \"([^\"]*)\" and \"([^\"]*)\"$")
	public void iTryToPutAPlayerToACardGame(String cardGameId, String playerId, String alias, String avatar, String securedLoan, String aiLevel) throws Throwable {
		
		//PUT    api/cardgames/1/setup/2         ?alias/avatar/securedLoan/aiLevel
		
		if (cardGameId.equals("latest")) {
			cardGameId = latestCardGamesID;
		}
		
		// Uri (URL) parameters
		Map<String, String> uriParams = new HashMap<>();
		uriParams.put("id", cardGameId);
		uriParams.put("playerId", playerId);
		
		// Query parameters
		Map<String, String>  queryParams = new HashMap<>();
		queryParams.put("alias", alias);
		queryParams.put("avatar", avatar);
		queryParams.put("securedLoan", securedLoan);
		queryParams.put("aiLevel", aiLevel);
		
		// body cannot be null since there is a put that wants a request body
		executePutWithUriAndQueryParam("http://localhost:8383/api/cardgames/"+ cardGameId + "/setup/{playerId}?", uriParams, "{}", queryParams);
		
	}
	
	
	// SHUFFLE
	@Given("^I try to shuffle a cardGame with id \"([^\"]*)\"$")
	public void iTryToPostAShuffleToACardGame(String cardGameId) throws Throwable {
		
		//POST    api/cardgames/1/shuffle
		
		if (cardGameId.equals("latest")) {
			cardGameId = latestCardGamesID;
		}
		
		// Uri (URL) parameters
		Map<String, String> uriParams = new HashMap<>();
		uriParams.put("id", cardGameId);
		
		// Query parameters
		Map<String, String>  queryParams = new HashMap<>();

		
		// body cannot be null since there is a put that wants a request body
		executePostWithUriAndQueryParam(cardGamesUrlWithId + "/shuffle", uriParams, "{}", queryParams);
		
	}
	
	// TURN a action
	@Given("^I try to make a turn with \"([^\"]*)\" action for player with id \"([^\"]*)\" in a cardGame with id \"([^\"]*)\"$")
	public void iTryToPutATurnForAPlayerInACardGame(String action, String playerId, String cardGameId) throws Throwable {
		
		//PUT    api/cardgames/1/setup/2         ?alias/avatar/securedLoan/aiLevel
		
		if (cardGameId.equals("latest")) {
			cardGameId = latestCardGamesID;
		}
		
		// Uri (URL) parameters
		Map<String, String> uriParams = new HashMap<>();
		uriParams.put("id", cardGameId);
		uriParams.put("playerId", playerId);
		
		// Query parameters
		Map<String, String>  queryParams = new HashMap<>();
		queryParams.put("action", action);
		
		// body cannot be null since there is a put that wants a request body
		executePutWithUriAndQueryParam(cardGamesUrlWithId + "/turn/{playerId}?", uriParams, "{}", queryParams);
		
	}
	
	// TURN a action
	@Given("^I try to make a autoturn with \"([^\"]*)\" action for player with id \"([^\"]*)\" in a cardGame with id \"([^\"]*)\"$")
	public void iTryToPutAAutoturnForAAiPlayerInACardGame(String action, String playerId, String cardGameId) throws Throwable {
		
		//PUT    api/cardgames/1/setup/2         ?alias/avatar/securedLoan/aiLevel
		
		if (cardGameId.equals("latest")) {
			cardGameId = latestCardGamesID;
		}
		
		// Uri (URL) parameters
		Map<String, String> uriParams = new HashMap<>();
		uriParams.put("id", cardGameId);
		uriParams.put("playerId", playerId);
		
		// Query parameters
		Map<String, String>  queryParams = new HashMap<>();
		queryParams.put("action", action);
		
		// body cannot be null since there is a put that wants a request body
		executePutWithUriAndQueryParam(cardGamesUrlWithId + "/autoturn/{playerId}?", uriParams, "{}", queryParams);
		
	}
	
	// DELETE a player in a cardGame
	@Given("^I try to delete a player with \"([^\"]*)\" for a cardGame with id \"([^\"]*)\"$")
	public void iTryToDeleteAPlayerInACardGame(String playerId, String cardGameId) throws Throwable {
		
		//PUT    api/cardgames/1/setup/2         ?alias/avatar/securedLoan/aiLevel
		
		if (cardGameId.equals("latest")) {
			cardGameId = latestCardGamesID;
		}
		
		// body cannot be null since there is a put that wants a request body
		executeDelete( "http://localhost:8383/api/cardgames/" + cardGameId + "/setup/" + playerId, null);
		
	}
	
	@Given("^I try to delete a cardGame \"([^\"]*)\"$")
	public void iTryToDeleteACardGameWith(String cardGameId) throws Throwable {
		if (cardGameId.equals("latest")) {
			cardGameId = latestCardGamesID;
		}
		
		if (!latestCardGamesIDs.isEmpty()) {
			latestCardGamesIDs.remove(latestCardGamesIDs.size() - 1);
		}
		executeDelete(baseCardGamesUrl + "/" + cardGameId, null);
	}
	
	//  And The json response should contain gameType "<gameType>" cardGame having human "<human>" and ante "<ante>" and state "<state>"
	@And("^The json response should contain gameType \"([^\"]*)\" cardGame having human \"([^\"]*)\" and ante \"([^\"]*)\" and state \"([^\"]*)\"$")
	public void theJsonResponseBodyShouldBeANewTypeCardGameHavingHumanAnteState(String gameType, String human, String ante, String state) throws Throwable {
		
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//JSON string to Object
		CardGame jsonGame = mapper.readValue(latestResponse.getBody(), CardGame.class);
		latestCardGamesID = String.valueOf(jsonGame.getGameId());
		// do not set the player here, the player has been set before when making the player
		
		assertThat(jsonGame.getGameType(), is(gameType));
		assertThat(jsonGame.getAnte(), is(Integer.parseInt(ante)));
		assertThat(jsonGame.getState(), is(state));
		
		
		if (human.equals("latest")) {
			human = latestPlayersID;
		}
		
		if (!human.isEmpty()) {
			assertThat(jsonGame.getWinner().getPlayerId(), is(Integer.parseInt(human)));
		}
	}
	
	@And("^The json response should contain a cardGame with response \"([^\"]*)\"$")
	public void theJsonResponseBodyShouldBeACardGame(String response) throws Throwable {
		
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//JSON string to Object
		String jsonString = latestResponse.getBody();
		assertThat(jsonString, is(response));
		
	}
	
}