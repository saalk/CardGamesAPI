package nl.knikit.cardgames.event;

import nl.knikit.cardgames.commons.event.AbstractEvent;
import nl.knikit.cardgames.commons.event.EventOutput;
import nl.knikit.cardgames.model.Card;
import nl.knikit.cardgames.model.Casino;
import nl.knikit.cardgames.model.Deck;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.model.state.CardGameStateMachine;
import nl.knikit.cardgames.service.ICasinoService;
import nl.knikit.cardgames.service.IDeckService;
import nl.knikit.cardgames.service.IGameService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
		
		// get the game and update the gametype and ante
		Game gameToUpdate;
		Casino dealToThisCasino;
		
		Deck deckToUpdate = new Deck();
		Deck deckUpdated = new Deck();
		
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
		
		String message = String.format("UpdateCasinoForGameAndPlayerEvent getSuppliedPlayerId is: %s", flowDTO.getSuppliedPlayerId());
		log.info(message);
		
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
		
		List<Deck> allDecksForGame; //TODO should be ordered with a set
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
		
		// find the top card
		boolean found = false;
		for (Deck deck : allDecksForGame) {
			if (deck.getDealtTo() == null || deck.getDealtTo().equals("")) {
				deckToUpdate = deck;
				found = true;
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
			//deckToUpdate.setDealtTo(dealToThisCasino); // TODO
			deckUpdated = deckService.update(deckToUpdate);
			
			message = String.format("UpdateCasinoForGameAndPlayerEvent deckToUpdate is: %s", deckToUpdate.toString());
			log.info(message);
			
		} catch (Exception e) {
			eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
			return eventOutput;
		}
		
		// OK, set a trigger for EventOutput to trigger a transition in the state machine
		flowDTO.setCurrentGame(gameService.findOne(Integer.parseInt(gameId)));

		
		flowDTO.setCurrentDeck(deckUpdated);
		flowDTO.setSuppliedDeckId(String.valueOf(deckUpdated.getDeckId()));
		
		flowDTO.setCurrentCard(deckUpdated.getCard());
		flowDTO.setSuppliedCardId(deckUpdated.getCard().getCardId());
		
		if (flowDTO.getSuppliedTrigger() == CardGameStateMachine.Trigger.PUT_TURN)
		
		{
			// not a key event, dealing the deck (card) to the hand is so do no transition
			eventOutput = new EventOutput(EventOutput.Result.SUCCESS);
			message = String.format("UpdateCasinoForGameAndPlayerEvent do no transition with trigger is: %s", flowDTO.getSuppliedTrigger());
			log.info(message);
		} else
		
		{
			eventOutput = new EventOutput(EventOutput.Result.SUCCESS);
			message = String.format("UpdateCasinoForGameAndPlayerEvent do no transition");
			log.info(message);
		}
		
		return eventOutput;
	}
	
	public interface UpdateDeckForGameAndCasinoEventDTO {
		
		// all game fields
		String getSuppliedGameId();
		void setCurrentGame(Game game);
		Game getCurrentGame();
		
		// rest of setter and getter
		String getSuppliedPlayerId();
		String getSuppliedCasinoId();
		
		void setCurrentDeck(Deck deck);
		void setCurrentCard(Card card);
		
		void setSuppliedCardId(String cardId);
		void setSuppliedDeckId(String deckId);
		
		CardGameStateMachine.Trigger getSuppliedTrigger();
		void setSuppliedTrigger(CardGameStateMachine.Trigger trigger);
		
	}
}
