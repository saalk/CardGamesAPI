package nl.knikit.cardgames.event;

import nl.knikit.cardgames.commons.event.AbstractEvent;
import nl.knikit.cardgames.commons.event.EventOutput;
import nl.knikit.cardgames.mapper.ModelMapperUtil;
import nl.knikit.cardgames.model.AiLevel;
import nl.knikit.cardgames.model.Avatar;
import nl.knikit.cardgames.model.Casino;
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
public class UpdatePlayerForCasinoDetailsEvent extends AbstractEvent {
	
	// @Resource = javax, @Inject = javax, @Autowire = spring bean factory
	@Autowired
	private IPlayerService playerService;
	
	@Autowired
	private ICasinoService casinoService;
	
	@Autowired
	private ModelMapperUtil mapUtil;
	
	@Override
	protected EventOutput execution(final Object... eventInput) {
		
		UpdatePlayerForCasinoDetailsEventDTO flowDTO = (UpdatePlayerForCasinoDetailsEventDTO) eventInput[0];
		EventOutput eventOutput;
		
		
		// get the player and update the playertype and ante
		// init
		Casino casinoToCheck;
		
		Player playerToUpdate;
		Player updatedPlayer;
		
		// find casino to update
		String casinoId = flowDTO.getSuppliedCasinoId();
		try {
			casinoToCheck = casinoService.findOne(Integer.parseInt(casinoId));
			if (casinoToCheck == null) {
				eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
				return eventOutput;
			}
		} catch (Exception e) {
			eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
			return eventOutput;
		}
		
		// check path var player/{id}
		String id = String.valueOf(casinoToCheck.getPlayer().getPlayerId());
		try {
			playerToUpdate = playerService.findOne(Integer.parseInt(id));
			if (playerToUpdate == null) {
				eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
				return eventOutput;
			}
		} catch (Exception e) {
			eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
			return eventOutput;
		}
		
		// do the update
		try {
			updatedPlayer = playerService.update(makeConsistentPlayer(flowDTO, playerToUpdate));
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
		String message = String.format("UpdatePlayerForCasinoDetailsEvent never does no transition");
		log.info(message);
		
		return eventOutput;
	}
	
	public interface UpdatePlayerForCasinoDetailsEventDTO {
		
		// all game fields
		String getSuppliedGameId();
		
		void setCurrentGame(Game game);
		
		Game getCurrentGame();
		
		// rest
		
		void setCurrentPlayer(Player player);
		
		String getSuppliedPlayerId();
		
		String getSuppliedCasinoId();
		
		String getSuppliedHumanOrAi();
		
		String getSuppliedAlias();
		
		Avatar getSuppliedAvatar();
		
		String getSuppliedSecuredLoan();
		
		AiLevel getSuppliedAiLevel();
		
		CardGameStateMachine.Trigger getSuppliedTrigger();
		
	}
	
	private Player makeConsistentPlayer(UpdatePlayerForCasinoDetailsEventDTO flowDTO, Player playerToUpdate) {
		
		// set defaults for human or aline
		if (playerToUpdate.getHuman()) {
			playerToUpdate.setAiLevel(AiLevel.HUMAN);
			if (flowDTO.getSuppliedAlias() != null && !flowDTO.getSuppliedAlias().isEmpty()) {
				playerToUpdate.setAlias(flowDTO.getSuppliedAlias());
			}
		} else {
			if (flowDTO.getSuppliedAiLevel().equals(AiLevel.HUMAN)) {
				playerToUpdate.setAiLevel(AiLevel.MEDIUM);
			} else {
				playerToUpdate.setAiLevel(flowDTO.getSuppliedAiLevel());
			}
			if (flowDTO.getSuppliedAlias() != null && !flowDTO.getSuppliedAlias().isEmpty()) {
				playerToUpdate.setAlias(flowDTO.getSuppliedAlias());
			}
		}
		
		if (flowDTO.getSuppliedAvatar() != null) {
			playerToUpdate.setAvatar(flowDTO.getSuppliedAvatar());
		}
		
		if (flowDTO.getSuppliedSecuredLoan() != null && !flowDTO.getSuppliedSecuredLoan().equals("null") && !flowDTO.getSuppliedSecuredLoan().isEmpty()) {
			playerToUpdate.setSecuredLoan(Integer.parseInt(flowDTO.getSuppliedSecuredLoan()));
		}
		return playerToUpdate;
	}
}

