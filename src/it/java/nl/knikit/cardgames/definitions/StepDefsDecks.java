package nl.knikit.cardgames.definitions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nl.knikit.cardgames.model.Avatar;
import nl.knikit.cardgames.model.Card;
import nl.knikit.cardgames.model.Deck;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.model.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

public class StepDefsDecks extends SpringIntegrationTest {
	
	private static String latestGameID = "";
	private static String latestDeckID = "";
	private static String latestPlayerID = "";
	
	private static Map<String, String> latestDeckIDs = new HashMap<>(); // key value
	
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
		SpringIntegrationTest.latestResponse = null;
		executeGet("http://localhost:8383/api/decks/" + deckId);
	}
	
	@Given("^I try to get a deck with invalid \"([^\"]*)\"$")
	public void iTryToGetADeckWithInvalid(String deckId) throws Throwable {
		if (deckId.equals("latest")) {
			deckId = StepDefsDecks.latestDeckID;
		}
		SpringIntegrationTest.latestResponse = null;
		executeGet("http://localhost:8383/api/decks/" + deckId);
	}
	
	@Given("^I try to post a new deck with shuffle \"([^\"]*)\" for game \"([^\"]*)\"$")
	public void iTryToPostANewTypeDeckWithDealtToAndCardOrder(boolean shuffle, String gameId) throws Throwable {
		
		
		Deck postDeck = new Deck();
		
		if (gameId.equals("latest")) {
			gameId = StepDefsDecks.latestGameID;
		}
		
		if (!gameId.isEmpty()) {
			Game postGame = new Game();
			postGame.setGameId(Integer.parseInt(gameId));
			postDeck.setGame(postGame);
		} else {
			postDeck.setGame(null);
		}
		
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//Object to JSON in String
		String jsonInString = mapper.writeValueAsString(postDeck);
		SpringIntegrationTest.latestResponse = null;
		executePost("http://localhost:8383/api/decks?shuffle=" + shuffle, jsonInString);
		
	}
	
	@Given("^I try to post a human \"([^\"]*)\" dealtTo having \"([^\"]*)\" and \"([^\"]*)\"$")
	public void iTryToPostANewHumanDealtToWithAvatarAlias(String human, String avatar, String alias) throws Throwable {
		
		Player postPlayer = new Player();
		postPlayer.setHuman(Boolean.parseBoolean(human));
		postPlayer.setAvatar(Avatar.valueOf(avatar));
		postPlayer.setAlias(alias);
		
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//Object to JSON in String
		String jsonInString = mapper.writeValueAsString(postPlayer);
		SpringIntegrationTest.latestResponse = null;
		executePost("http://localhost:8383/api/players", jsonInString);
		
	}
	
	@Given("^I try to put a deck with \"([^\"]*)\" having dealtTo \"([^\"]*)\"$")
	public void iTryToPutAnExistingDeckWithDealtTo(String deckId, String dealtTo) throws Throwable {
		if (deckId.equals("latest")) {
			deckId = StepDefsDecks.latestDeckID;
		}
		if (dealtTo.equals("latest")) {
			dealtTo = StepDefsDecks.latestPlayerID;
		}
		SpringIntegrationTest.latestResponse = null;
		executePut("http://localhost:8383/api/decks/" + deckId + "?dealTo=" + dealtTo, null);
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
		SpringIntegrationTest.latestResponse = null;
		executeDelete("http://localhost:8383/api/players/" + dealtTo, null);
	}
	
	@Given("^I try to delete a game for a deck with \"([^\"]*)\"$")
	public void iTryToDeleteAGameWith(String gameId) throws Throwable {
		if (gameId.equals("latest")) {
			gameId = StepDefsDecks.latestGameID;
		}
		SpringIntegrationTest.latestResponse = null;
		executeDelete("http://localhost:8383/api/games/" + gameId, null);
	}
	
	@And("^The json response should contain card \"([^\"]*)\" deck with shuffle \"([^\"]*)\" having \"([^\"]*)\" and \"([^\"]*)\" for game \"([^\"]*)\"$")
	public void theJsonResponseBodyShouldBeANewTypeDeckWithDealtToAndCardOrder(String card, boolean shuffle, String dealtTo, String cardOrder, String gameId) throws Throwable {
		
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//JSON string to Object
		//Deck jsonDeck = mapper.readValue(latestResponse.getBody(), Deck.class);
		List<Deck> jsonDecks = mapper.readValue(latestResponse.getBody(), new TypeReference<List<Deck>>() {
		});
		
		StepDefsDecks.latestDeckID = String.valueOf(jsonDecks.get(jsonDecks.size()-1).getDeckId());
		
		if (gameId.equals("latest")) {
			gameId = StepDefsDecks.latestGameID;
		}
		
		if (dealtTo.equals("latest")) {
			dealtTo = StepDefsDecks.latestPlayerID;
		}
		
		StepDefsDecks.latestDeckIDs.clear();
		for (Deck deck : jsonDecks) {
			
			StepDefsDecks.latestDeckIDs.put(String.valueOf(deck.getDeckId()), deck.getCard().getCardId());
			// expected , actual
			assertEquals(Integer.parseInt(gameId), deck.getGame().getGameId());
			
			if (!card.isEmpty()) {
				if (deck.getCard().getCardId() == new Card(card).getCardId()) {
					// assert if the input card has the input dealtTo and cardOder
					if (!dealtTo.isEmpty()) {
						assertEquals(Integer.parseInt(dealtTo),deck.getDealtTo().getPlayerId());
					}
					if (!cardOrder.isEmpty()) {
						// actual, matchers
						assertThat(deck.getCardOrder(), is(Integer.parseInt(cardOrder)));
					}
				}
			}
		}
		// AC is the first card when not shuffled
		assertEquals(shuffle,(jsonDecks.get(0).getCard().getCardId()!="AC"));
	}
		 
	@And("^The json response should contain a dealtTo$")
	public void theJsonResponseBodyShouldBeANewHumanDealtToWithAvatarAlias() throws Throwable {
		
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//JSON string to Object
		Player jsonPlayer = mapper.readValue(latestResponse.getBody(), Player.class);
		StepDefsDecks.latestPlayerID = String.valueOf(jsonPlayer.getPlayerId());
		
	}
	
	@And("^The json response should contain a game for a deck$")
	public void theJsonResponseBodyShouldBeANewGameForADeck() throws Throwable {
		
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//JSON string to Object
		Game jsonGame = mapper.readValue(latestResponse.getBody(), Game.class);
		StepDefsDecks.latestGameID = String.valueOf(jsonGame.getGameId());
		
	}
}