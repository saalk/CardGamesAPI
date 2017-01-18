package nl.knikit.cardgames.event;

import nl.knikit.cardgames.commons.event.AbstractEvent;
import nl.knikit.cardgames.commons.event.EventOutput;
import nl.knikit.cardgames.mapper.ModelMapperUtil;
import nl.knikit.cardgames.model.Casino;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.model.Player;
import nl.knikit.cardgames.model.state.CardGameStateMachine;
import nl.knikit.cardgames.service.ICasinoService;
import nl.knikit.cardgames.service.IGameService;
import nl.knikit.cardgames.service.IPlayerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
		Game gameToUpdate;
		Player playerToRemove;
		
		Casino casinoToDelete = new Casino();
		
		String gameId = flowDTO.getSuppliedGameId();
		try {
			gameToUpdate = gameService.findOne(Integer.parseInt(gameId));
			if (gameToUpdate == null) {
				eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
				return eventOutput;
			}
		} catch (Exception e) {
			eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
			return eventOutput;
		}
		
		String playerId = flowDTO.getSuppliedPlayerId();
		try {
			playerToRemove = playerService.findOne(Integer.parseInt(playerId));
			if (playerToRemove == null) {
				eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
				return eventOutput;
			}
		} catch (Exception e) {
			eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
			return eventOutput;
		}
		
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
		
		// do the remove
		boolean found = false;
		for (Casino casino : casinos) {
			if (casino.getPlayer().equals(playerToRemove.getPlayerId())) {
				try {
					casinoService.deleteOne(casino);
				} catch (Exception e) {
					eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
					return eventOutput;
				}
				found = true;
			}
		}
		if (!found) {
			eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
			return eventOutput;
		}
		
		// OK, set a trigger for EventOutput to trigger a transition in the state machine
		flowDTO.setCurrentGame(gameService.findOne(Integer.parseInt(gameId)));
		String message = String.format("DeleteCasinoForGameAndPlayerEvent setCurrentGame is: %s", flowDTO.getCurrentGame());
		log.info(message);
		
		flowDTO.setCurrentCasino(null);
		flowDTO.setCurrentPlayer(playerToRemove);
		
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
		
		String getSuppliedPlayerId();
		void setCurrentCasino(Casino casino);
		void setCurrentPlayer(Player player);
		
		CardGameStateMachine.Trigger getSuppliedTrigger();
		
	}
}
