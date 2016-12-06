package nl.knikit.cardgames.definitions;

import com.fasterxml.jackson.databind.ObjectMapper;

import nl.knikit.cardgames.DTO.PlayerDto;
import nl.knikit.cardgames.model.Avatar;
import nl.knikit.cardgames.model.Player;

import org.springframework.http.HttpStatus;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class StepDefsPlayers extends SpringIntegrationTest {
	
	private static String latestPlayerID = "";
	
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
		executeGet("http://localhost:8383/api/players/" + playerId);
	}
	
	@Given("^I try to get a player with invalid \"([^\"]*)\"$")
	public void iTryToGetAPlayerInvalidWith(String playerId) throws Throwable {
		if (playerId.equals("latest")) {
			playerId = StepDefsPlayers.latestPlayerID;
		}
		executeGet("http://localhost:8383/api/players/" + playerId);
	}
	
	@Given("^I try to post a human \"([^\"]*)\" player having \"([^\"]*)\" and \"([^\"]*)\"$")
	public void iTryToPostANewHumanPlayerWithAvatarAlias(String human, String avatar, String alias) throws Throwable {
		
		PlayerDto postPlayer = new PlayerDto();
		postPlayer.setHuman(Boolean.parseBoolean(human));
		postPlayer.setAvatar(Avatar.valueOf(avatar));
		postPlayer.setAlias(alias);
		
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//Object to JSON in String
		String jsonInString = mapper.writeValueAsString(postPlayer);
		executePost("http://localhost:8383/api/players", jsonInString);
		
	}
	
	@Given("^I try to put a player with \"([^\"]*)\" having human \"([^\"]*)\" avatar \"([^\"]*)\" and alias \"([^\"]*)\"$")
	public void iTryToPutANewHumanPlayerWithAvatarAlias(String playerId, String human, String avatar, String alias) throws Throwable {
		if (playerId.equals("latest")) {
			playerId = StepDefsPlayers.latestPlayerID;
		}
		PlayerDto postPlayer = new PlayerDto();
		postPlayer.setPlayerId(Integer.parseInt(playerId));
		postPlayer.setHuman(Boolean.parseBoolean(human));
		postPlayer.setAvatar(Avatar.valueOf(avatar));
		postPlayer.setAlias(alias);
		
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//Object to JSON in String
		String jsonInString = mapper.writeValueAsString(postPlayer);
		executePut("http://localhost:8383/api/players/" + playerId, jsonInString);
		
	}
	
	@Given("^I try to delete a player with \"([^\"]*)\"$")
	public void iTryToDeleteAPlayerWith(String playerId) throws Throwable {
		if (playerId.equals("latest")) {
			playerId = StepDefsPlayers.latestPlayerID;
		}
		executeDelete("http://localhost:8383/api/players/" + playerId, null);
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
	
	@And("^The json response should contain human \"([^\"]*)\" player having \"([^\"]*)\" and \"([^\"]*)\"$")
	public void theJsonResponseBodyShouldBeANewHumanPlayerWithAvatarAlias(String human, String avatar, String alias) throws Throwable {
		
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//JSON string to Object
		PlayerDto jsonPlayer = mapper.readValue(latestResponse.getBody(), PlayerDto.class);
		StepDefsPlayers.latestPlayerID = String.valueOf(jsonPlayer.getPlayerId());
		
		assertThat(jsonPlayer.getHuman(), is(human));
		assertThat(jsonPlayer.getAvatar(), is(avatar));
		assertThat(jsonPlayer.getAlias(), is(alias));
	}
}