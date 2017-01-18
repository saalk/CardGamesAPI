package nl.knikit.cardgames.event;

import nl.knikit.cardgames.commons.event.AbstractEvent;
import nl.knikit.cardgames.commons.event.EventOutput;
import nl.knikit.cardgames.mapper.ModelMapperUtil;
import nl.knikit.cardgames.model.Card;
import nl.knikit.cardgames.model.Casino;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.model.Hand;
import nl.knikit.cardgames.model.Player;
import nl.knikit.cardgames.model.state.CardGameStateMachine;
import nl.knikit.cardgames.service.ICardService;
import nl.knikit.cardgames.service.ICasinoService;
import nl.knikit.cardgames.service.IGameService;
import nl.knikit.cardgames.service.IHandService;
import nl.knikit.cardgames.service.IPlayerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
		
		// get the game and make a hand
		Game gameToUpdate;
		
		Player playerToDealTo;
		Card cardToDeal;
		Casino casinoToDealTo = new Casino();
		
		Hand handToCreate = new Hand();
		List<Hand> handsForCasino;
		
		// find the game
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
		
		// find the card
		String cardId = flowDTO.getCurrentCard().getCardId();
		try {
			cardToDeal = cardService.findOne(Integer.parseInt(cardId));
			if (cardToDeal == null) {
				eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
				return eventOutput;
			}
		} catch (Exception e) {
			eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
			return eventOutput;
		}
		
		
		// find the player
		String playerId = flowDTO.getSuppliedPlayerId();
		try {
			playerToDealTo = playerService.findOne(Integer.parseInt(playerId));
			if (playerToDealTo == null) {
				eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
				return eventOutput;
			}
		} catch (Exception e) {
			eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
			return eventOutput;
		}
		
		// find the casino for the player and game
		List<Casino> casinos;
		try {
			casinos = casinoService.findAllWhere("game", gameId);
			if (casinos == null) {
				eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
				return eventOutput;
			}
		} catch (Exception e) {
			eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
			return eventOutput;
		}
		boolean found = false;
		for (Casino casino : casinos) {
			if (casino.getPlayer().equals(playerToDealTo.getPlayerId())) {
				casinoToDealTo = casino;
				found = true;
			}
		}
		if (!found) {
			eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
			return eventOutput;
		}
		
		// find the existing hands for the player and casino
		try {
			handsForCasino = handService.findAllWhere("casino", String.valueOf(casinoToDealTo.getCasinoId()));
			if (handsForCasino == null) {
				eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
				return eventOutput;
			}
		} catch (Exception e) {
			eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
			return eventOutput;
		}
		
		// do the add
		handToCreate.setCasino(casinoToDealTo);
		handToCreate.setPlayer(playerToDealTo);
		handToCreate.setCard(cardToDeal);
		handToCreate.setCardOrder(handsForCasino.size()+1);
		
		try {
			handToCreate = handService.create(handToCreate);
			if (handToCreate == null) {
				eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
				return eventOutput;
			}
		} catch (Exception e) {
			eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
			return eventOutput;
		}
		
		
		// OK, set a trigger for EventOutput to trigger a transition in the state machine
		flowDTO.setCurrentGame(gameService.findOne(Integer.parseInt(gameId)));
		String message = String.format("CreateHandForCasinoForGameAndPlayerEvent setCurrentGame is: %s", flowDTO.getCurrentGame());
		log.info(message);
		
		// update ids
		flowDTO.setSuppliedHandId(String.valueOf(handToCreate.getHandId()));
		
		// update entities found
		flowDTO.setCurrentPlayer(playerToDealTo);
		flowDTO.setCurrentHand(handToCreate);
		
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
		
		// all game fields
		String getSuppliedGameId();
		void setCurrentGame(Game game);
		Game getCurrentGame();
		
		// rest
		String getSuppliedPlayerId();
		Card getCurrentCard();
		
		void setSuppliedHandId(String handId);
		void setCurrentPlayer(Player player);
		void setCurrentHand(Hand hand);
		
		CardGameStateMachine.Trigger getSuppliedTrigger();
		
	}
}
