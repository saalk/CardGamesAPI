package nl.knikit.cardgames.resource;

import nl.knikit.cardgames.DTO.GameDto;
import nl.knikit.cardgames.mapper.ModelMapperUtil;
import nl.knikit.cardgames.model.GameType;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.model.Player;
import nl.knikit.cardgames.model.state.GalacticCasinoStateMachine;
import nl.knikit.cardgames.service.IGameService;
import nl.knikit.cardgames.service.IPlayerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.hateoas.ExposesResourceFor;
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
	
	@GetMapping("/gameDtos/{id}")
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
	
	@GetMapping("/gameDtos")
	@Produces({MediaType.APPLICATION_JSON})
	public ResponseEntity getGames() {
		
		ArrayList<Game> games;
		try {
			games = (ArrayList<Game>) gameService.findAll("gameType", "ASC");
			List<GameDto> gamesDto = games.stream()
					                         .map(player -> mapUtil.convertToDto(player)).collect(Collectors.toList());
			// TODO make
			return ResponseEntity
					       .status(HttpStatus.OK)
					       .body(gamesDto);
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(e);
		}
	}
	
	// @QueryParam is a JAX-RS framework annotation and @RequestParam is from Spring
	//
	// SPRING
	// use @RequestParam(value = "date", required = false, defaultValue = "01-01-1999") Date dateOrNull)
	// you fromLabel the Date dataOrNull for ?date=12-05-2013
	//
	// JAX_RS
	// also use: @DefaultValue("false") @QueryParam("from") boolean human
	// you fromLabel the boolean human with value 'true' for ?human=true
	
	@GetMapping(value = "/gameDtos/", params = {"gameType"})
	@Produces({MediaType.APPLICATION_JSON})
	public ResponseEntity getGamesWhere(@RequestParam(value = "gameType", required = true) String param) {
		
		try {
			List<Game> games = gameService.findAllWhere("gameType", param);
			if (games == null) {
				return ResponseEntity
						       .status(HttpStatus.NOT_FOUND)
						       .body("Game not found");
			}
			List<GameDto> gamesDto = games.stream()
					                         .map(player -> mapUtil.convertToDto(player)).collect(Collectors.toList());
			
			return ResponseEntity
					       .status(HttpStatus.OK)
					       .body(gamesDto);
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(e);
		}
	}
	
	@PostMapping(name = "/gameDtos")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity createGame(@RequestBody GameDto gameDto) throws ParseException {
		
		if (gameDto == null) {
			return ResponseEntity
					       .status(HttpStatus.BAD_REQUEST)
					       .body("Body null");
		}
		Game game = mapUtil.convertToEntity(gameDto);
		Game consistentGame = makeConsistentGame(game);
		try {
			Game createdGame = gameService.create(consistentGame);
			if (createdGame == null) {
				return ResponseEntity
						       .status(HttpStatus.NOT_FOUND)
						       .body("Game not created");
			}
			return ResponseEntity
					       .status(HttpStatus.CREATED)
					       .body(createdGame);
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(e);
		}
	}
	
	@PostMapping(name = "/gameDtos/", params = {"jokers"})
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity createGameWithDeck(
			                                          @RequestParam(value = "jokers", required = false, defaultValue = "0") Integer jokers,
			                                          @RequestBody GameDto gameDto) throws ParseException {
		
		// TODO jokers IT testing
		if (gameDto == null) {
			return ResponseEntity
					       .status(HttpStatus.BAD_REQUEST)
					       .body("Body null or jokers missing");
		}
		Game game = mapUtil.convertToEntity(gameDto);
		Game consistentGame = makeConsistentGame(game);
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
	
	@PutMapping("/gameDtos/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity updateGame(@PathVariable int id, @RequestBody GameDto updateGameDto) throws ParseException {
		// always use the id in the path instead of id in the body
		if (updateGameDto == null || id == 0) {
			return ResponseEntity
					       .status(HttpStatus.BAD_REQUEST)
					       .body("Body null or id zero");
		}
		Game game = gameService.findOne(id);
		if (game == null) {
			return ResponseEntity
					       .status(HttpStatus.NOT_FOUND)
					       .body("Game for not found");
		}
		Game convertedGame = mapUtil.convertToEntity(updateGameDto);
		convertedGame.setGameId(id);
		//Game consistentGame = makeConsistentGame(convertedGame);
		try {
			Game updatedGame = gameService.update(convertedGame);
			if (updatedGame == null) {
				return ResponseEntity
						       .status(HttpStatus.NOT_FOUND)
						       .body("Game not updated");
			}
			GameDto updatedGameDto = mapUtil.convertToDto(updatedGame);
			return ResponseEntity
					       .status(HttpStatus.OK)
					       .body(updatedGameDto);
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(e);
		}
	}
	
	@PutMapping(value = "/gameDtos/{id}/", params = {"winner"})
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity updateGameWithWinner(@PathVariable int id,
	                                           @RequestParam(value = "winner", required = true) int winner) {
		if (id == 0 || winner == 0) {
			return ResponseEntity
					       .status(HttpStatus.BAD_REQUEST)
					       .body("Id or winner zero");
		}
		Player player = playerService.findOne(winner);
		if (player == null) {
			return ResponseEntity
					       .status(HttpStatus.NOT_FOUND)
					       .body("Winner not found");
		}
		Game game = gameService.findOne(id);
		if (game == null) {
			return ResponseEntity
					       .status(HttpStatus.NOT_FOUND)
					       .body("Game not found");
		}
		// TODO check circular update -> player.setGameDtos(null);
		game.setPlayer(player);
		try {
			Game updatedGame = gameService.update(game);
			if (updatedGame == null) {
				return ResponseEntity
						       .status(HttpStatus.NOT_FOUND)
						       .body("Game not updated");
			}
			GameDto updatedGameDto = mapUtil.convertToDto(updatedGame);
			return ResponseEntity
					       .status(HttpStatus.OK)
					       .body(updatedGameDto);
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(e);
		}
	}
	
	@DeleteMapping("/gameDtos/{id}")
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
					       .body("{}");
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
	
	// /gameDtos?id=1,2,3,4
	@DeleteMapping(value = "/gameDtos/", params = {"id"})
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
	
	private Game makeConsistentGame(Game game) {
		
		Game consistentGame = new Game();
		if (game.getPlayer() != null) {
			consistentGame.setPlayer(game.getPlayer());
		} else {
			consistentGame.setPlayer(null);
		}
		
		if (game.getDecks() != null) {
			consistentGame.setDecks(game.getDecks());
		} else {
			consistentGame.setDecks(null);
		}
		
		consistentGame.setAnte(game.getAnte());
		consistentGame.setCurrentRound(game.getCurrentRound());
		consistentGame.setCurrentTurn(game.getCurrentTurn());
		consistentGame.setMaxRounds(game.getMaxRounds());
		consistentGame.setMaxTurns(game.getMaxTurns());
		consistentGame.setMinRounds(game.getMinRounds());
		consistentGame.setMinTurns(game.getMinTurns());
		consistentGame.setTurnsToWin(game.getTurnsToWin());
		consistentGame.setGameId(game.getGameId() > 0 ? game.getGameId() : 0);
		
		// make state consistent
		if (game.getState() != null) {
			consistentGame.setState(game.getState());
		} else {
			consistentGame.setState(GalacticCasinoStateMachine.State.SELECT_GAME);
		}
		
		// make type consistent
		if (game.getGameType() != null) {
			consistentGame.setGameType(game.getGameType());
		} else {
			consistentGame.setGameType(GameType.HIGHLOW);
		}
		return consistentGame;
	}
}
