package nl.knikit.cardgames.definitions.definitions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nl.knikit.cardgames.DTO.PlayerDto;
import nl.knikit.cardgames.definitions.commons.SpringIntegrationTest;
import nl.knikit.cardgames.model.AiLevel;
import nl.knikit.cardgames.model.Avatar;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import java.util.List;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;

public class StepDefsPlayers extends SpringIntegrationTest {
	
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
			playerId = latestPlayersID;
		}
		executeGet(playersUrl + playerId);
	}
	
	@Given("^I try to get a player with invalid \"([^\"]*)\"$")
	public void iTryToGetAPlayerInvalidWith(String playerId) throws Throwable {
		if (playerId.equals("latest")) {
			playerId = StepDefsPlayers.latestPlayersID;
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
			playerId = StepDefsPlayers.latestPlayersID;
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
	
	@Given("^I try to delete a player \"([^\"]*)\"$")
	public void iTryToDeleteThePlayerForTheCasino(String player) throws Throwable {
		if (player.equals("latest")) {
			player = latestPlayersID;
		} else if (player.equals("latest-1")) {
			player = latestPlayersID2;
		}
		
		if (!latestPlayersIDs.isEmpty()) {
			latestPlayersIDs.remove(latestPlayersIDs.size() - 1);
		}
		
		executeDelete(playersUrl + player, null);
	}
	
	@Given("^I try to delete all players with \"([^\"]*)\"$")
	public void iTryToDeleteAllPlayersWith(String ids) throws Throwable {
		if (ids.equals("all")) {
			// all
		}
		executeDelete(allPlayersUrl + "?id=" + StringUtils.join(latestPlayersIDs, ','), null);
	}
	
	@Then("^I should see that the response has HTTP status \"([^\"]*)\"$")
	public void iShouldSeeThatTheResponseHas(int HTTPstatusCode) throws Throwable {
		final HttpStatus currentStatusCode = latestResponse.getTheResponse().getStatusCode();
		
		// assertThat is introduces with Junit 4.4 and is more readable
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
		List<PlayerDto> jsonPlayers = mapper.readValue(latestResponse.getBody(), new TypeReference<List<PlayerDto>>() {
		});
		
		latestPlayersIDs.clear();
		for (PlayerDto playerDto : jsonPlayers) {
			latestPlayersIDs.add(String.valueOf(playerDto.getPlayerId()));
			StepDefsPlayers.latestPlayersID = String.valueOf(playerDto.getPlayerId());
		}
		// at least equal but more can exist actual, expected
		//assertTrue(latestPlayersIDs.size()>=count);
		assertThat(latestPlayersIDs.size(), greaterThanOrEqualTo(count));
	}
	
	@And("^The json response should contain human \"([^\"]*)\" player having \"([^\"]*)\" and \"([^\"]*)\" and \"([^\"]*)\"$")
	public void theJsonResponseBodyShouldBeANewHumanPlayerWithAvatarAlias(String human, String avatar, String alias, String aiLevel) throws Throwable {
		
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//JSON string to Object
		PlayerDto jsonPlayer = mapper.readValue(latestResponse.getBody(), PlayerDto.class);
		StepDefsPlayers.latestPlayersID = String.valueOf(jsonPlayer.getPlayerId());
		
		assertThat(jsonPlayer.getHuman(), is(human));
		assertThat(jsonPlayer.getAvatar(), is(avatar));
		assertThat(jsonPlayer.getAlias(), is(alias));
		assertThat(jsonPlayer.getAiLevel(), is(aiLevel));
		
	}
	
	@And("^The json response should contain a player$")
	public void theJsonResponseBodyShouldBeANewHumanPlayerWithAvatarAliasForACasino() throws Throwable {
		
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//JSON string to Object
		PlayerDto jsonPlayer = mapper.readValue(latestResponse.getBody(), PlayerDto.class);
		
		// move the latest to latest-1
		latestPlayersID2 = latestPlayersID;
		latestPlayersID = String.valueOf(jsonPlayer.getPlayerId());
	}
}