package nl.knikit.cardgames.event;

import nl.knikit.cardgames.commons.event.AbstractEvent;
import nl.knikit.cardgames.commons.event.EventOutput;
import nl.knikit.cardgames.model.Card;
import nl.knikit.cardgames.model.Casino;
import nl.knikit.cardgames.model.Deck;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.model.Player;
import nl.knikit.cardgames.model.state.CardGameStateMachine;
import nl.knikit.cardgames.service.ICasinoService;
import nl.knikit.cardgames.service.IDeckService;
import nl.knikit.cardgames.service.IGameService;
import nl.knikit.cardgames.service.IPlayerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UpdateDeckForGameAndPlayerEvent extends AbstractEvent {
	
	// @Resource = javax, @Inject = javax, @Autowire = spring bean factory
	@Autowired
	private IGameService gameService;
	
	@Autowired
	private IPlayerService playerService;
	
	@Autowired
	private IDeckService deckService;
	
	@Override
	protected EventOutput execution(final Object... eventInput) {
		
		UpdateDeckForGameAndPlayerEventDTO flowDTO = (UpdateDeckForGameAndPlayerEventDTO) eventInput[0];
		EventOutput eventOutput;
		
		String upOrDown;
		if (flowDTO.getSuppliedPlayingOrder() == null || flowDTO.getSuppliedPlayingOrder().equals("null") || flowDTO.getSuppliedPlayingOrder().isEmpty()) {
			eventOutput = new EventOutput(EventOutput.Result.SUCCESS);
			String message = String.format("UpdateDeckForGameAndPlayerEvent no getSuppliedPlayingOrder");
			log.info(message);
			return eventOutput;
		} else if (flowDTO.getSuppliedPlayingOrder().equals("-1")) {
			upOrDown = "up";
		} else {
			upOrDown = "down";
		}
		
		// get the game and update the gametype and ante
		Game gameToUpdate;
		Player dealtToThisPlayer;
		
		Deck deckToUpdate = new Deck();
		Deck otherDeckToUpdate = new Deck();
		
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
			dealtToThisPlayer = playerService.findOne(Integer.parseInt(playerId));
			if (dealtToThisPlayer == null) {
				eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
				return eventOutput;
			}
		} catch (Exception e) {
			eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
			return eventOutput;
		}
		
		List<Deck> decks; //TODO should be ordered with a set
		try {
			decks = deckService.findAllWhere("game", gameId);
			if (decks == null) {
				eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
				return eventOutput;
			}
		} catch (Exception e) {
			eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
			return eventOutput;
		}
		
		// find the top card
		boolean found = false;
		for (Deck deck : decks) {
			if (deck.getDealtTo() == null || deck.getDealtTo().equals("")) {
				deckToUpdate = deck;
				found = true;
			}
		}
		if (!found) {
			eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
			return eventOutput;
		}
		
		// do the update
		try {
			deckToUpdate.setDealtTo(dealtToThisPlayer);
			deckToUpdate = deckService.update(deckToUpdate);
			
		} catch (Exception e) {
			eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
			return eventOutput;
		}
		
		// OK, set a trigger for EventOutput to trigger a transition in the state machine
		flowDTO.setCurrentGame(gameService.findOne(Integer.parseInt(gameId)));
		String message = String.format("UpdateCasinoForGameAndPlayerEvent setCurrentGame is: %s", flowDTO.getCurrentGame());
		log.info(message);
		
		flowDTO.setCurrentDeck(deckToUpdate);
		flowDTO.setSuppliedDeckId(String.valueOf(deckToUpdate.getDeckId()));
		flowDTO.setCurrentCard(deckToUpdate.getCard());
		flowDTO.setSuppliedCardId(deckToUpdate.getCard().getCardId());
		
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
	
	public interface UpdateDeckForGameAndPlayerEventDTO {
		
		// all game fields
		String getSuppliedGameId();
		void setCurrentGame(Game game);
		Game getCurrentGame();
		
		// rest of setter and getter
		String getSuppliedPlayerId();
		String getSuppliedPlayingOrder();
		
		void setCurrentDeck(Deck deck);
		void setCurrentCard(Card card);
		
		void setSuppliedCardId(String cardId);
		void setSuppliedDeckId(String deckId);
		
		CardGameStateMachine.Trigger getSuppliedTrigger();
		
	}
}
