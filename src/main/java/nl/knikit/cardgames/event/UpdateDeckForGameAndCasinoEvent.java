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
	@Autowired
	private IGameService gameService;
	
	@Autowired
	private ICasinoService casinoService;
	
	@Autowired
	private IDeckService deckService;
	
	@Override
	protected EventOutput execution(final Object... eventInput) {
		
		UpdateDeckForGameAndCasinoEventDTO flowDTO = (UpdateDeckForGameAndCasinoEventDTO) eventInput[0];
		EventOutput eventOutput;
		
		// find all decks to update; only when cardAction is DEAL, HIGHER, LOWER, NEXT
		String message;
		if (flowDTO.getSuppliedCardAction().equals(CardAction.PASS) ) 	{
			eventOutput = new EventOutput(EventOutput.Result.SUCCESS);
			message = String.format("UpdateCasinoForGameAndPlayerEvent do no update for a pass");
			log.info(message);
			return eventOutput;
		}
		
		// init all the object and lists
		Game gameToCheck;
		Casino dealToThisCasino;
		List<Deck> allDecksForGame;
		List<Deck> allDecksToUpdate = new ArrayList<>();
		List<Deck> decksUpdated = new ArrayList<>();
		
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
		
		message = String.format("UpdateCasinoForGameAndPlayerEvent getSuppliedCasinoId is: %s", flowDTO.getSuppliedCasinoId());
		log.info(message);
		
		// check the casino
		String casinoId = flowDTO.getSuppliedCasinoId();
		try {
			dealToThisCasino = casinoService.findOne(Integer.parseInt(casinoId));
			if (dealToThisCasino == null) {
				eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
				return eventOutput;
			}
		} catch (Exception e) {
			eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
			return eventOutput;
		}
		
		// get all decks

		try {
			allDecksForGame = deckService.findAllWhere("game", gameId);
			if (allDecksForGame == null) {
				eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
				return eventOutput;
			}
		} catch (Exception e) {
			eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
			return eventOutput;
		}
		
		// sort on card order
		Collections.sort(allDecksForGame, Comparator.comparing(Deck::getCardOrder).thenComparing(Deck::getCardOrder));
		
		
		// find all decks to update
		boolean found = false;
		int total = Integer.parseInt(flowDTO.getSuppliedTotal());
		for (Deck deck : allDecksForGame) {
			if (deck.getCardLocation() == CardLocation.STOCK && total > 0) {
				allDecksToUpdate.add(deck);
				found = true;
				total -= 1;
			}
		}
		
		// no cards left
		if (!found) {
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
				message = String.format("UpdateCasinoForGameAndPlayerEvent deckToUpdate is: %s", deck.toString());
				log.info(message);
			}
		} catch (Exception e) {
			eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
			return eventOutput;
		}
		
		// OK, set a trigger for EventOutput to trigger a transition in the state machine
		flowDTO.setCurrentGame(gameService.findOne(Integer.parseInt(gameId)));
		flowDTO.setDecks(decksUpdated);
		
		if (flowDTO.getSuppliedTrigger() == CardGameStateMachine.Trigger.PUT_TURN) 	{
			// not a key event, dealing the deck (card) to the hand is so do no transition
			eventOutput = new EventOutput(EventOutput.Result.SUCCESS);
			message = String.format("UpdateCasinoForGameAndPlayerEvent do no transition with trigger is: %s", flowDTO.getSuppliedTrigger());
			log.info(message);
		} else	{
			eventOutput = new EventOutput(EventOutput.Result.SUCCESS);
			message = String.format("UpdateCasinoForGameAndPlayerEvent do no transition");
			log.info(message);
		}
		
		return eventOutput;
	}
	
	public interface UpdateDeckForGameAndCasinoEventDTO {
		
		// all game and trigger fields
		String getSuppliedGameId();
		
		CardGameStateMachine.Trigger getSuppliedTrigger();
		
		Game getCurrentGame();
		
		void setCurrentGame(Game game);
		
		void setSuppliedTrigger(CardGameStateMachine.Trigger trigger);
		
		// the rest of the supplied fields and data stored by other events
		String getSuppliedCasinoId();
		
		CardAction getSuppliedCardAction();
		
		String getSuppliedTotal();
		
		CardLocation getSuppliedCardLocation();
		
		// pass on the data created here to other events
		
		void setDecks(List<Deck> decks);
		

	}
}
