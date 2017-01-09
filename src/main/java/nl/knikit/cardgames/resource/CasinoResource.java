package nl.knikit.cardgames.resource;

import nl.knikit.cardgames.DTO.CasinoDto;
import nl.knikit.cardgames.mapper.ModelMapperUtil;
import nl.knikit.cardgames.model.Casino;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.model.Player;
import nl.knikit.cardgames.service.ICasinoService;
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

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import lombok.extern.slf4j.Slf4j;

// @RestController = @Controller + @ResponseBody
@CrossOrigin
@RestController
@Component
//@ExposesResourceFor(Casino.class)
@Slf4j
@Scope("prototype")
@RequestScoped
public class CasinoResource {

    // @Resource = javax, @Inject = javax, @Autowire = spring bean factory
    @Autowired
    private ICasinoService casinoService;

    @Autowired
    private IGameService gameService;
    
    @Autowired
    private IPlayerService playerService;
    
    @Autowired
    private ModelMapperUtil mapUtil;
    
    @GetMapping("/casinos/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public ResponseEntity getCasino(@PathVariable("id") int id) {
        
        try {
            Casino casino = casinoService.findOne(id);
            if (casino == null) {
                return ResponseEntity
                               .status(HttpStatus.NOT_FOUND)
                               .body("Casino not found");
            }
            CasinoDto casinoDto = mapUtil.convertToDto(casino);
            return ResponseEntity
                           .status(HttpStatus.OK)
                           .body(casinoDto);
        } catch (Exception e) {
            return ResponseEntity
                           .status(HttpStatus.INTERNAL_SERVER_ERROR)
                           .body(e);
        }
    }
    
    @GetMapping("/casinos")
    @Produces({MediaType.APPLICATION_JSON})
    public ResponseEntity getCasinos() {
        
        ArrayList<Casino> casinos;
        try {
            casinos = (ArrayList<Casino>) casinoService.findAll("game", "ASC");
            ArrayList<CasinoDto> casinoDtos = new ArrayList<>();
            for (Casino casino : casinos) {
                casinoDtos.add(mapUtil.convertToDto(casino));
            }
            return ResponseEntity
                           .status(HttpStatus.OK)
                           .body(casinoDtos);
        } catch (Exception e) {
            return ResponseEntity
                           .status(HttpStatus.INTERNAL_SERVER_ERROR)
                           .body(e);
        }
    }
    
    @GetMapping(value = "/casinos", params = {"game", "player"})
    @Produces({MediaType.APPLICATION_JSON})
    public ResponseEntity getCasinosWhere(
    		@RequestParam(value = "game", required = false) String game,
    		@RequestParam(value = "player", required = false) String player) {
        
        try {
	        if ((game == null && player == null) || (game != null && player != null) ) {
		        return ResponseEntity
				               .status(HttpStatus.BAD_REQUEST)
				               .body("Game or Player not present or both present");
	        }
	        List<Casino> casinos;
	        if (game != null) {
		        casinos = casinoService.findAllWhere("game", game);
	        } else {
		        casinos = casinoService.findAllWhere("player", player);
	        }
            if (casinos == null) {
                // getCasinoDtos empty is not an error
                return ResponseEntity
                               .status(HttpStatus.OK)
                               .body("{}");
            }
            ArrayList<CasinoDto> casinosDto = new ArrayList<>();
            for (Casino casino : casinos) {
                casinosDto.add(mapUtil.convertToDto(casino));
            }
            return ResponseEntity
                           .status(HttpStatus.OK)
                           .body(casinosDto);
            
        } catch (Exception e) {
            return ResponseEntity
                           .status(HttpStatus.INTERNAL_SERVER_ERROR)
                           .body(e);
        }
    }
	
