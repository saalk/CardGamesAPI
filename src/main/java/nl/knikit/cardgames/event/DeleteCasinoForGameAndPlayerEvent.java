package nl.knikit.cardgames.event;

import nl.knikit.cardgames.commons.event.AbstractEvent;
import nl.knikit.cardgames.commons.event.EventOutput;
import nl.knikit.cardgames.model.Casino;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.model.state.CardGameStateMachine;
import nl.knikit.cardgames.service.ICasinoService;
import nl.knikit.cardgames.service.IGameService;
import nl.knikit.cardgames.service.IPlayerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DeleteCasinoForGameAndPlayerEvent extends AbstractEvent {
	
	// @Resource = javax, @Inject = javax, @Autowire = spring bean factory
	@Autowired
	private IGameService gameService;
	
	@Autowired
	private IPlayerService playerService;
	
	@Autowired
	private ICasinoService casinoService;
	
	@Override
	protected EventOutput execution(final Object... eventInput) {
		
		DeleteCasinoForGameAndPlayerEventDTO flowDTO = (DeleteCasinoForGameAndPlayerEventDTO) eventInput[0];
		EventOutput eventOutput;
		
		// get the game and update the gametype and ante
		Game gameToCheck;
		Casino casinoToDelete;
		Casino otherCasinoToUpdate;
		
		// always check the game
		String gameId = flowDTO.getSuppliedGameId();
		try {
			gameToCheck = gameService.findOne(Integer.parseInt(gameId));
			if (gameToCheck == null) {
				eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
				return eventOutput;
			}
		} catch (Exception e) {
			eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
			return eventOutput;
		}
		
		// find casino to delete
		String casinoId = flowDTO.getSuppliedCasinoId();
		try {
			casinoToDelete = casinoService.findOne(Integer.parseInt(casinoId));
			if (casinoToDelete == null) {
				eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
				return eventOutput;
			}
		} catch (Exception e) {
			eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
			return eventOutput;
		}
		
		// sort casinos on playing order
		List<Casino> allCasinosForAGame = gameToCheck.getCasinos();
		Map<Integer, Casino> casinosSorted = new HashMap<>(); // is sorted key automatically
		for (Casino casino : allCasinosForAGame) {
			casinosSorted.put(casino.getPlayingOrder(), casino);
		}
		
		// find casino to update
		List<Casino> casinos;
		try {
			casinos = casinoService.findAllWhere("game", gameId);
			if (casinos == null) {
				eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
				return eventOutput;
			}
		} catch (Exception e) {
			eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
			return eventOutput;
		}
		
		// do the delete
		try {
			Integer oldPlayingOrder = casinoToDelete.getPlayingOrder();
			
			// delete the current
			casinoService.deleteOne(casinoToDelete);
			
			// change playing order on the others
			for (Casino casino : casinosSorted.values()) {
				if (casino.getPlayingOrder() > oldPlayingOrder) {
					
					casino.setPlayingOrder(casino.getPlayingOrder() - 1);
					casinoService.update(casino);
				}
			}
			
		} catch (Exception e) {
			eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
			return eventOutput;
		}
		
		// OK, set a trigger for EventOutput to trigger a transition in the state machine
		flowDTO.setCurrentGame(gameService.findOne(Integer.parseInt(gameId)));
		String message = String.format("DeleteCasinoForGameAndPlayerEvent setCurrentGame is: %s", flowDTO.getCurrentGame());
		log.info(message);
		
		flowDTO.setCurrentCasino(null);
		
		if (flowDTO.getSuppliedTrigger() == CardGameStateMachine.Trigger.DELETE_SETUP_HUMAN) {
			// key event so do a transition but only when human
			eventOutput = new EventOutput(EventOutput.Result.SUCCESS, flowDTO.getSuppliedTrigger());
			message = String.format("DeleteCasinoForGameAndPlayerEvent do a transition with trigger is: %s", flowDTO.getSuppliedTrigger());
			log.info(message);
		} else {
			eventOutput = new EventOutput(EventOutput.Result.SUCCESS);
			message = String.format("DeleteCasinoForGameAndPlayerEvent do no transition");
			log.info(message);
		}
		
		return eventOutput;
	}
	
	public interface DeleteCasinoForGameAndPlayerEventDTO {
		
		// all game fields
		String getSuppliedGameId();
		
		void setCurrentGame(Game game);
		
		Game getCurrentGame();
		
		// rest
		String getSuppliedCasinoId();
		
		void setCurrentCasino(Casino casino);
		
		CardGameStateMachine.Trigger getSuppliedTrigger();
		
	}
}
