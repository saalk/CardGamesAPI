package nl.knikit.cardgames.event;

import nl.knikit.cardgames.DTO.DeckDto;
import nl.knikit.cardgames.DTO.GameDto;
import nl.knikit.cardgames.commons.event.AbstractEvent;
import nl.knikit.cardgames.commons.event.EventOutput;
import nl.knikit.cardgames.mapper.ModelMapperUtil;
import nl.knikit.cardgames.model.Card;
import nl.knikit.cardgames.model.Deck;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.model.Player;
import nl.knikit.cardgames.model.CardLocation;
import nl.knikit.cardgames.model.state.CardGameStateMachine;
import nl.knikit.cardgames.service.IDeckService;
import nl.knikit.cardgames.service.IGameService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CreateDeckForGameEvent extends AbstractEvent {
	
	// @Resource = javax, @Inject = javax, @Autowire = spring bean factory
	@Autowired
	private IGameService gameService;
	
	@Autowired
	private IDeckService deckService;
	
	@Autowired
	private ModelMapperUtil mapUtil;
	
	@Override
	protected EventOutput execution(final Object... eventInput) {
		
		CreateDeckForGameEventDTO flowDTO = (CreateDeckForGameEventDTO) eventInput[0];
		EventOutput eventOutput;
		
		Game gameToUpdate;
		
		// check the game
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
		
		// do the add
		// Get all cards and shuffle (since java 1.8)
		List<Card> cards = Card.newDeck(Integer.parseInt(flowDTO.getSuppliedJokers()));
		Collections.shuffle(cards);
		
		int order = 1;
		List<Deck> newDeckDtos = new ArrayList<>();
		for (Card card : cards) {
			Deck newDeck = new Deck();
			newDeck.setCard(card);
			newDeck.setCardOrder(order++);
			newDeck.setDealtTo(null);
			newDeck.setGame(gameToUpdate);
			newDeck.setCardLocation(CardLocation.STOCK);
			try {
				Deck createdDeck = deckService.create(newDeck);
				if (createdDeck == null) {
					eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
					return eventOutput;
				}
				newDeckDtos.add(createdDeck);
			} catch (Exception e) {
				eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
				return eventOutput;
			}
		}
		
		// OK, set a trigger for EventOutput to trigger a transition in the state machine
		flowDTO.setCurrentGame(gameService.findOne(Integer.parseInt(gameId)));
		String message = String.format("UpdateCardGameDetailsEvent setCurrentGame is: %s", flowDTO.getCurrentGame());
		log.info(message);
		
		flowDTO.setDecks(newDeckDtos);
		if (flowDTO.getSuppliedTrigger() == CardGameStateMachine.Trigger.POST_SHUFFLE) {
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
	
	public interface CreateDeckForGameEventDTO {
		
		// all game fields
		String getSuppliedGameId();
		void setCurrentGame(Game game);
		Game getCurrentGame();
		
		String getSuppliedJokers();
		void setDecks(List<Deck> decks);
		
		CardGameStateMachine.Trigger getSuppliedTrigger();
		
	}
}
