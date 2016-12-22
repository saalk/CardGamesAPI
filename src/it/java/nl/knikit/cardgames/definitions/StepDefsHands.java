package nl.knikit.cardgames.definitions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nl.knikit.cardgames.DTO.CardDto;
import nl.knikit.cardgames.DTO.CasinoDto;
import nl.knikit.cardgames.DTO.DeckDto;
import nl.knikit.cardgames.DTO.GameDto;
import nl.knikit.cardgames.DTO.HandDto;
import nl.knikit.cardgames.DTO.PlayerDto;
import nl.knikit.cardgames.model.Avatar;
import nl.knikit.cardgames.model.Card;

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

public class StepDefsHands extends SpringIntegrationTest {
	
	private static String latestHandID = "";
	private static List<String> latestHandIDs = new ArrayList<>();
	
	private static String latestCasinoID = "";
	private static List<String> latestCasinoIDs = new ArrayList<>();
	private static String latestPlayerID = "";
	private static String latestGameID = "";
	
	private static String handsUrl = "http://localhost:8383/api/hands/";
	private static String allHandsUrl = "http://localhost:8383/api/hands";
	private static String handsUrlWithId = "http://localhost:8383/api/hands/{id}";
	
	private static String casinosUrl = "http://localhost:8383/api/casinos/";
	private static String allCasinosUrl = "http://localhost:8383/api/casinos";
	
	private static String playersUrl = "http://localhost:8383/api/players/";
	private static String gamesUrl = "http://localhost:8383/api/games/";
	
	// API          HTTP
	//
	// UPDATE,PUT   OK(200, "OK"),
	// POST         CREATED(201, "Created"),
	// DELETE       NO_CONTENT(204, "No Content"),
	
	// no body      BAD_REQUEST(400, "Bad Request"),
	// wrong id     NOT_FOUND(404, "Not Found"),
	
	@Given("^I try to get a hand with valid \"([^\"]*)\"$")
	public void iTryToGetAHandWithValid(String handId) throws Throwable {
		if (handId.equals("latest")) {
			handId = StepDefsHands.latestHandID;
		}
		executeGet(handsUrl + handId);
	}
	
	@Given("^I try to get a hand with invalid \"([^\"]*)\"$")
	public void iTryToGetAHandWithInvalid(String handId) throws Throwable {
		if (handId.equals("latest")) {
			handId = StepDefsHands.latestHandID;
		}
		executeGet(handsUrl + handId);
	}
	
	@Given("^I try to get all hands$")
	public void iTryToGetAllHands() throws Throwable {
		
		executeGet(allHandsUrl);
	}
	
	@Given("^I try to get all hands for a casino \"([^\"]*)\"$")
	public void iTryToGetAllDecksForACasino(String casinoId) throws Throwable {
		
		if (casinoId.equals("latest")) {
			casinoId = StepDefsHands.latestCasinoID;
		}
		executeGet(allHandsUrl + "?casino=" + casinoId);
	}
	
	@Given("^I try to post a casino for a hand with game \"([^\"]*)\" and players \"([^\"]*)\" having playingOrder \"([^\"]*)\"$")
	public void iTryToPostACasinoForAHandWithPlayerAndPlayingOrder(String game, String player, String playingOrder) throws Throwable {
		
		CasinoDto postCasinoDto = new CasinoDto();
		postCasinoDto.setPlayingOrder(Integer.parseInt(playingOrder));
		
		if (game.equals("latest")) {
			game = StepDefsHands.latestGameID;
		}
		
		if (!game.isEmpty()) {
			GameDto postGameDto = new GameDto();
			postGameDto.setGameId(Integer.parseInt(game));
			postCasinoDto.setGameDto(postGameDto);
		} else {
			postCasinoDto.setGameDto(null);
		}
		
		// players are passed as params so set to null in the body
		postCasinoDto.setPlayerDto(null);
		if (player.equals("latest")) {
			player = StepDefsHands.latestPlayerID;
		}
		
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//Object to JSON in String
		String jsonInString = mapper.writeValueAsString(postCasinoDto);
		executePost(allCasinosUrl + "?player=" + player, jsonInString);
		
	}
	
