package nl.knikit.cardgames.resource;

/*
    http://viralpatel.net/blogs/spring-4-mvc-rest-example-json/

    | Annotation | Meaning in SPRING                                   |
    +------------+-----------------------------------------------------+
    | @Component | generic stereotype for any Spring-managed component |
    | @Repository| stereotype for persistence layer                    |
    | @Service   | stereotype for service layer                        |
    | @Controller| stereotype for presentation layer (spring-mvc)      |

    @RestController = @Controller + @ResponseBody
    You can only use @RequestMapping on @Controller annotated classes.

    new Spring MVC 4.3 REST annotations @GetMapping, @PlayerMapping, @PutMapping and @DeleteMapping
    instead of standard @RequestMapping. They act as wrapper to @RequestMapping.



    @GetMapping = @RequestMapping(method = RequestMethod.GET).

    JAX-RS = Java API for RESTful Web Services since JAVA EE 6:
    - @Path is from Java EE and identifies the URI path template to which the resource responds
    - @*Param and @GET, @PUT, @POST, @DELETE and @HEAD + @PATH and @Context

    Jersey = a reference implementation of JAX-RS from sun
    - an implementation together with test suites to server as Gold Standard
    - natively supported only by Glassfish (oracle owned)
    - Tomcat (aoache foundation) can run with the JRE and GlassFish requires the full JDK
    - Tomcat supports only servlet and JSP standards

    6 REST Endpoint                 HTTP Method 	Description
    /players 	                    GET             Returns the list of players
    /players/{id}                   GET             Returns player detail for given player {id}
    /players 	                    POST            Creates new player from the post data
    /players/{id}                   PUT             Replace the details for given player {id}
    /players 	                    DELETE          Deletes all players
    /players/{id}                   DELETE          Delete the player for given player {id}
*/


/*  The log levels in Java Logging API are different to other standard logging libraries
    Log4j/Logback	Java Logging
    fatal	        SEVERE
    error	        SEVERE
    warn	        WARNING
    info	        INFO
                    CONFIG
    debug	        FINE
                    FINER
    trace	        FINEST
*/


import nl.knikit.cardgames.DTO.GameDto;
import nl.knikit.cardgames.DTO.PlayerDto;
import nl.knikit.cardgames.mapper.ModelMapperUtil;
import nl.knikit.cardgames.model.AiLevel;
import nl.knikit.cardgames.model.Avatar;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.model.Player;
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

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import lombok.extern.slf4j.Slf4j;

// @RestController = @Controller + @ResponseBody
@CrossOrigin
@RestController
@Component
//@ExposesResourceFor(Player.class) // hateoas
@Slf4j
@Scope("prototype")
@RequestScoped
public class PlayerResource {
	
	// @Resource = javax, @Inject = javax, @Autowire = spring bean factory
	@Autowired
	private IPlayerService playerService;

	@Autowired
	private ModelMapperUtil mapUtil;
	
	@GetMapping("/players/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public ResponseEntity getPlayer(@PathVariable("id") int id) {
		
		try {
			Player player = playerService.findOne(id);
			if (player == null) {
				return ResponseEntity
						       .status(HttpStatus.NOT_FOUND)
						       .body("Player not found");
			}
			PlayerDto playerDto = mapUtil.convertToDto(player);
			return ResponseEntity
					       .status(HttpStatus.OK)
					       .body(playerDto);
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(e);
		}
		
	}
	
