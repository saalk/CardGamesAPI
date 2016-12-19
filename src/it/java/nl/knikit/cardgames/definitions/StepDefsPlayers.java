package nl.knikit.cardgames.definitions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nl.knikit.cardgames.DTO.PlayerDto;
import nl.knikit.cardgames.model.AiLevel;
import nl.knikit.cardgames.model.Avatar;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;

public class StepDefsPlayers extends SpringIntegrationTest {
	
	private static String latestPlayerID = "";
	private static List<String> latestPlayerIDs = new ArrayList<>();
	
	private static String playersUrl = "http://localhost:8383/api/players/";
	private static String allPlayersUrl = "http://localhost:8383/api/players";
	private static String playersUrlWithId = "http://localhost:8383/api/players/{id}";
	
	// API          HTTP
	//
	// UPDATE,PUT   OK(200, "OK"),
	// POST         CREATED(201, "Created"),
	// DELETE       NO_CONTENT(204, "No Content"),
	
	// no body      BAD_REQUEST(400, "Bad Request"),
	// wrong id     NOT_FOUND(404, "Not Found"),
	
	@Given("^I try to get a player with valid \"([^\"]*)\"$")
	public void iTryToGetAPlayerValidWith(String playerId) throws Throwable {
		if (playerId.equals("latest")) {
			playerId = StepDefsPlayers.latestPlayerID;
		}
		executeGet(playersUrl + playerId);
	}
	
	@Given("^I try to get a player with invalid \"([^\"]*)\"$")
	public void iTryToGetAPlayerInvalidWith(String playerId) throws Throwable {
		if (playerId.equals("latest")) {
			playerId = StepDefsPlayers.latestPlayerID;
		}
		executeGet(playersUrl + playerId);
	}
	
	@Given("^I try to get all human \"([^\"]*)\" players$")
	public void iTryToGetAllHumanOrAlienPlayers(String human) throws Throwable {

		executeGet(allPlayersUrl + "?human=" + human);
	}
	
	@Given("^I try to get all players")
	public void iTryToGetAllPlayers() throws Throwable {

		executeGet(allPlayersUrl);
	}
	
	@Given("^I try to post a human \"([^\"]*)\" player having \"([^\"]*)\" and \"([^\"]*)\" and \"([^\"]*)\"$")
	public void iTryToPostANewHumanPlayerWithAvatarAlias(String human, String avatar, String alias, String aiLevel) throws Throwable {
		
		PlayerDto postPlayer = new PlayerDto();
		postPlayer.setHuman(Boolean.parseBoolean(human));
		postPlayer.setAvatar(Avatar.valueOf(avatar));
		postPlayer.setAlias(alias);
		postPlayer.setAiLevel(AiLevel.valueOf(aiLevel));
		
		
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//Object to JSON in String
		String jsonInString = mapper.writeValueAsString(postPlayer);
		executePost(playersUrl, jsonInString);
		
	}
	
	@Given("^I try to put a player with \"([^\"]*)\" having human \"([^\"]*)\" avatar \"([^\"]*)\" and alias \"([^\"]*)\" and \"([^\"]*)\"$")
	public void iTryToPutANewHumanPlayerWithAvatarAlias(String playerId, String human, String avatar, String alias, String aiLevel) throws Throwable {
		if (playerId.equals("latest")) {
			playerId = StepDefsPlayers.latestPlayerID;
		}
		PlayerDto postPlayer = new PlayerDto();
		postPlayer.setPlayerId(Integer.parseInt(playerId));
		postPlayer.setHuman(Boolean.parseBoolean(human));
		postPlayer.setAvatar(Avatar.valueOf(avatar));
		postPlayer.setAiLevel(AiLevel.valueOf(aiLevel));
		
		postPlayer.setAlias(alias);
		
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//Object to JSON in String
		String jsonInString = mapper.writeValueAsString(postPlayer);
		executePut(playersUrl + playerId, jsonInString);
		
	}
	
	@Given("^I try to delete a player with \"([^\"]*)\"$")
	public void iTryToDeleteAPlayerWith(String playerId) throws Throwable {
		if (playerId.equals("latest")) {
			playerId = StepDefsPlayers.latestPlayerID;
			StepDefsPlayers.latestPlayerIDs.remove(latestPlayerIDs.size()-1);
		}
		executeDelete(playersUrl + playerId, null);
	}
	
	@Given("^I try to delete all players with \"([^\"]*)\"$")
	public void iTryToDeleteAllPlayersWith(String ids) throws Throwable {
		if (ids.equals("all")) {
			// all
		}
		executeDelete(allPlayersUrl + "?id=" + StringUtils.join(latestPlayerIDs,','), null);
	}
	
	@Then("^I should see that the response has HTTP status \"([^\"]*)\"$")
	public void iShouldSeeThatTheResponseHas(int HTTPstatusCode) throws Throwable {
		final HttpStatus currentStatusCode = latestResponse.getTheResponse().getStatusCode();
		
		assertThat("status code is incorrect : " + latestResponse.getBody(), currentStatusCode.value(), is(HTTPstatusCode));
	}
	
	@And("^The json response body should be like (.+)$")
	public void theJsonResponseBodyShouldBeLike(String body) throws Throwable {
		assertThat(latestResponse.getBody(), is(body));
	}
	
	@And("^The json response body should have no content$")
	public void theJsonResponseBodyShouldBeLike() throws Throwable {
		assertThat(latestResponse.getBody(), is(""));
	}
	
	@And("^The json response should contain at least \"([^\"]*)\" players$")
	public void theJsonPlayerResponseBodyShouldContainAtLeast(int count) throws Throwable {
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//JSON string to Object
		List<PlayerDto> jsonPlayers = mapper.readValue(latestResponse.getBody(),new TypeReference<List<PlayerDto>>(){});
		
		latestPlayerIDs.clear();
		for (PlayerDto playerDto : jsonPlayers ) {
			latestPlayerIDs.add(String.valueOf(playerDto.getPlayerId()));
			StepDefsPlayers.latestPlayerID = String.valueOf(playerDto.getPlayerId());
		}
		// at least equal but more can exist
		assertTrue(latestPlayerIDs.size()>=count);
	}
	
	@And("^The json response should contain human \"([^\"]*)\" player having \"([^\"]*)\" and \"([^\"]*)\" and \"([^\"]*)\"$")
	public void theJsonResponseBodyShouldBeANewHumanPlayerWithAvatarAlias(String human, String avatar, String alias, String aiLevel) throws Throwable {
		
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//JSON string to Object
		PlayerDto jsonPlayer = mapper.readValue(latestResponse.getBody(), PlayerDto.class);
		StepDefsPlayers.latestPlayerID = String.valueOf(jsonPlayer.getPlayerId());
		
		assertThat(jsonPlayer.getHuman(), is(human));
		assertThat(jsonPlayer.getAvatar(), is(avatar));
		assertThat(jsonPlayer.getAlias(), is(alias));
		assertThat(jsonPlayer.getAiLevel(), is(aiLevel));
		
	}
}