	@Given("^I try to post a new hand with cards \"([^\"]*)\" and orders \"([^\"]*)\" for a player \"([^\"]*)\" and a casino \"([^\"]*)\"$")
	public void iTryToPostAHandWithCardsAndPlayerAndCasino(String cards, String cardOrders, String player, String casino) throws Throwable {
		
		HandDto postHandDto = new HandDto();
		postHandDto.setCardOrder(0); // do not use cardOrder for new hands, this is generated
		
		if (player.equals("latest")) {
			player = StepDefsHands.latestPlayerID;
		}
		if (!player.isEmpty()) {
			PlayerDto postPlayerDto = new PlayerDto();
			postPlayerDto.setPlayerId(Integer.parseInt(player));
			postHandDto.setPlayerDto(postPlayerDto);
		}
		
		if (casino.equals("latest")) {
			casino = StepDefsHands.latestCasinoID;
		}
		if (!casino.isEmpty()) {
			CasinoDto postCasinoDto = new CasinoDto();
			postCasinoDto.setCasinoId(Integer.parseInt(casino));
			postHandDto.setCasinoDto(postCasinoDto);
		}
		
		String cardsList[] = new String[53]; // all cards with one joker
		if (cards.contains(",")) {
			cardsList = cards.split(",");
		} else {
			cardsList[0] = cards;
		}
		
		for (int i = 0; i < cardsList.length; i++) {
			
			// TODO use ArrayList instead of Array
			if (cardsList[i] != null) {
				
				CardDto cardDto = new CardDto();
				cardDto.setCardId(cardsList[i]);
				postHandDto.setCardDto(cardDto);
				
				// jackson has ObjectMapper that converts String to JSON
				ObjectMapper mapper = new ObjectMapper();
				
				//Object to JSON in String
				String jsonInString = mapper.writeValueAsString(postHandDto);
				executePost(allHandsUrl + "?card=" + cards, jsonInString);
			}
		}
	}
	
	@Given("^I try to put a hand with \"([^\"]*)\" having player \"([^\"]*)\"$")
	public void iTryToPutAnExistingHandWithPlayer(String handId, String player) throws Throwable {
		if (handId.equals("latest")) {
			handId = StepDefsHands.latestHandID;
		}
		if (player.equals("latest")) {
			player = StepDefsHands.latestPlayerID;
		}
		
		// Uri (URL) parameters
		Map<String, String> uriParams = new HashMap<>();
		uriParams.put("id", handId);
		
		// Query parameters
		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("player", player);
		
		// body cannot be null since there is a put that wants a request body
		executePutWithUriAndQueryParam(handsUrlWithId, uriParams, "{}", queryParams);
	}
	
	@Given("^I try to delete a hand with \"([^\"]*)\"$")
	public void iTryToDeleteAHandWith(String handId) throws Throwable {
		if (handId.equals("latest")) {
			handId = StepDefsHands.latestHandID;
			if (!StepDefsHands.latestHandIDs.isEmpty()) {
				StepDefsHands.latestHandIDs.remove(latestHandIDs.size() - 1);
			}
			
		}
		executeDelete(handsUrl + handId, null);
	}
	
	@Given("^I try to delete all hands with \"([^\"]*)\"$")
	public void iTryToDeleteAllHandsWith(String ids) throws Throwable {
		if (ids.equals("all")) {
			// all
		}
		executeDelete(allHandsUrl + "?id=" + StringUtils.join(latestHandIDs, ','), null);
		StepDefsHands.latestHandIDs.clear();
	}
	
	@Given("^I try to delete the player for the hand \"([^\"]*)\"$")
	public void iTryToDeleteThePlayer(String player) throws Throwable {
		if (player.equals("latest")) {
			player = StepDefsHands.latestPlayerID;
		}
		executeDelete(playersUrl + player, null);
	}
	
	@Given("^I try to delete a game for a casino for the hand with \"([^\"]*)\"$")
	public void iTryToDeleteAGameForACasinoForAHandWith(String gameId) throws Throwable {
		if (gameId.equals("latest")) {
			gameId = StepDefsHands.latestGameID;
		}
		executeDelete(gamesUrl + gameId, null);
	}
	
	@Given("^I try to delete a casino for a hand with \"([^\"]*)\"$")
	public void iTryToDeleteACasinoForAHandWith(String casinoId) throws Throwable {
		if (casinoId.equals("latest")) {
			casinoId = StepDefsHands.latestCasinoID;
		}
		executeDelete(casinosUrl + casinoId, null);
	}
	
	@And("^The json response should contain at least \"([^\"]*)\" hands$")
	public void theJsonHandResponseBodyShouldContainAtLeast(int count) throws Throwable {
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//JSON string to Object
		List<HandDto> jsonHands = mapper.readValue(latestResponse.getBody(), new TypeReference<List<HandDto>>() {
		});
		
		latestHandIDs.clear();
		for (HandDto handDto : jsonHands) {
			latestHandIDs.add(String.valueOf(handDto.getHandId()));
			StepDefsHands.latestHandID = String.valueOf(handDto.getHandId());
		}
		
		// at least equal but more can exist
		assertThat(latestHandIDs.size(), greaterThanOrEqualTo(count));
	}
	
