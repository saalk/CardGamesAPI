package nl.knikit.cardgames.resource;

import nl.knikit.cardgames.DTO.GameDto;
import nl.knikit.cardgames.mapper.ModelMapperUtil;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.model.GameType;
import nl.knikit.cardgames.model.Player;
import nl.knikit.cardgames.model.state.GalacticCasinoStateMachine;
import nl.knikit.cardgames.service.IGameService;
import nl.knikit.cardgames.service.IPlayerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import lombok.extern.slf4j.Slf4j;

// @RestController = @Controller + @ResponseBody
@CrossOrigin
@RestController
@Component
// @ExposesResourceFor(Game.class) //hateoas
@Slf4j
@Scope("prototype")
@RequestScoped
public class GameResource {
	
	// @Resource = javax, @Inject = javax, @Autowire = spring bean factory
	@Autowired
	private IGameService gameService;
	
	@Autowired
	private IPlayerService playerService;
	
	@Autowired
	private ModelMapperUtil mapUtil;
	
	@GetMapping("/games/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public ResponseEntity getGame(@PathVariable("id") int id) {
		
		try {
			Game game = gameService.findOne(id);
			if (game == null) {
				return ResponseEntity
						       .status(HttpStatus.NOT_FOUND)
						       .body("Game not found");
			}
			GameDto gameDto = mapUtil.convertToDto(game);
			return ResponseEntity
					       .status(HttpStatus.OK)
					       .body(gameDto);
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(e);
		}
	}
	
	@GetMapping("/games")
	@Produces({MediaType.APPLICATION_JSON})
	public ResponseEntity getGames() {
		
		ArrayList<Game> games;
		try {
			games = (ArrayList<Game>) gameService.findAll("gameType", "ASC");
			List<GameDto> gameDtos = games.stream()
					                         .map(game -> mapUtil.convertToDto(game)).collect(Collectors.toList());
			
			return ResponseEntity
					       .status(HttpStatus.OK)
					       .body(gameDtos);
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(e);
		}
	}
	
	@GetMapping(value = "/games", params = {"gameType"})
	@Produces({MediaType.APPLICATION_JSON})
	public ResponseEntity getGamesWhere(@RequestParam(value = "gameType", required = true) String param) {
		
		try {
			List<Game> games = gameService.findAllWhere("gameType", param);
			if (games == null) {
				// getGames empty is not an error
				return ResponseEntity
						       .status(HttpStatus.OK)
						       .body("{}");
			}
			List<GameDto> gamesDto = games.stream()
					                         .map(game -> mapUtil.convertToDto(game)).collect(Collectors.toList());
			
			return ResponseEntity
					       .status(HttpStatus.OK)
					       .body(gamesDto);
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(e);
		}
	}
	
	@PostMapping(name = "/games")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity createGame(@RequestBody GameDto gameDto) throws ParseException {
		
		if (gameDto == null) {
			return ResponseEntity
					       .status(HttpStatus.BAD_REQUEST)
					       .body("Body null");
		}
		
		GameDto consistentGameDto = makeConsistentGameDto(gameDto);
		Game consistentGame = mapUtil.convertToEntity(consistentGameDto);
		try {
			Game createdGame = gameService.create(consistentGame);
			if (createdGame == null) {
				return ResponseEntity
						       .status(HttpStatus.NOT_FOUND)
						       .body("Game not created");
			}
			GameDto createdGameDto = mapUtil.convertToDto(createdGame);
			return ResponseEntity
					       .status(HttpStatus.CREATED)
					       .body(createdGameDto);
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(e);
		}
	}
	
	@PostMapping(name = "/games", params = {"jokers"})
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity createGameWithDeck(
			                                        @RequestParam(value = "jokers", required = false, defaultValue = "0") Integer jokers,
			                                        @RequestBody GameDto gameDto) throws ParseException {
		
		if (gameDto == null) {
			return ResponseEntity
					       .status(HttpStatus.BAD_REQUEST)
					       .body("Body null");
		}
		
		GameDto consistentGameDto = makeConsistentGameDto(gameDto);
		Game consistentGame = mapUtil.convertToEntity(consistentGameDto);
		consistentGame.addShuffledDeckToGame(jokers);
		
		try {
			Game createdGame = gameService.create(consistentGame);
			if (createdGame == null) {
				return ResponseEntity
						       .status(HttpStatus.NOT_FOUND)
						       .body("Game not created");
			}
			GameDto newGameDto = mapUtil.convertToDto(createdGame);
			return ResponseEntity
					       .status(HttpStatus.CREATED)
					       .body(newGameDto);
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(e);
		}
	}
	
