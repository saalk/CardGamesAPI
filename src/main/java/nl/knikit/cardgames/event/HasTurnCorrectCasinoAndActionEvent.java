package nl.knikit.cardgames.event;

import nl.knikit.cardgames.commons.event.AbstractEvent;
import nl.knikit.cardgames.commons.event.EventOutput;
import nl.knikit.cardgames.mapper.ModelMapperUtil;
import nl.knikit.cardgames.model.CardAction;
import nl.knikit.cardgames.model.CardLocation;
import nl.knikit.cardgames.model.Casino;
import nl.knikit.cardgames.model.Deck;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.model.Hand;
import nl.knikit.cardgames.model.state.CardGameStateMachine;
import nl.knikit.cardgames.service.ICardService;
import nl.knikit.cardgames.service.ICasinoService;
import nl.knikit.cardgames.service.IGameService;
import nl.knikit.cardgames.service.IHandService;
import nl.knikit.cardgames.service.IPlayerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class HasTurnCorrectCasinoAndActionEvent extends AbstractEvent {
	
	// @Resource = javax, @Inject = javax, @Autowire = spring bean factory
	@Autowired
	private IGameService gameService;
	
	@Autowired
	private IPlayerService playerService;
	
	@Autowired
	private ICasinoService casinoService;
	
	@Autowired
	private ICardService cardService;
	
	@Autowired
	private IHandService handService;
	
	@Autowired
	private ModelMapperUtil mapUtil;
	
	@Override
	protected EventOutput execution(final Object... eventInput) {
		
		HasTurnCorrectCasinoAndActionEventDTO flowDTO = (HasTurnCorrectCasinoAndActionEventDTO) eventInput[0];
		EventOutput eventOutput = null;
		
		String message = String.format("HasTurnCorrectCasinoAndActionEvent setCurrentGame: %s", flowDTO.getCurrentGame());
		log.info(message);
		
		message = String.format("HasTurnCorrectCasinoAndActionEvent CardAction: %s", flowDTO.getSuppliedCardAction());
		log.info(message);
		
		message = String.format("HasTurnCorrectCasinoAndActionEvent CardLocation: %s", flowDTO.getSuppliedCardLocation());
		log.info(message);
		
		// init all the object and lists
		Game gameToCheck;
		Casino casinoToCheck;
		List<Hand> handsToCheck;
		
		// check the game
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
		
		// check the casino
		String casinoId = flowDTO.getSuppliedCasinoId();
		try {
			casinoToCheck = casinoService.findOne(Integer.parseInt(casinoId));
			if (casinoToCheck == null || (casinoToCheck.getGame().getGameId() != Integer.parseInt(gameId))) {
				eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
				return eventOutput;
			}
		} catch (Exception e) {
			eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
			return eventOutput;
		}
		
		// find all the current Hands for the Casino
		try {
			handsToCheck = handService.findAllWhere("casino", casinoId);
		} catch (Exception e) {
			eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
			return eventOutput;
		}
		
		// sort on card order
		Collections.sort(handsToCheck, Comparator.comparing(Hand::getCardOrder).thenComparing(Hand::getCardOrder));
		
		// do the check
		
		try {
			
			for (Deck deck : flowDTO.getDecks()) {
				
/**			check if
			1. casino supplied is the casino that is allowed to turn
            2. action is correct for player: human or ai
            3. when human
            - HIGHER or LOWER -> check if lastHand is DEAL
            - PASS -> check if lastHands starting from DEAL are equal or more than (min turns)
            - cardTotal -> check if card total is correct for gametype
            4. when ai
            -
 **/
				
				
			}
		} catch (Exception e) {
			eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
			return eventOutput;
		}
		
		// OK, set a trigger for EventOutput to trigger a transition in the state machine
		flowDTO.setCurrentGame(gameService.findOne(Integer.parseInt(gameId)));

		
		
		if (true) {
			eventOutput = new EventOutput(EventOutput.Result.SUCCESS);
			message = String.format("HasTurnCorrectCasinoAndActionEvent do no transition");
			log.info(message);
		} else {
			
		}
		return eventOutput;
	}
	
	public interface HasTurnCorrectCasinoAndActionEventDTO {
		
		// all game and trigger fields
		String getSuppliedGameId();
		
		CardGameStateMachine.Trigger getSuppliedTrigger();
		
		Game getCurrentGame();
		
		void setCurrentGame(Game game);
		
		void setSuppliedTrigger(CardGameStateMachine.Trigger trigger);
		
		// the rest of the supplied fields
		String getSuppliedCasinoId();
		
		CardAction getSuppliedCardAction();
		
		String getSuppliedTotal();
		
		CardLocation getSuppliedCardLocation();
		
		// get the data created by other events
		
		List<Deck> getDecks();
		
		// pass on the data created here for other events
		
		void setHands(List<Hand> hands);
	}
}
