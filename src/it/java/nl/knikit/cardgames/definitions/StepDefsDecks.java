package nl.knikit.cardgames.definitions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nl.knikit.cardgames.DTO.DeckDto;
import nl.knikit.cardgames.DTO.GameDto;
import nl.knikit.cardgames.DTO.PlayerDto;
import nl.knikit.cardgames.model.AiLevel;
import nl.knikit.cardgames.model.Avatar;
import nl.knikit.cardgames.model.Card;
import nl.knikit.cardgames.model.Deck;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.model.Player;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StepDefsDecks extends SpringIntegrationTest {
	
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
			deckId = latestDecksID;
		}
		executeGet(decksUrl + deckId);
	}
	
	@Given("^I try to get a deck with invalid \"([^\"]*)\"$")
	public void iTryToGetADeckWithInvalid(String deckId) throws Throwable {
		if (deckId.equals("latest")) {
			deckId = latestDecksID;
		}
		executeGet(decksUrl + deckId);
	}
	
	@Given("^I try to get all decks$")
	public void iTryToGetAllDecks() throws Throwable {
		
		executeGet(allDecksUrl);
	}
	
	@Given("^I try to get all decks for game \"([^\"]*)\"$")
	public void iTryToGetAllDecksForAGame(String gameId) throws Throwable {
		
		if (gameId.equals("latest")) {
			gameId = StepDefsDecks.latestGamesID;
		}
		executeGet(allDecksUrl + "?game=" + gameId);
	}
	
	@Given("^I try to post a new deck with shuffle \"([^\"]*)\" for game \"([^\"]*)\"$")
	public void iTryToPostANewTypeDeckWithDealtToAndCardOrder(boolean shuffle, String gameId) throws Throwable {
		
		
		DeckDto postDeckDto = new DeckDto();
		
		if (gameId.equals("latest")) {
			gameId = StepDefsDecks.latestGamesID;
		}
		
		if (!gameId.isEmpty()) {
			GameDto postGameDto = new GameDto();
			postGameDto.setGameId(Integer.parseInt(gameId));
			postDeckDto.setGameDto(postGameDto);
		}
		
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//Object to JSON in String
		String jsonInString = mapper.writeValueAsString(postDeckDto);
		executePost(allDecksUrl + "?shuffle=" + shuffle, jsonInString);
		
	}
	
	@Given("^I try to post a human \"([^\"]*)\" dealtTo having \"([^\"]*)\" and \"([^\"]*)\" and \"([^\"]*)\"$")
	public void iTryToPostANewHumanDealtToWithAvatarAlias(String human, String avatar, String alias, String aiLevel) throws Throwable {
		
		PlayerDto postPlayerDto = new PlayerDto();
		postPlayerDto.setHuman(Boolean.parseBoolean(human));
		postPlayerDto.setAvatar(Avatar.valueOf(avatar));
		//postPlayerDto.setAiLevel(AiLevel.valueOf(aiLevel));
		postPlayerDto.setAlias(alias);
		
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//Object to JSON in String
		String jsonInString = mapper.writeValueAsString(postPlayerDto);
		// SpringIntegrationTest.latestResponse = null;
		executePost(playersUrl, jsonInString);
		
	}
	
	@Given("^I try to put a deck with \"([^\"]*)\" having dealtTo \"([^\"]*)\"$")
	public void iTryToPutAnExistingDeckWithDealtTo(String deckId, String dealtTo) throws Throwable {
		if (deckId.equals("latest")) {
			deckId = latestDecksID;
		}
		if (dealtTo.equals("latest")) {
			dealtTo = StepDefsDecks.latestPlayersID;
		}
		
		// Uri (URL) parameters
		Map<String, String> uriParams = new HashMap<>();
		uriParams.put("id", deckId);
		
		// Query parameters
		Map<String, String>  queryParams = new HashMap<>();
		queryParams.put("dealtTo", dealtTo);
		
		// body cannot be null since there is a put that wants a request body
		executePutWithUriAndQueryParam(decksUrlWithId, uriParams, "{}", queryParams);
	}
	
	@Given("^I try to delete a deck with \"([^\"]*)\"$")
	public void iTryToDeleteADeckWith(String deckId) throws Throwable {
		if (deckId.equals("latest")) {
			deckId = latestDecksID;
			if (!latestDecksIDs.isEmpty()) {
				latestDecksIDs.remove(latestDecksIDs.size() - 1);
			}
		}
		executeDelete(decksUrl + deckId, null);
	}
	
	@Given("^I try to delete all decks with \"([^\"]*)\"$")
	public void iTryToDeleteAllDecksWith(String ids) throws Throwable {
		if (ids.equals("all")) {
			// all
		}
		executeDelete(allDecksUrl + "?id=" + StringUtils.join(latestDecksIDs, ','), null);
		latestDecksIDs.clear();
	}
	
	@And("^The json response should contain at least \"([^\"]*)\" decks")
	public void theJsonDeckResponseBodyShouldContainAtLeast(int count) throws Throwable {
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//JSON string to Object
		List<DeckDto> jsonDecks = mapper.readValue(latestResponse.getBody(), new TypeReference<List<DeckDto>>() {
		});
		
		latestDecksIDs.clear();
		for (DeckDto deckDto : jsonDecks) {
			latestDecksIDs.add(String.valueOf(deckDto.getDeckId()));
			latestDecksID = String.valueOf(deckDto.getDeckId());
		}
		
		// at least equal but more can exist
		assertThat(latestDecksIDs.size(), greaterThanOrEqualTo(count));
		
	}
	
	@And("^The json response should contain exactly \"([^\"]*)\" decks")
	public void theJsonDeckResponseBodyShouldContainExact(int count) throws Throwable {
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//JSON string to Object
		List<DeckDto> jsonDecks = mapper.readValue(latestResponse.getBody(), new TypeReference<List<DeckDto>>() {
		});
		
		latestDecksIDs.clear();
		for (DeckDto deckDto : jsonDecks) {
			latestDecksIDs.add(String.valueOf(deckDto.getDeckId()));
			latestDecksID = String.valueOf(deckDto.getDeckId());
		}
		
		assertThat(latestDecksIDs.size(), is(count));
	}
	
	@And("^The json response should contain card \"([^\"]*)\" deck with shuffle \"([^\"]*)\" having \"([^\"]*)\" and \"([^\"]*)\" for game \"([^\"]*)\"$")
	public void theJsonResponseBodyShouldBeANewTypeDeckWithDealtToAndCardOrder(String card, boolean shuffle, String dealtTo, String cardOrder, String gameId) throws Throwable {
		
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//JSON string to Object
		//Deck jsonDeck = mapper.readValue(latestResponse.getBody(), Deck.class);
		DeckDto jsonDtoDeck = mapper.readValue(latestResponse.getBody(), DeckDto.class);
		latestDecksID = String.valueOf(jsonDtoDeck.getDeckId());
		
		if (gameId.equals("latest")) {
			gameId = latestGamesID;
		}
		
		if (dealtTo.equals("latest")) {
			dealtTo = latestPlayersID;
		}
		
		// expected , actual
		assertThat(Integer.parseInt(gameId), is(jsonDtoDeck.getGameDto().getGameId()));
		
		if (!card.isEmpty()) {
			if (jsonDtoDeck.getCardDto().getCardId() == new Card(card).getCardId()) {
				// assert if the input cardDto has the input dealtToDto and cardOder
				if (!dealtTo.isEmpty()) {
					assertThat(Integer.parseInt(dealtTo), is(jsonDtoDeck.getDealtToDto().getPlayerId()));
				}
				if (!cardOrder.isEmpty()) {
					// actual, matchers
					assertThat(jsonDtoDeck.getCardOrder(), is(Integer.parseInt(cardOrder)));
				}
			}
			
		}
		// AC is the first cardDto when not shuffled
		assertThat(shuffle, is((jsonDtoDeck.getCardDto().getCardId() != "AC")));
	}
	
}