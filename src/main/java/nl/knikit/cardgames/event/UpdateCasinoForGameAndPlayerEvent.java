package nl.knikit.cardgames.event;

import nl.knikit.cardgames.commons.event.AbstractEvent;
import nl.knikit.cardgames.commons.event.EventOutput;
import nl.knikit.cardgames.model.Casino;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.model.Player;
import nl.knikit.cardgames.model.state.CardGameStateMachine;
import nl.knikit.cardgames.service.ICasinoService;
import nl.knikit.cardgames.service.IGameService;
import nl.knikit.cardgames.service.IPlayerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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
		
		String upOrDown;
		if (flowDTO.getSuppliedPlayingOrder() == null && flowDTO.getSuppliedPlayingOrder().equals("null") && flowDTO.getSuppliedPlayingOrder().isEmpty()) {
			eventOutput = new EventOutput(EventOutput.Result.SUCCESS);
			String message = String.format("UpdateCasinoForGameAndPlayerEvent no getSuppliedPlayingOrder");
			log.info(message);
			return eventOutput;
		} else if (flowDTO.getSuppliedPlayingOrder().equals("-1")) {
			upOrDown = "up";
		} else {
			upOrDown = "down";
		}
		
		// get the game and update the gametype and ante
		Game gameToUpdate;
		Player playerToUpdate;
		
		Casino casinoToUpdate = new Casino();
		Casino otherCasinoToUpdate = new Casino();
		
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
			playerToUpdate = playerService.findOne(Integer.parseInt(playerId));
			if (playerToUpdate == null) {
				eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
				return eventOutput;
			}
		} catch (Exception e) {
			eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
			return eventOutput;
		}
		
		List<Casino> casinos; //TODO should be ordered with a set
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
		
		// find the player
		int position = 0;
		int found = 0;
		for (Casino casino : casinos) {
			position += 1;
			if (casino.getPlayer().equals(playerToUpdate.getPlayerId())) {
				found = position;
			}
		}
		if (found == 0 || (found == 1 && upOrDown.equals("up")) || (found == casinos.size() && upOrDown.equals("down"))) {
			eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
			return eventOutput;
		}
		
		// do the switch
		try {
			casinoToUpdate = casinos.get(position - 1);
			int newPlayingOrder = upOrDown.equals("up") ? (casinoToUpdate.getPlayingOrder() - 1) : (casinoToUpdate.getPlayingOrder() + 1);
			casinoToUpdate.setPlayingOrder(newPlayingOrder);
			casinoToUpdate = casinoService.update(casinoToUpdate);
			
			
			otherCasinoToUpdate = upOrDown.equals("up") ? casinos.get(position - 2) : casinos.get(position);
			int newOtherPlayingOrder = upOrDown.equals("up") ? (otherCasinoToUpdate.getPlayingOrder() + 1) : (otherCasinoToUpdate.getPlayingOrder() - 1);
			otherCasinoToUpdate.setPlayingOrder(newOtherPlayingOrder);
			otherCasinoToUpdate = casinoService.update(otherCasinoToUpdate);
			
		} catch (Exception e) {
			eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
			return eventOutput;
		}
		
		// OK, set a trigger for EventOutput to trigger a transition in the state machine
		flowDTO.setCurrentGame(gameService.findOne(Integer.parseInt(gameId)));
		String message = String.format("UpdateCasinoForGameAndPlayerEvent setCurrentGame is: %s", flowDTO.getCurrentGame());
		log.info(message);
		
		flowDTO.setCurrentCasino(casinoToUpdate);
		
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
		String getSuppliedPlayerId();
		void setCurrentCasino(Casino casino);
		String getSuppliedPlayingOrder();
		void setCurrentPlayer(Player player);
		
		CardGameStateMachine.Trigger getSuppliedTrigger();
		
	}
}
