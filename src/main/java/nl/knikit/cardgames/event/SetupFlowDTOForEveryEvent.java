package nl.knikit.cardgames.event;

import nl.knikit.cardgames.commons.event.AbstractEvent;
import nl.knikit.cardgames.commons.event.EventOutput;
import nl.knikit.cardgames.model.Casino;
import nl.knikit.cardgames.model.Deck;
import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.model.Hand;
import nl.knikit.cardgames.model.Player;
import nl.knikit.cardgames.model.state.CardGameStateMachine;
import nl.knikit.cardgames.service.ICardService;
import nl.knikit.cardgames.service.ICasinoService;
import nl.knikit.cardgames.service.IDeckService;
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
public class SetupFlowDTOForEveryEvent extends AbstractEvent {
	
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
	private IDeckService deckService;
	
	@Override
	protected EventOutput execution(final Object... eventInput) {
		
		SetupFlowDTOForEveryEventDTO flowDTO = (SetupFlowDTOForEveryEventDTO) eventInput[0];
		EventOutput eventOutput;
		
		// save the game and decks and casinos sorted in flow DTO if supplied
		if ((flowDTO.getSuppliedGameId() != null) && (Integer.parseInt(flowDTO.getSuppliedGameId()) > 0)) {
			try {
				flowDTO.setCurrentGame(gameService.findOne(Integer.parseInt(flowDTO.getSuppliedGameId())));
				if (flowDTO.getCurrentGame() == null) {
					eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
					return eventOutput;
				}
				// find all the current Hands for the Game and sort them
				try {
					List<Deck> unsortedDecks;
					unsortedDecks = deckService.findAllWhere("casino", flowDTO.getSuppliedCasinoId());
					Collections.sort(unsortedDecks, Comparator.comparing(Deck::getCardOrder).thenComparing(Deck::getCardOrder));
					flowDTO.setDecks(unsortedDecks);
				} catch (Exception e) {
					eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
					return eventOutput;
				}
				// find all the current Casinos for the Game
				try {
					List<Casino> unsortedCasinos;
					unsortedCasinos = casinoService.findAllWhere("casino", flowDTO.getSuppliedCasinoId());
					Collections.sort(unsortedCasinos, Comparator.comparing(Casino::getPlayingOrder).thenComparing(Casino::getPlayingOrder));
					flowDTO.setCasinos(unsortedCasinos);
				} catch (Exception e) {
					eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
					return eventOutput;
				}
			} catch (Exception e) {
				eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
				return eventOutput;
			}
		}
		
		// save the casino and hands sorted in flow DTO if supplied
		if ((flowDTO.getSuppliedCasinoId() != null) && (Integer.parseInt(flowDTO.getSuppliedCasinoId()) > 0)) {
			try {
				flowDTO.setCurrentCasino(casinoService.findOne(Integer.parseInt(flowDTO.getSuppliedCasinoId())));
				if (flowDTO.getCurrentCasino() == null) {
					eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
					return eventOutput;
				}
				// find all the current Hands for the Casino
				try {
					List<Hand> unsortedHands;
					unsortedHands = handService.findAllWhere("casino", flowDTO.getSuppliedCasinoId());
					Collections.sort(unsortedHands, Comparator.comparing(Hand::getCardOrder).thenComparing(Hand::getCardOrder));
					flowDTO.setHands(unsortedHands);
				} catch (Exception e) {
					eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
					return eventOutput;
				}
			} catch (Exception e) {
				eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
				return eventOutput;
			}
		}
		
		// save the player in flow DTO if supplied
		if ((flowDTO.getSuppliedPlayerId() != null) && (Integer.parseInt(flowDTO.getSuppliedPlayerId()) > 0)) {
			try {
				flowDTO.setCurrentPlayer(playerService.findOne(Integer.parseInt(flowDTO.getSuppliedPlayerId())));
				if (flowDTO.getCurrentPlayer() == null) {
					eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
					return eventOutput;
				}
			} catch (Exception e) {
				eventOutput = new EventOutput(EventOutput.Result.FAILURE, flowDTO.getSuppliedTrigger());
				return eventOutput;
			}
		}
		eventOutput = new EventOutput(EventOutput.Result.SUCCESS);
		return eventOutput;
	}
	
	public interface SetupFlowDTOForEveryEventDTO {
		
		CardGameStateMachine.Trigger getSuppliedTrigger();
		
		// all supplied fields
		String getSuppliedGameId();
		
		String getSuppliedCasinoId();
		
		String getSuppliedPlayerId();
		
		// all the retrieved data
		Game getCurrentGame();
		
		void setCurrentGame(Game game);
		
		List<Deck> getDecks();
		
		void setDecks(List<Deck> decks);
		
		List<Casino> getCasinos();
		
		void setCasinos(List<Casino> casinos);
		
		Casino getCurrentCasino();
		
		void setCurrentCasino(Casino casino);
		
		List<Hand> getHands();
		
		void setHands(List<Hand> hands);
		
		Player getCurrentPlayer();
		
		void setCurrentPlayer(Player player);
		
	}
}
