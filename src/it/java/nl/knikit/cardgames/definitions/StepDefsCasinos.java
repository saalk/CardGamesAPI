package nl.knikit.cardgames.definitions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nl.knikit.cardgames.DTO.CasinoDto;
import nl.knikit.cardgames.DTO.DeckDto;
import nl.knikit.cardgames.DTO.GameDto;
import nl.knikit.cardgames.DTO.PlayerDto;
import nl.knikit.cardgames.model.Avatar;
import nl.knikit.cardgames.model.Card;
import nl.knikit.cardgames.model.Hand;

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

public class StepDefsCasinos extends SpringIntegrationTest {
	
	// API          HTTP
	//
	// UPDATE,PUT   OK(200, "OK"),
	// POST         CREATED(201, "Created"),
	// DELETE       NO_CONTENT(204, "No Content"),
	
	// no body      BAD_REQUEST(400, "Bad Request"),
	// wrong id     NOT_FOUND(404, "Not Found"),
	
	@Given("^I try to get a casino with valid \"([^\"]*)\"$")
	public void iTryToGetACasinoWithValid(String casinoId) throws Throwable {
		if (casinoId.equals("latest")) {
			casinoId = latestCasinosID;
		}
		executeGet(casinosUrl + casinoId);
	}
	
	@Given("^I try to get a casino with invalid \"([^\"]*)\"$")
	public void iTryToGetACasinoWithInvalid(String casinoId) throws Throwable {
		if (casinoId.equals("latest")) {
			casinoId = latestCasinosID;
		}
		executeGet(casinosUrl + casinoId);
	}
	
	@Given("^I try to get all casinos$")
	public void iTryToGetAllCasinos() throws Throwable {
		
		executeGet(allCasinosUrl);
	}
	
	@Given("^I try to get all casinos for game \"([^\"]*)\"$")
	public void iTryToGetAllCasinosForAGame(String gameId) throws Throwable {
		
		if (gameId.equals("latest")) {
			gameId = latestGamesID;
		}
		executeGet(allCasinosUrl + "?game=" + gameId);
	}
	
	@Given("^I try to post a casino for game \"([^\"]*)\" and players \"([^\"]*)\" having playingOrder \"([^\"]*)\"$")
	public void iTryToPostACasinoWithPlayerAndPlayingOrder(String game, String player, String playingOrder) throws Throwable {
		
		CasinoDto postCasinoDto = new CasinoDto();
		postCasinoDto.setPlayingOrder(Integer.parseInt(playingOrder));
		
		if (game.equals("latest")) {
			game = latestGamesID;
		}
		
		if (!game.isEmpty()) {
			GameDto postGameDto = new GameDto();
			postGameDto.setGameId(Integer.parseInt(game));
			postCasinoDto.setGameDto(postGameDto);
		} else {
			postCasinoDto.setGameDto(null);
		}
		
		// players are passed as params
		postCasinoDto.setPlayerDto(null);
		if (player.equals("latest")) {
			player = latestPlayersID;
		} else if (player.equals("latest-1")) {
			player = latestPlayersID2;
		}
		
		
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//Object to JSON in String
		String jsonInString = mapper.writeValueAsString(postCasinoDto);
		executePost(allCasinosUrl + "?player=" + player, jsonInString);
		
	}
	
	@Given("^I try to post a human \"([^\"]*)\" casino player having \"([^\"]*)\" and \"([^\"]*)\" and \"([^\"]*)\"$")
	public void iTryToPostANewHumanPlayerWithAvatarAlias(String human, String avatar, String alias, String aiLevel) throws Throwable {
		
		PlayerDto postPlayerDto = new PlayerDto();
		postPlayerDto.setHuman(Boolean.parseBoolean(human));
		postPlayerDto.setAvatar(Avatar.valueOf(avatar));
		//postPlayerDto.setAiLevel(AiLevel.valueOf(aiLevel));
		postPlayerDto.setAlias(alias);
		
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//Object to JSON in String
		String jsonInString = mapper.writeValueAsString(postPlayerDto);
		executePost(playersUrl, jsonInString);
		
	}
	