	@GetMapping("/players")
	@Produces({MediaType.APPLICATION_JSON})
	public ResponseEntity getPlayers() {
		
		ArrayList<Player> players;
		try {
			players = (ArrayList<Player>) playerService.findAll("human", "DESC");
			ArrayList<PlayerDto> playersDto = new ArrayList<>();
			for (Player player : players) {
				playersDto.add(mapUtil.convertToDto(player));
			}
			return ResponseEntity
					       .status(HttpStatus.OK)
					       .body(playersDto);
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
	
	@GetMapping(value = "/players", params = {"human"})
	@Produces({MediaType.APPLICATION_JSON})
	public ResponseEntity getPlayersWhere(@RequestParam(value = "human", required = true) String param) {
		
		try {
			List<Player> players = playerService.findAllWhere("human", param);
			if (players == null) {
				return ResponseEntity
						       .status(HttpStatus.OK)
						       .body("{}");
			}
			
			ArrayList<PlayerDto> playersDto = new ArrayList<>();
			for (Player player : players) {
				playersDto.add(mapUtil.convertToDto(player));
			}
			return ResponseEntity
					       .status(HttpStatus.OK)
					       .body(playersDto);
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(e);
		}
	}
	
	@PostMapping("/players")
	@Consumes(MediaType.APPLICATION_JSON)
	public ResponseEntity createPlayer(@RequestBody PlayerDto playerDto) throws ParseException {
		
		if (playerDto == null) {
			return ResponseEntity
					       .status(HttpStatus.BAD_REQUEST)
					       .body("Body not present");
		}
		PlayerDto consistentPlayerDto = makeConsistentPlayerDto(playerDto);
		Player consistentPlayer = mapUtil.convertToEntity(consistentPlayerDto);
		try {
			Player createdPlayer = playerService.create(consistentPlayer);
			if (createdPlayer == null) {
				return ResponseEntity
						       .status(HttpStatus.NOT_FOUND)
						       .body("Player not created");
			}
			PlayerDto newPlayerDto = mapUtil.convertToDto(createdPlayer);
			return ResponseEntity
					       .status(HttpStatus.CREATED)
					       .body(newPlayerDto);
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(e);
		}
	}
	
	@PostMapping(value = "/players", params = "human")
	@Consumes(MediaType.APPLICATION_JSON)
	public ResponseEntity initPlayer(@RequestParam(value = "human", required = true) String human) throws ParseException {
		
		if (human == null || human.isEmpty()) {
			return ResponseEntity
					       .status(HttpStatus.BAD_REQUEST)
					       .body("Param human null or empty");
		}
		
		PlayerDto initPlayerDto = new PlayerDto();
		initPlayerDto.setHuman(Boolean.parseBoolean(human));
		initPlayerDto = makeConsistentPlayerDto(initPlayerDto);
		Player consistentPlayer = mapUtil.convertToEntity(initPlayerDto);
		try {
			Player createdPlayer = playerService.create(consistentPlayer);
			if (createdPlayer == null) {
				return ResponseEntity
						       .status(HttpStatus.NOT_FOUND)
						       .body("Player not updated");
			}
			PlayerDto playerDto = mapUtil.convertToDto(createdPlayer);
			return ResponseEntity
					       .status(HttpStatus.CREATED)
					       .body(playerDto);
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(e);
		}
	}
	
	@PutMapping("/players/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public ResponseEntity updatePlayer(@PathVariable int id, @RequestBody PlayerDto updatePlayerDto) throws ParseException {
		// always use the id in the path instead of id in the body
		if (updatePlayerDto == null || id == 0) {
			return ResponseEntity
					       .status(HttpStatus.BAD_REQUEST)
					       .body("Body null or id zero");
		}
		Player player = playerService.findOne(id);
		if (player == null) {
			return ResponseEntity
					       .status(HttpStatus.NOT_FOUND)
					       .body("Player not found");
		}
		Player convertedPlayer = mapUtil.convertToEntity(updatePlayerDto);
		convertedPlayer.setPlayerId(id);
		//Player consistentPlayer = makeConsistentPlayer(convertedPlayer);
		try {
			Player updatedPlayer = playerService.update(convertedPlayer);
			if (updatedPlayer == null) {
				return ResponseEntity
						       .status(HttpStatus.NOT_FOUND)
						       .body("Player not updated");
			}
			PlayerDto updatedPlayerDto = mapUtil.convertToDto(updatedPlayer);
			return ResponseEntity
					       .status(HttpStatus.OK)
					       .body(updatedPlayerDto);
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(e);
		}
	}
	
	@DeleteMapping("/players/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public ResponseEntity deletePlayer(@PathVariable("id") int id) {
		try {
			Player deletePlayer = playerService.findOne(id);
			if (deletePlayer == null) {
				return ResponseEntity
						       .status(HttpStatus.NOT_FOUND)
						       .body("Player not found");
			}
			playerService.deleteOne(deletePlayer);
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
	
	// /players?id=1,2,3,4
	@DeleteMapping(value = "/players", params = {"id"})
	@Produces({MediaType.APPLICATION_JSON})
	public ResponseEntity deletePlayersById(@RequestParam(value = "id", required = false) List<String> ids) {
		
		try {
			for (int i = 0; i < ids.size(); i++) {
				Player deletePlayer = playerService.findOne(Integer.parseInt(ids.get(i)));
				if (deletePlayer == null) {
					return ResponseEntity
							       .status(HttpStatus.NOT_FOUND)
							       .body("Player not found: " + ids.get(i));
				}
			}
			playerService.deleteAllByIds(new Player(), ids);
			return ResponseEntity
					       .status(HttpStatus.NO_CONTENT)
					       .body("");
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(e);
		}
	}
	
	private PlayerDto makeConsistentPlayerDto(PlayerDto playerDto) {
		
		// set defaults for human or aline
		
		if (playerDto.getHuman() == "true") {
			playerDto.setAiLevel(AiLevel.HUMAN);
			if (playerDto.getAlias().isEmpty()){
				playerDto.setAlias("Consistent stranger");
			}
		} else {
			if (playerDto.getAiLevel() == null) {
				playerDto.setAiLevel(AiLevel.NONE);
			}
			if (playerDto.getAlias().isEmpty()){
				playerDto.setAlias("Consistent alien");
			}
		}
		
		// make avatar consistent
		if (playerDto.getAvatar() == null) {
			playerDto.setAvatar(Avatar.ELF);
		}
		
		return playerDto;
	}
	
}
