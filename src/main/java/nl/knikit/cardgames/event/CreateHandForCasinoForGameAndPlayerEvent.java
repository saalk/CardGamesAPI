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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CreateHandForCasinoForGameAndPlayerEvent extends AbstractEvent {
	
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
		
		CreateHandForCasinoForGameAndPlayerEventDTO flowDTO = (CreateHandForCasinoForGameAndPlayerEventDTO) eventInput[0];
		EventOutput eventOutput;
		
		// find all decks to update; only when cardAction is DEAL, HIGHER, LOWER, NEXT and cardLocation is HAND
		String message;
		if (flowDTO.getSuppliedCardAction().equals(CardAction.PASS) &&
				    flowDTO.getSuppliedCardLocation().equals(CardLocation.HAND)) {
			eventOutput = new EventOutput(EventOutput.Result.SUCCESS);
			message = String.format("UpdateCasinoForGameAndPlayerEvent do no update for a pass or not to a hand");
			log.info(message);
			return eventOutput;
		}
		
		// init all the object and lists
		Game gameToCheck;
		Casino dealToThisCasino;
		List<Deck> decksUpdated = flowDTO.getDecks();
		
		List<Hand> otherHandsForCasino;
		Hand handToCreate = new Hand();
		List<Hand> handsCreated = new ArrayList<>();
		
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
			dealToThisCasino = casinoService.findOne(Integer.parseInt(casinoId));
			if (dealToThisCasino == null || (dealToThisCasino.getGame().getGameId() != Integer.parseInt(gameId))) {
				eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
				return eventOutput;
			}
		} catch (Exception e) {
			eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
			return eventOutput;
		}
		
		// find all the current Hands for the Casino
		try {
			otherHandsForCasino = handService.findAllWhere("casino", casinoId);
		} catch (Exception e) {
			eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
			return eventOutput;
		}
		
		// sort on card order
		Collections.sort(otherHandsForCasino, Comparator.comparing(Hand::getCardOrder).thenComparing(Hand::getCardOrder));
		int cardOrder = otherHandsForCasino.size() + 1;
		
		// do the add
		try {
			
			for (Deck deck : flowDTO.getDecks()) {
				
				handToCreate.setCasino(dealToThisCasino);
				handToCreate.setPlayer(dealToThisCasino.getPlayer());
				handToCreate.setCard(deck.getCard());
				handToCreate.setCardOrder(cardOrder++);
				
				handsCreated.add(handService.create(handToCreate));
				
				message = String.format("UpdateCasinoForGameAndPlayerEvent handToCreate is: %s", handToCreate.toString());
				log.info(message);
			}
		} catch (Exception e) {
			eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
			return eventOutput;
		}
		
		// OK, set a trigger for EventOutput to trigger a transition in the state machine
		flowDTO.setCurrentGame(gameService.findOne(Integer.parseInt(gameId)));
		message = String.format("CreateHandForCasinoForGameAndPlayerEvent setCurrentGame is: %s", flowDTO.getCurrentGame());
		log.info(message);
		
		// update ids
		flowDTO.setHands(handsCreated);
		
		if (flowDTO.getSuppliedTrigger() == CardGameStateMachine.Trigger.PUT_TURN) {
			// key event so do a transition
			eventOutput = new EventOutput(EventOutput.Result.SUCCESS, flowDTO.getSuppliedTrigger());
			message = String.format("CreateHandForCasinoForGameAndPlayerEvent do a transition with trigger is: %s", flowDTO.getSuppliedTrigger());
			log.info(message);
		} else {
			eventOutput = new EventOutput(EventOutput.Result.SUCCESS);
			message = String.format("CreateHandForCasinoForGameAndPlayerEvent do no transition");
			log.info(message);
		}
		return eventOutput;
	}
	
	public interface CreateHandForCasinoForGameAndPlayerEventDTO {
		
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
