package nl.knikit.cardgames.event;

import nl.knikit.cardgames.commons.event.AbstractEvent;
import nl.knikit.cardgames.commons.event.EventOutput;
import nl.knikit.cardgames.model.Casino;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.model.state.CardGameStateMachine;
import nl.knikit.cardgames.service.ICasinoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UpdateCasinoForTurnAndBetEvent extends AbstractEvent {
	
	// @Resource = javax, @Inject = javax, @Autowire = spring bean factory
	@Autowired
	private ICasinoService casinoService;
	
	@Override
	protected EventOutput execution(final Object... eventInput) {
		
		UpdateCasinoForTurnAndBetEventDTO flowDTO = (UpdateCasinoForTurnAndBetEventDTO) eventInput[0];
		EventOutput eventOutput;
		String message;
		
		// INIT
		Casino casinoToUpdate = flowDTO.getCurrentCasino();
		Casino casinoUpdated;
		
		if (flowDTO.getNewCurrentTurn() != 0) {
			casinoToUpdate.setActiveTurn((flowDTO.getNewCurrentTurn()));
		}
		if (flowDTO.getNewBet() != 0) {
			casinoToUpdate.setBet((flowDTO.getNewBet()));
		}
		
		message = String.format("UpdateCasinoForTurnAndBetEvent setBet is going to be: ", casinoToUpdate.getBet());
		log.info(message);
		
		// UPDATE
		try {
			casinoUpdated = casinoService.update(casinoToUpdate);
		} catch (Exception e) {
			eventOutput = new EventOutput(EventOutput.Result.FAILURE, CardGameStateMachine.Trigger.ERROR);
			return eventOutput;
		}
		flowDTO.setCurrentCasino(casinoUpdated);
		flowDTO.setSuppliedPlayerId(String.valueOf(casinoUpdated.getPlayer().getPlayerId()));
		
		eventOutput = new EventOutput(EventOutput.Result.SUCCESS);
		message = String.format("UpdateCasinoForTurnAndBetEvent do no transition");
		log.info(message);
		return eventOutput;
	}
	
	public interface UpdateCasinoForTurnAndBetEventDTO {
		
		// all game fields
		String getSuppliedGameId();
		
		void setCurrentGame(Game game);
		
		Game getCurrentGame();
		
		Casino getCurrentCasino();
		
		// rest
		void setCurrentCasino(Casino casino);
		
		void setSuppliedPlayerId(String playerId);
		
		int getNewCurrentTurn();
		
		int getNewBet();
		
		CardGameStateMachine.Trigger getSuppliedTrigger();
		
	}
}
