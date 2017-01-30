package nl.knikit.cardgames.event;

import nl.knikit.cardgames.commons.event.AbstractEvent;
import nl.knikit.cardgames.commons.event.EventOutput;
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
	
	@Override
	protected EventOutput execution(final Object... eventInput) {
		
		CreateHandForCasinoForGameAndPlayerEventDTO flowDTO = (CreateHandForCasinoForGameAndPlayerEventDTO) eventInput[0];
		EventOutput eventOutput;
		
		// find all decks to update; only when cardAction is DEAL, HIGHER, LOWER; NEXT should be changed in a special ai event
		String message;
		
		// skip this event when pass turn
		if (flowDTO.getSuppliedTrigger() == CardGameStateMachine.Trigger.PUT_PASS_TURN) {
			// key event so do a transition
			eventOutput = new EventOutput(EventOutput.Result.SUCCESS, flowDTO.getSuppliedTrigger());
			message = String.format("CreateHandForCasinoForGameAndPlayerEvent do a transition with trigger is: %s", flowDTO.getSuppliedTrigger());
			log.info(message);
			return eventOutput;
			
		}
		
		// skip this event when no cards left turn
		if (flowDTO.getSuppliedTrigger() == CardGameStateMachine.Trigger.NO_CARDS_LEFT) {
			// no key event no a transition
			eventOutput = new EventOutput(EventOutput.Result.SUCCESS);
			message = String.format("CreateHandForCasinoForGameAndPlayerEvent no transition with trigger is: %s", flowDTO.getSuppliedTrigger());
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
			
			message = String.format("CreateHandForCasinoForGameAndPlayerEvent getDecks is: %s", flowDTO.getDecks());
			log.info(message);
			
			for (Deck deck : flowDTO.getDecks()) {
				
				handToCreate.setCasino(dealToThisCasino);
				handToCreate.setPlayer(dealToThisCasino.getPlayer());
				handToCreate.setCard(deck.getCard());
				handToCreate.setCardOrder(cardOrder++);
				
				if (flowDTO.getSuppliedCurrentTurn() != 0) {
					handToCreate.setTurn(flowDTO.getSuppliedCurrentTurn());
				}
				if (flowDTO.getSuppliedCurrentRound() != 0) {
					handToCreate.setRound(flowDTO.getSuppliedCurrentRound());
				}
				if (flowDTO.getSuppliedCardLocation() != null) {
					handToCreate.setCardLocation(flowDTO.getSuppliedCardLocation());
				}
				if (flowDTO.getSuppliedCardAction() != null) {
					handToCreate.setCardAction(flowDTO.getSuppliedCardAction());
				}
				handsCreated.add(handService.create(handToCreate));
				
				message = String.format("CreateHandForCasinoForGameAndPlayerEvent handToCreate is: %s", handToCreate.toString());
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
		
		// update ids and the fact that a turn has been made so +1 !!
		flowDTO.setHands(handsCreated);
		flowDTO.setSuppliedCurrentTurn(dealToThisCasino.getActiveTurn());
		
		
		// evaluate the last hand,
		// if lost: deduct the bet right away and end the game
		// if won 3 times: than add the bet to the player and update the winner
//		Hand lastHand = otherHandsForCasino.get(otherHandsForCasino.size()-1);
//		int result = handToCreate.getCard().compareTo(lastHand.getCard(),gameToCheck.getGameType());
//		if ((result == -1) && (flowDTO.getSuppliedCardAction()==CardAction.LOWER)){
//			message = String.format("CreateHandForCasinoForGameAndPlayerEvent evaluate result is: %s", result );
//
//		} else {
//			message = String.format("CreateHandForCasinoForGameAndPlayerEvent evaluate result is: %s", result );
//
//		}
		
		// lost and w.on: also update the cubits  to the player when passing
		// won: also add the winner to the game when passing
		
		if ((flowDTO.getSuppliedTrigger() == CardGameStateMachine.Trigger.PUT_DEAL_TURN) ||
				    (flowDTO.getSuppliedTrigger() == CardGameStateMachine.Trigger.PUT_PLAYING_TURN)) {
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
		
		CardLocation getSuppliedCardLocation();
		
		// get the data created by other events
		
		List<Deck> getDecks();
		
		int getSuppliedCurrentRound();
		
		void setSuppliedCurrentTurn(int turn);
		
		int getSuppliedCurrentTurn();
		
		// pass on the data created here for other events
		
		void setHands(List<Hand> hands);
		
		void setSuppliedCubits(String cubits);
	}
}