	@And("^The json response should contain a list of cards \"([^\"]*)\" hand with cardOrders \"([^\"]*)\" having \"([^\"]*)\" and casino \"([^\"]*)\"$")
	public void theJsonResponseBodyShouldBeAHandWithCardsAndPlayerAndCasino(List<String> cards, List<String> cardOrders, String player, String casino) throws Throwable {
		
		
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		
		//JSON string to Object
		List<HandDto> jsonHandDtos = mapper.readValue(latestResponse.getBody(), new TypeReference<List<HandDto>>() {
		});
		
		latestHandIDs.clear();
		for (HandDto handDto : jsonHandDtos) {
			latestHandIDs.add(String.valueOf(handDto.getHandId()));
			StepDefsHands.latestHandID = String.valueOf(handDto.getHandId());
		}
		
		if (casino.equals("latest")) {
			casino = StepDefsHands.latestCasinoID;
		}
		if (player.equals("latest")) {
			player = StepDefsHands.latestPlayerID;
		}
		
		int i = 0;
		for (HandDto handDto : jsonHandDtos) {
			
			
			// expected , actual all hands have the same casino and player
			assertThat(Integer.parseInt(casino), is(handDto.getCasinoDto().getCasinoId()));
			assertThat(Integer.parseInt(player), is(handDto.getPlayerDto().getPlayerId()));
			
			if (!cards.isEmpty() && !cardOrders.isEmpty()) {
				
				// expected , actual all hands have a different card and cardOrder
				// start with get(0), afterwards update the i
				assertThat(cards.get(i), is(handDto.getCardDto().getCardId()));
				assertThat(Integer.parseInt(cardOrders.get(i)), is(handDto.getCardOrder()));
			}
			
			i += 1;
		}
	}
	
	@And("^The json response should contain the card \"([^\"]*)\" hand with cardOrder \"([^\"]*)\" having \"([^\"]*)\" and casino \"([^\"]*)\"$")
	public void theJsonResponseBodyShouldBeAHandWithACardAndPlayerAndCasino(String cards, String cardOrders, String playerId, String casinoId) throws Throwable {
		
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//JSON string to Object
		//Hand jsonHand = mapper.readValue(latestResponse.getBody(), Hand.class);
		// TODO make it a list response ?
		HandDto jsonDtoHand = mapper.readValue(latestResponse.getBody(), HandDto.class);
		StepDefsHands.latestHandID = String.valueOf(jsonDtoHand.getHandId());
		
		if (casinoId.equals("latest")) {
			casinoId = StepDefsHands.latestCasinoID;
		}
		if (playerId.equals("latest")) {
			playerId = StepDefsHands.latestPlayerID;
		}
		
		// expected , actual
		assertThat(Integer.parseInt(casinoId), is(jsonDtoHand.getCasinoDto().getCasinoId()));
		assertThat(Integer.parseInt(playerId), is(jsonDtoHand.getPlayerDto().getPlayerId()));
		
		if (!cards.isEmpty() && !cardOrders.isEmpty()) {
			
			// expected , actual
			assertThat(cards, is(jsonDtoHand.getCardDto().getCardId()));
			assertThat(Integer.parseInt(cardOrders), is(jsonDtoHand.getCardOrder()));
		}
	}
	
	@And("^The json response should contain a game for a hand$")
	public void theJsonResponseBodyShouldBeANewGameForAHand() throws Throwable {
		
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//JSON string to Object
		GameDto jsonGame = mapper.readValue(latestResponse.getBody(), GameDto.class);
		
		StepDefsHands.latestGameID = String.valueOf(jsonGame.getGameId());
		
	}
	
	@And("^The json response should contain a player for a hand$")
	public void theJsonResponseBodyShouldBeANewHumanPlayerForAHand() throws Throwable {
		
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//JSON string to Object
		PlayerDto jsonPlayer = mapper.readValue(latestResponse.getBody(), PlayerDto.class);
		
		StepDefsHands.latestPlayerID = String.valueOf(jsonPlayer.getPlayerId());
		
	}
	
	@And("^The json response should contain a list of casinos for a hand$")
	public void theJsonResponseBodyShouldBeANewCasinoForAHand() throws Throwable {
		
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//JSON string to Object
		List<CasinoDto> jsonCasinos = mapper.readValue(latestResponse.getBody(), new TypeReference<List<CasinoDto>>() {
		});
		
		latestCasinoIDs.clear();
		for (CasinoDto casinoDto : jsonCasinos) {
			latestCasinoIDs.add(String.valueOf(casinoDto.getCasinoId()));
			StepDefsHands.latestCasinoID = String.valueOf(casinoDto.getCasinoId());
		}
		
		
	}
}