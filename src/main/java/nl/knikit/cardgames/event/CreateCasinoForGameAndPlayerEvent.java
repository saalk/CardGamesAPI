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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CreateCasinoForGameAndPlayerEvent extends AbstractEvent {
	
	// @Resource = javax, @Inject = javax, @Autowire = spring bean factory
//	@Autowired
//	private IGameService gameService;
//
//	@Autowired
//	private IPlayerService playerService;
//
	@Autowired
	private ICasinoService casinoService;
	
//	@Autowired
//	private ModelMapperUtil mapUtil;
	
	@Override
	protected EventOutput execution(final Object... eventInput) {
		
		CreateCasinoForGameAndPlayerEventDTO flowDTO = (CreateCasinoForGameAndPlayerEventDTO) eventInput[0];
		EventOutput eventOutput;
		
		// check the game
		Game gameToCheck = flowDTO.getCurrentGame();
		Player playerToCheck = flowDTO.getCurrentPlayer();
		
		Casino casinoToCreate = new Casino();
		Casino createdCasino;
		
		List<Casino> existingCasinos = flowDTO.getCurrentCasinos();
		
		// do the add
		casinoToCreate.setGame(gameToCheck);
		casinoToCreate.setPlayer(playerToCheck);
		casinoToCreate.setPlayingOrder(existingCasinos.size() + 1);
		casinoToCreate.setBet(0);
		casinoToCreate.setActiveTurn(0);
		
		try {
			createdCasino = casinoService.create(casinoToCreate);
			if (createdCasino == null) {
				eventOutput = new EventOutput(EventOutput.Result.FAILURE, CardGameStateMachine.Trigger.ERROR);
				return eventOutput;
			}
		} catch (Exception e) {
			eventOutput = new EventOutput(EventOutput.Result.FAILURE, CardGameStateMachine.Trigger.ERROR);
			return eventOutput;
		}
		
		// OK, set a trigger for EventOutput to trigger a transition in the state machine
		//flowDTO.setCurrentGame(gameService.findOne(Integer.parseInt(gameId)));
		String message = String.format("CreateCasinoForGameAndPlayerEvent setCurrentGame is: %s", flowDTO.getCurrentGame());
		log.info(message);
		
		flowDTO.setCurrentCasino(createdCasino);
		flowDTO.setSuppliedCasinoId(String.valueOf(createdCasino.getCasinoId()));
		
		
		if (flowDTO.getSuppliedTrigger() == CardGameStateMachine.Trigger.POST_INIT_HUMAN ||
				    flowDTO.getSuppliedTrigger() == CardGameStateMachine.Trigger.POST_SETUP_HUMAN ||
				    flowDTO.getSuppliedTrigger() == CardGameStateMachine.Trigger.POST_HUMAN) {
			// key event so do a transition
			eventOutput = new EventOutput(EventOutput.Result.SUCCESS, flowDTO.getSuppliedTrigger());
			message = String.format("CreateCasinoForGameAndPlayerEvent do a transition with trigger is: %s", flowDTO.getSuppliedTrigger());
			log.info(message);
		} else {
			eventOutput = new EventOutput(EventOutput.Result.SUCCESS);
			message = String.format("CreateCasinoForGameAndPlayerEvent do no transition");
			log.info(message);
		}
		
		return eventOutput;
	}
	
	public interface CreateCasinoForGameAndPlayerEventDTO {
		
		// all game fields
		String getSuppliedGameId();
		
		void setCurrentGame(Game game);
		
		Game getCurrentGame();
		
		Player getCurrentPlayer();
		
		// rest
		String getSuppliedPlayerId();
		
		void setCurrentCasino(Casino casino);
		
		List<Casino> getCurrentCasinos();
		
		void setSuppliedCasinoId(String casinoId);
		
		CardGameStateMachine.Trigger getSuppliedTrigger();
		
	}
}
