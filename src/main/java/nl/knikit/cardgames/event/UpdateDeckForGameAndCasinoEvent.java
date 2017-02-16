package nl.knikit.cardgames.event;

import nl.knikit.cardgames.commons.event.AbstractEvent;
import nl.knikit.cardgames.commons.event.EventOutput;
import nl.knikit.cardgames.model.CardAction;
import nl.knikit.cardgames.model.CardLocation;
import nl.knikit.cardgames.model.Casino;
import nl.knikit.cardgames.model.Deck;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.model.state.CardGameStateMachine;
import nl.knikit.cardgames.service.ICasinoService;
import nl.knikit.cardgames.service.IDeckService;
import nl.knikit.cardgames.service.IGameService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UpdateDeckForGameAndCasinoEvent extends AbstractEvent {
	
	// @Resource = javax, @Inject = javax, @Autowire = spring bean factory
//	@Autowired
//	private IGameService gameService;
//
//	@Autowired
//	private ICasinoService casinoService;
//
	@Autowired
	private IDeckService deckService;
	
	@Override
	protected EventOutput execution(final Object... eventInput) {
		
		UpdateDeckForGameAndCasinoEventDTO flowDTO = (UpdateDeckForGameAndCasinoEventDTO) eventInput[0];
		EventOutput eventOutput;
		
		// find all decks to update; only when cardAction is DEAL, HIGHER, LOWER, NEXT
		String message;
		if (flowDTO.getSuppliedCardAction().equals(CardAction.PASS)) {
			eventOutput = new EventOutput(EventOutput.Result.SUCCESS);
			message = String.format("UpdateDeckForGameAndCasinoEvent do no update for a pass");
			log.info(message);
			return eventOutput;
		}
		
		// init all the object and lists
		Game gameToCheck = flowDTO.getCurrentGame();
		Casino dealToThisCasino = flowDTO.getCurrentCasino();
		List<Deck> allDecksForGame = flowDTO.getCurrentDecks();
		List<Deck> allDecksToUpdate = new ArrayList<>();
		List<Deck> decksUpdated = new ArrayList<>();
		
		message = String.format("UpdateDeckForGameAndCasinoEvent getSuppliedCasinoId is: %s", flowDTO.getSuppliedCasinoId());
		log.info(message);
		
		// do not do HIGHER, LOWER when the current casino in the game is not the casino supplied
		if (((flowDTO.getSuppliedCardAction().equals(CardAction.HIGHER))
				    || (flowDTO.getSuppliedCardAction().equals(CardAction.LOWER))) &&
				    (gameToCheck.getActiveCasino()!= Integer.parseInt(flowDTO.getSuppliedCasinoId()))  )  {
			eventOutput = new EventOutput(EventOutput.Result.SUCCESS);
			
			message = String.format("UpdateDeckForGameAndCasinoEvent switch to different casino when playing is not allowed");
			log.info(message);
			
			return eventOutput;
		}
		
		// when current round is zero do not DEAL to casinos other than having playingOrder 1
		if (((flowDTO.getSuppliedCardAction().equals(CardAction.DEAL)) && (gameToCheck.getCurrentRound()==0) &&
				    (dealToThisCasino.getPlayingOrder()!=1)))  {
			eventOutput = new EventOutput(EventOutput.Result.SUCCESS);
			
			message = String.format("UpdateDeckForGameAndCasinoEvent switch to not first casino when dealing is not allowed");
			log.info(message);
			
			return eventOutput;
		}
		
		// sort on card order
		Collections.sort(allDecksForGame, Comparator.comparing(Deck::getCardOrder).thenComparing(Deck::getCardOrder));
		
		
		// find all decks to update
		boolean found = false;
		int total = Integer.parseInt(flowDTO.getSuppliedTotal());
		for (Deck deck : allDecksForGame) {
			if (deck.getCardLocation() == CardLocation.STACK && total > 0) {
				allDecksToUpdate.add(deck);
				found = true;
				total -= 1;
			}
		}
		
		// no cards left
		if (allDecksToUpdate.size() == 0 && (total > 0)) {
			message = String.format("UpdateDeckForGameAndCasinoEvent allDecksToUpdate is zero, no cards left is but needed: %s", total);
			log.info(message);
			flowDTO.setSuppliedTrigger(CardGameStateMachine.Trigger.NO_CARDS_LEFT);
			eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
			return eventOutput;
		}
		
		// do the update
		try {
			
			for (Deck deck : allDecksToUpdate) {
				
				deck.setDealtTo(dealToThisCasino);
				deck.setCardLocation(flowDTO.getSuppliedCardLocation());
				deck = deckService.update(deck);
				decksUpdated.add(deck);
				message = String.format("UpdateDeckForGameAndCasinoEvent deckToUpdate is: %s", deck.toString());
				log.info(message);
			}
		} catch (Exception e) {
			eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
			return eventOutput;
		}
		
		// OK, set a trigger for EventOutput to trigger a transition in the state machine
		//flowDTO.setCurrentGame(gameService.findOne(Integer.parseInt(flowDTO.getSuppliedGameId())));
		flowDTO.setCurrentDecks(decksUpdated);
		
		
		// 2x: settings for the game to update:
		
		// set the current round+1 when action is DEAL for a casino that has playingOrder 1
		// else set the current round to what is present in game
		if (flowDTO.getSuppliedCardAction() == CardAction.DEAL &&
				    dealToThisCasino.getPlayingOrder() == 1) {
			flowDTO.setSuppliedCurrentRound(gameToCheck.getCurrentRound() + 1);
		} else {
			flowDTO.setSuppliedCurrentRound(gameToCheck.getCurrentRound());
		}
		// set the activeCasino to the currentCasino
		flowDTO.setSuppliedActiveCasino(Integer.parseInt(flowDTO.getSuppliedCasinoId()));
		
		
		// 1x: settings for the casino to update
		
		// set the current turn to 1 for DEAL and to turn +1 when the action is HIGHER, LOWER for a casino
		if (flowDTO.getSuppliedCardAction() == CardAction.DEAL) {
			flowDTO.setSuppliedCurrentTurn(1);
		}
		if (flowDTO.getSuppliedCardAction() == CardAction.HIGHER ||
				    flowDTO.getSuppliedCardAction() == CardAction.LOWER) {
			flowDTO.setSuppliedCurrentTurn(dealToThisCasino.getActiveTurn() + 1);
		}
		
		
		if ((flowDTO.getSuppliedTrigger() == CardGameStateMachine.Trigger.PUT_DEAL_TURN) ||
				    (flowDTO.getSuppliedTrigger() == CardGameStateMachine.Trigger.PUT_PASS_TURN) ||
				    (flowDTO.getSuppliedTrigger() == CardGameStateMachine.Trigger.PUT_PLAYING_TURN)) {
			// not a key event, dealing the deck (card) to the hand is so do no transition
			eventOutput = new EventOutput(EventOutput.Result.SUCCESS);
			
			message = String.format("UpdateDeckForGameAndCasinoEvent not do a transition with trigger is: %s", flowDTO.getSuppliedTrigger());
			log.info(message);
		} else
		
		{
			eventOutput = new EventOutput(EventOutput.Result.SUCCESS);
			message = String.format("UpdateDeckForGameAndCasinoEvent do no transition");
			log.info(message);
		}
		
		return eventOutput;
	}
	
	public interface UpdateDeckForGameAndCasinoEventDTO {
		
		// all game and trigger fields
		String getSuppliedGameId();
		
		CardGameStateMachine.Trigger getSuppliedTrigger();
		
		Game getCurrentGame();
		
		List<Deck> getCurrentDecks();
		
		Casino getCurrentCasino();
		
		void setCurrentGame(Game game);
		
		void setSuppliedTrigger(CardGameStateMachine.Trigger trigger);
		
		// the rest of the supplied fields and data stored by other events
		String getSuppliedCasinoId();
		
		CardAction getSuppliedCardAction();
		
		String getSuppliedTotal();
		
		CardLocation getSuppliedCardLocation();
		
		// pass on the data created here to other events
		
		void setCurrentDecks(List<Deck> decks);
		
		void setSuppliedCurrentRound(int currentRound);
		
		void setSuppliedCurrentTurn(int currentTurn);
		
		void setSuppliedActiveCasino(int activeCasino);
		
		
	}
}