	@Given("^I try to put a casino \"([^\"]*)\" with a new playingOrder \"([^\"]*)\"$")
	public void iTryToPutAnExistingCasinoWithPlayingOrder(String casino, String playingOrder) throws Throwable {
		if (casino.equals("latest")) {
			casino = latestCasinosID;
		}
		// Uri (URL) parameters
		Map<String, String> uriParams = new HashMap<>();
		uriParams.put("id", casino);
		
		// Query parameters
		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("playingOrder", playingOrder);
		
		// body cannot be null since there is a put that wants a request body
		executePutWithUriAndQueryParam(casinosUrlWithId, uriParams, "{}", queryParams);
	}
	
	@Given("^I try to delete a casino with \"([^\"]*)\"$")
	public void iTryToDeleteACasinoWith(String casinoId) throws Throwable {
		if (casinoId.equals("latest")) {
			casinoId = latestCasinosID;
			if (!latestCasinosIDs.isEmpty()) {
				latestCasinosIDs.remove(latestCasinosIDs.size() - 1);
			}
		}
		executeDelete(casinosUrl + casinoId, null);
	}
	
	@Given("^I try to delete all casinos with \"([^\"]*)\"$")
	public void iTryToDeleteAllCasinosWith(String ids) throws Throwable {
		if (ids.equals("all")) {
			// all
		}
		executeDelete(allCasinosUrl + "?id=" + StringUtils.join(latestCasinosIDs, ','), null);
		latestCasinosIDs.clear();
	}
	
//	@Given("^I try to delete a player \"([^\"]*)\" for the casino$")
//	public void iTryToDeleteThePlayerForTheCasino(String player) throws Throwable {
//		if (player.equals("latest")) {
//			player = latestPlayersID;
//		} else if (player.equals("latest-1")) {
//			player = latestPlayersID2;
//		}
//
//		executeDelete(playersUrl + player, null);
//	}
	
//	@Given("^I try to delete a game \"([^\"]*)\" for a casino$")
//	public void iTryToDeleteAGameForACasinoWith(String gameId) throws Throwable {
//		if (gameId.equals("latest")) {
//			gameId = latestGamesID;
//		}
//		executeDelete(gamesUrl + gameId, null);
//	}
	
	@And("^The json response should contain at least \"([^\"]*)\" casinos")
	public void theJsonCasinoResponseBodyShouldContainAtLeast(int count) throws Throwable {
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//JSON string to Object
		List<CasinoDto> jsonCasinos = mapper.readValue(latestResponse.getBody(), new TypeReference<List<CasinoDto>>() {
		});
		
		latestCasinosIDs.clear();
		for (CasinoDto casinoDto : jsonCasinos) {
			latestCasinosIDs.add(String.valueOf(casinoDto.getCasinoId()));
			latestCasinosID = String.valueOf(casinoDto.getCasinoId());
		}
		
		// at least equal but more can exist
		assertThat(latestCasinosIDs.size(), greaterThanOrEqualTo(count));
		
	}
	
	@And("^The json response should contain exactly \"([^\"]*)\" casinos")
	public void theJsonCasinoResponseBodyShouldContainExact(int count) throws Throwable {
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//JSON string to Object
		List<CasinoDto> jsonCasinos = mapper.readValue(latestResponse.getBody(), new TypeReference<List<CasinoDto>>() {
		});
		
		latestCasinosIDs.clear();
		for (CasinoDto casinoDto : jsonCasinos) {
			latestCasinosIDs.add(String.valueOf(casinoDto.getCasinoId()));
			latestCasinosID = String.valueOf(casinoDto.getCasinoId());
		}
		
		assertThat(latestCasinosIDs.size(), is(count));
	}
	
