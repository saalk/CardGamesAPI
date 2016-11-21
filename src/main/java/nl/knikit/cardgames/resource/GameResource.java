package nl.knikit.cardgames.resource;

import nl.knikit.cardgames.model.CardGameType;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.model.Player;
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

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import lombok.extern.slf4j.Slf4j;

// @RestController = @Controller + @ResponseBody
@CrossOrigin
@RestController
@Component
@ExposesResourceFor(Game.class)
@Slf4j
@Scope("prototype")
public class GameResource {
	
	// @Resource = javax, @Inject = javax, @Autowire = spring bean factory
	@Autowired
	private IGameService gameService;
	
	@Autowired
	private IPlayerService playerService;
	
	@GetMapping("/games")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity<ArrayList<Game>> getGames() {
		
		ArrayList<Game> games;
		try {
			games = (ArrayList<Game>) gameService.findAll("cardGameType", "ASC");
			return ResponseEntity
					       .status(HttpStatus.OK)
					       .body(games);
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(new ArrayList<Game>());
		}
	}
	
	@GetMapping("/games/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity getGame(@PathVariable("id") int id) {
		
		try {
			Game game = gameService.findOne(id);
			if (game == null) {
				return ResponseEntity
						       .status(HttpStatus.NOT_FOUND)
						       .body("[{}]");
			}
			return ResponseEntity
					       .status(HttpStatus.OK)
					       .body(game);
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(new ArrayList<Game>());
		}
	}
	
	// @QueryParam is a JAX-RS framework annotation and @RequestParam is from Spring
	//
	// SPRING
	// use @RequestParam(value = "date", required = false, defaultValue = "01-01-1999") Date dateOrNull)
	// you fromLabel the Date dataOrNull for ?date=12-05-2013
	//
	// JAX_RS
	// also use: @DefaultValue("false") @QueryParam("from") boolean isHuman
	// you fromLabel the boolean isHuman with value 'true' for ?isHuman=true
	
	@GetMapping(value = "/games/", params = {"cardGameType"})
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity<ArrayList<Game>> findAllWhere(@RequestParam(value = "cardGameType", required = true) String param) {

		try {
			ArrayList<Game> games = (ArrayList) gameService.findAllWhere("cardGameType", param);
			if (games == null) {
				return ResponseEntity
						       .status(HttpStatus.NOT_FOUND)
						       .body(new ArrayList<Game>());
			}
			return ResponseEntity
					       .status(HttpStatus.OK)
					       .body(games);
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(new ArrayList<Game>());
		}
	}
	
	@PostMapping(name = "/games")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity createGame(@RequestBody Game game) {
		
		if (game== null) {
			return ResponseEntity
					       .status(HttpStatus.BAD_REQUEST)
					       .body("[{}]");
		}
		
		Game consistentGame = makeConsistentGame(game);
		try {
			Game createdGame = gameService.create(consistentGame);
			if (createdGame == null) {
				return ResponseEntity
						       .status(HttpStatus.NOT_FOUND)
						       .body(new ArrayList<Game>());
			}
			return ResponseEntity
					       .status(HttpStatus.CREATED)
					       .body(createdGame);
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(consistentGame);
		}
	}
	
	@PostMapping(name = "/games", params = {"jokers"})
	public ResponseEntity createGameWithJokers(
			                                          @RequestParam(value = "jokers", required = false, defaultValue = "0") Integer
					                                          jokers,
			                                          @RequestBody Game game) {
		
		// TODO jokers param
		if (game== null) {
			return ResponseEntity
					       .status(HttpStatus.BAD_REQUEST)
					       .body("[{}]");
		}
		
		Game consistentGame = makeConsistentGame(game);
		try {
			Game createdGame = gameService.create(consistentGame);
			if (createdGame == null) {
				return ResponseEntity
						       .status(HttpStatus.NOT_FOUND)
						       .body(new ArrayList<Game>());
			}
			return ResponseEntity
					       .status(HttpStatus.CREATED)
					       .body(createdGame);
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(consistentGame);
		}
	}
	
