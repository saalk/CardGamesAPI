package nl.knikit.cardgames.resource;

import nl.knikit.cardgames.DTO.CardGame;
import nl.knikit.cardgames.DTO.GameDto;
import nl.knikit.cardgames.mapper.ModelMapperUtil;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.service.IGameService;
import nl.knikit.cardgames.service.IPlayerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.ArrayList;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import lombok.extern.slf4j.Slf4j;

// @RestController = @Controller + @ResponseBody
@CrossOrigin
@RestController
@Component
//@ExposesResourceFor(cardgame.class)
@Slf4j
@Scope("prototype")
@RequestScoped
public class CardGameResource {
	
	// @Resource = javax, @Inject = javax, @Autowire = spring bean factory
	@Autowired
	private IGameService gameService;
	
	@Autowired
	private IPlayerService playerService;
	
	@Autowired
	private ModelMapperUtil mapUtil;
	
	@GetMapping("/cardgames/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public ResponseEntity getCardGame(@PathVariable("id") int id) {
		
		try {
			Game game = gameService.findOne(id);
			if (game == null) {
				return ResponseEntity
						       .status(HttpStatus.NOT_FOUND)
						       .body("CardGame not found");
			}
			GameDto gameDto = mapUtil.convertToDto(game);
			CardGame cardGame = mapUtil.convertFromDto(gameDto);
			return ResponseEntity
					       .status(HttpStatus.OK)
					       .body(cardGame);
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(e);
		}
	}
	
	@GetMapping("/cardgames")
	@Produces({MediaType.APPLICATION_JSON})
	public ResponseEntity getCardGames() {
		
		ArrayList<Game> games;
		try {
			games = (ArrayList<Game>) gameService.findAll("game", "ASC");
			ArrayList<CardGame> cardGames = new ArrayList<>();
			for (Game game : games) {
				GameDto gameDto = mapUtil.convertToDto(game);
				cardGames.add(mapUtil.convertFromDto(gameDto));
			}
			return ResponseEntity
					       .status(HttpStatus.OK)
					       .body(cardGames);
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(e);
		}
	}
	
	@PostMapping(value = "/cardgames/setup", params = {"name", "avatar", "aiLevel"})
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity createCardGame(
			                                    @RequestBody CardGame cardGame,
			                                    @RequestParam(value = "name", required = true) String name) throws Exception {
		
		return ResponseEntity
				       .status(HttpStatus.BAD_REQUEST)
				       .body("TODO");
		
	}
	
	
	// a body is always needed but can be {}
	@PutMapping(value = "/cardGames/setup/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity updateCardGame(
			                                    @PathVariable("id") Integer pathId,
			                                    @RequestBody CardGame cardGameToUpdate,
			                                    @RequestParam(value = "playingOrder", required = true) Integer playingOrder) throws ParseException {
		return ResponseEntity
				       .status(HttpStatus.BAD_REQUEST)
				       .body("TODO");
	}

}
