package nl.knikit.cardgames.event;

import nl.knikit.cardgames.commons.event.AbstractEvent;
import nl.knikit.cardgames.commons.event.EventOutput;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.model.Player;
import nl.knikit.cardgames.model.state.CardGameStateMachine;
import nl.knikit.cardgames.service.ICasinoService;
import nl.knikit.cardgames.service.IPlayerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UpdatePlayerCubitsAndSecuredLoanEvent extends AbstractEvent {
	
	// @Resource = javax, @Inject = javax, @Autowire = spring bean factory
	@Autowired
	private IPlayerService playerService;
	
	@Autowired
	private ICasinoService casinoService;
	
	@Override
	protected EventOutput execution(final Object... eventInput) {
		
		UpdatePlayerCubitsAndSecuredLoanEventDTO flowDTO = (UpdatePlayerCubitsAndSecuredLoanEventDTO) eventInput[0];
		EventOutput eventOutput;
		String message;
		
		
		message = String.format("UpdatePlayerCubitsAndSecuredLoanEvent getSuppliedPlayerId is: %s", flowDTO.getSuppliedPlayerId());
		log.info(message);
		
		// skip this event when no cubits and securedloan are supplied
		if ((flowDTO.getSuppliedCubits() == null || flowDTO.getSuppliedCubits().equals("null") || flowDTO.getSuppliedCubits().isEmpty()) &&
				    (flowDTO.getSuppliedSecuredLoan() == null || flowDTO.getSuppliedSecuredLoan().equals("null") || flowDTO.getSuppliedSecuredLoan().isEmpty())) {
			// no key event no a transition
			eventOutput = new EventOutput(EventOutput.Result.SUCCESS);
			
			message = String.format("UpdatePlayerCubitsAndSecuredLoanEvent skip this event when no cubits and securedloan are supplied trigger is: %s", flowDTO.getSuppliedTrigger());
			log.info(message);
			
			return eventOutput;
		}
		
		// get the player and update
		Player playerToUpdate;
		Player updatedPlayer;
		

		
		// find player to update
		String id = flowDTO.getSuppliedPlayerId();
		try {
			playerToUpdate = playerService.findOne(Integer.parseInt(id));
			if (playerToUpdate == null) {
				
				message = String.format("UpdatePlayerCubitsAndSecuredLoanEvent playerToUpdate 404: %s", id);
				log.info(message);
				
				
				eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
				return eventOutput;
			}
		} catch (Exception e) {
			
			message = String.format("UpdatePlayerCubitsAndSecuredLoanEvent playerToUpdate 404: %s", e);
			log.info(message);
			
			eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
			return eventOutput;
		}
		
		message = String.format("UpdatePlayerCubitsAndSecuredLoanEvent playerToUpdate is: %s", playerToUpdate);
		log.info(message);
		
		// do the update
		playerToUpdate = makeConsistentCubitsAndSecuredLoan(flowDTO, playerToUpdate);
		try {
			updatedPlayer = playerService.update(playerToUpdate);
			if (updatedPlayer == null) {
				eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
				return eventOutput;
			}
		} catch (Exception e) {
			eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
			return eventOutput;
		}
		
		flowDTO.setCurrentPlayer(updatedPlayer);
		
		// never do a transition, this is no key event
		eventOutput = new EventOutput(EventOutput.Result.SUCCESS);
		message = String.format("UpdatePlayerCubitsAndSecuredLoanEvent never does transition");
		log.info(message);
		
		return eventOutput;
	}
	
	public interface UpdatePlayerCubitsAndSecuredLoanEventDTO {
		
		// all game fields
		String getSuppliedGameId();
		
		void setCurrentGame(Game game);
		
		Game getCurrentGame();
		
		// rest
		
		void setCurrentPlayer(Player player);
		
		String getSuppliedPlayerId();
		
		String getSuppliedSecuredLoan();
		
		String getSuppliedCubits();
		
		CardGameStateMachine.Trigger getSuppliedTrigger();
		
	}
	
	private Player makeConsistentCubitsAndSecuredLoan(UpdatePlayerCubitsAndSecuredLoanEventDTO flowDTO, Player player) {
		
		String message = String.format("UpdatePlayerCubitsAndSecuredLoanEvent makeConsistentCubitsAndSecuredLoan is: %s", player);
		log.info(message);
		
		int oldSecuredLoan = player.getSecuredLoan();
		
		if ((flowDTO.getSuppliedCubits() != null) && (!flowDTO.getSuppliedCubits().equals("null")) && (!flowDTO.getSuppliedCubits().isEmpty())) {
			
			player.setCubits(player.getCubits() + Integer.parseInt(flowDTO.getSuppliedCubits()));
			
			message = String.format("UpdatePlayerCubitsAndSecuredLoanEvent setCubits is: %s", player.getCubits());
			log.info(message);
		}
		if ((flowDTO.getSuppliedSecuredLoan() != null) && (!flowDTO.getSuppliedSecuredLoan().equals("null")) && (!flowDTO.getSuppliedSecuredLoan().isEmpty())) {
			
			player.setSecuredLoan(Integer.parseInt(flowDTO.getSuppliedSecuredLoan()));
			
			message = String.format("UpdatePlayerCubitsAndSecuredLoanEvent setSecuredLoan is: %s", player.getSecuredLoan());
			log.info(message);
			
			int cubitsRaise = player.getSecuredLoan() - oldSecuredLoan;
			player.setCubits(player.getCubits() + cubitsRaise);
			
		}
		return player;
	}
}

