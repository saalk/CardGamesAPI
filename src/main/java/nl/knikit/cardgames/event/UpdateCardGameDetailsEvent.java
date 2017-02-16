package nl.knikit.cardgames.event;

import nl.knikit.cardgames.commons.event.AbstractEvent;
import nl.knikit.cardgames.commons.event.EventOutput;
import nl.knikit.cardgames.mapper.ModelMapperUtil;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.model.GameType;
import nl.knikit.cardgames.model.state.CardGameStateMachine;
import nl.knikit.cardgames.service.IGameService;
import nl.knikit.cardgames.service.IPlayerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UpdateCardGameDetailsEvent extends AbstractEvent {
	
	// @Resource = javax, @Inject = javax, @Autowire = spring bean factory
	@Autowired
	private IGameService gameService;
	
	@Override
	protected EventOutput execution(final Object... eventInput) {
		
		UpdateCardGameDetailsEventDTO flowDTO = (UpdateCardGameDetailsEventDTO) eventInput[0];
		EventOutput eventOutput;
		
		String message = String.format("UpdateCardGameDetailsEvent game to update is: %s", flowDTO.getSuppliedGameId());
		log.info(message);
		
		// get the game and update the gametype and ante
		// init
		Game gameToUpdate = flowDTO.getCurrentGame();
		Game updatedGame;
		
		// check path var game/{id}
//		String gameId = flowDTO.getSuppliedGameId();
//		try {
//			gameToUpdate = gameService.findOne(Integer.parseInt(gameId));
//			message = String.format("UpdateCardGameDetailsEvent game find before update in Event: %s", gameToUpdate);
//			log.info(message);
//			if (gameToUpdate == null) {
//				eventOutput = new EventOutput(EventOutput.Result.FAILURE, CardGameStateMachine.Trigger.ERROR);
//				return eventOutput;
//			}
//		} catch (Exception e) {
//			message = String.format("UpdateCardGameDetailsEvent game find before update has exception: %s", e);
//			log.info(message);
//			eventOutput = new EventOutput(EventOutput.Result.FAILURE, CardGameStateMachine.Trigger.ERROR);
//			return eventOutput;
//		}
		
		// find out what to update
		if (flowDTO.getSuppliedGameType() != null) {
			gameToUpdate.setGameType(flowDTO.getSuppliedGameType());
		}
		if (flowDTO.getSuppliedAnte() != null) {
			gameToUpdate.setAnte(Integer.parseInt(flowDTO.getSuppliedAnte()));
		}
		if (flowDTO.getSuppliedActiveCasino() != 0) {
			gameToUpdate.setActiveCasino(flowDTO.getSuppliedActiveCasino());
		}
		if (flowDTO.getSuppliedCurrentRound() != 0) {
			gameToUpdate.setCurrentRound(flowDTO.getSuppliedCurrentRound());
		}
		
		// do the update
		try {
			updatedGame = gameService.update(gameToUpdate);
			if (updatedGame == null) {
				eventOutput = new EventOutput(EventOutput.Result.FAILURE, CardGameStateMachine.Trigger.ERROR);
				return eventOutput;
			}

		} catch (Exception e) {
			eventOutput = new EventOutput(EventOutput.Result.FAILURE, CardGameStateMachine.Trigger.ERROR);
			return eventOutput;
		}
		
		
		// OK, set a trigger for EventOutput to trigger a transition in the state machine
		flowDTO.setCurrentGame(updatedGame);
		flowDTO.setSuppliedGameId(String.valueOf(updatedGame.getGameId()));
		
		if (flowDTO.getSuppliedTrigger() == CardGameStateMachine.Trigger.POST_INIT) {
			// key event so do a transition
			eventOutput = new EventOutput(EventOutput.Result.SUCCESS, flowDTO.getSuppliedTrigger());
			message = String.format("UpdateCardGameDetailsEvent do a transition with trigger is: %s", flowDTO.getSuppliedTrigger());
			log.info(message);
		} else {
			eventOutput = new EventOutput(EventOutput.Result.SUCCESS);
			message = String.format("UpdateCardGameDetailsEvent do no transition");
			log.info(message);
		}
		
		return eventOutput;
	}
	
	public interface UpdateCardGameDetailsEventDTO {
		
		// all game fields
		String getSuppliedGameId();
		
		void setSuppliedGameId(String gameId);
		
		void setCurrentGame(Game game);
		
		Game getCurrentGame();
		
		// rest
		GameType getSuppliedGameType();
		
		String getSuppliedAnte();
		
		int getSuppliedCurrentRound();
		
		int getSuppliedActiveCasino();
		
		CardGameStateMachine.Trigger getSuppliedTrigger();
		
	}
}