	@And("^The json response should contain a casino having game \"([^\"]*)\" and player \"([^\"]*)\" for playingOrder \"([^\"]*)\"$")
	public void theJsonResponseBodyShouldBeACasinoWithGameAndPlayerAndPlayingOrder(String game, String player, String playingOrder) throws Throwable {
		
		if (game.equals("latest")) {
			game = latestGamesID;
		}
		
		if (player.equals("latest")) {
			player = latestPlayersID;
		} else if (player.equals("latest-1")) {
			player = latestPlayersID2;
		} else {
			player = "";
		}
		
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//JSON string to Object
		CasinoDto jsonCasinoDto = mapper.readValue(latestResponse.getBody(), CasinoDto.class);
		
		// expected , actual each casino should have the same game and player
		assertThat(Integer.parseInt(game), is(jsonCasinoDto.getGameDto().getGameId()));
		assertThat(Integer.parseInt(player), is(jsonCasinoDto.getPlayerDto().getPlayerId()));

		if (!playingOrder.isEmpty()) {
			// actual, matchers
			assertThat(jsonCasinoDto.getPlayingOrder(), is(Integer.parseInt(playingOrder)));
		}
		
	}
	
	@And("^The json response should contain a list of casino having game \"([^\"]*)\" and player \"([^\"]*)\" for playingOrder \"([^\"]*)\"$")
	public void theJsonResponseBodyShouldBeAListOfCasinoWithGameAndPlayerAndPlayingOrder(String game, String player, String playingOrder) throws Throwable {
		
		if (game.equals("latest")) {
			game = latestGamesID;
		}
		
		if (player.equals("latest")) {
			player = latestPlayersID;
		} else if (player.equals("latest-1")) {
			player = latestPlayersID2;
		} else {
			player = "";
		}
		
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//JSON string to Object
		//This is a List since multiple players can be added in one go
		List<CasinoDto> jsonCasinoDtos = mapper.readValue(latestResponse.getBody(), new TypeReference<List<CasinoDto>>() {
		});
		
		latestCasinosIDs.clear();
		for (CasinoDto casinoDto : jsonCasinoDtos) {
			latestCasinosIDs.add(String.valueOf(casinoDto.getCasinoId()));
			latestCasinosID = String.valueOf(casinoDto.getCasinoId());
			
			// expected , actual each casino should have the same game
			assertThat(Integer.parseInt(game), is(casinoDto.getGameDto().getGameId()));
			
			// expected , actual this casino should have the player and order supplied
			if (!player.isEmpty()) {
				assertThat(Integer.parseInt(player), is(casinoDto.getPlayerDto().getPlayerId()));
			}
			if (!playingOrder.isEmpty()) {
				// actual, matchers
				assertThat(casinoDto.getPlayingOrder(), is(Integer.parseInt(playingOrder)));
			}
		}
		
	}
	
	@And("^The json response should contain a casino \"([^\"]*)\" with a new playingOrder \"([^\"]*)\"$")
	public void theJsonResponseBodyShouldBeACasinoWithANewPlayingOrder(String casino, String playingOrder) throws Throwable {
		
		if (casino.equals("latest")) {
			casino = latestCasinosID;
		}
		
		// jackson has ObjectMapper that converts String to JSON
		ObjectMapper mapper = new ObjectMapper();
		
		//JSON string to Object
		CasinoDto jsonCasinoDto = mapper.readValue(latestResponse.getBody(), CasinoDto.class);
		
		if (!playingOrder.isEmpty()) {
			// actual, matchers
			assertThat(jsonCasinoDto.getPlayingOrder(), is(Integer.parseInt(playingOrder)));
		}
		
	}
	
//	@And("^The json response should contain a player for a casino$")
//	public void theJsonResponseBodyShouldBeANewHumanPlayerWithAvatarAliasForACasino() throws Throwable {
//
//		// jackson has ObjectMapper that converts String to JSON
//		ObjectMapper mapper = new ObjectMapper();
//
//		//JSON string to Object
//		PlayerDto jsonPlayer = mapper.readValue(latestResponse.getBody(), PlayerDto.class);
//
//		// move the latest to latest-1
//		latestPlayersID2 = latestPlayersID;
//		latestPlayersID = String.valueOf(jsonPlayer.getPlayerId());
//
//	}
	
//	@And("^The json response should contain a game for a casino$")
//	public void theJsonResponseBodyShouldBeANewGameForACasino() throws Throwable {
//
//		// jackson has ObjectMapper that converts String to JSON
//		ObjectMapper mapper = new ObjectMapper();
//
//		//JSON string to Object
//		GameDto jsonGame = mapper.readValue(latestResponse.getBody(), GameDto.class);
//		latestGamesID = String.valueOf(jsonGame.getGameId());
//
//	}
}