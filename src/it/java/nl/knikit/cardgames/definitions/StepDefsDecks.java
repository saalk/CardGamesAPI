package nl.knikit.cardgames.definitions;

import com.fasterxml.jackson.databind.ObjectMapper;

import nl.knikit.cardgames.model.Avatar;
import nl.knikit.cardgames.model.Card;
import nl.knikit.cardgames.model.Deck;
import nl.knikit.cardgames.model.Player;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

public class StepDefsDecks extends SpringIntegrationTest {
	
	private static String latestDeckID = "";
	private static String latestPlayerID = "";
	
	// API          HTTP
	//
	// UPDATE,PUT   OK(200, "OK"),
	// POST         CREATED(201, "Created"),
	// DELETE       NO_CONTENT(204, "No Content"),
	
	// no body      BAD_REQUEST(400, "Bad Request"),
	// wrong id     NOT_FOUND(404, "Not Found"),
	
	@Given("^I try to get a deck with valid \"([^\"]*)\"$")
	public void iTryToGetADeckWithValid(String deckId) throws Throwable {
		if (deckId.equals("latest")) {
			deckId = StepDefsDecks.latestDeckID;
		}
		executeGet("http://localhost:8383/api/decks/" + deckId);
	}
	
	@Given("^I try to get a deck with invalid \"([^\"]*)\"$")
	public void iTryToGetADeckWithInvalid(String deckId) throws Throwable {
		if (deckId.equals("latest")) {
			deckId = StepDefsDecks.latestDeckID;
		}
		executeGet("http://localhost:8383/api/decks/" + deckId);
	}
	
	@Given("^I try to post a card \"([^\"]*)\" deck having \"([^\"]*)\" and \"([^\"]*)\"$")
	public void iTryToPostANewTypeDeckWithDealtToAndCardOrder(String card, String dealtTo, String cardOrder) throws Throwable {
		
		
		Deck postDeck = new Deck();
		
		if (!dealtTo.isEmpty()) {
			Player postPlayer = new Player();
			postPlayer.setPlayerId(Integer.parseInt(dealtTo));
			postDeck.setDealtTo(postPlayer);
		}
		postDeck.setCardObj(new Card(card));
		postDeck.setCardOrder(Integer.parseInt(cardOrder));
		
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//Object to JSON in String
		String jsonInString = mapper.writeValueAsString(postDeck);
		executePost("http://localhost:8383/api/decks", jsonInString);
		
	}
	
	@Given("^I try to post a isHuman \"([^\"]*)\" dealtTo having \"([^\"]*)\" and \"([^\"]*)\"$")
	public void iTryToPostANewHumanDealtToWithAvatarAlias(String isHuman, String avatar, String alias) throws Throwable {
		
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
	
	@Given("^I try to put a deck with \"([^\"]*)\" having card \"([^\"]*)\" dealtTo \"([^\"]*)\" and cardOrder \"([^\"]*)\"$")
	public void iTryToPutANewTypeDeckWithDealtToAndCardOrder(String deckId, String card, String dealtTo, String cardOrder) throws Throwable {
		
		Deck postDeck = new Deck();
		if (deckId.equals("latest")) {
			deckId = StepDefsDecks.latestDeckID;
		}
		//TODO set the playerId also in the body or not ?
		
		postDeck.setCardObj(new Card(card));
		postDeck.setCardOrder(Integer.parseInt(cardOrder));
		
		if (!dealtTo.isEmpty()) {
			if (dealtTo.equals("latest")) {
				dealtTo = StepDefsDecks.latestDeckID;
			}
			Player postPlayer = new Player();
			postPlayer.setPlayerId(Integer.parseInt(dealtTo));
			postDeck.setDealtTo(postPlayer);
		}
		
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//Object to JSON in String
		String jsonInString = mapper.writeValueAsString(postDeck);
		executePut("http://localhost:8383/api/decks/" + deckId, jsonInString);
		
	}
	
	@Given("^I try to put a deck with \"([^\"]*)\" having dealtTo \"([^\"]*)\"$")
	public void iTryToPutAnExistingDeckWithDealtTo(String deckId, String dealtTo) throws Throwable {
		if (deckId.equals("latest")) {
			deckId = StepDefsDecks.latestDeckID;
		}
		if (dealtTo.equals("latest")) {
			dealtTo = StepDefsDecks.latestPlayerID;
		}

		executePut("http://localhost:8383/api/decks/" + deckId + "?dealtTo=" + dealtTo, null);
	}
	
	@Given("^I try to delete a deck with \"([^\"]*)\"$")
	public void iTryToDeleteADeckWith(String deckId) throws Throwable {
		if (deckId.equals("latest")) {
			deckId = StepDefsDecks.latestDeckID;
		}
		executeDelete("http://localhost:8383/api/decks/" + deckId, null);
	}
	
	@Given("^I try to delete the dealtTo \"([^\"]*)\"$")
	public void iTryToDeleteTheDealtTo(String dealtTo) throws Throwable {
		if (dealtTo.equals("latest")) {
			dealtTo = StepDefsDecks.latestPlayerID;
		}
		executeDelete("http://localhost:8383/api/players/" + dealtTo, null);
	}
		
	@And("^The json response should contain card \"([^\"]*)\" deck having \"([^\"]*)\" and \"([^\"]*)\"$")
	public void theJsonResponseBodyShouldBeANewTypeDeckWithDealtToAndCardOrder(String card, String dealtTo, String cardOrder) throws Throwable {
		
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//JSON string to Object
		Deck jsonDeck = mapper.readValue(latestResponse.getBody(), Deck.class);
		StepDefsDecks.latestDeckID = String.valueOf(jsonDeck.getDeckId());
		// do not set the dealtTo here, the dealtTo has been set before when making the player
		
		if (dealtTo.equals("latest")) {
			dealtTo = StepDefsDecks.latestPlayerID;
		}
		
		if (!dealtTo.isEmpty()) {
			assertEquals(jsonDeck.getDealtTo().getPlayerId(), Integer.parseInt(dealtTo));
		}
		
		assertEquals(jsonDeck.getCardObj(), null);
		assertThat(jsonDeck.getCardOrder(), is(Integer.parseInt(cardOrder)));
	}

	@And("^The json response should contain a dealtTo$")
	public void theJsonResponseBodyShouldBeANewHumanDealtToWithAvatarAlias() throws Throwable {
		
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//JSON string to Object
		Player jsonPlayer = mapper.readValue(latestResponse.getBody(), Player.class);
		StepDefsDecks.latestPlayerID  = String.valueOf(jsonPlayer.getPlayerId());

	}
}