package nl.knikit.cardgames.definitions;

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
		executeGet("http://localhost:8383/api/players/"+playerId);
		throw new PendingException();
	}
	
	@Then("^I should see that the response has \"([^\"]*)\"$")
	public void iShouldSeeThatTheResponseHas(int HTTPstatusCode) throws Throwable {
		final HttpStatus currentStatusCode = latestResponse.getTheResponse().getStatusCode();
		assertThat("status code is incorrect : " + latestResponse.getBody(), currentStatusCode.value(), is(HTTPstatusCode));
		throw new PendingException();
	}
	
	@And("^The json response body should be like (.+)$")
	public void theJsonResponseBodyShouldBeLike(String body) throws Throwable {
		assertThat(latestResponse.getBody(), is(body));
		throw new PendingException();
	}
	
    @When("^the client posts /players/1234567890$")
    public void the_client_issues_POST_hello() throws Throwable {
        executePost("http://localhost:8383/players/1234567890");
    }
	
}