	@PutMapping("/games/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity updateGame(@PathVariable int id, @RequestBody Game game) {
		
		if (game == null || id==0) {
			return ResponseEntity
					       .status(HttpStatus.BAD_REQUEST)
					       .body("[{}]");
		}
		Game consistentGame = makeConsistentGame(game);
		try {
			Game updatedGame = gameService.update(consistentGame);
			if (updatedGame == null) {
				return ResponseEntity
						       .status(HttpStatus.NOT_FOUND)
						       .body(new ArrayList<Player>());
			}
			return ResponseEntity
					       .status(HttpStatus.OK)
					       .body(updatedGame);
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(consistentGame);
		}
	}
	
	@PutMapping(value = "/games/{id}", params = {"winner"})
	public ResponseEntity updateGameWithWinner(@PathVariable int id, @RequestBody Game game,
	                                           @RequestParam(value = "winner", required = true) String winner) {
		Player player = playerService.findOne(Integer.parseInt(winner));
		if (player == null) {
			return ResponseEntity
					       .status(HttpStatus.BAD_REQUEST)
					       .body("[{}]");
		}
		if (game == null || id==0) {
			return ResponseEntity
					       .status(HttpStatus.BAD_REQUEST)
					       .body("[{}]");
		}
		Game consistentGame = makeConsistentGame(game);
		//TODO check if redundant
		consistentGame.setWinner(player);
		try {
			Game updatedGame = gameService.update(consistentGame);
			if (updatedGame == null) {
				return ResponseEntity
						       .status(HttpStatus.NOT_FOUND)
						       .body(new ArrayList<Player>());
			}
			return ResponseEntity
					       .status(HttpStatus.OK)
					       .body(updatedGame);
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(consistentGame);
		}
	}
	
	@DeleteMapping("/games/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity deleteGames(@PathVariable("id") int id) {
		
		try {
			Game deleteGame = gameService.findOne(id);
			if (deleteGame == null) {
				return ResponseEntity
						       .status(HttpStatus.NOT_FOUND)
						       .body("[{}]");
			}
			gameService.deleteOne(deleteGame);
			return ResponseEntity
					       .status(HttpStatus.NO_CONTENT)
					       .body("[{}]");
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(new ArrayList<Game>());
		}
	}
	
	
	// @MultipartConfig is a JAX-RS framework annotation and @RequestParam is from Spring
	//
	// SPRING
	// use @RequestParam(value = "date", required = false, defaultValue = "01-01-1999") Date dateOrNull)
	// you fromLabel the Date dataOrNull for ?date=12-05-2013
	//
	// JAX_RS
	// also use: @DefaultValue("false") @QueryParam("from") boolean isHuman
	// you fromLabel the boolean isHuman with value 'true' for ?isHuman=true
	
	// /games?id=1,2,3,4
	@DeleteMapping(value = "/games/", params = {"id"})
	public ResponseEntity deleteGamesById( @RequestParam(value = "id", required = false) List<String> ids) {
		
		try {
			for (int i = 0; i < ids.size(); i++) {
				Game deleteGame = gameService.findOne(Integer.parseInt(ids.get(i)));
				if (deleteGame == null) {
					return ResponseEntity
							       .status(HttpStatus.NOT_FOUND)
							       .body(ids.get(i));
				}
			}
			gameService.deleteAllByIds(new Game(), ids);
			return ResponseEntity
					       .status(HttpStatus.NO_CONTENT)
					       .body("");
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(new ArrayList<Game>());
		}
	}
	
	private Game makeConsistentGame(Game game) {
		
		Game consistentGame = new Game();
		Player consistentPlayer = new Player();
		if (game.getWinner().getPlayerId() > 0) {
			consistentPlayer.setPlayerId(game.getWinner().getPlayerId());
		} else {
			consistentPlayer.setPlayerId(0);
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
		consistentGame.setWinner(consistentPlayer);
		
		// make state consistent
		if (game.getState() == null || game.getState().isEmpty()) {
			consistentGame.setState("SELECT_GAME");
		} else {
			consistentGame.setState(game.getState());
		}
		
		// make cardGameType consistent
		if (game.getCardGameType() == null || game.getCardGameType().name().isEmpty()) {
			consistentGame.setCardGameType(CardGameType.HIGHLOW);
		} else {
			consistentGame.setCardGameType(game.getCardGameType());
		}
		return consistentGame;
	}
}
