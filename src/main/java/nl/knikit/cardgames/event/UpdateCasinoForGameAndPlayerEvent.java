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
public class UpdateCasinoForGameAndPlayerEvent extends AbstractEvent {
	
	// @Resource = javax, @Inject = javax, @Autowire = spring bean factory
	@Autowired
	private IGameService gameService;
	
	@Autowired
	private IPlayerService playerService;
	
	@Autowired
	private ICasinoService casinoService;
	
	@Override
	protected EventOutput execution(final Object... eventInput) {
		
		UpdateCasinoForGameAndPlayerEventDTO flowDTO = (UpdateCasinoForGameAndPlayerEventDTO) eventInput[0];
		EventOutput eventOutput;
		
		// check if playing order is up (-1) or down (+1)
		boolean playingOrderChanged = false;
		boolean moveTowardsFirst = false;
		boolean moveTowardsLast = false;
		
		if (flowDTO.getSuppliedPlayingOrder() == null && flowDTO.getSuppliedPlayingOrder().equals("null") && flowDTO.getSuppliedPlayingOrder().isEmpty()) {
			eventOutput = new EventOutput(EventOutput.Result.SUCCESS);
			String message = String.format("UpdateCasinoForGameAndPlayerEvent no getSuppliedPlayingOrder");
			log.info(message);
			return eventOutput;
		} else if (flowDTO.getSuppliedPlayingOrder().equals("-1")) {
			moveTowardsFirst = true;
			playingOrderChanged = true;
		} else if ((flowDTO.getSuppliedPlayingOrder().equals("+1"))) {
			moveTowardsLast = true;
			playingOrderChanged = true;
		} else {
			playingOrderChanged = false;
			moveTowardsFirst = false;
			moveTowardsLast = false;
		}
		
		Game gameToCheck;
		Casino casinoToUpdate;
		Casino otherCasinoToUpdate;
		Casino casinoUpdated = null;
		
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
		
		// find casino to update
		String casinoId = flowDTO.getSuppliedCasinoId();
		try {
			casinoToUpdate = casinoService.findOne(Integer.parseInt(casinoId));
			if (casinoToUpdate == null) {
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
		
		// see if the change can be done
		if ((casinoToUpdate.getPlayingOrder() == 1) && (playingOrderChanged) && (moveTowardsFirst)) {
			playingOrderChanged = false;
		} else if ((casinoToUpdate.getPlayingOrder() == allCasinosForAGame.size()) && (playingOrderChanged) && (moveTowardsLast)) {
			playingOrderChanged = false;
		}
		
		if (playingOrderChanged) {
			// do the switch
			try {
				Integer oldPlayingOrder = casinoToUpdate.getPlayingOrder();
				
				// update the current
				Integer newPlayingOrder = moveTowardsFirst ? (casinoToUpdate.getPlayingOrder() - 1) : (casinoToUpdate.getPlayingOrder() + 1);
				casinoToUpdate.setPlayingOrder(newPlayingOrder);
				casinoUpdated = casinoService.update(casinoToUpdate);
				
				// find the other that is currently on the newPlayingOrder
				otherCasinoToUpdate = casinosSorted.get(newPlayingOrder);
				
				otherCasinoToUpdate.setPlayingOrder(oldPlayingOrder);
				casinoService.update(otherCasinoToUpdate);
				
			} catch (Exception e) {
				eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
				return eventOutput;
			}
			
		}
		
		
		// OK, set a trigger for EventOutput to trigger a transition in the state machine
		flowDTO.setCurrentGame(gameService.findOne(Integer.parseInt(gameId)));
		String message = String.format("UpdateCasinoForGameAndPlayerEvent setCurrentGame is: %s", flowDTO.getCurrentGame());
		log.info(message);
		
		flowDTO.setCurrentCasino(casinoUpdated);
		
		if (flowDTO.getSuppliedTrigger() == CardGameStateMachine.Trigger.DELETE_SETUP_HUMAN)
		
		{
			// key event so do a transition but only when human
			eventOutput = new EventOutput(EventOutput.Result.SUCCESS, flowDTO.getSuppliedTrigger());
			message = String.format("UpdateCasinoForGameAndPlayerEvent do a transition with trigger is: %s", flowDTO.getSuppliedTrigger());
			log.info(message);
		} else
		
		{
			eventOutput = new EventOutput(EventOutput.Result.SUCCESS);
			message = String.format("UpdateCasinoForGameAndPlayerEvent do no transition");
			log.info(message);
		}
		return eventOutput;
	}
	
	public interface UpdateCasinoForGameAndPlayerEventDTO {
		
		// all game fields
		String getSuppliedGameId();
		
		void setCurrentGame(Game game);
		
		Game getCurrentGame();
		
		// rest
		String getSuppliedCasinoId();
		
		void setCurrentCasino(Casino casino);
		
		String getSuppliedPlayingOrder();
		
		CardGameStateMachine.Trigger getSuppliedTrigger();
		
	}
}
