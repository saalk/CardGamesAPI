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

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CreateCasinoForGameAndPlayerEvent extends AbstractEvent {
	
	// @Resource = javax, @Inject = javax, @Autowire = spring bean factory
	@Autowired
	private IGameService gameService;
	
	@Autowired
	private IPlayerService playerService;
	
	@Autowired
	private ICasinoService casinoService;
	
	@Autowired
	private ModelMapperUtil mapUtil;
	
	@Override
	protected EventOutput execution(final Object... eventInput) {
		
		CreateCasinoForGameAndPlayerEventDTO flowDTO = (CreateCasinoForGameAndPlayerEventDTO) eventInput[0];
		EventOutput eventOutput;
		
		// get the game and update the gametype and ante
		Game gameToUpdate;
		Player playerToUpdate;
		
		Casino casinoToCreate = new Casino();
		Casino createdCasino;
		
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
		
		// do the add
		casinoToCreate.setGame(gameToUpdate);
		casinoToCreate.setPlayer(playerToUpdate);
		// TODO list casinos for game to count
		casinoToCreate.setPlayingOrder(1);
		try {
			createdCasino = casinoService.create(casinoToCreate);
			if (createdCasino == null) {
				eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
				return eventOutput;
			}
		} catch (Exception e) {
			eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
			return eventOutput;
		}
		
		// OK, set a trigger for EventOutput to trigger a transition in the state machine
		flowDTO.setCurrentGame(gameService.findOne(Integer.parseInt(gameId)));
		String message = String.format("UpdateCasinoForGameAndPlayerEvent setCurrentGame is: %s", flowDTO.getCurrentGame());
		log.info(message);

		flowDTO.setCurrentCasino(casinoToCreate);
		flowDTO.setSuppliedCasinoId(String.valueOf(createdCasino.getCasinoId()));
		flowDTO.setCurrentPlayer(playerToUpdate);
		
		
		if (flowDTO.getSuppliedTrigger() == CardGameStateMachine.Trigger.POST_INIT_HUMAN ||
		flowDTO.getSuppliedTrigger() == CardGameStateMachine.Trigger.POST_SETUP_HUMAN ) {
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
	
	public interface CreateCasinoForGameAndPlayerEventDTO {
		
		// all game fields
		String getSuppliedGameId();
		void setCurrentGame(Game game);
		Game getCurrentGame();
		
		// rest
		String getSuppliedPlayerId();
		void setCurrentCasino(Casino casino);
		void setSuppliedCasinoId(String casinoId);
		void setCurrentPlayer(Player player);
		
		CardGameStateMachine.Trigger getSuppliedTrigger();
		
	}
}