	@PostMapping(value = "/casinos", params = {"player"})
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity createCasino(
			                                @RequestBody CasinoDto casinoDto,
			                                @RequestParam(value = "player", required = true) List<String> players) throws Exception {
		// check the gameDto in the CasinoDto
		if (casinoDto == null || casinoDto.getGameDto() == null) {
			return ResponseEntity
					       .status(HttpStatus.BAD_REQUEST)
					       .body("Body or suppliedGameId not present");
		}
		Game game = gameService.findOne((casinoDto.getGameDto().getGameId()));
		if (game == null) {
			return ResponseEntity
					       .status(HttpStatus.NOT_FOUND)
					       .body("Game not found");
		}
		
		// check the playerDto in the CasinoDto
		if (casinoDto.getPlayerDto() != null) {
			return ResponseEntity
					       .status(HttpStatus.BAD_REQUEST)
					       .body("Player playerDto for new casino not null");
		}
		
		// check param player
		List<Player> addPlayers = new ArrayList<>();
		if (players == null ) {
			return ResponseEntity
					       .status(HttpStatus.BAD_REQUEST)
					       .body("Param player not present: " + players);
		}
		try {
			for (int i = 0; i < players.size(); i++) {
				Player addPlayer = playerService.findOne(Integer.parseInt(players.get(i)));
				if (addPlayer == null) {
					return ResponseEntity
							       .status(HttpStatus.NOT_FOUND)
							       .body("Player not found for param: " + players.get(i));
				}
				addPlayers.add(addPlayer);
			}
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(e);
		}
		
		// loop over the players in the param and create casinos
		int order = 1;
		List<CasinoDto> newCasinoDtos = new ArrayList<>();
		for (Player player : addPlayers) {
			Casino newCasino= new Casino();
			newCasino.setPlayer(player);
			newCasino.setPlayingOrder(order++);
			newCasino.setGame(game);
			try {
				Casino createdCasino = casinoService.create(newCasino);
				if (createdCasino == null) {
					return ResponseEntity
							       .status(HttpStatus.NOT_FOUND)
							       .body("Casino not created");
				}
				newCasinoDtos.add(mapUtil.convertToDto(createdCasino));
				
			} catch (Exception e) {
				return ResponseEntity
						       .status(HttpStatus.INTERNAL_SERVER_ERROR)
						       .body(e);
			}
		}
		return ResponseEntity
				       .status(HttpStatus.CREATED)
				       .body(newCasinoDtos);
	}
	
	
	// a body is always needed but can be {}
	@PutMapping(value = "/casinos/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity updateCasino(
			                                @PathVariable("id") Integer pathId,
			                                @RequestBody CasinoDto casinoDtoToUpdate,
			                                @RequestParam(value = "playingOrder", required = true) Integer playingOrder) throws ParseException {
		// init
		CasinoDto updatedCasinoDto;
		Casino casinoToUpdate;
		Casino updatedCasino;
		Player player;
		
		// check path var casinos/{id} and the param playingOrder
		int id = pathId;
		if (playingOrder == null || playingOrder == 0) {
			return ResponseEntity
					       .status(HttpStatus.BAD_REQUEST)
					       .body("Id in /casinos{id}?playingOrder={playingOrder} null or zero");
		} else {
			try {
				casinoToUpdate = casinoService.findOne(id);
				if (casinoToUpdate == null) {
					return ResponseEntity
							       .status(HttpStatus.NOT_FOUND)
							       .body("Casino for /casinos{id}?playingOrder={playingOrder} not found");
				}
			} catch (Exception e) {
				return ResponseEntity
						       .status(HttpStatus.INTERNAL_SERVER_ERROR)
						       .body(e);
			}
		}
		
		// do the update
		casinoToUpdate.setPlayingOrder(playingOrder);
		try {
			updatedCasino = casinoService.update(casinoToUpdate);
			if (updatedCasino == null) {
				return ResponseEntity
						       .status(HttpStatus.NOT_FOUND)
						       .body("Casino in /casinos{id}?player={player} could not be updated");
			}
			updatedCasinoDto = mapUtil.convertToDto(updatedCasino);
			return ResponseEntity
					       .status(HttpStatus.OK)
					       .body(updatedCasinoDto);
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(e);
		}
	}
	
	@DeleteMapping("/casinos/{id}")
	public ResponseEntity deleteCasino(@PathVariable("id") int id) {
		
		try {
			Casino deleteCasino = casinoService.findOne(id);
			if (deleteCasino == null) {
				return ResponseEntity
						       .status(HttpStatus.NOT_FOUND)
						       .body("Casino not found");
			}
			casinoService.deleteOne(deleteCasino);
			return ResponseEntity
					       .status(HttpStatus.NO_CONTENT)
					       .body("");
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(e);
		}
	}
	
	@DeleteMapping(value = "/casinos", params = {"id"})
	public ResponseEntity deleteCasinosById(
			                                     @RequestParam(value = "id", required = false) List<String> ids) {
		
		try {
			for (int i = 0; i < ids.size(); i++) {
				Casino deleteCasino = casinoService.findOne(Integer.parseInt(ids.get(i)));
				if (deleteCasino == null) {
					return ResponseEntity
							       .status(HttpStatus.NOT_FOUND)
							       .body("Casino not found: " + ids.get(i));
				}
			}
			casinoService.deleteAllByIds(new Casino(), ids);
			return ResponseEntity
					       .status(HttpStatus.NO_CONTENT)
					       .body("");
		} catch (Exception e) {
			return ResponseEntity
					       .status(HttpStatus.INTERNAL_SERVER_ERROR)
					       .body(e);
		}
	}
}
