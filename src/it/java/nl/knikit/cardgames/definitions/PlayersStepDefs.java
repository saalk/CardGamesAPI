package nl.knikit.cardgames.definitions;

import com.fasterxml.jackson.databind.ObjectMapper;

import nl.knikit.cardgames.model.Avatar;
import nl.knikit.cardgames.model.Player;

import org.springframework.http.HttpStatus;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PlayersStepDefs extends SpringIntegrationTest {
	
	@Given("^I try to get a player with \"([^\"]*)\"$")
	public void iTryToGetAPlayerWith(String playerId) throws Throwable {
		executeGet("http://localhost:8383/api/players/" + playerId);
	}
	
	@Then("^I should see that the response has \"([^\"]*)\"$")
	public void iShouldSeeThatTheResponseHas(int HTTPstatusCode) throws Throwable {
		final HttpStatus currentStatusCode = latestResponse.getTheResponse().getStatusCode();
		assertThat("status code is incorrect : " + latestResponse.getBody(), currentStatusCode.value(), is(HTTPstatusCode));
	}
	
	@And("^The json response body should be like (.+)$")
	public void theJsonResponseBodyShouldBeLike(String body) throws Throwable {
		assertThat(latestResponse.getBody(), is(body));
	}
	
	@And("^The json response body should be like: \"([^\"]*)\"$$")
	public void theJsonResponseBodyShouldBeLikeExtra(String body) throws Throwable {
		assertThat(latestResponse.getBody(), is(body));
	}
	
	@When("^I try to post a player with (.+)$")
	public void iTryToPostAPlayerWith(String body) throws Throwable {
		executePost("http://localhost:8383/players/1234567890", null);
	}
	
	@Given("^I try to post a \"([^\"]*)\" player having \"([^\"]*)\" and \"([^\"]*)\"$")
	public void iTryToPostANewHumanPlayerWithAvatarAlias(String isHuman, String avatar, String alias) throws Throwable {
		
		Player postPlayer = new Player();
		postPlayer.setHuman(Boolean.parseBoolean(isHuman));
		postPlayer.setAvatar(Avatar.fromAvatarName(avatar));
		postPlayer.setAlias(alias);
		
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//Object to JSON in String
		String jsonInString = mapper.writeValueAsString(postPlayer);

		/* example of a mapper
			{
			"@id":"1",
			"gameObjs": [],
			"human":true,
			"playerId":0,
			"created":"161117-02:40-52859",
			"avatar":null,
			"alias":
			"Cukes Doe",
			"isHuman":true,
			"aiLevel":null,
			"cubits":0,
			"securedLoan":0
			}
		*/
		executePost("http://localhost:8383/players", jsonInString);
	}
}