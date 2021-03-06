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
		String message;
		
		// NO CARDS LEFT
		if (flowDTO.getSuppliedTrigger() == CardGameStateMachine.Trigger.NO_CARDS_LEFT) {
			// no key event no a transition
			eventOutput = new EventOutput(EventOutput.Result.SUCCESS);
			message = String.format("CreateHandForCasinoForGameAndPlayerEvent no transition with trigger is: %s", flowDTO.getSuppliedTrigger());
			log.info(message);
			return eventOutput;
		}
		
		// INIT
		Casino dealToThisCasino = flowDTO.getCurrentCasino();
		List<Hand> otherHandsForCasino = flowDTO.getCurrentHands();
		Hand handToCreate = new Hand();
		List<Hand> handsCreated = new ArrayList<>();
		
		// ADD
		try {
			
			message = String.format("CreateHandForCasinoForGameAndPlayerEvent getDecks is: %s", flowDTO.getCurrentDecks());
			log.info(message);
			
			int cardOrder = otherHandsForCasino.size() + 1;
			for (Deck deck : flowDTO.getCurrentDecks()) {
				
				handToCreate.setCasino(dealToThisCasino);
				handToCreate.setPlayer(dealToThisCasino.getPlayer());
				handToCreate.setCard(deck.getCard());
				handToCreate.setCardOrder(cardOrder++);
				
				if (flowDTO.getNewCurrentTurn() != 0) {
					handToCreate.setTurn(flowDTO.getNewCurrentTurn());
				}
				if (flowDTO.getNewCurrentRound() != 0) {
					handToCreate.setRound(flowDTO.getNewCurrentRound());
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
			eventOutput = new EventOutput(EventOutput.Result.FAILURE, CardGameStateMachine.Trigger.ERROR);
			return eventOutput;
		}
		otherHandsForCasino.addAll(handsCreated);
		flowDTO.setCurrentHands(otherHandsForCasino);
		
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
		
		Casino getCurrentCasino();
		
		List<Hand> getCurrentHands();
		
		void setCurrentGame(Game game);
		
		// the rest of the supplied fields
		CardAction getSuppliedCardAction();
		
		CardLocation getSuppliedCardLocation();
		
		// get the data created by other events
		
		List<Deck> getCurrentDecks();
		
		int getNewCurrentRound();
		
		int getNewCurrentTurn();
		
		// pass on the data created here for other events
		
		void setCurrentHands(List<Hand> hands);
	}
}