	// a body is always needed but can be {}
	@PutMapping(value = "/games/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity updateGame(
			                                @PathVariable("id") Integer pathId,
			                                @RequestBody GameDto gameDtoToUpdate,
			                                @RequestParam(value = "winner", required = false) Integer winner) throws ParseException {
		
		// init
		GameDto updatedGameDto;
		Game gameToUpdate;
		Game updatedGame;
		Player player;
		
		// check path var game/{id}
		int id = pathId;
		if (id == 0) {
			return ResponseEntity
					       .status(HttpStatus.BAD_REQUEST)
					       .body("Id in /games{id}?winner={winner} null or zero");
		} else {
			try {
				gameToUpdate = gameService.findOne(id);
				if (gameToUpdate == null) {
					return ResponseEntity
							       .status(HttpStatus.NOT_FOUND)
							       .body("Game for /games{id}?winner={winner} not found");
				}
			} catch (Exception e) {
				return ResponseEntity
						       .status(HttpStatus.INTERNAL_SERVER_ERROR)
						       .body(e);
			}
		}
		
		// check request param ?winner={winner} if present
		if (!(winner == null)) {
			
			// use the gameDto found for path var in previous section
			if (winner.toString().isEmpty()) {
				gameToUpdate.setPlayer(null);
			} else {
				try {
					player = playerService.findOne(winner);
					if (player == null || player.getPlayerId() == 0) {
						return ResponseEntity
								       .status(HttpStatus.NOT_FOUND)
								       .body("Winner in /games{id}?winner={winner} not found");
					}
					gameToUpdate.setPlayer(player);
				} catch (Exception e) {
					return ResponseEntity
							       .status(HttpStatus.INTERNAL_SERVER_ERROR)
							       .body(e);
				}
			}
		} else if (gameDtoToUpdate != null && gameDtoToUpdate.getGameId() != 0) {
			
			// use the gameDtoToUpdate from the request body
			gameToUpdate = mapUtil.convertToEntity(gameDtoToUpdate);
		} else {
			return ResponseEntity
					       .status(HttpStatus.BAD_REQUEST)
					       .body("Body or winner in /games{id}?winner={winner} should be present");
		}
		
		// do the update
		try {
			updatedGame = gameService.update(gameToUpdate);
			if (updatedGame == null) {
				return ResponseEntity
						       .status(HttpStatus.NOT_FOUND)
						       .body("Game in /games{id}?winner={winner} could not be updated");
			}
			updatedGameDto = mapUtil.convertToDto(updatedGame);
			return ResponseEntity
					       .status(HttpStatus.OK)
					       .body(updatedGameDto);
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(e);
		}
	}
	
	
	@DeleteMapping("/games/{id}")
	public ResponseEntity deleteGames(@PathVariable("id") int id) {
		
		try {
			Game deleteGame = gameService.findOne(id);
			if (deleteGame == null) {
				return ResponseEntity
						       .status(HttpStatus.NOT_FOUND)
						       .body("Game not found");
			}
			gameService.deleteOne(deleteGame);
			return ResponseEntity
					       .status(HttpStatus.NO_CONTENT)
					       .body("");
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(e);
		}
	}
	
	
	// @MultipartConfig is a JAX-RS framework annotation and @RequestParam is from Spring
	//
	// SPRING
	// use @RequestParam(value = "date", required = false, defaultValue = "01-01-1999") Date dateOrNull)
	// you fromLabel the Date dataOrNull for ?date=12-05-2013
	//
	// JAX_RS
	// also use: @DefaultValue("false") @QueryParam("from") boolean human
	// you fromLabel the boolean human with value 'true' for ?human=true
	
	// /games?id=1,2,3,4
	@DeleteMapping(value = "/games", params = {"id"})
	public ResponseEntity deleteGamesById(@RequestParam(value = "id", required = false) List<String> ids) {
		
		try {
			for (int i = 0; i < ids.size(); i++) {
				Game deleteGame = gameService.findOne(Integer.parseInt(ids.get(i)));
				if (deleteGame == null) {
					return ResponseEntity
							       .status(HttpStatus.NOT_FOUND)
							       .body("Game not found: " + ids.get(i));
				}
			}
			gameService.deleteAllByIds(new Game(), ids);
			return ResponseEntity
					       .status(HttpStatus.NO_CONTENT)
					       .body("");
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(e);
		}
	}
	
	private GameDto makeConsistentGameDto(GameDto gameDto) {
		
		// set defaults for notNull fields
		if (gameDto.getState() == null) {
			gameDto.setState(GalacticCasinoStateMachine.State.SELECT_GAME);
		}
		if (gameDto.getGameType() == null) {
			gameDto.setGameType(GameType.HIGHLOW);
		}
		return gameDto;
	}
}
