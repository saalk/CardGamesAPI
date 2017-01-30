package nl.knikit.cardgames.event;

import nl.knikit.cardgames.commons.event.AbstractEvent;
import nl.knikit.cardgames.commons.event.EventOutput;
import nl.knikit.cardgames.model.Casino;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.model.GameType;
import nl.knikit.cardgames.model.state.CardGameStateMachine;
import nl.knikit.cardgames.service.ICasinoService;
import nl.knikit.cardgames.service.IGameService;
import nl.knikit.cardgames.service.IPlayerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UpdateCasinoForTurnAndBetEvent extends AbstractEvent {
	
	// @Resource = javax, @Inject = javax, @Autowire = spring bean factory
	@Autowired
	private IGameService gameService;
	
	@Autowired
	private IPlayerService playerService;
	
	@Autowired
	private ICasinoService casinoService;
	
	@Override
	protected EventOutput execution(final Object... eventInput) {
		
		UpdateCasinoForTurnAndBetEventDTO flowDTO = (UpdateCasinoForTurnAndBetEventDTO) eventInput[0];
		EventOutput eventOutput;
		
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
		
		// get the turn or make it 1
		if (flowDTO.getSuppliedCurrentTurn() != 0) {
			casinoToUpdate.setActiveTurn((flowDTO.getSuppliedCurrentTurn()));
		} else {
			casinoToUpdate.setActiveTurn(1);
		}
		
		String message = String.format("UpdateCasinoForTurnAndBetEvent setActiveTurn is going to be: ",casinoToUpdate.getActiveTurn());
		log.info(message);
		
		// set the bet
		if (gameToCheck.getGameType() == GameType.HIGHLOW) {
			
			// its double or nothing for hi-lo, set the bet so this can be updated for the player
			int raise = 0;
			if (casinoToUpdate.getActiveTurn() > 2) {
				raise = (int) Math.pow(2, casinoToUpdate.getActiveTurn() - 1);
			} else {
				raise = casinoToUpdate.getActiveTurn();
			}
			
			casinoToUpdate.setBet((raise * gameToCheck.getAnte()));
			flowDTO.setSuppliedBet(casinoToUpdate.getBet());
		} else {
			if (flowDTO.getSuppliedBet() != 0) {
				casinoToUpdate.setBet((flowDTO.getSuppliedBet()));
			}
		}
		
		message = String.format("UpdateCasinoForTurnAndBetEvent setBet is going to be: ",casinoToUpdate.getBet());
		log.info(message);
		
		
		// do the update
		try {
			
			casinoUpdated = casinoService.update(casinoToUpdate);
		} catch (Exception e) {
			eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
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
		
		// rest
		String getSuppliedCasinoId();
		
		void setCurrentCasino(Casino casino);
		
		void setSuppliedPlayerId(String playerId);
		
		int getSuppliedCurrentTurn();
		
		int getSuppliedBet();
		
		void setSuppliedBet(int bet);
		
		
		CardGameStateMachine.Trigger getSuppliedTrigger();
		
	}
}
