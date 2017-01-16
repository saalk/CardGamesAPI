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
	
	@Autowired
	private IPlayerService playerService;
	
	@Autowired
	private ModelMapperUtil mapUtil;
	
	@Override
	protected EventOutput execution(final Object... eventInput) {
		
		UpdateCardGameDetailsEventDTO flowDTO = (UpdateCardGameDetailsEventDTO) eventInput[0];
		EventOutput eventOutput;
		
		String message = String.format("UpdateCardGameDetailsEvent game to update is: %s", flowDTO.getSuppliedGameId());
		log.info(message);
		
		// get the game and update the gametype and ante
		// init
		Game gameToUpdate;
		Game updatedGame;
		
		// check path var game/{id}
		String id = flowDTO.getSuppliedGameId();
		try {
			gameToUpdate = gameService.findOne(Integer.parseInt(id));
			message = String.format("UpdateCardGameDetailsEvent game find before update in Event: %s", gameToUpdate);
			log.info(message);
			if (gameToUpdate == null) {
				eventOutput = new EventOutput(EventOutput.Result.FAILURE, CardGameStateMachine.Trigger.ERROR);
				return eventOutput;
			}
		} catch (Exception e) {
			message = String.format("UpdateCardGameDetailsEvent game find before update has exception: %s", e);
			log.info(message);
			eventOutput = new EventOutput(EventOutput.Result.FAILURE, CardGameStateMachine.Trigger.ERROR);
			return eventOutput;
		}
	
		// do the update
		gameToUpdate.setGameType(flowDTO.getSuppliedGameType());
		gameToUpdate.setAnte(Integer.parseInt(flowDTO.getSuppliedAnte()));
		
		message = String.format("UpdateCardGameDetailsEvent gameType before update has details: %s", flowDTO.getSuppliedGameType());
		log.info(message);
		
		try {
			updatedGame = gameService.update(gameToUpdate);
			if (updatedGame == null) {
				eventOutput = new EventOutput(EventOutput.Result.FAILURE, CardGameStateMachine.Trigger.ERROR);
				return eventOutput;
			}
			message = String.format("UpdateCardGameDetailsEvent game update in Event: %s", id);
			log.info(message);
		} catch (Exception e) {
			eventOutput = new EventOutput(EventOutput.Result.FAILURE, CardGameStateMachine.Trigger.ERROR);
			return eventOutput;
		}
		
		
		// OK, set a trigger for EventOutput to trigger a transition in the state machine
		flowDTO.setCurrentGame(updatedGame);
		eventOutput = new EventOutput(EventOutput.Result.SUCCESS, flowDTO.getSuppliedTrigger());
		return eventOutput;
	}
	
	public interface UpdateCardGameDetailsEventDTO {
		
		void setCurrentGame(Game game);
		
		String getSuppliedGameId();
		
		GameType getSuppliedGameType();
		
		String getSuppliedAnte();
		
		CardGameStateMachine.Trigger getSuppliedTrigger();
		
	}
}
