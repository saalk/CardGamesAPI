package nl.knikit.cardgames.event;

import nl.knikit.cardgames.commons.event.AbstractEvent;
import nl.knikit.cardgames.commons.event.EventOutput;
import nl.knikit.cardgames.mapper.ModelMapperUtil;
import nl.knikit.cardgames.model.AiLevel;
import nl.knikit.cardgames.model.Avatar;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.model.Player;
import nl.knikit.cardgames.model.state.CardGameStateMachine;
import nl.knikit.cardgames.service.IPlayerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UpdatePlayerDetailsEvent extends AbstractEvent {
	
	// @Resource = javax, @Inject = javax, @Autowire = spring bean factory
	@Autowired
	private IPlayerService playerService;
	
	@Autowired
	private ModelMapperUtil mapUtil;
	
	@Override
	protected EventOutput execution(final Object... eventInput) {
		
		UpdatePlayerDetailsEventDTO flowDTO = (UpdatePlayerDetailsEventDTO) eventInput[0];
		EventOutput eventOutput;
		
		
		// get the player and update the playertype and ante
		// init
		Player playerToUpdate;
		Player updatedPlayer;
		
		// check path var player/{id}
		String id = flowDTO.getSuppliedPlayerId();
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
			updatedPlayer = playerService.update(makeConsistentPlayer(flowDTO));
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
		String message = String.format("UpdatePlayerDetailsEvent never does no transition");
		log.info(message);
		
		return eventOutput;
	}
	
	public interface UpdatePlayerDetailsEventDTO {
		
		// all game fields
		String getSuppliedGameId();
		
		void setCurrentGame(Game game);
		
		Game getCurrentGame();
		
		void setCurrentPlayer(Player player);
		
		String getSuppliedPlayerId();
		
		String getSuppliedHumanOrAi();
		
		String getSuppliedAlias();
		
		Avatar getSuppliedAvatar();
		
		String getSuppliedSecuredLoan();
		
		AiLevel getSuppliedAiLevel();
		
		CardGameStateMachine.Trigger getSuppliedTrigger();
		
	}
	
	private Player makeConsistentPlayer(UpdatePlayerDetailsEventDTO flowDTO) {
		
		// set defaults for human or aline
		Player player = new Player();
		if (flowDTO.getSuppliedHumanOrAi().equals("human")) {
			player.setAiLevel(AiLevel.HUMAN);
			player.setHuman(true);
			if (flowDTO.getSuppliedAlias() != null && flowDTO.getSuppliedAlias().isEmpty()) {
				player.setAlias("Stranger");
			} else {
				player.setAlias(flowDTO.getSuppliedAlias());
			}
		} else {
			player.setHuman(false);
			if (flowDTO.getSuppliedAiLevel().equals(AiLevel.HUMAN)) {
				player.setAiLevel(AiLevel.MEDIUM);
			} else {
				player.setAiLevel(flowDTO.getSuppliedAiLevel());
			}
			if (flowDTO.getSuppliedAlias() != null && flowDTO.getSuppliedAlias().isEmpty()) {
				player.setAlias("Alien");
			} else {
				player.setAlias(flowDTO.getSuppliedAlias());
			}
		}
		
		if (flowDTO.getSuppliedAvatar() != null) {
			player.setAvatar(flowDTO.getSuppliedAvatar());
		}
		
		if (flowDTO.getSuppliedSecuredLoan() != null && !flowDTO.getSuppliedSecuredLoan().equals("null") && !flowDTO.getSuppliedSecuredLoan().isEmpty()) {
			player.setSecuredLoan(Integer.parseInt(flowDTO.getSuppliedSecuredLoan()));
		}
		return player;
	}
}
